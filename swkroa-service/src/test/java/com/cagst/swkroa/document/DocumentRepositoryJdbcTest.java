package com.cagst.swkroa.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.filesystem.FileDTO;
import com.cagst.swkroa.filesystem.FileSystem;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Test class for the {@link DocumentRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class DocumentRepositoryJdbcTest extends BaseTestRepository {
  private DocumentRepositoryJdbc repo;
  private FileSystem fileSystem = mock(FileSystem.class);
  private CodeValueRepository codeValueRepo = mock(CodeValueRepository.class);

  private static final String FILE_LOCATION = "file://swkroa/document/store/renewal/testfile.txt";

  private CodeValue renewalCodeValue;
  private User user;

  @Before
  public void setUp() throws Exception {
    FileDTO testFile = FileDTO.builder()
        .setFileId(FILE_LOCATION)
        .setFileName("testfile.txt")
        .setFileFormat("txt")
        .setFileType("RENEWAL")
        .build();

    when(fileSystem.saveFileForEntity(anyString(), anyLong(), anyString(), anyString(), anyString(), anyObject()))
        .thenReturn(Optional.of(testFile));

    renewalCodeValue = new CodeValue();
    renewalCodeValue.setCodeValueUID(1L);
    renewalCodeValue.setDisplay("Renewal Letter");
    renewalCodeValue.setMeaning("RENEWAL");

    when(codeValueRepo.getCodeValueByUID(anyLong())).thenReturn(renewalCodeValue);

    repo = new DocumentRepositoryJdbc(createTestDataSource(), fileSystem, codeValueRepo);

    user = new User();
    user.setUserUID(1L);
  }

  /**
   * Test the getDocumentByUID method and not finding the Document.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetDocumentByUID_NotFound() {
    repo.getDocumentByUID(999L);
  }

  /**
   * Test the getDocumentByUID method and finding the Document.
   */
  @Test
  public void testGetDocumentByUID_Found() {
    Document document = repo.getDocumentByUID(1L);

    assertNotNull("Ensure document was found", document);
    assertEquals("Ensure we found the correct document", 1L, document.getDocumentUID());
    assertEquals("Ensure we found the correct document", "2013 - 2014 Renewal Letter", document.getDocumentDescription());
  }

  /**
   * Test the getDocumentsForMembership method and not finding any documents.
   */
  @Test
  public void testGetDocumentsForMembership() {
    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    List<Document> documents = repo.getDocumentsForMembership(membership);
    assertNotNull("Ensure we have a valid collection", documents);
    assertFalse("Ensure the collection is not empty", documents.isEmpty());
    assertEquals("Ensure we found the correct number of documents", 3, documents.size());
  }

  /**
   * Test the getGlobalDocuments method.
   */
  @Test
  public void testGetGlobalDocuments() {
    List<Document> documents = repo.getGlobalDocuments();
    assertNotNull("Ensure we have a valid collection", documents);
    assertFalse("Ensure the collection is not empty", documents.isEmpty());
    assertEquals("Ensure we found the correct number of documents", 2, documents.size());
  }

  /**
   * Test the saveDocument method by inserting a new document.
   */
  @Test
  public void testSaveDocument_Insert() {
    Document document = new Document();
    document.setDocumentDescription("Document used for testing.");
    document.setDocumentName("testfile.txt");
    document.setDocumentFormat("txt");
    document.setDocumentType(renewalCodeValue);
    document.setDocumentContents("Some test data to be places in the document.\nAnd more on another line".getBytes());
    document.setBeginEffectiveDate(new DateTime());

    Document savedDocument = repo.saveDocument(document, user);
    assertNotNull("Ensure the document object is valid", savedDocument);
    assertTrue("Ensure it has an ID", savedDocument.getDocumentUID() > 0L);
    assertEquals("Ensure it has the same description", document.getDocumentDescription(), savedDocument.getDocumentDescription());
    assertEquals("Ensure it has the proper update count", 0, document.getDocumentUpdateCount());
    assertNull("Ensure the location was not set", document.getDocumentLocation());

    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    List<Document> documents = repo.getDocumentsForMembership(membership);
    assertNotNull("Ensure we have a valid collection", documents);
    assertFalse("Ensure the collection is not empty", documents.isEmpty());
    assertEquals("Ensure we found the correct number of documents", 4, documents.size());
  }

  /**
   * Test the saveDocument method by inserting a new document that exceeds the size we want to store in the DB.
   */
  @Test
  public void testSaveDocument_Insert_Large() throws IOException {
    InputStream in = this.getClass().getResourceAsStream("CarrotCakeCheesecake.pdf");

    Document document = new Document();
    document.setDocumentDescription("Another document used for testing.");
    document.setDocumentName("testfile.txt");
    document.setDocumentFormat("txt");
    document.setDocumentType(renewalCodeValue);
    document.setDocumentContents(IOUtils.toByteArray(in));
    document.setBeginEffectiveDate(new DateTime());

    IOUtils.closeQuietly(in);

    Document savedDocument = repo.saveDocument(document, user);
    assertNotNull("Ensure the document object is valid", savedDocument);
    assertTrue("Ensure it has an ID", savedDocument.getDocumentUID() > 0L);
    assertEquals("Ensure it has the same description", document.getDocumentDescription(), savedDocument.getDocumentDescription());
    assertEquals("Ensure it has the proper update count", 0, document.getDocumentUpdateCount());
    assertNotNull("Ensure the location was set", document.getDocumentLocation());
    assertNull("Ensure the contents where cleared", document.getDocumentContents());

    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    List<Document> documents = repo.getDocumentsForMembership(membership);
    assertNotNull("Ensure we have a valid collection", documents);
    assertFalse("Ensure the collection is not empty", documents.isEmpty());
    assertEquals("Ensure we found the correct number of documents", 4, documents.size());
  }

  /**
   * Test the saveDocument method by updating an existing document.
   */
  @Test
  public void testSaveDocument_Update() {
    Document document = repo.getDocumentByUID(1L);

    assertNotNull("Ensure document was found", document);
    assertEquals("Ensure we found the correct document", 1L, document.getDocumentUID());
    assertEquals("Ensure we found the correct document", "2013 - 2014 Renewal Letter", document.getDocumentDescription());
    assertNull("Ensure the contents is null", document.getDocumentContents());
    assertEquals("Ensure the update count is correct", 0, document.getDocumentUpdateCount());

    String newDocDesc = document.getDocumentDescription() + "_EDITED";

    document.setDocumentDescription(newDocDesc);
    document.setDocumentContents("Some test data to be places in the document.\nAnd more on another line".getBytes());

    Document savedDocument = repo.saveDocument(document, user);
    assertNotNull("Ensure the document object is valid", savedDocument);
    assertEquals("Ensure the information was updated correctly", document.getDocumentDescription(), savedDocument.getDocumentDescription());
    assertEquals("Ensure the information was updated correctly", 1, savedDocument.getDocumentUpdateCount());

    Document retrievedDocument = repo.getDocumentByUID(1L);
    assertNotNull("Ensure the document object is valid", retrievedDocument);
    assertEquals("Ensure the information was updated correctly", document.getDocumentDescription(), retrievedDocument.getDocumentDescription());
    assertEquals("Ensure the information was updated correctly", 1, retrievedDocument.getDocumentUpdateCount());
  }
}
