package com.cagst.swkroa.controller.api;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the {@link UserApiController} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class UserApiControllerTest {
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserApiController userController;

  @Before
  public void setUp() {
    // Process mock annotations
    MockitoAnnotations.initMocks(this);

    // Setup Spring test in standalone mode
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  /**
   * Test the getUserByUID GET method with an invalid user.
   */
  @Test
  public void testGetUserByUID_NotFound() throws Exception {
    when(userService.getUserByUID(anyLong())).thenThrow(IncorrectResultSizeDataAccessException.class);

    mockMvc.perform(get("/api/users/123")).andExpect(status().isNotFound());

    verify(userService, times(1)).getUserByUID(anyLong());
  }

  /**
   * Test the getUserByUID GET method with a valid user.
   */
  @Test
  public void testGetUserByUID_Found() throws Exception {
    User user = new User();
    user.setUserUID(123);
    user.setUsername("resetme");
    user.setPasswordTemporary(false);

    when(userService.getUserByUID(123)).thenReturn(user);

    mockMvc.perform(get("/api/users/123").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    verify(userService, times(1)).getUserByUID(anyLong());
  }

  /**
   * Tests the resetUserPassword PUT method with an invalid user.
   */
  @Test
  public void testResetUserPassword_NotFound() throws Exception {
    when(userService.getUserByUID(anyLong())).thenThrow(IncorrectResultSizeDataAccessException.class);

    mockMvc.perform(put("/api/users/123/resetpassword")).andExpect(status().isNotFound());

    verify(userService, times(1)).getUserByUID(anyLong());
  }

  /**
   * Test the resetUserPassword PUT method with a valid user.
   */
  @Test
  public void testResetUserPassword_Found() throws Exception {
    User user = new User();
    user.setUserUID(123);
    user.setUsername("resetme");
    user.setPasswordTemporary(false);

    when(userService.getUserByUID(123)).thenReturn(user);

    mockMvc.perform(put("/api/users/123/resetpassword").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }
}
