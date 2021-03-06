package com.cagst.swkroa.utils;

import java.math.BigDecimal;
import java.util.Collection;

import com.cagst.swkroa.member.MembershipCounty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Default implementation of the {@link MineralUtilities} interfaces.
 *
 * @author Craig Gaskill
 */
@JsonSerialize
public class DefaultMineralUtilities implements MineralUtilities {
  private final Integer baseAcres = 160;
  private final BigDecimal incrementalFee = new BigDecimal(5.00);

  @Override
  public BigDecimal calculateFeesForMembershipCounty(final MembershipCounty county) {
    BigDecimal fee = new BigDecimal(0);

    int extraAcerage = county.getNetMineralAcres() - baseAcres;
    if (extraAcerage > 0) {
      Double extraUnits = Math.ceil(extraAcerage / baseAcres.doubleValue());
      int units = extraUnits.intValue();

      fee = fee.add(incrementalFee.multiply(new BigDecimal(units)));
    }

    return fee;
  }

  @Override
  public BigDecimal calculateFeesForMembershipCounties(final Collection<MembershipCounty> counties) {
    BigDecimal fees = new BigDecimal(0);

    for (MembershipCounty county : counties) {
      fees = fees.add(calculateFeesForMembershipCounty(county));
    }

    return fees;
  }
}
