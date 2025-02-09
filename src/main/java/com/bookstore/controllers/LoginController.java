package com.bookstore.controllers;

import java.security.Principal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Login page request handling class.
 */
@Controller
public class LoginController extends ControllerBase {
	private static final Logger logger = LogManager.getLogger(LoginController.class);

	/**
	 * Create login controller object
	 */
	public LoginController() {
		
	}

	/**
	 * Login page url request handling method.
	 * 
	 * @param model model
	 * @return login page
	 */
	@GetMapping("/login")
	public String displayLogin(Model model) {
		// If the user session is valid redirect to dahsboard
		if (hasValidUserSession()) {
			logger.info("User already logged in, redirecting to dashboard.");
			return "redirect:/dashboard";
		}

		addCommonAttributes(model);

		// Not needed after implementing Spring Security
		// model.addAttribute("loginModel", new LoginModel());
		logger.info("Displaying login page.");
		return "login";
	}

	/**
	 * Login error handler to set error and return login
	 * @param model model
	 * @return login view
	 */
	@GetMapping("/login-error")
	public String loginError(Model model) {
		addCommonAttributes(model);
		model.addAttribute("loginError", "Invalid user name or password. Please try again.");
		logger.error("Login failed: Invalid username or password.");
		return "login";
	}
	
	/**
	 * Login success handler to set login session information on business object
	 * @param principal security details
	 * @return dashboard view
	 */
	@GetMapping("/login-success")
	public String loginSuccess(Principal principal) {
		logger.info("User {} successfully logged in.", principal.getName());
		// When login succeeds, set the session information to our session bean
		loggedInSession.setCurrentUser(principal.getName());
		// Then redirect to dashboard
		return "redirect:/dashboard";
	}
	
	/**
	 * Logout handler to logout the session user on business object
	 * @param model model
	 * @return home page
	 */
	@PostMapping("/logout")
	public String logOut(Model model) {
		logger.info("User {} logged out successfully.", loggedInSession.getUserName());
		// The http details are invalidated by security. Just logout the business service
		loggedInSession.logOut();
		return "redirect:/";		
	}
}
