package com.cagst.swkroa.role;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.user.User;

/**
 * JDBC Template implementation of the {@link RoleRepository} interface.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Repository("roleRepo")
/* package */final class RoleRepositoryJdbc extends BaseRepositoryJdbc implements RoleRepository {
	private static final Logger logger = LoggerFactory.getLogger(RoleRepositoryJdbc.class);

	private static final String GET_ROLES_FOR_USER = "GET_ROLES_FOR_USER";
	private static final String GET_ACTIVE_ROLES = "GET_ACTIVE_ROLES";
	private static final String GET_ACTIVE_PRIVILEGES = "GET_ACTIVE_PRIVILEGES";
	private static final String GET_PRIVILEGES_FOR_ROLE = "GET_PRIVILEGES_FOR_ROLE";
	private static final String GET_PRIVILEGES_FOR_USER = "GET_PRIVILEGES_FOR_USER";

	/**
	 * Primary Constructor used to create an instance of <i>RoleRepositoryJdbc</i>.
	 * 
	 * @param dataSource
	 *          The {@link DataSource} used to retrieve / persist data objects.
	 */
	public RoleRepositoryJdbc(final DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Collection<Role> getRolesForUser(final User user) {
		Assert.notNull(user, "Assertion Failure - argument [user} cannot be null.");

		logger.info("Calling getRolesForUser [{}].", user.getUsername());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("user_id", user.getUserUID());

		return getJdbcTemplate().query(stmtLoader.load(GET_ROLES_FOR_USER), params, new RoleMapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.role.RoleRepository#getActiveRoles()
	 */
	@Override
	public List<Role> getActiveRoles() {
		logger.info("Calling getActiveRoles.");

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		return getJdbcTemplate().getJdbcOperations().query(stmtLoader.load(GET_ACTIVE_ROLES), new RoleMapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.role.RoleRepository#getActivePrivileges()
	 */
	@Override
	public List<Privilege> getActivePrivileges() {
		logger.info("Calling getActivePrivilges.");

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		return getJdbcTemplate().getJdbcOperations().query(stmtLoader.load(GET_ACTIVE_PRIVILEGES), new PrivilegeMapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.role.RoleRepository#getPrivilegesForRole(com.cagst.swkroa.role.Role)
	 */
	@Override
	public List<Privilege> getPrivilegesForRole(final Role role) {
		Assert.notNull(role, "Assertion Failure - argument [role] cannot be null.");

		logger.info("Calling getPrivilegesForRole [{}].", role.getRoleName());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("role_id", role.getRoleUID());

		return getJdbcTemplate().query(stmtLoader.load(GET_PRIVILEGES_FOR_ROLE), params, new PrivilegeMapper());
	}

	@Override
	public Set<Privilege> getPrivilegesForUser(final User user) {
		Assert.notNull(user, "Assertion Failure - argument [user] cannot be null.");

		logger.info("Calling getPrivilegesForUser [{}].", user.getUsername());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("user_id", user.getUserUID());

		List<Privilege> privs = null;
		if (user.getUserUID() < 10) {
			privs = getActivePrivileges();
		} else {
			privs = getJdbcTemplate().query(stmtLoader.load(GET_PRIVILEGES_FOR_USER), params, new PrivilegeMapper());
		}

		return new HashSet<Privilege>(privs);
	}
}
