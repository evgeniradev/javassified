package com.evgeniradev.javassified.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evgeniradev.javassified.entity.Ad;
import com.evgeniradev.javassified.entity.Region;
import com.evgeniradev.validation.ValidAd;

public interface AdService {

	public Ad findById(int id);

	public Ad findBySlug(String slug);

	void save(ValidAd validAd);
	
	void delete(Ad ad);
	
	public Page<Ad> findAllByRegion(Region region, Pageable pageable);

}
