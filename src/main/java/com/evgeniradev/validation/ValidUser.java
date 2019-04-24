package com.evgeniradev.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch.List({
  @FieldMatch(firstField = "password", secondField = "confirmPassword", message = "Password must match")
})
public class ValidUser {

	@NotNull
	@Size(min = 3, message = "Username must be at least 3 characters long")
	private String username;

	@NotNull
	@Size(min = 4, message = "Email must be at least 3 characters long")
	private String email;

	@NotNull
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;

	@NotNull
	private String confirmPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
