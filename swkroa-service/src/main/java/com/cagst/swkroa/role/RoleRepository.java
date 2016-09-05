package com.cagst.swkroa.role;

import com.cagst.swkroa.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Definition of a repository that retrieves and persists {@link Role} and {@link Privilege}
 * objects.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface RoleRepository {
  /**
   * Retrieves the {@link Collection} of {@link Role Roles} associated with the specified
   * {@link User}.
   *
   * @param user
   *     The {@link User} to retrieve the Role for.
   *
   * @return The {@link Collection} of {@link Role Roles} associated with the specified user.
   */
  public Collection<Role> getRolesForUser(final User user);

  /**
   * @return A {@link List} of active {@link Role Roles} defined within the system.
   */
  public List<Role> getActiveRoles();

  /**
   * @return A {@link List} of active {@link Privileges} defined within the system.
   */
  public List<Privilege> getActivePrivileges();

  /**
   * Retrieves a {@link List} of {@link Privilege Privileges} associated with the specified
   * {@link Role}.
   *
   * @param role
   *     The {@link Role} to retrieve the privileges for.
   *
   * @return A {@link List} of {@link Privilege Privileges} associated with the specified Role.
   */
  public List<Privilege> getPrivilegesForRole(final Role role);

  /**
   * Retrieves a {@link Set} of {@link Privilege Privileges} associated with the specified
   * {@link User}.
   *
   * @param user
   *     The {@link User} to retrieve the privileges for.
   *
   * @return A {@link Set} of {@link Privilege Privileges} associated with the specified User.
   */
  public Set<Privilege> getPrivilegesForUser(final User user);
}
