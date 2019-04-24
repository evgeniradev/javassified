package com.evgeniradev.javassified;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.evgeniradev.javassified.interceptor.LayoutSetterInterceptor;

@SpringBootApplication
public class JavassifiedApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(JavassifiedApplication.class, args);
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LayoutSetterInterceptor());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
	}

}
