package com.cagst.swkroa.integration;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.county.County;
import com.cagst.swkroa.county.CountyRepository;
import com.cagst.swkroa.member.*;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionEntry;
import com.cagst.swkroa.transaction.TransactionType;
import com.cagst.swkroa.user.User;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Test / helper class that will import Members/Memberships from a CSV file.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class CSVImportTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(CSVImportTest.class);

  private MembershipService membershipService;
  private CountyRepository countyRepo;

  private final int FIELD_MEMBERID             = 0;
  private final int FIELD_MEMBERSHIPID         = 1;
  private final int FIELD_OWNERID              = 2;
  private final int FIELD_TITLETYPEID          = 3;
  private final int FIELD_FIRSTNAME            = 4;
  private final int FIELD_MIDDLENAME           = 5;
  private final int FIELD_LASTNAME             = 6;
  private final int FIELD_ADDRESS1             = 7;
  private final int FIELD_ADDRESS2             = 8;
  private final int FIELD_ADDRESS3             = 9;
  private final int FIELD_CITY                 = 10;
  private final int FIELD_STATE                = 11;
  private final int FIELD_ZIPCODE              = 12;
  private final int FIELD_ZIPCODE4             = 13;
  private final int FIELD_MEMBERTYPE           = 14;
  private final int FIELD_RELATIONSHIPTYPEID   = 15;
  private final int FIELD_DUESAMOUNT           = 16;
  private final int FIELD_SPOUSE               = 17;
  private final int FIELD_2015BASE             = 18;
  private final int FIELD_2015INC              = 19;
  private final int FIELD_2015FAMILY           = 20;
  private final int FIELD_2015TOTAL            = 21;
  private final int FIELD_2015CREDIT           = 22;
  private final int FIELD_2015DEBIT            = 23;
  private final int FIELD_2015DUE              = 24;
  private final int FIELD_RELATED_FM           = 25;
  private final int FIELD_2015OK               = 26;
  private final int FIELD_2015NOTES            = 27;
  private final int FIELD_2015PROBLEMS         = 28;
  private final int FIELD_SPLIT_OUT            = 29;
  private final int FIELD_PAIDTO               = 30;
  private final int FIELD_2015SPECIAL_FUNDS    = 31;
  private final int FIELD_DATE_PAID            = 32;
  private final int FIELD_CHECK_NO             = 33;
  private final int FIELD_GREETING             = 34;
  private final int FIELD_RESCOUNTY            = 35;

  private final int FIELD_MINERAL_ACRES        = 36;
  private final int FIELD_SURFACE_ACRES        = 37;
  private final int FIELD_COMMENTS             = 38;
  private final int FIELD_PHONE                = 39;
  private final int FIELD_JOINED               = 40;
  private final int FIELD_EMAIL1               = 41;
  private final int FIELD_EMAIL2               = 42;
  private final int FIELD_MAILNEWSLETTER       = 43;
  private final int FIELD_ELECTRONICNEWSLETTER = 44;
  private final int FIELD_ACTIVE               = 45;

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
  private final String INVOICE = "Invoice 2014-2015";
  private final String PAYMENT = "Payment";

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

  private CodeValue transEntryType_Base;
  private CodeValue transEntryType_Inc;
  private CodeValue transEntryType_Family;
  private CodeValue transEntryType_Credit;
  private CodeValue transEntryType_Special;
  private CodeValue transEntryType_Payment;

  private DateTime unknownDueDt;
  private DateTime nowDt;
  private DateTime currentDueDt;

  private User user;

  @Before
  public void setUp() {
    ApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:/test-appCtx/**/*.xml");

    CodeValueRepository codeValueRepo = (CodeValueRepository) appCtx.getBean("codeValueRepo");

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
    memberTypeA_Associate = memberTypeRepo.getMemberTypeByMeaning("ASSOCIATE");
    memberTypeFH_FamilyHead = memberTypeRepo.getMemberTypeByMeaning("FAMILY_HEAD");
    memberTypeFM_FamilyMember = memberTypeRepo.getMemberTypeByMeaning("FAMILY_MEMBER");
    memberTypeR_Regular = memberTypeRepo.getMemberTypeByMeaning("REGULAR");
    memberType_Spouse = memberTypeRepo.getMemberTypeByMeaning("SPOUSE");
    memberTypeM_Mailing = memberTypeRepo.getMemberTypeByMeaning("MAIL_LIST");

    membershipService = (MembershipService) appCtx.getBean("membershipService");
    countyRepo = (CountyRepository) appCtx.getBean("countyRepo");

    transEntryType_Base = codeValueRepo.getCodeValueByMeaning("TRANS_DUES_BASE");
    transEntryType_Inc = codeValueRepo.getCodeValueByMeaning("TRANS_DUES_INC");
    transEntryType_Family = codeValueRepo.getCodeValueByMeaning("TRANS_DUES_FAMILY");
    transEntryType_Special = codeValueRepo.getCodeValueByMeaning("TRANS_SPECIAL_FUNDS");
    transEntryType_Credit = codeValueRepo.getCodeValueByMeaning("TRANS_CREDIT");
    transEntryType_Payment = codeValueRepo.getCodeValueByMeaning("TRANS_PAYMENT");

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

    ClassPathResource res = new ClassPathResource("swkroa_memberships_20140716_b.csv");
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
      if (StringUtils.isNotBlank(fldJoined)) {
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
      if (StringUtils.isNotBlank(fldAddress1)) {
        Address address = new Address();

        if (StringUtils.startsWithIgnoreCase(fldAddress1, "c/o")) {
          member.setInCareOf(fldAddress1.substring("c/o".length()).trim());

          fldAddress1 = fldAddress2;
          fldAddress2 = fldAddress3;
          fldAddress3 = null;
        }

        address.setAddressLine1(fldAddress1);
        if (StringUtils.isNotBlank(fldAddress2)) {
          address.setAddressLine2(fldAddress2);
        }
        if (StringUtils.isNotBlank(fldAddress3)) {
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
      if (StringUtils.isNotBlank(fldEmail1)) {
        EmailAddress email = new EmailAddress();
        email.setEmailAddress(fldEmail1);
        email.setEmailType(emailType_Home);

        member.addEmailAddress(email);
      }

      // Populate Email2 (if applicable)
      if (StringUtils.isNotBlank(fldEmail2)) {
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
      if (StringUtils.isNotBlank(fldPhone)) {
        PhoneNumber phone = new PhoneNumber();
        phone.setPhoneNumber(fldPhone.replace("-", ""));
        phone.setPhoneType(phoneType_Home);

        member.addPhoneNumber(phone);
      }

      // Populate Membership County (if applicable)
      fldResCounty = StringUtils.replace(fldResCounty, ";", " ");
      fldResCounty = StringUtils.replace(fldResCounty, ":", " ");

      if (StringUtils.isNotBlank(fldResCounty) && !fldResCounty.equalsIgnoreCase("0")) {
        int mineralAcres = 0;
        int surfaceAcres = 0;

        if (StringUtils.isNotBlank(fldMineralAcres)) {
          try {
            mineralAcres = Integer.parseInt(fldMineralAcres);
          } catch (NumberFormatException ex) {
            LOGGER.error("Failed to parse Mineral Acres of [{}]", fldMineralAcres);
          }
        }

        if (StringUtils.isNotBlank(fldSurfaceAcres)) {
          try {
            surfaceAcres = Integer.parseInt(fldSurfaceAcres);
          } catch (NumberFormatException ex) {
            LOGGER.error("Failed to parse Surface Acres of [{}]", fldSurfaceAcres);
          }
        }

        if (fldResCounty.length() > 2) {
          String[] fldCounties = fldResCounty.split(" ");
          for (String fldCounty : fldCounties) {
            MembershipCounty memCounty = createMembershipCounty(fldCounty, fldMembershipId);
            if (memCounty != null) {
              memCounty.setNetMineralAcres(mineralAcres);
              memCounty.setSurfaceAcres(surfaceAcres);

              if (!membership.getMembershipCounties().contains(memCounty)) {
                membership.addCounty(memCounty);
              }
            }
          }
        } else {
          MembershipCounty memCounty = createMembershipCounty(fldResCounty, fldMembershipId);
          if (memCounty != null) {
            memCounty.setNetMineralAcres(mineralAcres);
            memCounty.setSurfaceAcres(surfaceAcres);

            if (!membership.getMembershipCounties().contains(memCounty)) {
              membership.addCounty(memCounty);
            }
          }
        }
      }

      if (StringUtils.isNotBlank(fldComments) && !StringUtils.equals(fldComments, "-0-")) {
        Comment comment = new Comment();
        comment.setCommentDate(new DateTime(nowDt));
        comment.setCommentText(fldComments);

        membership.addComment(comment);
      }

      // Calculate Invoice
      BigDecimal duesAmount = new BigDecimal(0.0);
      Transaction invoice = null;
      boolean existing = false;
      // attempt to find invoice
      for (Transaction trans : membership.getTransactions()) {
        if (trans.getTransactionType() == TransactionType.INVOICE) {
          invoice = trans;
          duesAmount = invoice.getTransactionAmount();
          existing = true;
          break;
        }
      }

      if (invoice == null) {
        invoice = new Transaction();
        invoice.setTransactionDescription(INVOICE);
        invoice.setTransactionDate(currentDueDt);
        invoice.setTransactionType(TransactionType.INVOICE);
      }

      if (StringUtils.isNotBlank(fld2015base)) {
        try {
          BigDecimal transAmount = new BigDecimal(fld2015base);
          if (transAmount.compareTo(new BigDecimal(0)) != 0) {
            TransactionEntry entry = new TransactionEntry();
            entry.setTransactionEntryAmount(transAmount.negate());

            if (MemberType.FAMILY_MEMBER.equals(member.getMemberType().getMemberTypeMeaning())) {
              entry.setTransactionEntryType(transEntryType_Family);
            } else {
              entry.setTransactionEntryType(transEntryType_Base);
            }

            entry.setMember(member);
            invoice.addEntry(entry);
          }

          // Add the Base to our Dues Amount
          duesAmount = duesAmount.add(transAmount.negate());
        } catch (NumberFormatException ex) {
          LOGGER.warn("Failed to convert base amount [{}].", fld2015base);
        }
      }

      if (StringUtils.isNotBlank(fld2015inc)) {
        try {
          BigDecimal transAmount = new BigDecimal(fld2015inc);
          if (transAmount.compareTo(new BigDecimal(0)) != 0) {
            TransactionEntry entry = new TransactionEntry();
            entry.setTransactionEntryAmount(transAmount.negate());
            entry.setTransactionEntryType(transEntryType_Inc);

            entry.setMember(member);
            invoice.addEntry(entry);
          }

          // Add the Incremental to our Dues Amount
          duesAmount = duesAmount.add(transAmount.negate());
        } catch (NumberFormatException ex) {
          LOGGER.warn("Failed to convert incremental due [{}].", fld2015inc);
        }
      }

      if (duesAmount.doubleValue() != 0.0) {
        if (!existing) {
          membership.addTransaction(invoice);
        }
      }

      if (!MemberType.FAMILY_MEMBER.equals(member.getMemberType().getMemberTypeMeaning()) &&
          !MemberType.SPOUSE.equals(member.getMemberType().getMemberTypeMeaning()) &&
          StringUtils.isNotBlank(fldDatePaid)) {

        // Calculate Payment
        BigDecimal paymentAmount = duesAmount.negate();
        BigDecimal totalAmount = duesAmount.negate();

        Transaction payment = new Transaction();
        payment.setTransactionType(TransactionType.PAYMENT);
        payment.setTransactionDescription(PAYMENT);

        if (StringUtils.isNotBlank(fldCheckNo)) {
          payment.setReferenceNumber(fldCheckNo);
        }

        try {
          payment.setTransactionDate(new DateTime(dateFormat.parse(fldDatePaid)));
        } catch (ParseException ex) {
          LOGGER.warn("Unable to parse date [{}]", fldDatePaid);
          payment.setTransactionDate(currentDueDt);
        }

        membership.addTransaction(payment);

        if (StringUtils.isNotBlank(fld2015specialFunds)) {
          try {
            BigDecimal transAmount = new BigDecimal(fld2015specialFunds);
            if (transAmount.compareTo(new BigDecimal(0)) != 0) {
              TransactionEntry entry = new TransactionEntry();
              entry.setTransactionEntryAmount(transAmount);
              entry.setTransactionEntryType(transEntryType_Special);
              entry.setMember(member);
              payment.addEntry(entry);

              // Add the Special Funds to our Total Payment Amount
              totalAmount = totalAmount.add(transAmount);
              paymentAmount = paymentAmount.add(transAmount);
            }
          } catch (NumberFormatException ex) {
            LOGGER.warn("Failed to convert special fund [{}].", fld2015specialFunds);
          }
        }

        if (StringUtils.isNotBlank(fld2015credit)) {
          try {
            BigDecimal transAmount = new BigDecimal(fld2015credit);
            if (transAmount.compareTo(new BigDecimal(0)) != 0) {
              TransactionEntry entry = new TransactionEntry();
              entry.setTransactionEntryAmount(transAmount);
              entry.setTransactionEntryType(transEntryType_Credit);
              entry.setMember(member);
              payment.addEntry(entry);

              // Add the Credit to our Payment Amount
              totalAmount = totalAmount.add(transAmount);
              paymentAmount = paymentAmount.add(transAmount);
            }
          } catch (NumberFormatException ex) {
            LOGGER.warn("Failed to convert credit [{}].", fld2015credit);
          }
        }
      }

      membership.setDuesAmount(duesAmount.abs());
      membership.addMember(member);
    }

    stringReader.close();

    // Perform additional cleanup / validation
    for (Membership checkMembership : memberships.values()) {
      // now ensure all Members have a join date
      // and determine if the membership should be active (based upon its' members)
      boolean active = false;
      Member primary = null;
      Member spouse = null;

      for (Member checkMember : checkMembership.getMembers()) {
        if (checkMember.isActive()) {
          active = true;
        }

        // ensure the Person record has the same active status as the Member record
        if (checkMember.getPerson() != null) {
          checkMember.getPerson().setActive(checkMember.isActive());
        }

        if (checkMember.getMemberType() == null) {
          LOGGER.error("Member Type is NULL for [{}]", checkMember.getOwnerIdent());
        }

        // remove Addresses from Spouse and associate with Primary
        if (MemberType.SPOUSE.equalsIgnoreCase(checkMember.getMemberType().getMemberTypeMeaning())) {
          if (spouse == null) {
            spouse = checkMember;
          }
        } else if (!MemberType.FAMILY_MEMBER.equalsIgnoreCase(checkMember.getMemberType().getMemberTypeMeaning())) {
          if (primary == null) {
            primary = checkMember;
          }
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
      checkMembership.setActive(active);

      if (checkMembership.getNextDueDate() == null) {
        checkMembership.setNextDueDate(unknownDueDt);
      }

      // check to see if there was a payment and if so, calculate the total payment
      Transaction pymt = null;
      Transaction inv = null;
      for (Transaction trans : checkMembership.getTransactions()) {
        if (trans.getTransactionType() == TransactionType.PAYMENT) {
          pymt = trans;
        } else if (trans.getTransactionType() == TransactionType.INVOICE) {
          inv = trans;
        }
      }

      if (pymt != null) {
        TransactionEntry entry = new TransactionEntry();
        entry.setTransactionEntryType(transEntryType_Payment);
        entry.setTransactionEntryAmount(checkMembership.getDuesAmount());
        entry.setMember(primary);
        entry.setRelatedTransaction(inv);
        pymt.addEntry(entry);

      }

      try {
        checkMembership.setMembershipUID(0L);
        membershipService.saveMembership(checkMembership, user);
      } catch (Exception ex) {
        LOGGER.error("Failed to save Membership [{}] due to [{}]", checkMembership.getMembers().get(0)
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

  private MembershipCounty createMembershipCounty(String fldCounty, final String fldMembershipId) {
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
      LOGGER.error("Unable to find county [{}] membership [{}]", fldCounty, fldMembershipId);
      return null;
    }

    MembershipCounty memCounty = new MembershipCounty();
    memCounty.setCounty(county);
    memCounty.setNetMineralAcres(0);
    memCounty.setSurfaceAcres(0);

    return memCounty;
  }
}
