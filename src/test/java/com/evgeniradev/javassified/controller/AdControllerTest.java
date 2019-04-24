package com.evgeniradev.javassified.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.evgeniradev.javassified.entity.Ad;
import com.evgeniradev.javassified.entity.Region;
import com.evgeniradev.javassified.entity.User;
import com.evgeniradev.javassified.service.AdService;
import com.evgeniradev.javassified.service.RegionService;
import com.evgeniradev.javassified.service.UserService;

@WebMvcTest(value = {AdController.class})
class AdControllerTest {
  
  @Autowired
  MockMvc mockMvc;
  
  @MockBean
  DataSource dataSource;
  
  @MockBean
  RegionService regionService;

  @MockBean
  AdService adService;
  
  @MockBean
  UserService userService;

  @MockBean
  JavaMailSender theEmailSender;
  
  Page<Ad> ads;
  
  List<Region> regions;
  
  Ad ad;
  
  Region region;
  
  User user;
  
  User loggedInUser;
  
  int ADS_PER_PAGE;
  
  @BeforeEach
  void setUp() {
    ADS_PER_PAGE = new AdController().ADS_PER_PAGE;
    
    user = new User("John1", "ad_user@test.com", "password", "USER_ROLE");
    
    region = new Region("New York");
    region.setId(1);
    
    regions = new ArrayList<Region>();
    regions.add(region);
    
    loggedInUser = new User();
    loggedInUser.setEmail("loggedInUser@test.com");
    loggedInUser.setRole("ROLE_ADMIN");
    
    ad = new Ad(
      "This is the Ad's title", 
      "This is the Ad's description",
      yesterday(),
      yesterday(),
      region,
      user);
    
    List<Ad> tmpAds = new ArrayList<Ad>();
    tmpAds.add(ad);
    
    ads = new PageImpl<Ad>(
      tmpAds,
      PageRequest.of(1, ADS_PER_PAGE, Sort.by("createdAt").descending()),
      tmpAds.size()
    );
  }
  
  @AfterEach
  void tearDown() {
    reset(dataSource);
    reset(regionService);
    reset(adService);
    reset(theEmailSender);
  }
  
  @Test
  void indexTest() throws Exception {
    given(regionService.findBySlug(region.getName())).willReturn(region);
    given(adService.findAllByRegion(eq(region), any(Pageable.class)))
      .willReturn(ads);
    
    mockMvc
      .perform(
        get("/ads")
          .param("region", region.getName())
          .param("page", "1"))
      .andExpect(status().isOk())
      .andExpect(view().name("ads/index"))
      .andExpect(model().attribute("ads", ads))
      .andExpect(model().attributeExists("previousPage"))
      .andExpect(model().attributeExists("nextPage"))
      .andExpect(model().attributeExists("currentPage"))
      .andExpect(model().attributeExists("totalPages"));
     
    then(regionService).should().findBySlug(region.getName());
    then(adService).should()
      .findAllByRegion(
        region,
        PageRequest.of(0, ADS_PER_PAGE, Sort.by("createdAt").descending())
      );
  }
  
  @Test
  void indexNoRegionTest() throws Exception {
    given(regionService.findBySlug(null)).willReturn(null);

    mockMvc
      .perform(get("/ads").param("page", "1"))
      .andExpect(status().is3xxRedirection());
     
    then(regionService).should().findBySlug(null);
  }
  
  @Test
  void indexNoPageTest() throws Exception {
    given(regionService.findBySlug(region.getName())).willReturn(region);

    mockMvc
      .perform(get("/ads").param("region", region.getName()))
      .andExpect(status().is3xxRedirection());
     
    then(regionService).should().findBySlug(region.getName());
  }
  
  @WithMockUser
  @Test
  void newAdTest() throws Exception {
    given(regionService.findAll()).willReturn(regions);

    mockMvc
      .perform(get("/ads/new"))
      .andExpect(status().isOk())
      .andExpect(view().name("ads/new"))
      .andExpect(model().attribute("regions", regions))
      .andExpect(model().attributeExists("ad"));
     
    then(regionService).should().findAll();
  }
  
  @Test
  void newAdUnauthenticatedTest() throws Exception {
    mockMvc
      .perform(get("/ads/new"))
      .andExpect(status().is3xxRedirection());
  }
  
