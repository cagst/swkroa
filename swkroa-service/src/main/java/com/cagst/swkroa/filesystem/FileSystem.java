package com.cagst.swkroa.filesystem;

import javax.annotation.Nullable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.Optional;

import org.apache.commons.vfs2.FileSystemException;

/**
 * Defines methods available to persist files to an external filesystem instead of the database.
 *
 * @author Craig Gaskill
 */
public interface FileSystem {
  /**
   * Retrieves a file by its unique identifier.
   *
   * @param fileId
   *      A {@link String} that uniquely identifies the file to retrieve.
   *
   * @return An {@link Optional} that may contain a {@link FileDTO} that is associated with the specified fileId.
   *
   * @throws NoSuchFileException if the file is not found.
   * @throws FileSystemException if any other error occurs.
   */
  Optional<FileDTO> getFileById(final String fileId) throws NoSuchFileException, FileSystemException;

  /**
   * Persists a file to persistent storage.
   *
   * @param parentEntityName
   *    A {@link String} that defines the entity this document is associated with.
   * @param parentEntityId
   *    A {@link long} that uniquely identifies the entity this document is associated with.
   * @param fileName
   *    A {@link String} that gives a name to the file.
   * @param fileType
   *    A {@link String} that defines the type of the file (i.e. NEWS_LETTER, RENEWAL_NOTICE, etc.).
   * @param fileFormat
   *    A {@link String} that defines the type of the file (i.e. PDF, XML, etc.).
   * @param fileContents
   *    A {@link byte} array that make up the contents of the file.
   *
   * @return An {@link Optional} that may contain a {@link FileDTO} after it has been successfully persisted.
   *
   * @throws FileAlreadyExistsException if the file already exists in the persistent store.
   * @throws FileSystemException for any other exception.
   */
  Optional<FileDTO> saveFileForEntity(final @Nullable String parentEntityName,
                                      final long parentEntityId,
                                      final String fileName,
                                      final String fileType,
                                      final String fileFormat,
                                      final byte[] fileContents)
    throws FileAlreadyExistsException, FileSystemException;

  /**
   * Deletes a file specified by its unique identifier from the persistent store.
   *
   * @param fileId
   *      A {@link String} that uniquely identifies the file to delete from the persistent store.
   *
   * @return {@code true} if the file was successfully deleted from the persistent store, false otherwise.
   *
   * @throws NoSuchFileException if the file is not found.
   * @throws FileSystemException if any other error occurs.
   */
  boolean deleteFileById(final String fileId) throws NoSuchFileException, FileSystemException;
}
