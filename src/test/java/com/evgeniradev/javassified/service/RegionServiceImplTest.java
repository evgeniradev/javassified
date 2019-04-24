package com.evgeniradev.javassified.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evgeniradev.javassified.dao.RegionRepository;
import com.evgeniradev.javassified.entity.Region;

@ExtendWith(MockitoExtension.class)
public class RegionServiceImplTest {
  
  @Mock(lenient = true)
  RegionRepository regionRepository;
  
  @InjectMocks
  RegionServiceImpl regionService;
  
  Region region;
  
  String regionName;
  
  final ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
  
  @BeforeEach
  void setUp() {
    region = new Region("New York");
    regionName = region.getName();
  }
  
  @Test
  void findAllTest() {
    //given
    
    //when
    regionService.findAll();

    //then
    then(regionRepository).should().findAll();
  }
  
  @Test
  void findByNameTest() {
    //given
    given(regionRepository.findByName(regionName)).willReturn(region);
    
    //when
    regionService.findByName(regionName);

    //then
    then(regionRepository).should().findByName(regionName);
  }
  
  @Test
  void findBySlugTest() {
    //given
    String slug = region.getSlug();
    given(regionRepository.findByName(regionName)).willReturn(region);
    
    //when
    regionService.findBySlug(slug);

    //then
    then(regionRepository).should().findByName(regionName.toLowerCase());
  }
  
}
