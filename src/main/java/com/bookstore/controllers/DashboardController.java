package com.bookstore.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User dash board page request handling class.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController extends ControllerBase {
	private static final Logger logger = LogManager.getLogger(DashboardController.class);
	
	/**
	 * Create dashboard controller object
	 */
	public DashboardController() {
		
	}

	/**
	 * Display the main user page
	 * 
	 * @param model model
	 * @return user dashboard page if logged in home otherwise
	 */
	@GetMapping()
	public String displayDashboard(Model model) {

		addCommonAttributes(model);

		if (hasValidUserSession()) {
			logger.info("Displaying dashboard for user: {}", loggedInSession.getUserName());
			model.addAttribute("dashboard", loggedInSession.getUserDashboard());
		}
		else {
			logger.warn("User session is not valid. Redirecting to login.");
		}
		return "dashboard";
	}
}
