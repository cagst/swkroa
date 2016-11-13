package com.cagst.swkroa.codevalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for CodeValueRepositoryJdbc class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class CodeValueRepositoryJdbcTest extends BaseTestRepository {
  private CodeValueRepositoryJdbc repo;

  private User user;

  @Before
  public void setUp() {
    repo = new CodeValueRepositoryJdbc(createTestDataSource());
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);

    user = new User();
    user.setUserUID(1L);
  }

  /**
   * Test the getCodeSetByUID method and not finding the CodeSet.
   */
  @Test
  public void testGetCodeSetByUID_NotFound() {
    CodeSet codeset = repo.getCodeSetByUID(999);
    assertNull("No codeset found!", codeset);
  }

  /**
   * Test the getCodeSetByUID method and finding the CodeSet.
   */
  @Test
  public void testGetCodeSetByUID_Found() {
    CodeSet codeset = repo.getCodeSetByUID(3L);
    assertNotNull("No codeset found!", codeset);
    assertEquals("EMAIL_TYPE", codeset.getMeaning());
  }

  /**
   * Test the getActiveCodeSets method.
   */
  @Test
  public void testGetActiveCodeSets() {
    List<CodeSet> codeSets = repo.getActiveCodeSets();
    assertNotNull("Ensure the codesets were found.", codeSets);
    assertFalse("Ensure the codeset list isn't empty.", codeSets.isEmpty());
    assertEquals("Ensure we found the correct number of code sets.", 5, codeSets.size());
  }

  /**
   * Test the getCodeValuesForCodeSet method and not finding any CodeValue(s).
   */
  @Test
  public void testGetCodeValuesForCodeSet_NotFound() {
    CodeSet codeset = repo.getCodeSetByUID(4L);
    assertNotNull("No codeset found!", codeset);
    assertEquals("DOCUMENT_TYPE", codeset.getMeaning());

    List<CodeValue> codevalues = repo.getCodeValuesForCodeSet(codeset);
    assertNotNull("Should not return NULL!", codevalues);
    assertTrue(codevalues.isEmpty());
  }

  /**
   * Test the getCodeValuesForCodeSet method and finding CodeValue(s).
   */
  @Test
  public void testGetCodeValuesForCodeSet_Found() {
    CodeSet codeset = repo.getCodeSetByUID(2L);
    assertNotNull("No codeset found!", codeset);
    assertEquals("PHONE_TYPE", codeset.getMeaning());

    List<CodeValue> codevalues = repo.getCodeValuesForCodeSet(codeset);
    assertNotNull("Should not return NULL!", codevalues);
    assertFalse(codevalues.isEmpty());
    assertEquals(3, codevalues.size());
  }

  /**
   * Test the getCodeValuesForCodeSetByMeaning and finding CodeValue(s).
   */
  @Test
  public void testGetCodeValuesForCodeSetByMeaning_Found() {
    List<CodeValue> codevalues = repo.getCodeValuesForCodeSetByType(CodeSetType.PHONE_TYPE);
    assertNotNull("Should not return NULL!", codevalues);
    assertFalse(codevalues.isEmpty());
    assertEquals(3, codevalues.size());
  }

  /**
   * Test the getCodeValueByUID method and not finding any.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetCodeValueByUID_NotFound() {
    repo.getCodeValueByUID(99L);
  }

  /**
   * Test the getCodeValueByUID method and finding it.
   */
  @Test
  public void testGetCodeValueByUID_Found() {
    CodeValue codevalue = repo.getCodeValueByUID(1L);
    assertNotNull("Ensure the codevalue was found.", codevalue);
    assertEquals("Ensure we found the correct codevalue.", 1, codevalue.getCodeValueUID());
    assertEquals("Ensure we found the correct codevalue.", "HOME", codevalue.getMeaning());
  }

  /**
   * Test the getCodeValueByMeaning and not finding any.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetCodeValueByMeaning_NoneFound() {
    CodeValue codevalue = repo.getCodeValueByMeaning(CodeSetType.PHONE_TYPE, "IPHONE");
    assertNull("Ensure a codevalue was not found.", codevalue);
  }

  /**
   * Test the getCodeValueByMeaning and finding one.
   */
  @Test
  public void testGetCodeValueByMeaning_OneFound() {
    CodeValue codevalue = repo.getCodeValueByMeaning(CodeSetType.PHONE_TYPE, "FAX");
    assertNotNull("Ensure a codevalue was found.", codevalue);
    assertEquals("Ensure it was the correct codevalue.", "FAX", codevalue.getMeaning());
  }

  /**
   * Test the saveCodeValue method and inserting a CodeValue.
   */
  @Test
  public void testSaveCodeValue_Insert() {
    CodeSet codeSet = repo.getCodeSetByUID(2L);

    List<CodeValue> codevalues1 = repo.getCodeValuesForCodeSet(codeSet);
    assertNotNull("Should not return NULL!", codevalues1);
    assertFalse(codevalues1.isEmpty());
    assertEquals(3, codevalues1.size());

    CodeValue builder = CodeValue.builder()
        .setCodeSetUID(2L)
        .setDisplay("TEST_DISPLAY")
        .setMeaning("TEST_MEANING")
        .build();

    CodeValue cv = repo.saveCodeValueForCodeSet(builder, user);
    assertNotNull("Ensure the codevalue is not null.", cv);
    assertEquals("Ensure it is the same codevalue.", "TEST_DISPLAY", cv.getDisplay());
    assertTrue("Ensure it has a valid UID.", cv.getCodeValueUID() > 0L);

    List<CodeValue> codevalues2 = repo.getCodeValuesForCodeSet(codeSet);
    assertNotNull("Should not return NULL!", codevalues2);
    assertFalse(codevalues2.isEmpty());
    assertEquals(4, codevalues2.size());
  }

  /**
   * Test the saveCodeValue method and updating a CodeValue.
   */
  @Test
  public void testSaveCodeValue_Update() {
    CodeValue codevalue = repo.getCodeValueByMeaning(CodeSetType.PHONE_TYPE, "FAX");
    assertNotNull("Ensure a codevalue was found.", codevalue);
    assertEquals("Ensure it was the correct codevalue.", "FAX", codevalue.getMeaning());

    String newDisplay = codevalue.getDisplay() + "-EDITED";
    CodeValue editedCodeValue = CodeValue.builder(codevalue)
        .setDisplay(newDisplay)
        .build();

    CodeValue cv = repo.saveCodeValueForCodeSet(editedCodeValue, user);
    assertNotNull("Ensure the codevalue is not null.", cv);
    assertEquals("Ensure it has been edited.", newDisplay, cv.getDisplay());
    assertTrue("Ensure it has a valid UID.", cv.getCodeValueUpdateCount() > 0L);
  }

  /**
   * Test the saveCodeValue method and updating a CodeValue but failing (due to an invalid update
   * count).
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveCodeValue_Update_Failed() {
    CodeValue cv = repo.getCodeValueByMeaning(CodeSetType.PHONE_TYPE, "FAX");
    assertNotNull("Ensure a codevalue was found.", cv);
    assertEquals("Ensure it was the correct codevalue.", "FAX", cv.getMeaning());

    String newDisplay = cv.getDisplay() + "-EDITED";

    CodeValue editedCodeVale = CodeValue.builder(cv)
        .setDisplay(newDisplay)
        .setCodeValueUpdateCount(cv.getCodeValueUpdateCount() + 1)
        .build();

    repo.saveCodeValueForCodeSet(editedCodeVale, user);
  }
}
