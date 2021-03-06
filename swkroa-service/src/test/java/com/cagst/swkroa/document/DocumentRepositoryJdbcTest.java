package com.cagst.swkroa.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Test class for the {@link DocumentRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class DocumentRepositoryJdbcTest extends BaseTestRepository {
  private DocumentRepositoryJdbc repo;
  private CodeValueRepository codeValueRepo = mock(CodeValueRepository.class);

  private CodeValue renewalCodeValue;
  private User user;

  @Before
  public void setUp() throws Exception {
    renewalCodeValue = CodeValue.builder()
        .setCodeValueUID(1L)
        .setDisplay("Renewal Letter")
        .setMeaning("RENEWAL")
        .build();


    when(codeValueRepo.getCodeValueByUID(anyLong())).thenReturn(renewalCodeValue);

    repo = new DocumentRepositoryJdbc(createTestDataSource(), codeValueRepo);

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
    List<Document> documents = repo.getDocumentsForMembership(1L);
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

    List<Document> documents = repo.getDocumentsForMembership(1L);
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
