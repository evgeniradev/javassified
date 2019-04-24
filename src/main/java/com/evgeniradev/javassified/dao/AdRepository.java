package com.evgeniradev.javassified.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.evgeniradev.javassified.entity.Ad;
import com.evgeniradev.javassified.entity.Region;

public interface AdRepository extends JpaRepository<Ad, Integer> {
  
  public Page findAllByRegion(Region region, Pageable pageable);

}
