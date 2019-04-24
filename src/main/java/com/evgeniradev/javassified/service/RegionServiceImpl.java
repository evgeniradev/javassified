package com.evgeniradev.javassified.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evgeniradev.javassified.dao.RegionRepository;
import com.evgeniradev.javassified.entity.Region;

@Service
public class RegionServiceImpl implements RegionService {

	@Autowired
	private RegionRepository regionRepository;

	@Override
	public List<Region> findAll() {
		return regionRepository.findAll();
	}

	@Override
	public Region findByName(String name) {
		return regionRepository.findByName(name);
	}

	@Override
	public Region findBySlug(String slug) {
		return findByName(slug.replaceAll("-", " "));
	}
	
	@Override
  public Region findById(int id) {
    return regionRepository.findById(id).get();
  }
	
}
