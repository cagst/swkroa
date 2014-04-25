package com.cagst.swkroa.integration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.county.County;
import com.cagst.swkroa.county.CountyRepository;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberType;
import com.cagst.swkroa.member.MemberTypeRepository;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.member.MembershipCounty;
import com.cagst.swkroa.member.MembershipService;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.user.User;

/**
 * Test / helper class that will import Members/Memberships from a CSV file.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@RunWith(JUnit4.class)
public class CSVImportTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVImportTest.class);

	private MembershipService membershipService;
	private CountyRepository countyRepo;

	private final int FIELD_MEMBERID = 0;
	private final int FIELD_MEMBERSHIPID = 1;
	private final int FIELD_OWNERID = 2;
	private final int FIELD_TITLETYPEID = 3;
	private final int FIELD_FIRSTNAME = 4;
	private final int FIELD_MIDDLENAME = 5;
	private final int FIELD_LASTNAME = 6;
	private final int FIELD_ADDRESS1 = 7;
	private final int FIELD_ADDRESS2 = 8;
	private final int FIELD_ADDRESS3 = 9;
	private final int FIELD_CITY = 10;
	private final int FIELD_STATE = 11;
	private final int FIELD_ZIPCODE = 12;
	private final int FIELD_ZIPCODE4 = 13;
	private final int FIELD_MEMBERTYPE = 14;
	private final int FIELD_RELATIONSHIPTYPEID = 15;
	private final int FIELD_DUESAMOUNT = 16;
	private final int FIELD_SPOUSE = 17;
	private final int FIELD_2015BASE = 18;
	private final int FIELD_2015INC = 19;
	private final int FIELD_2015FAMILY = 20;
	private final int FIELD_2015TOTAL = 21;
	private final int FIELD_2015CREDIT = 22;
	private final int FIELD_2015DEBIT = 23;
	private final int FIELD_2015DUE = 24;
	private final int FIELD_RELATED_FM = 25;
	private final int FIELD_2015OK = 26;
	private final int FIELD_2015NOTES = 27;
	private final int FIELD_2015PROBLEMS = 28;
	private final int FIELD_SPLIT_OUT = 29;
	private final int FIELD_PAIDTO = 30;
	private final int FIELD_2015SPECIAL_FUNDS = 31;
	private final int FIELD_DATE_PAID = 32;
	private final int FIELD_CHECK_NO = 33;
	private final int FIELD_GREETING = 34;
	private final int FIELD_RESCOUNTY = 35;

	private final int FIELD_MINERAL_ACRES = 36;
	private final int FIELD_SURFACE_ACRES = 37;
	private final int FIELD_COMMENTS = 38;
	private final int FIELD_PHONE = 39;
	private final int FIELD_JOINED = 40;
	private final int FIELD_EMAIL1 = 41;
	private final int FIELD_EMAIL2 = 42;
	private final int FIELD_MAILNEWSLETTER = 43;
	private final int FIELD_ELECTRONICNEWSLETTER = 44;
	private final int FIELD_ACTIVE = 45;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

	private CodeValue membershipTypeR_Regular;
	private CodeValue membershipTypeA_Associate;
	private CodeValue membershipTypeF_Family;
	private CodeValue membershipTypeM_Mailing;

	private CodeValue title_Mr;
	private CodeValue title_Ms;
	private CodeValue title_Mrs;
	private CodeValue title_Miss;
	private CodeValue title_Dr;

	private CodeValue addressType_Home;
	private CodeValue emailType_Home;
	private CodeValue emailType_Other;
	private CodeValue phoneType_Home;

	private CodeValue entityType_Individual;
	private CodeValue entityType_LLC;
	private CodeValue entityType_Trust;
	private CodeValue entityType_Corporation;

	private MemberType memberTypeA_Associate;
	private MemberType memberTypeFH_FamilyHead;
	private MemberType memberTypeFM_FamilyMember;
	private MemberType memberTypeR_Regular;
	private MemberType memberType_Spouse;
	private MemberType memberTypeM_Mailing;

	private CodeValue transType_Base;
	private CodeValue transType_Inc;
	private CodeValue transType_Family;
	private CodeValue transType_Credit;
	private CodeValue transType_Special;
	private CodeValue transType_Payment;

	private DateTime unknownDueDt;
	private DateTime nowDt;
	private DateTime currentDueDt;

	private User user;

	@Before
	public void setUp() {
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:/test-appCtx/**/*.xml");

		CodeValueRepository codeValueRepo = (CodeValueRepository) appCtx.getBean("codeValueRepo");
		membershipTypeR_Regular = codeValueRepo.getCodeValueByMeaning("MEMBERSHIP_REGULAR");
		membershipTypeA_Associate = codeValueRepo.getCodeValueByMeaning("MEMBERSHIP_ASSOCIATE");
		membershipTypeF_Family = codeValueRepo.getCodeValueByMeaning("MEMBERSHIP_FAMILY");
		membershipTypeM_Mailing = codeValueRepo.getCodeValueByMeaning("MEMBERSHIP_MAILING");

		entityType_Individual = codeValueRepo.getCodeValueByMeaning("ENTITY_INDIVIDUAL");
		entityType_LLC = codeValueRepo.getCodeValueByMeaning("ENTITY_LLC");
		entityType_Trust = codeValueRepo.getCodeValueByMeaning("ENTITY_TRUST");
		entityType_Corporation = codeValueRepo.getCodeValueByMeaning("ENTITY_CORPORATION");

		title_Mr = codeValueRepo.getCodeValueByMeaning("TITLE_MR");
		title_Ms = codeValueRepo.getCodeValueByMeaning("TITLE_MS");
		title_Mrs = codeValueRepo.getCodeValueByMeaning("TITLE_MRS");
		title_Miss = codeValueRepo.getCodeValueByMeaning("TITLE_MISS");
		title_Dr = codeValueRepo.getCodeValueByMeaning("TITLE_DR");

		addressType_Home = codeValueRepo.getCodeValueByMeaning("ADDRESS_HOME");
		emailType_Home = codeValueRepo.getCodeValueByMeaning("EMAIL_HOME");
		emailType_Other = codeValueRepo.getCodeValueByMeaning("EMAIL_OTHER");
		phoneType_Home = codeValueRepo.getCodeValueByMeaning("PHONE_HOME");

		MemberTypeRepository memberTypeRepo = (MemberTypeRepository) appCtx.getBean("memberTypeRepo");
		memberTypeA_Associate = memberTypeRepo.getMemberTypeByMeaning("MEMBER_ASSOCIATE");
		memberTypeFH_FamilyHead = memberTypeRepo.getMemberTypeByMeaning("MEMBER_FAMILY_HEAD");
		memberTypeFM_FamilyMember = memberTypeRepo.getMemberTypeByMeaning("MEMBER_FAMILY_MEMBER");
		memberTypeR_Regular = memberTypeRepo.getMemberTypeByMeaning("MEMBER_REGULAR");
		memberType_Spouse = memberTypeRepo.getMemberTypeByMeaning("MEMBER_SPOUSE");
		memberTypeM_Mailing = memberTypeRepo.getMemberTypeByMeaning("MEMBER_MAIL_LIST");

		membershipService = (MembershipService) appCtx.getBean("membershipService");
		countyRepo = (CountyRepository) appCtx.getBean("countyRepo");

		transType_Base = codeValueRepo.getCodeValueByMeaning("TRANS_2014_DUES_BASE");
		transType_Inc = codeValueRepo.getCodeValueByMeaning("TRANS_2014_DUES_INC");
		transType_Family = codeValueRepo.getCodeValueByMeaning("TRANS_2014_DUES_FAMILY");
		transType_Special = codeValueRepo.getCodeValueByMeaning("TRANS_2014_SPECIAL_FUNDS");
		transType_Credit = codeValueRepo.getCodeValueByMeaning("TRANS_CREDIT");
		transType_Payment = codeValueRepo.getCodeValueByMeaning("TRANS_PAYMENT");

		unknownDueDt = new DateTime(2015, 3, 1, 0, 0);
		nowDt = new DateTime();

		currentDueDt = new DateTime(2014, 3, 1, 0, 0);

		user = new User();
		user.setUserUID(1L);
	}

	/**
	 * Test / Perform the import of Members/Memberships from the CSV file.
	 * 
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void testCVSImport() throws IOException {
		LOGGER.info("BEGIN IMPORT");

		Map<String, Membership> memberships = new HashMap<String, Membership>();

		ClassPathResource res = new ClassPathResource("swkroa_memberships_20140406.csv");
		FileReader reader = new FileReader(res.getFile());
		BufferedReader stringReader = new BufferedReader(reader);

		String line = stringReader.readLine(); // we want to skip the first line
		while ((line = stringReader.readLine()) != null) {
			String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

			// String fldMemberId = getField(fields, FIELD_MEMBERID);
			String fldMembershipId = getField(fields, FIELD_MEMBERSHIPID);
			String fldOwnerId = getField(fields, FIELD_OWNERID);
			String fldTitleTypeId = getField(fields, FIELD_TITLETYPEID);
			String fldFirstName = getField(fields, FIELD_FIRSTNAME);
			String fldMiddleName = getField(fields, FIELD_MIDDLENAME);
			String fldLastName = getField(fields, FIELD_LASTNAME);
			String fldAddress1 = getField(fields, FIELD_ADDRESS1);
			String fldAddress2 = getField(fields, FIELD_ADDRESS2);
			String fldAddress3 = getField(fields, FIELD_ADDRESS3);
			String fldCity = getField(fields, FIELD_CITY);
			String fldState = getField(fields, FIELD_STATE);
			String fldZipcode = getField(fields, FIELD_ZIPCODE);
			String fldZipcode4 = getField(fields, FIELD_ZIPCODE4);
			String fldMemberType = getField(fields, FIELD_MEMBERTYPE);
			String fldRelationshipTypeId = getField(fields, FIELD_RELATIONSHIPTYPEID);
			// String fldDuesAmount = getField(fields, FIELD_DUESAMOUNT);
			// String fldSpouse = getField(fields, FIELD_SPOUSE);
			String fld2015base = getField(fields, FIELD_2015BASE);
			String fld2015inc = getField(fields, FIELD_2015INC);
			String fld2015family = getField(fields, FIELD_2015FAMILY);
			// String fld2015total = getField(fields, FIELD_2015TOTAL);
			String fld2015credit = getField(fields, FIELD_2015CREDIT);
			String fld2015debit = getField(fields, FIELD_2015DEBIT);
			// String fld2015due = getField(fields, FIELD_2015DUE);
			// String fldRelatedFm = getField(fields, FIELD_RELATED_FM);
			// String fld2015ok = getField(fields, FIELD_2015OK);
			// String fld2015notes = getField(fields, FIELD_2015NOTES);
			// String fld2015problems = getField(fields, FIELD_2015PROBLEMS);
			// String fldSplitOut = getField(fields, FIELD_SPLIT_OUT);
			// String fld2PaidTo = getField(fields, FIELD_PAIDTO);
			String fld2015specialFunds = getField(fields, FIELD_2015SPECIAL_FUNDS);
			String fldDatePaid = getField(fields, FIELD_DATE_PAID);
			String fldCheckNo = getField(fields, FIELD_CHECK_NO);
			String fldGreetings = getField(fields, FIELD_GREETING);
			String fldResCounty = getField(fields, FIELD_RESCOUNTY);
			String fldMineralAcres = getField(fields, FIELD_MINERAL_ACRES);
			String fldSurfaceAcres = getField(fields, FIELD_SURFACE_ACRES);
			String fldComments = getField(fields, FIELD_COMMENTS);
			String fldPhone = getField(fields, FIELD_PHONE);
			String fldJoined = getField(fields, FIELD_JOINED);
			String fldEmail1 = getField(fields, FIELD_EMAIL1);
			String fldEmail2 = getField(fields, FIELD_EMAIL2);
			String fldMailNewsLetter = getField(fields, FIELD_MAILNEWSLETTER);
			String fldEmailNewsLetter = getField(fields, FIELD_ELECTRONICNEWSLETTER);
			String fldActive = getField(fields, FIELD_ACTIVE);

			Membership membership = memberships.get(fldMembershipId);
			if (membership == null) {
				membership = new Membership();

				try {
					membership.setMembershipUID(Long.parseLong(fldMembershipId));
				} catch (NumberFormatException ex) {
					LOGGER.error("Failed to parse membership id [{}].", fldMembershipId);
				}

				memberships.put(fldMembershipId, membership);

				if ("R".equalsIgnoreCase(fldMemberType)) {
					membership.setMembershipType(membershipTypeR_Regular);
				} else if ("A".equalsIgnoreCase(fldMemberType)) {
					membership.setMembershipType(membershipTypeA_Associate);
				} else if ("F".equalsIgnoreCase(fldMemberType) || "FH".equalsIgnoreCase(fldMemberType)
						|| "FM".equalsIgnoreCase(fldMemberType)) {
					membership.setMembershipType(membershipTypeF_Family);
				} else if ("M".equalsIgnoreCase(fldMemberType)) {
					membership.setMembershipType(membershipTypeM_Mailing);
				} else {
					LOGGER.debug("Unrecognized Member(ship) Type [{}]", fldMemberType);
					membership.setMembershipType(membershipTypeR_Regular);
				}

				// assume an Individual membership (may get reset later after reviewing members)
				membership.setEntityType(entityType_Individual);
			} else {
				LOGGER.debug("Found existing Membership [{}]", fldMembershipId);
			}

			Member member = new Member();
			if (StringUtils.isBlank(fldFirstName)) {
				String companyName = fldLastName;
				if (StringUtils.isEmpty(companyName)) {
					// if the last name doesn't contain the Company Name
					// nor does the first name
					// assume the Address 1 field has the Company Name and shift everything appropriately
					companyName = fldAddress1;
					fldAddress1 = fldAddress2;
					fldAddress2 = fldAddress3;
					fldAddress3 = null;
				}

				member.setCompanyName(companyName);
				if (StringUtils.containsIgnoreCase(companyName, "LLC")) {
					membership.setEntityType(entityType_LLC);
				} else if (StringUtils.containsIgnoreCase(companyName, "Trust")) {
					membership.setEntityType(entityType_Trust);
				} else {
					membership.setEntityType(entityType_Corporation);
				}
			} else {
				Person person = new Person();

				if ("Mr.".equalsIgnoreCase(fldTitleTypeId)) {
					person.setTitle(title_Mr);
				} else if ("Ms.".equalsIgnoreCase(fldTitleTypeId)) {
					person.setTitle(title_Ms);
				} else if ("Mrs.".equalsIgnoreCase(fldTitleTypeId)) {
					person.setTitle(title_Mrs);
				} else if ("Miss".equalsIgnoreCase(fldTitleTypeId)) {
					person.setTitle(title_Miss);
				} else if ("Dr.".equalsIgnoreCase(fldTitleTypeId)) {
					person.setTitle(title_Dr);
				}

				person.setFirstName(fldFirstName);
				person.setMiddleName(fldMiddleName);
				person.setLastName(fldLastName);
				member.setPerson(person);
			}

			member.setOwnerIdent(fldOwnerId);
			member.setGreeting(fldGreetings);
			member.setActive(BooleanUtils.toBoolean(fldActive));
			member.setMailNewsletter(BooleanUtils.toBoolean(fldMailNewsLetter));
			member.setEmailNewsletter(BooleanUtils.toBoolean(fldEmailNewsLetter));

			if ("A".equalsIgnoreCase(fldMemberType)) {
				member.setMemberType(memberTypeA_Associate);
			} else if ("FH".equalsIgnoreCase(fldMemberType)) {
				member.setMemberType(memberTypeFH_FamilyHead);
			} else if ("FM".equalsIgnoreCase(fldMemberType)) {
				member.setMemberType(memberTypeFM_FamilyMember);
			} else if ("R".equalsIgnoreCase(fldMemberType)) {
				member.setMemberType(memberTypeR_Regular);
			} else if ("M".equalsIgnoreCase(fldMemberType)) {
				member.setMemberType(memberTypeM_Mailing);
			} else {
				LOGGER.error("Unrecognized Member Type [{}]", fldMemberType);
				member.setMemberType(memberTypeR_Regular);
			}

			if ("1".equalsIgnoreCase(fldRelationshipTypeId)) {
				member.setMemberType(memberType_Spouse);
			}

			// Populate Join Date (if applicable)
			if (!StringUtils.isBlank(fldJoined)) {
				try {
					// if there are only 4 characters, assume it is the YYYY
					if (fldJoined.length() == 4) {
						fldJoined = "01/01/" + fldJoined;
					}
					member.setJoinDate(new DateTime(dateFormat.parse(fldJoined)));
				} catch (ParseException ex) {
					LOGGER.error("Unable to parse join date [{}]", fldJoined);
				}
			}

			// Populate Address (if applicable)
			if (!StringUtils.isBlank(fldAddress1)) {
				Address address = new Address();

				if (StringUtils.startsWithIgnoreCase(fldAddress1, "c/o")) {
					member.setInCareOf(fldAddress1.substring("c/o".length()).trim());

					fldAddress1 = fldAddress2;
					fldAddress2 = fldAddress3;
					fldAddress3 = null;
				}

				address.setAddressLine1(fldAddress1);
				if (!StringUtils.isBlank(fldAddress2)) {
					address.setAddressLine2(fldAddress2);
				}
				if (!StringUtils.isBlank(fldAddress3)) {
					address.setAddressLine3(fldAddress3);
				}
				address.setCity(fldCity);
				address.setState(fldState);
				address.setPostalCode(fldZipcode.replace("-", "") + fldZipcode4);

				address.setCountry("US");
				address.setAddressType(addressType_Home);

				member.addAddress(address);
			}

			// Populate Email1 (if applicable)
			if (!StringUtils.isBlank(fldEmail1)) {
				EmailAddress email = new EmailAddress();
				email.setEmailAddress(fldEmail1);
				email.setEmailType(emailType_Home);

				member.addEmailAddress(email);
			}

			// Populate Email2 (if applicable)
			if (!StringUtils.isBlank(fldEmail2)) {
				EmailAddress email = new EmailAddress();
				email.setEmailAddress(fldEmail2);

				if (StringUtils.isBlank(fldEmail1)) {
					// if we don't have a value for email1, then set email2 to HOME
					email.setEmailType(emailType_Home);
				} else {
					// otherwise set it to OTHER
					email.setEmailType(emailType_Other);
				}

				member.addEmailAddress(email);
			}

			// Populate Phone (if applicable)
			if (!StringUtils.isBlank(fldPhone)) {
				PhoneNumber phone = new PhoneNumber();
				phone.setPhoneNumber(fldPhone.replace("-", ""));
				phone.setPhoneType(phoneType_Home);

				member.addPhoneNumber(phone);
			}

			// Populate Membership County (if applicable)
			fldResCounty = StringUtils.replace(fldResCounty, ";", " ");
			fldResCounty = StringUtils.replace(fldResCounty, ":", " ");

			if (!StringUtils.isBlank(fldResCounty) && !fldResCounty.equalsIgnoreCase("0")) {
				int mineralAcres = 0;
				int surfaceAcres = 0;

				if (!StringUtils.isBlank(fldMineralAcres)) {
					try {
						mineralAcres = Integer.parseInt(fldMineralAcres);
					} catch (NumberFormatException ex) {
						LOGGER.error("Failed to parse Mineral Acres of [{}]", fldMineralAcres);
					}
				}

				if (!StringUtils.isBlank(fldSurfaceAcres)) {
					try {
						surfaceAcres = Integer.parseInt(fldSurfaceAcres);
					} catch (NumberFormatException ex) {
						LOGGER.error("Failed to parse Surface Acres of [{}]", fldSurfaceAcres);
					}
				}

				if (fldResCounty.length() > 2) {
					LOGGER.info("Multiple counties exist [{}]", fldResCounty);
					String[] fldCounties = fldResCounty.split(" ");
					for (String fldCounty : fldCounties) {
						MembershipCounty memCounty = createMembershipCounty(fldCounty);
						if (memCounty != null) {
							memCounty.setNetMineralAcres(mineralAcres);
							memCounty.setSurfaceAcres(surfaceAcres);

							if (!membership.getMembershipCounties().contains(memCounty)) {
								membership.addCounty(memCounty);
							}
						}
					}
				} else {
					MembershipCounty memCounty = createMembershipCounty(fldResCounty);
					if (memCounty != null) {
						memCounty.setNetMineralAcres(mineralAcres);
						memCounty.setSurfaceAcres(surfaceAcres);

						if (!membership.getMembershipCounties().contains(memCounty)) {
							membership.addCounty(memCounty);
						}
					}
				}
			}

			if (!StringUtils.isBlank(fldComments) && !StringUtils.equals(fldComments, "-0-")) {
				Comment comment = new Comment();
				comment.setCommentDate(new DateTime(nowDt));
				comment.setCommentText(fldComments);

				membership.addComment(comment);
			}

			BigDecimal duesAmount = new BigDecimal(0.0);

			if (!StringUtils.isBlank(fld2015base)) {
				try {
					BigDecimal transAmount = new BigDecimal(fld2015base);
					if (transAmount.compareTo(new BigDecimal(0)) != 0) {
						Transaction trans = new Transaction();
						trans.setTransactionDate(currentDueDt);
						trans.setTransactionAmount(transAmount.negate());

						if (member.getMemberType() == memberTypeFM_FamilyMember) {
							trans.setTransactionType(transType_Family);
						} else {
							trans.setTransactionType(transType_Base);
						}

						trans.setMember(member);
						membership.addTransaction(trans);
					}

					// Add the Base to our Dues Amount
					duesAmount = duesAmount.add(transAmount);
				} catch (NumberFormatException ex) {
					LOGGER.warn("Failed to convert base amount [{}].", fld2015base);
				}
			}

			if (!StringUtils.isBlank(fld2015inc)) {
				try {
					BigDecimal transAmount = new BigDecimal(fld2015inc);
					if (transAmount.compareTo(new BigDecimal(0)) != 0) {
						Transaction trans = new Transaction();
						trans.setTransactionDate(currentDueDt);
						trans.setTransactionAmount(transAmount.negate());
						trans.setTransactionType(transType_Inc);

						trans.setMember(member);
						membership.addTransaction(trans);
					}

					// Add the Incremental to our Dues Amount
					duesAmount = duesAmount.add(transAmount);
				} catch (NumberFormatException ex) {
					LOGGER.warn("Failed to convert incremental due [{}].", fld2015inc);
				}
			}

			if (!StringUtils.isBlank(fld2015family)) {
				try {
					BigDecimal transAmount = new BigDecimal(fld2015family);

					// Add the Family Dues to our Dues Amount
					duesAmount = duesAmount.add(transAmount);
				} catch (NumberFormatException ex) {
					LOGGER.warn("Failed to convert family dues [{}].", fld2015family);
				}
			}

			if (!StringUtils.isBlank(fld2015specialFunds)) {
				try {
					BigDecimal transAmount = new BigDecimal(fld2015specialFunds);
					if (transAmount.compareTo(new BigDecimal(0)) != 0) {
						Transaction trans = new Transaction();
						trans.setTransactionAmount(transAmount.negate());
						trans.setTransactionType(transType_Special);

						if (!StringUtils.isBlank(fldDatePaid)) {
							try {
								trans.setTransactionDate(new DateTime(dateFormat.parse(fldDatePaid)));
							} catch (ParseException ex) {
								LOGGER.warn("Unable to parse date [{}]", fldDatePaid);
								trans.setTransactionDate(currentDueDt);
							}
						} else {
							trans.setTransactionDate(currentDueDt);
						}

						trans.setMember(member);
						membership.addTransaction(trans);
					}
				} catch (NumberFormatException ex) {
					LOGGER.warn("Failed to convert special fund [{}].", fld2015specialFunds);
				}
			}

			if (!StringUtils.isBlank(fld2015credit)) {
				try {
					BigDecimal transAmount = new BigDecimal(fld2015credit);
					if (transAmount.compareTo(new BigDecimal(0)) != 0) {
						Transaction trans = new Transaction();
						trans.setTransactionDate(currentDueDt);
						trans.setTransactionAmount(transAmount);
						trans.setTransactionType(transType_Credit);

						trans.setMember(member);
						membership.addTransaction(trans);
					}
				} catch (NumberFormatException ex) {
					LOGGER.warn("Failed to convert credit [{}].", fld2015credit);
				}
			}

			if (!MemberType.MEMBER_FAMILY_MEMBER.equals(member.getMemberType().getMemberTypeMeaning())
					&& !MemberType.MEMBER_SPOUSE.equals(member.getMemberType().getMemberTypeMeaning())) {
				if (!StringUtils.isBlank(fldDatePaid)) {
					Transaction trans = new Transaction();
					trans.setTransactionType(transType_Payment);
					trans.setTransactionAmount(new BigDecimal(0.0));
					trans.setReferenceNumber(fldCheckNo);

					if (!StringUtils.isBlank(fld2015debit)) {
						LOGGER.warn("2015 Debit of [{}]", fld2015debit);
						try {
							BigDecimal debitAmount = new BigDecimal(fld2015debit);
							trans.setTransactionAmount(debitAmount);
						} catch (NumberFormatException ex) {
							LOGGER.warn("Failed to convert debit [{}]", fld2015debit);
						}
					}

					try {
						trans.setTransactionDate(new DateTime(dateFormat.parse(fldDatePaid)));
					} catch (ParseException ex) {
						LOGGER.warn("Unable to parse date [{}]", fldDatePaid);
						trans.setTransactionDate(currentDueDt);
					}

					trans.setMember(member);
					membership.addTransaction(trans);
				}
			}

			if (!MemberType.MEMBER_FAMILY_MEMBER.equals(member.getMemberType().getMemberTypeMeaning())
					&& !MemberType.MEMBER_SPOUSE.equals(member.getMemberType().getMemberTypeMeaning())) {
				membership.setDuesAmount(duesAmount);
			}

			membership.addMember(member);
		}

		stringReader.close();

		// Perform additional cleanup / validation
		for (Membership membership : memberships.values()) {
			// now ensure all Members have a join date
			// and determine if the membership should be active (based upon its' members)
			boolean active = false;
			Member primary = null;
			Member spouse = null;

			for (Member member : membership.getMembers()) {
				if (member.isActive()) {
					active = true;
				}

				// ensure the Person record has the same active status as the Member record
				if (member.getPerson() != null) {
					member.getPerson().setActive(member.isActive());
				}

				if (member.getMemberType() == null) {
					LOGGER.error("Member Type is NULL for [{}]", member.getOwnerIdent());
				}

				// remove Addresses from Spouse and associate with Primary
				if (MemberType.MEMBER_SPOUSE.equalsIgnoreCase(member.getMemberType().getMemberTypeMeaning())) {
					spouse = member;
				} else if (!MemberType.MEMBER_FAMILY_MEMBER.equalsIgnoreCase(member.getMemberType().getMemberTypeMeaning())) {
					primary = member;
				}

				if (primary != null && spouse != null) {
					if (!CollectionUtils.isEmpty(spouse.getAddresses())) {
						for (Address spouseAddress : spouse.getAddresses()) {
							boolean found = false;
							for (Address primaryAddress : primary.getAddresses()) {
								if (spouseAddress.equals(primaryAddress)) {
									found = true;
								}
							}

							if (!found) {
								primary.addAddress(spouseAddress);
							}
						}

						// add this point all the Spouse's address should have been added to the Primary address
						// list
						// so we can clear the addresses for the Spouse
						spouse.clearAddresses();
					}
				}
			}
			membership.setActive(active);

			// check for any payments
			Transaction payment = null;
			BigDecimal amountDue = new BigDecimal(0.0);
			for (Transaction trans : membership.getTransactions()) {
				if (trans.getTransactionType() == transType_Payment) {
					payment = trans;
				} else {
					amountDue = amountDue.add(trans.getTransactionAmount());
				}
			}

			if (payment != null) {
				// at this point our Amount Due should be negative (the Debit amount the Membership owes)
				// so we need to negate it to turn it into the amount that needs to be paid (Credited)
				amountDue = amountDue.negate();

				// now we need to calculate the amount paid.
				// we would have previously stored any 2015 debit amount here
				// which would be the amount they still owe after the payment
				BigDecimal amountPaid = amountDue.subtract(payment.getTransactionAmount());
				payment.setTransactionAmount(amountPaid);

				// now to go through the transactions an calculate how much was paid for each one
				// this will be a first come first serve
				for (Transaction trans : membership.getTransactions()) {
					if (trans.getTransactionType() != transType_Credit && trans.getTransactionType() != transType_Payment) {
						BigDecimal amt = trans.getTransactionAmount().abs();

						int result = amt.compareTo(amountPaid);
						if (result < 0) {
							// if the LHS < RHS then the payment exceeds this amount owed
							// so the entire amount is paid

							// now calculate the amount that still needs to be associated to transactions
							amountPaid = amountPaid.subtract(amt);
						} else if (result == 0) {
							// if the LHS == RHS then the payment matches this amount
							// so the entire amount is paid

							// the entire balance has been paid
							break;
						} else {
							// if the LHS > RHS then the payment wasn't enough to cover the amount owed

							// there is no more to cover additional debit
							break;
						}

						trans.addRelatedTransaction(payment);
					}
				}
			}

			if (membership.getDueOn() == null) {
				membership.setDueOn(unknownDueDt);
			}

			try {
				membership.setMembershipUID(0L);
				membershipService.saveMembership(membership, user);
			} catch (Exception ex) {
				LOGGER.error("Failed to save Membership [{}] due to [{}]", membership.getMembers().get(0)
						.getEffectiveMemberName(), ex.getMessage());
			}
		}

		LOGGER.info("END IMPORT");
	}

	private String getField(final String[] fields, final int field) {
		try {
			if (fields.length > field) {
				return StringUtils.trimToEmpty(fields[field].replaceAll("\"", ""));
			} else {
				return null;
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			LOGGER.error("Array out of bounds for field [{}] in fields [{}].", field, fields.toString());
			return null;
		}
	}

	private MembershipCounty createMembershipCounty(String fldCounty) {
		if (StringUtils.isBlank(fldCounty)) {
			return null;
		}

		if ("GR".equalsIgnoreCase(fldCounty)) {
			fldCounty = "GL";
		} else if ("CO".equalsIgnoreCase(fldCounty)) {
			fldCounty = "CA";
		} else if ("LA".equalsIgnoreCase(fldCounty)) {
			fldCounty = "LE";
		} else if ("WI".equalsIgnoreCase(fldCounty)) {
			fldCounty = "WL";
		}

		County county = countyRepo.getCountyByStateAndCode("KS", fldCounty);
		if (county == null) {
			LOGGER.error("Unable to find county [{}]", fldCounty);
			return null;
		}

		MembershipCounty memCounty = new MembershipCounty();
		memCounty.setCounty(county);
		memCounty.setNetMineralAcres(0);
		memCounty.setSurfaceAcres(0);

		return memCounty;
	}
}
