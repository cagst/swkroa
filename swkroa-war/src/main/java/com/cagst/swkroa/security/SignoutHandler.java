package com.cagst.swkroa.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class SignoutHandler implements LogoutHandler {

  @Override
  public void logout(final HttpServletRequest request, final HttpServletResponse response,
                     final Authentication authentication) {
    // TODO Auto-generated method stub

  }

}
