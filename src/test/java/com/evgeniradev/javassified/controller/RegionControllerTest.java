package com.evgeniradev.javassified.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.evgeniradev.javassified.entity.Region;
import com.evgeniradev.javassified.service.RegionService;

@WebMvcTest(value = {RegionController.class})
class RegionControllerTest {
  
  @Autowired
  MockMvc mockMvc;
  
  @MockBean
  DataSource dataSource;
  
  @MockBean
  RegionService regionService;
  
  List<Region> regions;
  
  @BeforeEach
  void setUp() {
    regions = new ArrayList<Region>();
    regions.add(new Region("New York"));
    regions.add(new Region("Berlin"));
  }
  
  @AfterEach
  void tearDown() {
    reset(dataSource);
    reset(regionService);
  }
  
  @WithMockUser
  @Test
  void indexTestAuthenticated() throws Exception {    
    given(regionService.findAll()).willReturn(regions);
    
    mockMvc.perform(get("/"))
      .andExpect(status().isOk())
      .andExpect(view().name("regions/index"))
      .andExpect(model().attribute("regions", regions));
     
    then(regionService).should().findAll();
  }
}
