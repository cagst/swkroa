package com.cagst.swkroa.utils;

import java.math.BigDecimal;
import java.util.Collection;

import com.cagst.swkroa.member.MembershipCounty;

/**
 * Defines utilities for Membership Minerals.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public interface MineralUtilities {
	/**
	 * Calculate any additional fees for the Membership based upon the specified Membership County.
	 * 
	 * @param county
	 *          The {@link MembershipCounty} to use to calculate any additional fees for.
	 * 
	 * @return A {@link BigDecimal} that represents any additional fees for the Membership based upon
	 *         the specified MembershipCounty.
	 */
	public BigDecimal calculateFeesForMembershipCounty(final MembershipCounty county);

	/**
	 * Calculate any additional fees for the Membership based upon the collection of Membership
	 * Counties.
	 * 
	 * @param county
	 *          A {@link Collection} of {@link MembershipCounty MembershipCounties} to use to
	 *          calculate any additional fees for.
	 * 
	 * @return A {@link BigDecimal} that represents any additional fees for the Membership based upon
	 *         the specified Membership Counties.
	 */
	public BigDecimal calculateFeesForMembershipCounties(final Collection<MembershipCounty> counties);
}
