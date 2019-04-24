package com.evgeniradev.javassified.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = {SessionController.class})
class SessionControllerTest {
  
  @Autowired
  MockMvc mockMvc;
  
  @MockBean
  DataSource dataSource;
  
  @Test
  void loginTest() throws Exception {    
    mockMvc.perform(get("/login"))
      .andExpect(status().isOk())
      .andExpect(view().name("sessions/index"))
      .andExpect(model().attributeDoesNotExist("loginError"));
  }
  
  @Test
  void loginWithErrorTest() throws Exception {    
    mockMvc.perform(get("/login").param("error", ""))
      .andExpect(status().isOk())
      .andExpect(view().name("sessions/index"))
      .andExpect(model().attributeExists("loginError"));
  }
  
}
