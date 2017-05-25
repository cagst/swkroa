package com.cagst.swkroa.role;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

/**
 * JDBC Template implementation of the {@link RoleRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("roleRepository")
/* package */ final class RoleRepositoryJdbc extends BaseRepositoryJdbc implements RoleRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryJdbc.class);

  private static final String GET_ROLES_FOR_USER = "GET_ROLES_FOR_USER";
  private static final String GET_ACTIVE_ROLES   = "GET_ACTIVE_ROLES";
  private static final String GET_ROLE_BY_KEY    = "GET_ROLE_BY_KEY";

  /**
   * Primary Constructor used to create an instance of <i>RoleRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public RoleRepositoryJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public List<Role> getRolesForUser(User user) {
    Assert.notNull(user, "Argument [user} cannot be null.");

    LOGGER.info("Calling getRolesForUser [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(
        stmtLoader.load(GET_ROLES_FOR_USER),
        new MapSqlParameterSource("user_id", user.getUserUID()),
        new RoleMapper());
  }

  @Override
  public List<Role> getActiveRoles() {
    LOGGER.info("Calling getActiveRoles.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    return getJdbcTemplate().query(stmtLoader.load(GET_ACTIVE_ROLES), new RoleMapper());
  }

  @Override
  public Optional<Role> getRoleByKey(String key) throws IncorrectResultSizeDataAccessException {
    LOGGER.info("Calling getRoleByKey [{}]", key);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Role> roles = getJdbcTemplate().query(
        stmtLoader.load(GET_ROLE_BY_KEY),
        new MapSqlParameterSource("role_key", key),
        new RoleMapper());

    if (roles.size() == 1) {
      return Optional.of(roles.get(0));
    } else if (roles.size() == 0) {
      return Optional.empty();
    } else {
      throw new IncorrectResultSizeDataAccessException(1, roles.size());
    }
  }
}
