package com.cagst.swkroa.comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import com.cagst.swkroa.internal.StatementDialect;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for the {@link CommentRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class CommentRepositoryJdbcTest extends BaseTestRepository {
  private CommentRepositoryJdbc repo;

  @Before
  public void setUp() {
    User user = new User();
    user.setUserUID(11L);

    UserRepository userRepo = Mockito.mock(UserRepository.class);
    Mockito.when(userRepo.getUserByUID(11L)).thenReturn(user);

    repo = new CommentRepositoryJdbc(createTestDataSource());
    repo.setStatementDialect(StatementDialect.HSQLDB);
  }

  /**
   * Tests the getCommentByUID and not finding the Comment.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetCommentByUID_NotFound() {
    repo.getCommentByUID(999L);
  }

  /**
   * Tests the getCommentByUID and finding the Comment.
   */
  @Test
  public void testGetCommentByUID_Found() {
    Comment comment = repo.getCommentByUID(1L);

    assertNotNull("Ensure comment was found.", comment);
    assertEquals("Ensure we found the correct comment.", 1L, comment.getCommentUID());
    assertEquals("Ensure we found the correct comment.", "TEST COMMENT 1", comment.getCommentText());
    assertNotNull("Ensure we have a comment date.", comment.getCommentDate());
  }

  /**
   * Tests the getCommentsForMembership and not finding any Comments.
   */
  @Test
  public void testGetCommentsForMembership_NoneFound() {
    Membership membership = new Membership();
    membership.setMembershipUID(999L);

    List<Comment> comments = repo.getCommentsForMembership(membership);
    assertNotNull("Ensure we have a valid collection.", comments);
    assertTrue("Ensure the collection is empty.", comments.isEmpty());
  }

  /**
   * Tests the getCommentsForMembership and finding comments.
   */
  @Test
  public void testGetCommentsForMembership_Found() {
    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    List<Comment> comments = repo.getCommentsForMembership(membership);
    assertNotNull("Ensure we have a valid collection.", comments);
    assertFalse("Ensure the collection is empty.", comments.isEmpty());
    assertEquals("Ensure the collection has the correct number of comments.", 2, comments.size());
  }

  /**
   * Test the saveComment method by inserting a new comment.
   */
  @Test
  public void testSaveComment_Insert() {
    String msg = "My Test Comment";

    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    User user = new User();
    user.setUserUID(11L);

    Comment comment = new Comment();
    comment.setParentEntityName("MEMBERSHIP");
    comment.setParentEntityUID(1L);
    comment.setCommentDate(LocalDate.now());
    comment.setCommentText(msg);

    Comment newComment = repo.saveComment(comment, user);
    assertNotNull("Ensure we have a new Comment.", newComment);
    assertTrue("Ensure our new Comment has an Id.", newComment.getCommentUID() > 0L);
    assertNotNull("Ensure it has a comment date.", newComment.getCommentDate());

    List<Comment> comments = repo.getCommentsForMembership(membership);
    assertNotNull("Ensure we have a valid collection.", comments);
    assertFalse("Ensure the collection is emtpy.", comments.isEmpty());
    assertEquals("Ensure the collection has the correct number of comments.", 3, comments.size());
  }

  /**
   * Test the saveComment method by updating an existing comment.
   */
  @Test
  public void testSaveComment_Update() {
    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    User user = new User();
    user.setUserUID(12L);

    Comment comment1 = repo.getCommentByUID(1L);
    assertNotNull("Ensure we found a comment.", comment1);

    String newComment = comment1.getCommentText() + "- EDITED";
    comment1.setCommentText(newComment);

    Comment updatedComment = repo.saveComment(comment1, user);
    assertNotNull("Ensure we have a valid comment.", updatedComment);
    assertEquals("Ensure the comment was updated.", 1L, updatedComment.getCommentUpdateCount());
    assertEquals("Ensure it is the same comment.", newComment, updatedComment.getCommentText());

    Comment comment2 = repo.getCommentByUID(1L);
    assertNotNull("Ensure we found the comment.", comment2);
    assertEquals("Ensure it was updated.", newComment, comment2.getCommentText());
  }

  /**
   * Test the saveComment method by updating an existing Comment but failing due to update count mismatch.
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveComment_Update_Failed() {
    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    User user = new User();
    user.setUserUID(12L);

    Comment comment1 = repo.getCommentByUID(1L);
    assertNotNull("Ensure we found a comment.", comment1);

    String newComment = comment1.getCommentText() + "- EDITED";
    comment1.setCommentText(newComment);

    // force a failure by manually updating the update count.
    comment1.setCommentUpdateCount(comment1.getCommentUpdateCount() + 1);

    repo.saveComment(comment1, user);
  }
}
