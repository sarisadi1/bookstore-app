package com.bookstore.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookstore.business.BookStoreInterface;
import com.bookstore.business.UserManagementService;
import com.bookstore.models.UserModel;

import jakarta.validation.Valid;

/**
 * User details request handling class.
 */
@Controller
@RequestMapping("/myaccount")
public class UserAccountController extends ControllerBase {
	private static final Logger logger = LogManager.getLogger(UserAccountController.class);
	
	@Autowired
	private UserManagementService userManager;
	
	@Autowired
	private BookStoreInterface bookService;
	
	/**
	 * Create UserAccount controller object
	 */
	public UserAccountController() {
		
	}

	/**
	 * Account details page url request mapping.
	 * 
	 * @param model model
	 * @return account page
	 */
	@GetMapping()
	public String displayMyAccount(Model model) {

		// If the user session is valid redirect to login
		if (hasValidUserSession()) {
			// Show account details
			addCommonAttributes(model);
			var user = userManager.getUser(loggedInSession.getUserName());
			
			model.addAttribute("userModel", user);
			logger.info("Displaying account details for user: {}", loggedInSession.getUserName());
		}
		else {
			logger.warn("Attempted access to account details without a valid session.");
		}

		return "myaccount";
	}

	/**
	 * Edit account details page url request mapping.
	 * 
	 * @param model model
	 * @return account page
	 */
	@GetMapping("edit")
	public String displayEditMyAccount(Model model) {

		// If the user session is valid redirect to login
		if (hasValidUserSession()) {
			// Show account details
			addCommonAttributes(model);
			var user = userManager.getUser(loggedInSession.getUserName());
			model.addAttribute("userModel", user);
			logger.info("Displaying edit account page for user: {}", loggedInSession.getUserName());
		}
		else {
			logger.warn("Attempted access to edit account page without a valid session.");
		}

		return "editaccount";
	}

	/**
	 * Sing up form request handler. Check for user validity and adds a user.
	 * 
	 * @param userModel user object
	 * @param result      result
	 * @param model       model
	 * @return login page on success sign up page on failure
	 */
	@PostMapping("/doEditUser")
	public String doEditUser(@ModelAttribute @Valid UserModel userModel, BindingResult result,
			Model model) {

		// If the user session is valid redirect to login
		if (!hasValidUserSession()) {
			logger.warn("No valid session found while attempting to edit user details.");
			return "editaccount";
		}

		// otherwise process edit
		addCommonAttributes(model);
		
		// check if there is errors
		if (result.hasErrors()) {
			logger.warn("Validation errors occurred while editing user details for user: {}", loggedInSession.getUserName());
			return "editaccount";
		}


		// If all looks OK. update the user to store
		userModel.setPassword(bcrypt.encode(userModel.getPassword())); // encrypt password text
		if (!userManager.updateUser(userModel)) {
			// If failed set the error 
			result.rejectValue("name", "error.userModel", "Error updating details. Please try again later.");
			logger.error("Failed to update user details for user: {}", loggedInSession.getUserName());
			return "editaccount";
		}
		
		// When edit succeeds, we can redirect to account
		logger.info("Successfully updated user details for user: {}", loggedInSession.getUserName());
		return "redirect:/myaccount";
	}
	
	/**
	 * Delete account details page url request mapping.
	 * 
	 * @param model model
	 * @return account page
	 */
	@GetMapping("delete")
	public String displayDeleteMyAccount(Model model) {

		// If the user session is valid redirect to login
		if (hasValidUserSession()) {
			// Show account details
			addCommonAttributes(model);
			var user = userManager.getUser(loggedInSession.getUserName());
			model.addAttribute("userModel", user);
			logger.info("Displaying delete account page for user: {}", loggedInSession.getUserName());
		}
		else {
			logger.warn("Attempted access to delete account page without a valid session.");
		}
			return "deleteaccount";
	}

	/**
	 * Sing up form request handler. Check for user validity and adds a user.
	 * 
	 * @param userModel user object
	 * @param result      result
	 * @param model       model
	 * @return login page on success sign up page on failure
	 */
	@PostMapping("/doDeleteUser")
	public String doDeleteUser(@ModelAttribute @Valid UserModel userModel, BindingResult result,
			Model model) {

		// If the user session is valid redirect to login
		if (!hasValidUserSession()) {
			logger.warn("No valid session found while attempting to delete user account.");
			return "deleteaccount";
		}

		// We are deleting current session user. So model is not relevant here
		var user = userManager.getUser(loggedInSession.getUserName());
		// delete the user from store
		if (!removeBooksAndUser(user)) {
			// If failed set the error 
			result.rejectValue("name", "error.userModel", "Error deleting user. Please try again later.");
			logger.error("Failed to delete user account for user: {}", loggedInSession.getUserName());
			return "deleteaccount";
		}
		
		// When deletion succeed log out the user as it is no longer valid
		loggedInSession.logOut();
		logger.info("Successfully deleted user account for user: {}", loggedInSession.getUserName());
		
		// When delete succeeds, we can redirect to home
		return "redirect:/";
	}
	
	/**
	 * Delete books for user and then delete user
	 * @param user user
	 * @return success or fail
	 */
	@Transactional
	private boolean removeBooksAndUser(UserModel user) {
		try {
		// TODO: Delete orders and invoice for user
		if (!bookService.deleteAllBooksForUser(user.getUserName())){
			logger.error("Failed to delete books for user: {}", user.getUserName());
			return false;
		}
		
		if (!userManager.deleteUser(user)) {
			logger.error("Failed to delete user: {}", user.getUserName());
			return false;
		}
		}catch(Exception ex) {
			logger.error("Exception occurred while deleting user and books for user: {}", user.getUserName(), ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
