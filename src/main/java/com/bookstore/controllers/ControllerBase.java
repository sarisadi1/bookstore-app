package com.bookstore.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;

import com.bookstore.business.UserSessionInterface;

/**
 * Base class for all the controllers
 */
public abstract class ControllerBase {

	private static final Logger logger = LogManager.getLogger(ControllerBase.class);
	
	@Autowired
	protected  UserSessionInterface loggedInSession;
	
	/**
	 * Base constructor for all controllers
	 */
	public ControllerBase() {
		
	}
	
	// For handling password encryption
	protected BCryptPasswordEncoder bcrypt  = new BCryptPasswordEncoder();
	
	/**
	 * Adds common attributes to the models for all controller classes
	 * @param model model
	 */
	protected void addCommonAttributes(Model model) {
		// Add the user store so the view can get current user if any
		logger.info("Adding common attributes to the model.");

		model.addAttribute("sessionInfo", loggedInSession);
	}
	
	protected boolean hasValidUserSession() {		
		var name = loggedInSession.getUserName();
		if (name != null && !name.isEmpty()) {
			logger.info("User session is valid for user: {}", name);
			return true;
		}
		logger.warn("Invalid user session.");
		return false;
	}
}
