package com.evgeniradev.javassified.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.eq;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evgeniradev.javassified.dao.AdRepository;
import com.evgeniradev.javassified.entity.Ad;
import com.evgeniradev.javassified.entity.User;
import com.evgeniradev.validation.ValidAd;

@ExtendWith(MockitoExtension.class)
public class AdServiceImplTest {
  
  @Mock
  AdRepository adRepository;
  
  @Mock
  UserService userService;

  @InjectMocks
  AdServiceImpl adService;
 
  Optional<Ad> optionalAd;
  
  Ad ad;
  
  int adId = 1;
  
  String adSlug = adId + "some-slug";
  
  final ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
  
  final ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
  
  @BeforeEach
  void setUp() {
    ad = new Ad("This is the Ad's title", "This is the Ad's description");
    optionalAd = Optional.of(ad);
  }
  
  
  @Test
  void findByIdSuccessTest() {
    //given
    given(adRepository.findById(intCaptor.capture())).willReturn(optionalAd);
    
    //when
    Ad returnedAd = adService.findById(adId);

    //then
    assertThat(intCaptor.getValue()).isEqualTo(adId);
    assertThat(returnedAd).isEqualTo(ad);
  }
  
  @Test
  void findByIdFailTest() {
    //given
    given(adRepository.findById(intCaptor.capture())).willReturn(Optional.empty());
    
    //when
    assertThrows(EntityNotFoundException.class, () ->  adService.findById(adId));
    
    //then
    assertThat(intCaptor.getValue()).isEqualTo(adId);
  }
  
  @Test
  void findBySlugSuccessTest() {
    //given
    given(adRepository.findById(intCaptor.capture())).willReturn(optionalAd);
    
    //when
    Ad returnedAd = adService.findBySlug(adSlug);
    
    //then
    assertThat(intCaptor.getValue()).isEqualTo(adId);
    assertThat(returnedAd).isEqualTo(ad);
  }
  
  @Test
  void findBySlugFailTest() {
    //given
    given(adRepository.findById(intCaptor.capture())).willReturn(Optional.empty());
    
    //when
    assertThrows(EntityNotFoundException.class, () ->  adService.findBySlug(adSlug));
    
    //then
    assertThat(intCaptor.getValue()).isEqualTo(adId);
  }
  
  @Test
  void saveTest() {
    User user = new User();
    ValidAd validAd = new ValidAd();
    validAd.setTitle("Ad title");
    validAd.setDescription("Ad description");
    
    //given
    given(adRepository.save(adCaptor.capture())).willReturn(new Ad());
    given(userService.currentUser()).willReturn(user);
    
    //when
    adService.save(validAd);
    
    //then
    assertThat(adCaptor.getValue().getTitle())
      .isEqualTo(validAd.getTitle());
    assertThat(adCaptor.getValue().getDescription())
      .isEqualTo(validAd.getDescription());
    assertThat(adCaptor.getValue().getUser())
      .isEqualTo(user);
  }
  
  @Test
  void deleteTest() {
    //when
    adService.delete(ad);
    
    //then
    then(adRepository).should().delete(ad);
  }
  
}
