package com.evgeniradev.javassified.controller;

import java.util.Date;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evgeniradev.javassified.entity.Ad;
import com.evgeniradev.javassified.entity.Region;
import com.evgeniradev.javassified.entity.User;
import com.evgeniradev.javassified.service.AdService;
import com.evgeniradev.javassified.service.RegionService;
import com.evgeniradev.javassified.service.UserService;
import com.evgeniradev.validation.ValidAd;

@Controller
@RequestMapping("ads")
public class AdController {

	@Autowired
	private RegionService regionService;

	@Autowired
	private AdService adService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender theEmailSender;

	Logger logger = LoggerFactory.getLogger(AdController.class);
	
	final public int ADS_PER_PAGE = 5;

	@GetMapping
	public String index(HttpServletRequest request, Model model, Pageable pageable) {
		String regionSlug = request.getParameter("region");
		Region region = regionService.findBySlug(regionSlug);
		
    if (region == null)
      return  "redirect:/";
		
		int currentPage = getCurrentPage(request.getParameter("page"));
		String redirect = "redirect:/ads?region=" + regionSlug + "&page=1";
		
		if (currentPage == 0)
      return redirect;

		Page<Ad> paginatedAds = adService.findAllByRegion(
		    region, 
		    PageRequest.of(
		        currentPage > 0 ? currentPage - 1 : currentPage, ADS_PER_PAGE,
		        Sort.by("createdAt").descending())
		);
		
		int totalPages = paginatedAds.getTotalPages();
		
		if (currentPage > totalPages && !(currentPage == 1 && paginatedAds.getContent().size() == 0))
		  return redirect;

    addPaginationAttributes(model, currentPage, totalPages);
		
		model.addAttribute("ads", paginatedAds);
		model.addAttribute("regionSlug", regionSlug);
		
		return "ads/index";
	}	
	
	@GetMapping("new")
	public String newAd(Model model) {
	  
	  addRegions(model);
	  model.addAttribute("ad", new Ad());
	  
	  return "ads/new";
	}
	
	@PostMapping("new")
  public String processNewAd(
      @Valid @ModelAttribute("ad") ValidAd validAd,
      BindingResult bindingResult,
      Model model, 
      HttpServletRequest request, 
      RedirectAttributes redirectAttributes) {
    
	  if (bindingResult.hasErrors()) {
	    addRegions(model);
	    model.addAttribute("ad", validAd);
	    model.addAttribute("selectedRegion", validAd.getRegion());
	    return "ads/new";
	  }
	  
	  int regionId = Integer.parseInt(request.getParameter("region"));

	  adService.save(validAd);
	  
    createSuccessMessage("Ad posted successfully!", redirectAttributes);
    
    return "redirect:/ads?region=" + regionService.findById(regionId).getSlug() + "&page=1";
  }
	
	@PostMapping("{adSlug}/delete")
	public String delete(
	    @PathVariable String adSlug,
	    HttpServletResponse response,
	    RedirectAttributes redirectAttributes) {
	  Ad ad = adService.findBySlug(adSlug);
	  
	  if (!canDelete(ad.getUser())) {
	    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	    return null;
	  }
	    
	  adService.delete(ad);
	  
	  createSuccessMessage("Ad delete successfully!", redirectAttributes);

    
	  return "redirect:/ads?region=" + ad.getRegion().getSlug() + "&page=1";
	}

	@GetMapping("{adSlug}")
	public String show(@PathVariable String adSlug, Model model) {
		Ad ad = adService.findBySlug(adSlug);

		model.addAttribute("ad", ad);
		model.addAttribute("canDelete", canDelete(ad.getUser()));
		model.addAttribute("daysAgo", daysAgo(ad.getCreatedAt()));

		return "ads/show";
	}

	@GetMapping("{adSlug}/reply")
	public String reply(@PathVariable String adSlug, Model model) {
		Ad ad = adService.findBySlug(adSlug);

		model.addAttribute("ad", ad);

		return "ads/reply";
	}

	@PostMapping("{adSlug}/reply")
	public String processReply(
	    @PathVariable String adSlug,
	    RedirectAttributes redirectAttributes,
	    HttpServletRequest request) {
	  
		Ad ad = adService.findBySlug(adSlug);
		String toEmail = ad.getUser().getEmail();
		String fromEmail = userService.currentUser().getEmail();
		String body = request.getParameter("body");

		sendEmail(ad, fromEmail, toEmail, body, redirectAttributes);

		return "redirect:/ads?region=" + ad.getRegion().getSlug() + "&page=1";
	}
	
	private void sendEmail(
	    Ad ad,
	    String fromEmail,
	    String toEmail,
	    String body,
	    RedirectAttributes redirectAttributes) {
	  
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom(fromEmail);
		message.setReplyTo(fromEmail);
    message.setTo(toEmail);
    message.setSubject("Re: " + ad.getTitle());
    message.setText(body + "\n \n ------ \n Remember to reply to " + toEmail + " directly.");

    try {
    	theEmailSender.send(message);
	    createSuccessMessage("Message sent successfully!", redirectAttributes);
    } catch (MailException e) {
    	logger.error(e.getMessage());
    	redirectAttributes.addFlashAttribute("message", "Message failed to send.");
	    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
    }
	}

	private long daysAgo(Date adDate) {
		return (new Date().getTime() - adDate.getTime()) / 86400000;
	}
	
	private void addRegions(Model model) {
    model.addAttribute("regions", regionService.findAll());
  }
	
	private Boolean canDelete(User user) {
	  User currentUser = userService.currentUser();
	  
	  if (currentUser == null)
	    return false;
	  
    return (currentUser == user) || (currentUser.getRole().equals("ROLE_ADMIN"));
  }
	
  private int getCurrentPage(String pageParam) {
    int pageNumber = pageParam != null ? Integer.parseInt(pageParam) : 0;
    
    return pageNumber;
  }
 
  private void addPaginationAttributes(Model model, int currentPage, int totalPages) {
    model.addAttribute("previousPage", currentPage - 1 <= 0 || totalPages < currentPage? 1 : currentPage - 1);
    model.addAttribute("nextPage", currentPage + 1 > totalPages ? totalPages : currentPage + 1);
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", IntStream.range(1, totalPages + 1).toArray());
  }
  
  private void createSuccessMessage(String message, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("message", message);
    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
  }
  
}
