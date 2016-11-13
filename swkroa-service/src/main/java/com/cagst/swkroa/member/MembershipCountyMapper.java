package com.cagst.swkroa.member;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.county.County;
import com.cagst.swkroa.county.CountyRepository;
import com.cagst.swkroa.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Used to marshal/unmarshal a {@link MembershipCounty} to/from the database.
 *
 * @author Craig Gaskill
 */
/* package */final class MembershipCountyMapper implements RowMapper<MembershipCounty> {
  private static final String MEMBERSHIP_COUNTY_ID = "membership_county_id";
  private static final String COUNTY_ID = "county_id";
  private static final String MEMBERSHIP_ID = "membership_id";
  private static final String NET_MINERAL_ACRES = "net_mineral_acres";
  private static final String SURFACE_ACRES = "surface_acres";
  private static final String VOTING_IND = "voting_ind";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID = "create_id";
  private static final String UPDT_ID = "updt_id";
  private static final String MEMBERSHIP_COUNTY_UPDT_CNT = "membership_county_updt_cnt";

  private final CountyRepository countyRepo;

  /**
   * Primary constructor used to create an instance of <i>MembershipCountyMapper</i>.
   *
   * @param countyRepo
   *     The {@link CountyRepository} to use to retrieve the {@link County} associated with
   *     this Membership.
   */
  public MembershipCountyMapper(final CountyRepository countyRepo) {
    this.countyRepo = countyRepo;
  }

  @Override
  public MembershipCounty mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    MembershipCounty county = new MembershipCounty();

    County cnty = countyRepo.getCountyByUID(rs.getLong(COUNTY_ID));

    county.setMembershipCountyUID(rs.getLong(MEMBERSHIP_COUNTY_ID));
    county.setCounty(cnty);
    county.setNetMineralAcres(rs.getInt(NET_MINERAL_ACRES));
    county.setSurfaceAcres(rs.getInt(SURFACE_ACRES));
    county.setVotingCounty(rs.getBoolean(VOTING_IND));

    county.setMembershipCountyUpdateCount(rs.getLong(MEMBERSHIP_COUNTY_UPDT_CNT));
    county.setActive(rs.getBoolean(ACTIVE_IND));

    return county;
  }

  /**
   * Will marshal a {@link MembershipCounty} into a {@link MapSqlParameterSource} for inserting into
   * the database.
   *
   * @param builder
   *     The {@link MembershipCounty} to map into an insert statement.
   * @param membership
   *     The {@link Membership} this Member is associated with.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final MembershipCounty county, final Membership membership,
                                                         final User user) {

    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(MEMBERSHIP_ID, membership.getMembershipUID());
    params.addValue(COUNTY_ID, county.getCounty().getCountyUID());
    params.addValue(NET_MINERAL_ACRES, county.getNetMineralAcres());
    params.addValue(SURFACE_ACRES, county.getSurfaceAcres());
    params.addValue(VOTING_IND, county.isVotingCounty());
    params.addValue(ACTIVE_IND, county.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link MembershipCounty} into a {@link MapSqlParameterSource} for updating into
   * the database.
   *
   * @param county
   *     The {@link MembershipCounty} to map into an update statement.
   * @param membership
   *     The {@link Membership} this Member is associated with.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final MembershipCounty county, final Membership membership,
                                                         final User user) {

    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(MEMBERSHIP_ID, membership.getMembershipUID());
    params.addValue(COUNTY_ID, county.getCounty().getCountyUID());
    params.addValue(NET_MINERAL_ACRES, county.getNetMineralAcres());
    params.addValue(SURFACE_ACRES, county.getSurfaceAcres());
    params.addValue(VOTING_IND, county.isVotingCounty());
    params.addValue(ACTIVE_IND, county.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(MEMBERSHIP_COUNTY_ID, county.getMembershipCountyUID());
    params.addValue(MEMBERSHIP_COUNTY_UPDT_CNT, county.getMembershipCountyUpdateCount());

    return params;
  }
}
