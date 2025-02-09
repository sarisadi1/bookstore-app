package com.bookstore.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home page request handling class.
 */
@Controller
@RequestMapping("/")
public class HomeController extends ControllerBase {
	private static final Logger logger = LogManager.getLogger(HomeController.class);
	
	/**
	 * Create Home Controller object
	 */
	public HomeController() {
		
	}
	
	/**
	 * Display the home page based on the situation
	 * @param model model
	 * @return home page
	 */
	@GetMapping()
	public String displayHome(Model model) {
		addCommonAttributes(model);

		logger.info("Request to display home page");
		
		if (!hasValidUserSession()) {
			logger.info("No valid user session. Displaying home page.");
			// return the home view
			return "home";
		} else {
			logger.info("User is logged in. Redirecting to dashboard.");
			// If the user is logged in show dash board
			return "redirect:/dashboard";
		}
		
		//return "home";
	}
	
	/**
	 * About page mapping 
	 * @param model model object
	 * @return about page
	 */
	@GetMapping("/about")
	public String displayAbout(Model model) {
		addCommonAttributes(model);

		logger.info("Request to display about page");

		// Return the about view
		return "about";
	}
}
