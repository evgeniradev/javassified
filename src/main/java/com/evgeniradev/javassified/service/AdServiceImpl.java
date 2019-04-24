package com.evgeniradev.javassified.service;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evgeniradev.javassified.dao.AdRepository;
import com.evgeniradev.javassified.entity.Ad;
import com.evgeniradev.javassified.entity.Region;
import com.evgeniradev.validation.ValidAd;

@Service
public class AdServiceImpl implements AdService {

  @Autowired
  private AdRepository adRepository;
  
  @Autowired
  private UserService userService;
  
  @Override
  public Ad findById(int id) {
  	Ad ad = null;
  
  	Optional<Ad> result = adRepository.findById(id);
  
  	if (result.isPresent()) {
  		ad = result.get();
  	}
  	else {
  		throw new EntityNotFoundException();
  	}
  
  	return ad;
  }
  
  @Override
  public Ad findBySlug(String slug) {
  	int id = 0;
  	Pattern pattern = Pattern.compile("\\d+");
  
  	Matcher matcher = pattern.matcher(slug);
  
  	if (matcher.find()){
  		id = Integer.parseInt(matcher.group());
  	}
    else {
      throw new EntityNotFoundException();
    }
  
  	return findById(id);
  }
  
  @Override
  public void save(ValidAd validAd) {
     Ad ad = new Ad(
       validAd.getTitle(),
       validAd.getDescription(),
       new Date(),
       new Date(),
       validAd.getRegion(),
       userService.currentUser());
     
     adRepository.save(ad);
  }
  
  @Override
  public void delete(Ad ad) {
    adRepository.delete(ad);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Page<Ad> findAllByRegion(Region region, Pageable pageable) {
    return adRepository.findAllByRegion(region, pageable);
  }
   
}
