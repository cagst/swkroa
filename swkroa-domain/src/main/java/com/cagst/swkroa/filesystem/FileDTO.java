package com.cagst.swkroa.filesystem;

/**
 * Represents a File stored in an offline persistent store.
 *
 * @author Craig Gaskill
 */
public class FileDTO {
  private final String fileId;
  private final String parentEntityName;
  private final long parentEntityId;
  private final String fileName;
  private final String fileType;
  private final String fileFormat;
  private final byte[] fileContents;

  /**
   * Private Constructor to force the Builder design pattern.
   *
   * @param fileId
   *    A {@link String} that uniquely identifies the file.
   * @param parentEntityName
   *    A {@link String} that identifies the entity the file is associated with.
   * @param parentEntityId
   *    A {@link long} that uniquely identifies the entity the file is associated with.
   * @param fileName
   *    A {@link String} that gives a name to the file.
   * @param fileType
   *    A {@link String} that defines the type of the file (i.e. NEWS_LETTER, RENEWAL_NOTICE, etc.).
   * @param fileFormat
   *    A {@link String} that defines the type of the file (i.e. PDF, XML, etc.).
   * @param fileContents
   *    A {@link byte} array that make up the contents of the file.
   */
  private FileDTO(final String fileId,
                  final String parentEntityName,
                  final long parentEntityId,
                  final String fileName,
                  final String fileType,
                  final String fileFormat,
                  final byte[] fileContents
  ) {
    this.fileId = fileId;
    this.parentEntityName = parentEntityName;
    this.parentEntityId = parentEntityId;
    this.fileName = fileName;
    this.fileType = fileType;
    this.fileFormat = fileFormat;
    this.fileContents = fileContents;
  }

  public String getFileId() {
    return fileId;
  }

  public String getParentEntityName() {
    return parentEntityName;
  }

  public long getParentEntityId() {
    return parentEntityId;
  }

  public String getFileName() {
    return fileName;
  }

  public String getFileType() {
    return fileType;
  }

  public String getFileFormat() {
    return fileFormat;
  }

  public byte[] getFileContents() {
    return fileContents;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String fileId;
    private String parentEntityName;
    private long parentEntityId;
    private String fileName;
    private String fileType;
    private String fileFormat;
    private byte[] fileContents;

    public Builder setFileId(final String fileId) {
      this.fileId = fileId;
      return this;
    }

    public Builder setParentEntityName(final String parentEntityName) {
      this.parentEntityName = parentEntityName;
      return this;
    }

    public Builder setParentEntityId(final long parentEntityId) {
      this.parentEntityId = parentEntityId;
      return this;
    }

    public Builder setFileName(final String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder setFileType(final String fileType) {
      this.fileType = fileType;
      return this;
    }

    public Builder setFileFormat(final String fileFormat) {
      this.fileFormat = fileFormat;
      return this;
    }

    public Builder setFileContents(final byte[] fileContents) {
      this.fileContents = fileContents;
      return this;
    }

    public FileDTO build() {
      return new FileDTO(fileId, parentEntityName, parentEntityId, fileName, fileType, fileFormat, fileContents);
    }
  }
}
