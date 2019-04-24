package com.evgeniradev.javassified.service;

import com.evgeniradev.javassified.entity.User;
import com.evgeniradev.validation.ValidUser;

public interface UserService {
  
	User findByEmail(String email);
	
	User currentUser();
	
	void save(ValidUser user);

}
