package com.evgeniradev.javassified.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.evgeniradev.javassified.service.UserService;
import com.evgeniradev.validation.ValidUser;

@WebMvcTest(value = {UserController.class})
class UserControllerTest {
  
  @Autowired
  MockMvc mockMvc;
  
  @MockBean
  DataSource dataSource;
  
  @MockBean
  UserService userService;
  
  @AfterEach
  void tearDown() {
    reset(dataSource);
    reset(userService);
  }
  
  @Test
  void signupTest() throws Exception {    
    mockMvc.perform(get("/users/signup"))
      .andExpect(status().isOk())
      .andExpect(view().name("user/signup"))
      .andExpect(model().attributeExists("user"));
  }
  
  @WithMockUser
  @Test
  void signupAuthenticatedTest() throws Exception {    
    mockMvc.perform(get("/users/signup"))
      .andExpect(status().isForbidden());
  }
  
  @Test
  void processSignupSuccessTest() throws Exception {    
    mockMvc
      .perform(
        post("/users/signup")
        .with(csrf())
        .param("username", "test_usename")
        .param("email", "test@test.com")
        .param("password", "123123")
        .param("confirmPassword", "123123"))
      .andExpect(status().is3xxRedirection())
      .andExpect(view().name("redirect:/login"))
      .andExpect(flash().attributeExists("message"));;
    
    then(userService).should().save(any(ValidUser.class));
  }
  
  @Test
  void processSignupFailTest() throws Exception {    
    mockMvc
      .perform(
        post("/users/signup")
        .with(csrf())
        .param("username", "test_usename")
        .param("email", "test@test.com")
        .param("password", "123123")
        .param("confirmPassword", ""))
      .andExpect(status().isOk())
      .andExpect(view().name("user/signup"));
  }
  
}
