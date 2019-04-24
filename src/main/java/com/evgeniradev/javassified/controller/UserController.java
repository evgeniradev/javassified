package com.evgeniradev.javassified.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evgeniradev.javassified.entity.User;
import com.evgeniradev.javassified.service.UserService;
import com.evgeniradev.validation.ValidUser;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("signup")
	public String signup(Model model) {
	  model.addAttribute("user", new ValidUser());

		return "user/signup";
	}

	@PostMapping("signup")
	public String processSignup(
	    @Valid @ModelAttribute("user") ValidUser userValidator,
	    BindingResult bindingResult,
	    RedirectAttributes redirectAttributes) {
	  
		if (bindingResult.hasErrors())
			return "user/signup";

		userService.save(userValidator);
		
		redirectAttributes.addFlashAttribute("message", "Sign-up successful!");
    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
    
		return "redirect:/login";
	}

}
