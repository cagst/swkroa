package com.cagst.swkroa.controller.web;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import java.util.Optional;

import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles and retrieves the member page(s) depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
@RolesAllowed("ROLE_MEMBER")
@RequestMapping(value = "/member")
public class MemberController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

  private final MemberRepository memberRepository;

  @Inject
  public MemberController(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @RequestMapping(value = "/home", method = RequestMethod.GET)
  private ModelAndView getMemberHomePage() {
    LOGGER.info("Received request to show the member's home page.");

    ModelAndView mav = new ModelAndView("member/home");

    User user = WebAppUtils.getUser();
    if (user != null) {
      Optional<Member> checkMember = memberRepository.getMemberByPersonUID(user.getPersonUID());
      if (checkMember.isPresent()) {
        mav.addObject("membershipUID", checkMember.get().getMembershipUID());
      }
    }

    return mav;
  }
}
