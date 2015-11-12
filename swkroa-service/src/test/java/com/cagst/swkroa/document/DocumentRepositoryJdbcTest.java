package com.cagst.swkroa.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.cagst.swkroa.filesystem.FileSystem;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.test.BaseTestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * Test class for the {@link DocumentRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class DocumentRepositoryJdbcTest extends BaseTestRepository {
  private DocumentRepositoryJdbc repo;
  private FileSystem fileSystem = Mockito.mock(FileSystem.class);

  @Before
  public void setUp() {
    repo = new DocumentRepositoryJdbc(createTestDataSource(), fileSystem);
  }

  /**
   * Test the getDocumentByUID and not finding the Document.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetDocumentByUID_NotFound() {
    repo.getDocumentByUID(999L);
  }

  /**
   * Test the getDocumentByUID and finding the Document.
   */
  @Test
  public void testGetDocumentByUID_Found() {
    Document document = repo.getDocumentByUID(1L);

    assertNotNull("Ensure document was found", document);
    assertEquals("Ensure we found the correct document", 1L, document.getDocumentUID());
    assertEquals("Ensure we found the correct document", "2013 - 2014 Renewal Letter", document.getDocumentDescription());
  }

  /**
   * Test the getDocumentsForMembership and not finding any documents.
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
}
