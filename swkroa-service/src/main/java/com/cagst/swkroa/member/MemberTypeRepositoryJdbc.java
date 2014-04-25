package com.cagst.swkroa.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;

/**
 * JDBC Template implementation of the {@link MemberTypeRepository} interface.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Repository("memberTypeRepo")
/* package */class MemberTypeRepositoryJdbc extends BaseRepositoryJdbc implements MemberTypeRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemberTypeRepositoryJdbc.class);

	private static final String GET_MEMBERTYPE_BY_UID = "GET_MEMBERTYPE_BY_UID";
	private static final String GET_MEMBERTYPE_BY_MEANING = "GET_MEMBERTYPE_BY_MEANING";
	private static final String GET_MEMBERTYPES_ACTIVE = "GET_MEMBERTYPES_ACTIVE";

	/**
	 * Primary constructor used to create an instance of the MemberTypeRepositoryJdbc.
	 * 
	 * @param dataSource
	 *          The {@link DataSource} to use to retrieve / persist data objects.
	 */
	public MemberTypeRepositoryJdbc(final DataSource dataSource) {
		super(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MemberRepository#getMemberTypeByID(long)
	 */
	@Override
	@Cacheable(value = "memberTypes")
	public MemberType getMemberTypeByID(final long id) throws EmptyResultDataAccessException,
			IncorrectResultSizeDataAccessException {

		LOGGER.info("Calling getMemberTypeByID for [{}].", id);

		return getMemberTypeByIDAsOf(id, DateTime.now());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MemberRepository#getMemberTypeByIDAsOf(long,
	 * org.joda.time.DateTime)
	 */
	@Override
	@Cacheable(value = "memberTypes")
	public MemberType getMemberTypeByIDAsOf(final long id, final DateTime effectiveDateTime)
			throws EmptyResultDataAccessException, IncorrectResultSizeDataAccessException {

		Assert.notNull(effectiveDateTime, "Assertion Failed - argument [effectiveDateTime] cannot be null");

		LOGGER.info("Calling getMemberTypeByIDAsOf for [{}] as of [{}]", id, effectiveDateTime);

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("member_type_id", id);
		params.put("eff_dt_tm", effectiveDateTime.toDate());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		List<MemberType> types = getJdbcTemplate().query(stmtLoader.load(GET_MEMBERTYPE_BY_UID), params,
				new MemberTypeMapper());

		if (types.size() == 1) {
			return types.get(0);
		} else if (types.size() == 0) {
			LOGGER.warn("MemberType with ID of [{}] was not found for [{}].", id, effectiveDateTime);
			throw new EmptyResultDataAccessException(1);
		} else {
			LOGGER.error("More than one MemberType with ID of [{}] was found for [{}].", id, effectiveDateTime);
			throw new IncorrectResultSizeDataAccessException(1, types.size());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MemberRepository#getMemberTypeByMeaning(java.lang.String)
	 */
	@Override
	@Cacheable(value = "memberTypes")
	public MemberType getMemberTypeByMeaning(final String meaning) throws EmptyResultDataAccessException,
			IncorrectResultSizeDataAccessException {

		LOGGER.info("Calling getMemberTypeByMeaning for [{}].", meaning);

		return getMemberTypeByMeaningAsOf(meaning, DateTime.now());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MemberRepository#getMemberTypeByMeaningAsOf(java.lang.String,
	 * org.joda.time.DateTime)
	 */
	@Override
	@Cacheable(value = "memberTypes")
	public MemberType getMemberTypeByMeaningAsOf(final String meaning, final DateTime effectiveDateTime)
			throws EmptyResultDataAccessException, IncorrectResultSizeDataAccessException {

		Assert.hasText(meaning, "Assertion Failed - argument [meaning] cannot be null or empty");
		Assert.notNull(effectiveDateTime, "Assertion Failed - argument [effectiveDateTime] cannot be null");

		LOGGER.info("Calling getMemberTypeByMeaningAsOf for [{}] as of [{}]", meaning, effectiveDateTime);

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("member_type_meaning", meaning);
		params.put("eff_dt_tm", effectiveDateTime.toDate());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		List<MemberType> types = getJdbcTemplate().query(stmtLoader.load(GET_MEMBERTYPE_BY_MEANING), params,
				new MemberTypeMapper());

		if (types.size() == 1) {
			return types.get(0);
		} else if (types.size() == 0) {
			LOGGER.warn("MemberType with Meaning of [{}] was not found for [{}].", meaning, effectiveDateTime);
			throw new EmptyResultDataAccessException(1);
		} else {
			LOGGER.error("More than one MemberType with Meaning of [{}] was found for [{}].", meaning, effectiveDateTime);
			throw new IncorrectResultSizeDataAccessException(1, types.size());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MemberRepository#getActiveMemberTypes()
	 */
	@Override
	@Cacheable(value = "memberTypeList")
	public List<MemberType> getActiveMemberTypes() {
		LOGGER.info("Calling getActiveMemberTypes.");

		return getActiveMemberTypesAsOf(DateTime.now());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MemberRepository#getActiveMemberTypesAsOf(org.joda.time.DateTime)
	 */
	@Override
	public List<MemberType> getActiveMemberTypesAsOf(final DateTime effectiveDateTime) {
		Assert.notNull(effectiveDateTime, "Assertion Failed - argument [effectiveDateTime] cannot be null");

		LOGGER.info("Calling getActiveMemberTypesAsOf [{}]", effectiveDateTime);

		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("eff_dt_tm", effectiveDateTime.toDate());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERTYPES_ACTIVE), params, new MemberTypeMapper());
	}
}
