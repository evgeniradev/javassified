package com.evgeniradev.javassified.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evgeniradev.javassified.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {

	public Region findByName(String name);

}
