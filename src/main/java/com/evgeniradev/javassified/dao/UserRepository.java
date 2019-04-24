package com.evgeniradev.javassified.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evgeniradev.javassified.entity.Region;
import com.evgeniradev.javassified.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

  public User findByEmail(String email);
  
}
