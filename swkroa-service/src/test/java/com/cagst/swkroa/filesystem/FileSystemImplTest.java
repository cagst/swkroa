package com.cagst.swkroa.filesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileSystemException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.NoSuchFileException;
import java.util.Optional;

/**
 * Test class for the {@link FileSystemImpl} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class FileSystemImplTest {
  private FileSystemImpl fileSystem;

  private final String FILE_CONTENT = "Just some text to be placed in the file.\nAnd to prove we can span lines.";

  private FileDTO file1;
  private FileDTO file2;

  @Before
  public void setUp() throws Exception {
    String userHome = System.getProperty("user.home");

    fileSystem = new FileSystemImpl();
    fileSystem.setBasePath(userHome + "/testing/document/store");

    file1 = fileSystem.saveFileForEntity("TEST", 123, "FILE_NAME", "FILE_TYPE", "txt", FILE_CONTENT.getBytes()).get();
    file2 = fileSystem.saveFileForEntity(null, 0, "FILE_NAME", "FILE_TYPE", "txt", FILE_CONTENT.getBytes()).get();
  }

  @After
  public void tearDown() {
    try {
      fileSystem.deleteFileById(file1.getFileId());
    } catch (Exception ex) {
      // Ignore any exception during teardown
    }

    try {
      fileSystem.deleteFileById(file2.getFileId());
    } catch (Exception ex) {
      // Ignore any exception during teardown
    }
  }

  /**
   * Test the normalizeFilename method.
   */
  @Test
  public void testNormalizeFilename() throws Exception {
    String nullString = fileSystem.normalizeFilename(null);
    assertNull("Ensure the normalized filename is null.", nullString);

    String emptyString = fileSystem.normalizeFilename(StringUtils.EMPTY);
    assertNotNull("Ensure the normalized filename is not null.", emptyString);
    assertTrue("Ensure the normalized filename is empty.", emptyString.isEmpty());

    String spacedString = fileSystem.normalizeFilename(" News \t Letter ");
    assertNotNull("Ensure the normalized filename is not null.", spacedString);
    assertEquals("Ensure the normalized filename was trimed of spaces and replaces with underscore", "News_Letter", spacedString);

    String accentString = fileSystem.normalizeFilename(" News \t Létter ");
    assertNotNull("Ensure the normalized filename is not null.", accentString);
    assertEquals("Ensure the normalized filename has accents stripped out and replaces with an appropriate replacement", "News_Letter", accentString);
  }

  /**
   * Test the buildUri method.
   */
  @Test
  public void testBuildUri() {
    String baseUri1 = fileSystem.buildUri("MEMBERSHIP", 123, "2015 - 2016 Membership Renewal", "RENEWAL_LETTER", "pdf");
    assertNotNull("Ensure the path is not null", baseUri1);

    String expectedStart1 = fileSystem.getScheme() + ":" + fileSystem.getBasePath() + "membership/123/renewal_letter/";
    String expectedEnd1   = "2015_2016_Membership_Renewal.pdf";

    assertTrue("Ensure the client base path is built correctly for the default schema.", baseUri1.startsWith(expectedStart1));
    assertTrue("Ensure the client base path is built correctly for the default schema.", baseUri1.endsWith(expectedEnd1));

    String baseUri2 = fileSystem.buildUri(null, 0, "2016 Newsletter", "NEWS_LETTER", "pdf");
    assertNotNull("Ensure the path is not null", baseUri2);

    String expectedStart2 = fileSystem.getScheme() + ":" + fileSystem.getBasePath() + "news_letter/";
    String expectedEnd2   = "2016_Newsletter.pdf";

    assertTrue("Ensure the client base path is built correctly for the default schema.", baseUri2.startsWith(expectedStart2));
    assertTrue("Ensure the client base path is built correctly for the default schema.", baseUri2.endsWith(expectedEnd2));
  }

  /**
   * Test the getFileDTOBuilderFromFileId method.
   */
  @Test
  public void testGetFileDTOBuilderFromFileId() {
    FileDTO fileDTO1 = fileSystem.getFileDTOBuilderFromFileId(file1.getFileId()).build();
    assertNotNull("Ensure the FileDTO was build", fileDTO1);
    assertNotNull("Ensure the FileDTO was built successfully", fileDTO1.getParentEntityName());
    assertEquals("Ensure the FileDTO was built successfully", "test", fileDTO1.getParentEntityName());
    assertEquals("Ensure the FileDTO was built successfully", 123, fileDTO1.getParentEntityId());
    assertNotNull("Ensure the FileDTO was built successfully", fileDTO1.getFileType());
    assertEquals("Ensure the FileDTO was built successfully", "file_type", fileDTO1.getFileType());
    assertNotNull("Ensure the FileDTO was built successfully", fileDTO1.getFileName());
    assertEquals("Ensure the FileDTO was built successfully", "FILE_NAME", fileDTO1.getFileName());

    FileDTO fileDTO2 = fileSystem.getFileDTOBuilderFromFileId(file2.getFileId()).build();
    assertNull("Ensure the FileDTO was built successfully", fileDTO2.getParentEntityName());
    assertEquals("Ensure the FileDTO was built successfully", 0, fileDTO2.getParentEntityId());
    assertNotNull("Ensure the FileDTO was built successfully", fileDTO2.getFileType());
    assertEquals("Ensure the FileDTO was built successfully", "file_type", fileDTO2.getFileType());
    assertNotNull("Ensure the FileDTO was built successfully", fileDTO2.getFileName());
    assertEquals("Ensure the FileDTO was built successfully", "FILE_NAME", fileDTO2.getFileName());
  }

  /**
   * Tes the getFileById method and not finding the file.
   */
  @Test(expected = NoSuchFileException.class)
  public void testGetFileById_NotFound() throws Exception {
    fileSystem.getFileById(file1.getFileId() + "_MUCKITUP");
  }

  /**
   * Tes the getFileById method and finding the file.
   */
  @Test
  public void testGetFileById_Found() throws Exception {
    Optional<FileDTO> dto = fileSystem.getFileById(file1.getFileId());

    assertTrue("Ensure the file was found", dto.isPresent());
    assertEquals("Ensure the file was found", "test", dto.get().getParentEntityName());
    assertEquals("Ensure the file was found", 123, dto.get().getParentEntityId());
    assertEquals("Ensure the file was found", "file_type", dto.get().getFileType());
    assertEquals("Ensure the file was found", "FILE_NAME", dto.get().getFileName());
  }

  /**
   * Test the saveFileForEntity method using an invalid base location.
   */
  @Test(expected = FileSystemException.class)
  public void testSaveFileForEntity_InvalidBase() throws Exception {
    fileSystem.setBasePath("e:\\document\\store\\");
    fileSystem.saveFileForEntity("MEMBERSHIP", 123, "filename", "RENEWAL_LETTER", "txt", FILE_CONTENT.getBytes());
  }

  /**
   * Test the saveFileForEntity method with invalid parameters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSaveFileForEntity_InvalidArgument() throws Exception {
    fileSystem.saveFileForEntity("MEMBERSHIP", 123, "filename", null, "txt", null);
  }

  /**
   * Test the saveFileForEntity method using the local file system.
   */
  @Test
  public void testSaveFileForEntity_LocalFileSystem() throws Exception {
    Optional<FileDTO> dto = fileSystem.saveFileForEntity("MEMBERSHIP", 123, "filename", "RENEWAL_LETTER", "txt", FILE_CONTENT.getBytes());

    assertNotNull("Ensure we have a valid dto", dto);
    assertTrue("Ensure we have a valid dto", dto.isPresent());
    assertNotNull("Ensure the id is populated", dto.get().getFileId());

    // cleanup
    fileSystem.deleteFileById(dto.get().getFileId());
  }

  /**
   * Test the saveFileForEntity method using sftp file system.
   */
  @Test
  @Ignore
  public void testSaveFileForEntity_SftpFileSystem() throws Exception {
    fileSystem.setScheme("sftp");
    fileSystem.setUser("vagrant");
    fileSystem.setPassword("vagrant");
    fileSystem.setHost("127.0.0.1");
    fileSystem.setPort(2222);
    fileSystem.setBasePath("/caremanager/document/store/");

    Optional<FileDTO> dto = fileSystem.saveFileForEntity("MEMBERSHIP", 123, "filename", "RENEWAL_LETTER", "txt", FILE_CONTENT.getBytes());

    assertNotNull("Ensure we have a valid dto", dto);
    assertNotNull("Ensure we have a valid dtO", dto.isPresent());
    assertNotNull("Ensure the id is populated", dto.get().getFileId());

    // cleanup
    fileSystem.deleteFileById(dto.get().getFileId());
  }

  /**
   * Test the deleteFileById method and not finding the file to delete.
   */
  @Test(expected = NoSuchFileException.class)
  public void testDeleteFile_NotFound() throws Exception {
    fileSystem.deleteFileById("file:/testing/document/store/123/renewal_letter/201511011855_Renewal_Letter.txt");
  }

  /**
   * Test the deleteFileById method and finding the file to delete.
   */
  @Test
  public void testDeleteFile_Found() throws Exception {
    boolean deleted = fileSystem.deleteFileById(file1.getFileId());
    assertTrue("Ensure the file was deleted", deleted);
  }

}