  @WithMockUser
  @Test
  void processNewAdSuccessTest() throws Exception {
    given(regionService.findById(region.getId())).willReturn(region);
    
    mockMvc
      .perform(
        post("/ads/new")
          .with(csrf())
          .param("region", region.getId() + "")
          .param("title", "New long enough AD title")
          .param("description", "New long enough AD description"))
      .andExpect(status().is3xxRedirection())
      .andExpect(view().name("redirect:/ads?region=" + ad.getRegion().getSlug() + "&page=1"))
      .andExpect(flash().attributeExists("message"));
    
    then(regionService).should().findById(region.getId());
  }
  
  @Test
  void processNewAdUnauthenticatedTest() throws Exception {
    mockMvc
      .perform(
        post("/ads/new")
          .with(csrf())
          .param("region", region.getId() + "")
          .param("title", "New long enough AD title")
          .param("description", "New long enough AD description"))
      .andExpect(status().is3xxRedirection());
    
    then(regionService).shouldHaveZeroInteractions();
  }
  
  @WithMockUser
  @Test
  void processNewAdNoTitleTest() throws Exception {
    given(regionService.findAll()).willReturn(regions);
    
    mockMvc
      .perform(
        post("/ads/new")
          .with(csrf())
          .param("region", region.getId() + "")
          .param("description", "New long enough AD description"))
      .andExpect(status().isOk())
      .andExpect(view().name("ads/new"))
      .andExpect(model().attribute("regions", regions))
      .andExpect(model().attributeExists("selectedRegion"))
      .andExpect(model().attributeExists("ad"));
    
    then(regionService).should().findAll();
  }
  
  @WithMockUser
  @Test
  void processNewAdNoDescriptionTest() throws Exception {
    given(regionService.findAll()).willReturn(regions);
    
    mockMvc
      .perform(
        post("/ads/new")
          .with(csrf())
          .param("region", region.getId() + "")
          .param("title", "New long enough AD title"))
      .andExpect(status().isOk())
      .andExpect(view().name("ads/new"))
      .andExpect(model().attribute("regions", regions))
      .andExpect(model().attributeExists("selectedRegion"))
      .andExpect(model().attributeExists("ad"));
    
    then(regionService).should().findAll();
  }
  
  @WithMockUser
  @Test
  void processNewAdNoRegionTest() throws Exception {
    given(regionService.findAll()).willReturn(regions);
    
    mockMvc
      .perform(
        post("/ads/new")
          .with(csrf())
          .param("title", "New long enough AD title")
          .param("description", "New long enough AD description"))
      .andExpect(status().isOk())
      .andExpect(view().name("ads/new"))
      .andExpect(model().attribute("regions", regions))
      .andExpect(model().attributeDoesNotExist("selectedRegion"))
      .andExpect(model().attributeExists("ad"));
    
    then(regionService).should().findAll();
  }
  
  @WithMockUser
  @Test
  void deleteWhenUserAdminSuccessTest() throws Exception {
    given(adService.findBySlug(ad.getSlug())).willReturn(ad);
    given(userService.currentUser()).willReturn(loggedInUser);
    
    mockMvc
      .perform(
        post("/ads/{adSlug}/delete", ad.getSlug()).with(csrf()))
      .andExpect(status().is3xxRedirection())
      .andExpect(view().name("redirect:/ads?region=" + ad.getRegion().getSlug() + "&page=1"))
      .andExpect(flash().attributeExists("message"));
    
    then(adService).should().findBySlug(ad.getSlug());
    then(userService).should().currentUser();
  }
  
  @Test
  void deleteUnauthenticatedTest() throws Exception {
    mockMvc
      .perform(
        post("/ads/{adSlug}/delete", ad.getSlug()).with(csrf()))
      .andExpect(status().is3xxRedirection());
    
    
    then(adService).shouldHaveZeroInteractions();
    then(userService).shouldHaveZeroInteractions();
  }
  
  @WithMockUser
  @Test
  void deleteWhenUserNotAdminButOwnsAdSuccessTest() throws Exception {
    given(adService.findBySlug(ad.getSlug())).willReturn(ad);
    given(userService.currentUser()).willReturn(ad.getUser());
    
    mockMvc
      .perform(
        post("/ads/{adSlug}/delete", ad.getSlug()).with(csrf()))
      .andExpect(status().is3xxRedirection())
      .andExpect(view().name("redirect:/ads?region=" + ad.getRegion().getSlug() + "&page=1"))
      .andExpect(flash().attributeExists("message"));
    
    then(adService).should().findBySlug(ad.getSlug());
    then(userService).should().currentUser();
  }
  
