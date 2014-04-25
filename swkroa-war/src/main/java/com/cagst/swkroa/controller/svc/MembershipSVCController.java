package com.cagst.swkroa.controller.svc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.member.MemberType;
import com.cagst.swkroa.member.MemberTypeRepository;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.member.MembershipCounty;
import com.cagst.swkroa.member.MembershipService;
import com.cagst.swkroa.model.MembershipModel;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.web.util.WebAppUtils;

/**
 * Handles and retrieves {@link Membership}, {@link Member}, and {@link MembershipCounty} objects depending upon the URI
 * template.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Controller
public final class MembershipSVCController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MembershipSVCController.class);

	@Autowired
	private CodeValueRepository codeValueRepo;

	@Autowired
	private MembershipService membershipService;

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private MemberTypeRepository memberTypeRepo;

	/**
	 * Handles the request and retrieves the active Memberships within the system.
	 * 
	 * @return A JSON representation of the active Memberships within the system.
	 */
	@RequestMapping(value = "/svc/memberships", method = RequestMethod.GET)
	@ResponseBody
	public List<MembershipModel> getMemberships(final @RequestParam("q") String query) {
		LOGGER.info("Received request to retrieve memberships using query string [{}]", query);

		List<Membership> memberships;
		if (StringUtils.isNotBlank(query)) {
			memberships = membershipService.getMembershipsForName(query);
		} else {
			memberships = membershipService.getActiveMemberships();
		}

		List<MembershipModel> models = new ArrayList<MembershipModel>(memberships.size());
		for (Membership membership : memberships) {
			models.add(new MembershipModel(membership));
		}

		Collections.sort(models);

		return models;
	}

	/**
	 * Handles the request and retrieves the {@link Membership} associated with the specified membership ID.
	 * 
	 * @param membershipId
	 *          A {@link long} that uniquely identifies the {@link Membership} to retrieve.
	 * 
	 * @return A {@link MembershipModel} that represents the {@link Membership} for the specified membership ID.
	 */
	@RequestMapping(value = { "/svc/membership/{membershipId}", "/membership/svc/membership/{membershipId}" }, method = RequestMethod.GET)
	@ResponseBody
	public MembershipModel getMembership(final @PathVariable long membershipId) {
		LOGGER.info("Recevied request to retrieve membership [{}].", membershipId);

		if (membershipId == 0L) {
			Member primary = new Member();
			primary.setMemberType(memberTypeRepo.getMemberTypeByMeaning(MemberType.MEMBER_REGULAR));
			primary.setPerson(new Person());

			Membership membership = new Membership();
			membership.setMembershipType(codeValueRepo.getCodeValueByMeaning("MEMBERSHIP_REGULAR"));
			membership.setEntityType(codeValueRepo.getCodeValueByMeaning("ENTITY_INDIVIDUAL"));
			membership.addMember(primary);

			return new MembershipModel(membership);
		} else {
			Membership membership = membershipService.getMembershipByUID(membershipId);

			return new MembershipModel(membership);
		}
	}

	/**
	 * Handles the generation of an OwnerID based upon a first name and last name.
	 * 
	 * @param firstName
	 *          The first name of the member to generate an OwnerID for.
	 * @param lastName
	 *          The last name of the member to generate an OwnerID for.
	 * 
	 * @return The next available OwnerId based upon the specified first name and last name, or an empty string if no
	 *         OwnerId could be determined.
	 */
	@RequestMapping(value = { "/svc/generateOwnerId/{firstName}/{lastName}",
			"/membership/svc/generateOwnerId/{firstName}/{lastName}" })
	public @ResponseBody
	String generateOwnerId(final @PathVariable String firstName, final @PathVariable String lastName) {
		String ownerId = StringUtils.EMPTY;

		try {
			ownerId = memberRepo.generateOwnerId(firstName, lastName);
		} catch (IllegalArgumentException ex) {
			// do nothing
		}

		return ownerId;
	}

	/**
	 * Handles the request and persists the {@link MembershipModel} to persistent storage. Called from the Add/Edit
	 * Membership page.
	 * 
	 * @param model
	 *          The {@link MembershipModel} to persist.
	 * 
	 * @return The {@link Membership} after it has been persisted.
	 */
	@RequestMapping(value = { "/membership/svc/membership", "/svc/membership" }, method = RequestMethod.POST)
	public void saveMembershipModel(final @RequestBody MembershipModel model) {
		LOGGER.info("Received request to save membership [{}]", model.getMembershipUID());

		model.setMemberTypeRepository(memberTypeRepo);
		membershipService.saveMembership(model.build(), WebAppUtils.getUser());
	}

	/**
	 * Handles the request and persists the {@link Membership} to persistent storage. Called from the Membership Details
	 * page when deleting a membership.
	 * 
	 * @param membership
	 *          The {@link Membership} to persist.
	 * 
	 * @return The {@link Membership} after it has been persisted.
	 */
	@RequestMapping(value = { "/svc/membership", "/membership/svc/membership" }, method = RequestMethod.PUT)
	@ResponseBody
	public Membership saveMembership(final @RequestBody Membership membership) {
		LOGGER.info("Received request to remove membership [{}]", membership.getMembershipUID());

		return membershipService.saveMembership(membership, WebAppUtils.getUser());
	}
}
