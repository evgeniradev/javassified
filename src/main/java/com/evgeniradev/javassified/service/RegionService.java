package com.evgeniradev.javassified.service;

import java.util.List;

import com.evgeniradev.javassified.entity.Region;

public interface RegionService {

	public List<Region> findAll();

	public Region findByName(String name);

	public Region findBySlug(String slug);
	
	public Region findById(int id);

}