  @WithMockUser
  @Test
  void deleteWhenUserNotAdminAndDoesntOwnAdFailTest() throws Exception {
    User newUser = new User();
    newUser.setRole("ROLE_USER");
    
    given(adService.findBySlug(ad.getSlug())).willReturn(ad);
    given(userService.currentUser()).willReturn(newUser);
    
    mockMvc
      .perform(
        post("/ads/{adSlug}/delete", ad.getSlug()).with(csrf()))
      .andExpect(status().isForbidden());
    
    then(adService).should().findBySlug(ad.getSlug());
    then(userService).should().currentUser();
  }
  
  @Test
  void showTest() throws Exception {
    given(adService.findBySlug(ad.getSlug())).willReturn(ad);
    
    mockMvc
      .perform(get("/ads/{adSlug}", ad.getSlug()))
      .andExpect(status().isOk())
      .andExpect(model().attribute("ad", ad))
      .andExpect(view().name("ads/show"))
      .andExpect(model().attribute("daysAgo", 1L));     
  }
  
  @WithMockUser
  @Test
  void replyTest() throws Exception {
    given(adService.findBySlug(ad.getSlug())).willReturn(ad);
    
    mockMvc
      .perform(get("/ads/{adSlug}/reply", ad.getSlug()))
      .andExpect(status().isOk())
      .andExpect(view().name("ads/reply"))
      .andExpect(model().attribute("ad", ad));
  }
  
  @Test
  void replyUnauthenticatedTest() throws Exception {
    mockMvc
      .perform(get("/ads/{adSlug}/reply", ad.getSlug()))
      .andExpect(status().is3xxRedirection());
  }
  
  @WithMockUser
  @Test
  void processReplyTest() throws Exception {    
    given(adService.findBySlug(ad.getSlug())).willReturn(ad);
    given(userService.currentUser()).willReturn(loggedInUser);
    
    mockMvc
      .perform(post("/ads/{adSlug}/reply", ad.getSlug())
          .with(csrf()))
      .andExpect(status().is3xxRedirection())
      .andExpect(view().name("redirect:/ads?region=" + ad.getRegion().getSlug() + "&page=1"));
      
    then(theEmailSender).should().send(any(SimpleMailMessage.class));
  }
  
  @Test
  void processReplyUnauthenticatedTest() throws Exception {
    mockMvc
      .perform(post("/ads/{adSlug}/reply", ad.getSlug())
          .with(csrf()))
      .andExpect(status().is3xxRedirection());
      
    then(theEmailSender).shouldHaveZeroInteractions();
  }
  
  @WithMockUser
  @Test
  void sendEmailSuccessTest() throws Exception {
    String messageBody = "Message content";
    String toEmail = ad.getUser().getEmail();
    SimpleMailMessage message = new SimpleMailMessage();
    
    given(adService.findBySlug(ad.getSlug())).willReturn(ad);
    given(userService.currentUser()).willReturn(loggedInUser);
    
    message.setFrom(loggedInUser.getEmail());
    message.setReplyTo(loggedInUser.getEmail());
    message.setTo(toEmail);
    message.setSubject("Re: " + ad.getTitle());
    message.setText(messageBody + "\n \n ------ \n Remember to reply to " + toEmail + " directly.");
    
    mockMvc
      .perform(post("/ads/{adSlug}/reply", ad.getSlug())
          .with(csrf())
          .param("body", messageBody))
      .andExpect(flash().attribute("message", "Message sent successfully!"))
      .andExpect(flash().attribute("alertClass", "alert-success"));
    
    then(theEmailSender).should().send(message);
  }
  
  @WithMockUser
  @Test
  void sendEmailFailTest() throws Exception {
    given(adService.findBySlug(ad.getSlug())).willReturn(ad);
    given(userService.currentUser()).willReturn(loggedInUser);
    
    willThrow(new MailAuthenticationException("Error"))
      .given(theEmailSender).send(any(SimpleMailMessage.class));
    
    mockMvc
      .perform(post("/ads/{adSlug}/reply", ad.getSlug())
          .with(csrf()))
      .andExpect(flash().attribute("message", "Message failed to send."))
      .andExpect(flash().attribute("alertClass", "alert-danger"));
    
    then(theEmailSender).should().send(any(SimpleMailMessage.class));
  }
  
  private Date yesterday() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    return cal.getTime();
  }
  
}
