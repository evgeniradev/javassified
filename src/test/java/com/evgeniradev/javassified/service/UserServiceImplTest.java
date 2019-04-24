package com.evgeniradev.javassified.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.evgeniradev.javassified.dao.UserRepository;
import com.evgeniradev.javassified.entity.User;
import com.evgeniradev.validation.ValidUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  
  @Mock
  UserRepository userRepository;
  
  @Mock
  PasswordEncoder passwordEncoder;
  
  @InjectMocks
  UserServiceImpl userService;

  ValidUser validUser;
  
  final ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
  
  @BeforeEach
  void setUp() {
    validUser = new ValidUser();
    validUser.setUsername("username");
    validUser.setEmail("test@test.com");
    validUser.setPassword("123123");
  }
  
  @Test
  void saveTest() {
    //given
    String encodedPassword = "Encoded password";
    given(userRepository.save(captor.capture())).willReturn(new User());
    given(passwordEncoder.encode(validUser.getPassword())).willReturn(encodedPassword);
    
    //when
    userService.save(validUser);
    
    //then
    then(passwordEncoder).should().encode(validUser.getPassword());
    then(userRepository).should().save(captor.getValue());
    assertThat(captor.getValue().getPassword()).isEqualTo(encodedPassword);
  }
  
  @Test
  void findByEmailTest() {
    //given
    String email = "test@test.com";
    
    //when
    userService.findByEmail(email);
    
    //then
    then(userRepository).should().findByEmail(email);
  }
  
  @Disabled("To do")
  @Test
  void currentUser() {
  }
 
}
