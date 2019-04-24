package com.evgeniradev.javassified.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.evgeniradev.javassified.dao.UserRepository;
import com.evgeniradev.javassified.entity.User;
import com.evgeniradev.validation.ValidUser;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void save(ValidUser userValidator) {
		User user = new User(
		    userValidator.getUsername(),
		    userValidator.getEmail(),
		    passwordEncoder.encode(userValidator.getPassword()),
		    "USER");

		userRepository.save(user);
	}

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User currentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
    if (principal instanceof UserDetails) {
      return findByEmail(((UserDetails)principal).getUsername());
    } else {
      return null;
    }
  }
  
}
