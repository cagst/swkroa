package com.cagst.swkroa.role;

import java.util.List;
import java.util.Optional;

import com.cagst.swkroa.user.User;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * Definition of a repository that retrieves and persists {@link Role} objects.
 *
 * @author Craig Gaskill
 */
public interface RoleRepository {
  /**
   * Retrieves the {@link List} of {@link Role Roles} associated with the specified
   * {@link User}.
   *
   * @param user
   *     The {@link User} to retrieve the Role for.
   *
   * @return The {@link List} of {@link Role Roles} associated with the specified user.
   */
  List<Role> getRolesForUser(User user);

  /**
   * @return A {@link List} of active {@link Role Roles} defined within the system.
   */
  List<Role> getActiveRoles();

  /**
   * Retrieves a {@link Role} associated with the specified mnemonic.
   *
   * @param mnemonic
   *    A {@link String} that identifies the {@link Role} to retrieve.
   *
   * @return An {@link Optional} that will contain a {@link Role} associated with the specified key if found.
   *
   * @throws IncorrectResultSizeDataAccessException if more than 1 role was found
   */
  Optional<Role> getRoleByKey(String mnemonic) throws IncorrectResultSizeDataAccessException;
}
