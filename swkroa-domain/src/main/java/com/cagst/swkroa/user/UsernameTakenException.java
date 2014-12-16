package com.cagst.swkroa.user;

/**
 * Thrown if a {@link UserRepository} or {@link UserService} implementation cannot save a
 * {@link User} because the username is already being used.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public class UsernameTakenException extends RuntimeException {
  private static final long serialVersionUID = -6984306087180818384L;

  /**
   * Constructs a {@code UsernameTakenException} with the specified message.
   *
   * @param msg
   *     The detailed message.
   */
  public UsernameTakenException(final String msg) {
    super(msg);
  }

  /**
   * Constructs a {@code UsernameTakenException} with the specified message and root cause.
   *
   * @param msg
   *     The detailed message.
   * @param ex
   *     A {@link Throwable} that represents the root cause.
   */
  public UsernameTakenException(final String msg, final Throwable ex) {
    super(msg, ex);
  }
}
