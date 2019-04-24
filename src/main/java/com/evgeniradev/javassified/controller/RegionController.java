package com.evgeniradev.javassified.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.evgeniradev.javassified.service.RegionService;

@Controller
public class RegionController {

	@Autowired
	private RegionService regionService;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("regions", regionService.findAll());

		return "regions/index";
	}

}
