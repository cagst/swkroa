package com.cagst.swkroa.controller.api;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.member.MembershipService;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.web.util.JacksonObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.ehcache.transaction.xa.OptimisticLockFailureException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the {@link MembershipApiController} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class MembershipApiControllerTest {
  private MockMvc mockMvc;

  @Mock
  private MembershipService membershipService;

  @InjectMocks
  private MembershipApiController membershipController;

  @Before
  public void setUp() {
    // Process mock annotations
    MockitoAnnotations.initMocks(this);

    // Setup Spring test in standalone mode
    mockMvc = MockMvcBuilders.standaloneSetup(membershipController).build();
  }

  /**
   * Test the getMembershipByUID GET method with an invalid membership.
   */
  @Test
  public void testGetMembershipByUID_NotFound() throws Exception {
    when(membershipService.getMembershipByUID(anyLong())).thenThrow(EmptyResultDataAccessException.class);

    mockMvc.perform(get("/api/memberships/123")).andExpect(status().isNotFound());

    verify(membershipService, times(1)).getMembershipByUID(anyLong());
  }

  /**
   * Test the getMembershipByUID GET method with a valid membership.
   */
  @Test
  public void testGetMembershipByUID_Found() throws Exception {
    Membership membership = new Membership();
    membership.setMembershipUID(123);
    membership.setAmountDue(new BigDecimal(123.45));

    when(membershipService.getMembershipByUID(123)).thenReturn(membership);

    mockMvc.perform(get("/api/memberships/123").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    verify(membershipService, times(1)).getMembershipByUID(anyLong());
  }

  /**
   * Test the saveMembership POST method where the save failes due to Optimistic Locking.
   */
  @Test
  @Ignore
  public void testSaveMembership_Conflict() throws Exception {
    Membership membership = new Membership();
    User user = new User();

    when(membershipService.saveMembership(membership, user)).thenThrow(OptimisticLockFailureException.class);

    ObjectMapper mapper = new JacksonObjectMapper();
    String json = mapper.writeValueAsString(membership);

    mockMvc.perform(
        post("/api/memberships", membership)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict());

    verify(membershipService, times(1)).saveMembership(membership, user);
  }
}
