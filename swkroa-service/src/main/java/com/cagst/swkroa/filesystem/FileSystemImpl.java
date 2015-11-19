package com.cagst.swkroa.filesystem;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.Optional;

/**
 * An implementation of the {@link FileSystem} interface using the Apache Commons VFS.
 *
 * @author Craig Gaskill
 */
@Named("fileSystem")
public class FileSystemImpl implements FileSystem {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemImpl.class);

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyyMMddHHmmss");

  private static final String DEFAULT_SEPARATOR = "_";
  private static final String DEFAULT_SCHEME    = "file";
  private static final String DEFAULT_BASEPATH  = "document/store";

  private final FileSystemManager fileSystemManager;

  private String scheme;
  private String basePath;

  private String user;
  private String password;
  private String host;
  private Integer port;

  /**
   * Default Constructor used to create an instance of <i>FileSystemImpl</i>.
   *
   * @throws FileSystemException
   */
  public FileSystemImpl() throws FileSystemException {
    fileSystemManager = VFS.getManager();

    scheme = DEFAULT_SCHEME;
    basePath = DEFAULT_BASEPATH;
  }

  /**
   * @return A {@link String} that defines the scheme used for the persistent store (i.e. file, ftp, http, etc.).
   * @see <a href="https://commons.apache.org/proper/commons-vfs/filesystems.html">Apache Commons VFS</a>
   */
  public String getScheme() {
    return scheme;
  }

  /**
   * Sets the scheme to use for the persistent store.
   *
   * @param scheme
   *      A {@link String} the defines the scheme to use for the persistent store.
   *
   * @see <a href="https://commons.apache.org/proper/commons-vfs/filesystems.html">Apache Commons VFS</a>
   */
  public void setScheme(final String scheme) {
    Assert.hasText(scheme, "Argument [scheme] is null or empty");

    this.scheme = scheme;
  }

  /**
   * @return A {@link String} that represents the base path for all files stored in the persistent store.
   */
  public String getBasePath() {
    return StringUtils.appendIfMissing(basePath, "/");
  }

  /**
   * Sets the base path for all files stored in the persistent store.
   *
   * @param basePath
   *      A {@link String} that specifies the base bath for all files stored in the persistent store.
   */
  public void setBasePath(final String basePath) {
    Assert.hasText(basePath, "Argument [basePath] is null or empty");

    this.basePath = basePath;
  }

  /**
   * Sets the username to authenticate with.
   *
   * @param user
   *      A {@link String} that specifies the username to authenticate with.
   */
  public void setUser(final String user) {
    this.user = user;
  }

  /**
   * Sets the password to authenticate with.
   *
   * @param password
   *      A {@link String} that specifies the password to authenticate with.
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Sets the host to connect with.
   *
   * @param host
   *      A {@link String} that specifies the host to connect with.
   */
  public void setHost(final String host) {
    this.host = host;
  }

  /**
   * Sets the remote port to connect with.
   *
   * @param port
   *      A {@link int} that specifies the port to connect with.
   */
  public void setPort(final int port) {
    this.port = port;
  }

  @Override
  public Optional<FileDTO> getFileById(final String fileId) throws NoSuchFileException, FileSystemException {
    Assert.hasText(fileId, "Argument [fileId] is null");

    LOGGER.debug("Calling getFileById for [{}]", fileId);

    UriComponents uri = UriComponentsBuilder.fromUriString(fileId).build();

    // rebuild the URI so it has the user:password (if applicable)
    String fileUri = buildUriFromBasePath(uri.getPath());

    FileObject fileObject;
    try {
      fileObject = fileSystemManager.resolveFile(fileUri);
      if (!fileObject.exists()) {
        String msg = "File [" + fileId + "] was not found!";
        LOGGER.error(msg);

        throw new NoSuchFileException(fileId);
      }
    } catch (FileSystemException ex) {
      String msg = "Failed to find the file [" + fileId + "]";
      LOGGER.error(msg, ex);

      throw ex;
    }

    return Optional.of(buildFileDTOFromFileObject(fileObject, true));
  }

  @Override
  public Optional<FileDTO> saveFileForEntity(final @Nullable String parentEntityName,
                                             final long parentEntityId,
                                             final String fileName,
                                             final String fileType,
                                             final String fileFormat,
                                             final byte[] fileContents)
      throws FileAlreadyExistsException, FileSystemException {

    Assert.hasText(fileName, "Argument [fileName] is null");
    Assert.hasText(fileType, "Argument [fileType] is null");
    Assert.notNull(fileContents, "Argument [fileContents] is null");

    LOGGER.debug("Calling saveFileForEntity for entity [{}], filename [{}], and filetype [{}]", parentEntityName, fileName, fileType);

    String uri = buildUri(parentEntityName, parentEntityId, fileName, fileType);

    FileObject fileObject;
    try {
      fileObject = fileSystemManager.resolveFile(uri);
      if (fileObject.exists()) {
        String msg = "File [" + uri + "] already exists!";

        LOGGER.error(msg);
        throw new FileAlreadyExistsException(uri);
      }

      // create the file
      fileObject.createFile();
    } catch (FileSystemException ex) {
      String msg = "Failed to create file [" + uri + "]";
      LOGGER.error(msg, ex);

      throw ex;
    }

    // retrieve the OutputStream to write to
    OutputStream out = fileObject.getContent().getOutputStream();
    try {
      out.write(fileContents);
    } catch (IOException ex) {
      String msg = "Failed writing content to [" + uri + "]";
      LOGGER.error(msg, ex);

      throw new FileSystemException(msg);
    } finally {
      IOUtils.closeQuietly(out);
    }

    return Optional.of(buildFileDTOFromFileObject(fileObject, true));
  }

  @Override
  public boolean deleteFileById(String fileId) throws NoSuchFileException, FileSystemException {
    Assert.hasText(fileId, "Argument [fileId] is null");

    LOGGER.debug("Calling deleteFileById for [{}]", fileId);

    UriComponents uri = UriComponentsBuilder.fromUriString(fileId).build();

    // rebuild the URI so it has the user:password (if applicable)
    String fileUri = buildUriFromBasePath(uri.getPath());

    FileObject fileObject;
    try {
      fileObject = fileSystemManager.resolveFile(fileUri);
      if (!fileObject.exists()) {
        String msg = "File [" + fileId + "] was not found!";
        LOGGER.error(msg);

        throw new NoSuchFileException(fileId);
      }
    } catch (FileSystemException ex) {
      String msg = "Failed to find the file [" + fileId + "]";
      LOGGER.error(msg, ex);

      throw ex;
    }

    return fileObject.delete();
  }

  /**
   * Normalizes the specified fileName removing special characters and replacing spaces with underscores.
   *
   * @param fileName
   *      A {@link String} that represents the name of the file to be normalized.
   *
   * @return A {@link String} that has been normalized of all special characters and spaces replaced with underscores.
   */
  @VisibleForTesting
  String normalizeFilename(final String fileName) {
    String normalized = StringUtils.remove(fileName, '-');
    normalized = StringUtils.normalizeSpace(normalized);
    normalized = StringUtils.replaceChars(normalized, ' ', '_');
    normalized = StringUtils.stripAccents(normalized);

    return normalized;
  }

  /**
   * Builds the uri for the file based upon the tenantId, clientId, fileName, and fileType.
   *
   * @param parentEntityName
   *    A {@link String} that defines the entity this document is associated with.
   * @param parentEntityId
   *    A {@link long} that uniquely identifies the entity this document is associated with.
   * @param fileName
   *      A {@link String} that represents the name of the document / file.
   * @param fileType
   *      A {@link String} that defines the type of document / file being stored.
   *
   * @return A {@link String} that represents the location of the file.
   */
  @VisibleForTesting
  String buildUri(
      final String parentEntityName,
      final long parentEntityId,
      final String fileName,
      final String fileType
  ) {
    String path = DATE_TIME_FORMATTER.print(new DateTime());
    path += DEFAULT_SEPARATOR;
    path += normalizeFilename(fileName);

    UriComponentsBuilder builder = UriComponentsBuilder.fromPath(getBasePath())
        .scheme(scheme)
        .userInfo(StringUtils.isNoneEmpty(user, password) ? user + ":" + password : null)
        .host(host);

    if (port != null) {
      builder.port(port);
    }

    if (StringUtils.isNotBlank(parentEntityName)) {
      builder.pathSegment(parentEntityName.toLowerCase());
    }

    if (parentEntityId > 0L) {
      builder.pathSegment(String.valueOf(parentEntityId));
    }

    UriComponents uri = builder.pathSegment(fileType.toLowerCase()).path(path).build();
    return uri.toString();
  }

  private String buildUriFromBasePath(final String basePath) {
    UriComponents uri;
    if (port != null) {
      uri = UriComponentsBuilder.fromPath(basePath)
          .scheme(scheme)
          .userInfo(StringUtils.isNoneEmpty(user, password) ? user + ":" + password : null)
          .host(host)
          .port(port)
          .build();
    } else {
      uri = UriComponentsBuilder.fromPath(basePath)
          .scheme(scheme)
          .userInfo(StringUtils.isNoneEmpty(user, password) ? user + ":" + password : null)
          .host(host)
          .build();
    }

    return uri.toString();
  }

  @VisibleForTesting
  FileDTO.Builder getFileDTOBuilderFromFileId(final String fileId) {
    String path = fileId;
    int pos = StringUtils.indexOf(fileId, getBasePath());
    if (pos >= 0) {
      path = StringUtils.mid(fileId, pos + StringUtils.length(getBasePath()), fileId.length());
    }

    String[] segments = StringUtils.split(path, '/');

    String parentEntityName = null;
    String parentEntityId   = null;
    String fileType         = null;
    String fileName         = null;

    if (segments.length == 4) {
      parentEntityName = segments[0];
      parentEntityId   = segments[1];
      fileType         = segments[2];
      fileName         = segments[3];
    } else if (segments.length == 3) {
      parentEntityId   = segments[0];
      fileType         = segments[1];
      fileName         = segments[2];
    } else if (segments.length == 2) {
      fileType         = segments[0];
      fileName         = segments[1];
    }

    pos = StringUtils.indexOf(fileName, "_");
    if (pos >= 0) {
      fileName = StringUtils.mid(fileName, pos+1, StringUtils.length(fileName));
    }

    pos = StringUtils.indexOf(fileName, ".");
    if (pos >= 0) {
      fileName = StringUtils.mid(fileName, 0, pos);
    }

    FileDTO.Builder builder = FileDTO.builder();
    builder.setParentEntityName(parentEntityName);
    builder.setFileType(fileType);
    builder.setFileName(fileName);

    if (StringUtils.isNumeric(parentEntityId)) {
      builder.setParentEntityId(Long.valueOf(parentEntityId));
    }

    return builder;
  }

  private FileDTO buildFileDTOFromFileObject(final FileObject obj, final boolean withContents) throws FileSystemException {
    byte[] fileContents = null;

    if (withContents) {
      InputStream in = null;
      try {
        in = obj.getContent().getInputStream();
        if (in == null) {
          String msg = "Failed reading file [" + obj.getName().getFriendlyURI() + "]";
          LOGGER.error(msg);
        } else {
          fileContents = IOUtils.toByteArray(in);
        }
      } catch (IOException ex) {
        String msg = "Failed reading file [" + obj.getName().getFriendlyURI() + "]";
        LOGGER.error(msg, ex);
      } finally {
        IOUtils.closeQuietly(in);
      }
    }

    String fileId = obj.getName().getFriendlyURI();

    return getFileDTOBuilderFromFileId(fileId)
        .setFileId(fileId)
        .setFileFormat(obj.getName().getExtension())
        .setFileContents(fileContents)
        .build();
  }
}
