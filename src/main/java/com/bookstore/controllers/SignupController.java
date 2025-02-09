package com.bookstore.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookstore.business.UserManagementInterface;
import com.bookstore.models.UserModel;

import jakarta.validation.Valid;

/**
 * Sign up page request handling class.
 */
@Controller
@RequestMapping("/signUp")
public class SignupController extends ControllerBase {

	private static final Logger logger = LogManager.getLogger(SignupController.class);


	@Autowired()
	private UserManagementInterface userManager;
	
	/**
	 * Create Singup controller object
	 */
	public SignupController() {
		
	}

	/**
	 * Sign up page url request mapping.
	 * 
	 * @param model model
	 * @return singup page
	 */
	@GetMapping()
	public String displaySignUp(Model model) {

		// If the user session is valid redirect to dahsboard
		if (hasValidUserSession()) {
			logger.info("User is already logged in, redirecting to dashboard.");
			return "redirect:/dashboard";
		} else {
			addCommonAttributes(model);
			model.addAttribute("signUpModel", new UserModel());
			logger.info("Displaying sign-up page.");
			return "signup";
		}
	}

	/**
	 * Sing up form request handler. Check for user validity and adds a user.
	 * 
	 * @param signUpModel user object
	 * @param result      result
	 * @param model       model
	 * @return login page on success sign up page on failure
	 */
	@PostMapping("/doSignUp")
	public String doSignUp(@ModelAttribute("signUpModel") @Valid UserModel signUpModel, BindingResult result,
			Model model) {

		// If the user session is valid redirect to dahsboard
		if (hasValidUserSession()) {
			logger.info("User is already logged in, redirecting to dashboard.");
			return "redirect:/dashboard";
		}

		// otherwise process signup
		addCommonAttributes(model);
		// check if there is errors
		if (result.hasErrors()) {
			logger.warn("Form validation errors during sign-up attempt.");
			return "signup";
		}

		// Check if user exist with same name
		if (this.userManager.isDuplicateUser(signUpModel.getUserName())) {
			result.rejectValue("userName", "error.signUpModel", "User name exist. Please choose another name");
			logger.error("User name already exists: {}", signUpModel.getUserName());
			return "signup";
		}

		// If all looks OK. add the user to store. Encode password text
		signUpModel.setPassword(bcrypt.encode(signUpModel.getPassword()));
		userManager.addUser(signUpModel);

		logger.info("User {} successfully signed up.", signUpModel.getUserName());

		// When sign up succeeds, we can redirect to login
		return "redirect:/login";
	}
}
