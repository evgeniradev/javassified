package com.evgeniradev.javassified.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
	private DataSource dataSource;
  
  @Autowired
	private PasswordEncoder passwordEncoder;

  @Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			  .antMatchers("/ads/**/reply", "/ads/**/delete", "/ads/new").authenticated()
			  .antMatchers("/users/signup").anonymous()
				.anyRequest().permitAll()
				.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.failureUrl("/login?error")
				.permitAll();
	}


	@Override
  protected void configure(AuthenticationManagerBuilder builder) throws Exception {
    builder.jdbcAuthentication()
    	.authoritiesByUsernameQuery("SELECT email as principal, role FROM users WHERE email=?")
    	.usersByUsernameQuery("SELECT email as principal, password as credentials, true FROM users WHERE email=?")
    	.passwordEncoder(passwordEncoder)
      .dataSource(dataSource);
  }

}
