package com.bookstore.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.bookstore.models.DashboardModel;
import com.bookstore.models.UserModel;

/**
 * Session based service object to handle logged in user 
 */
@Service
@SessionScope
public class UserSessionService implements UserSessionInterface{
	private static final Logger logger = LogManager.getLogger(UserSessionService.class);
	
	@Autowired
	private UserManagementService userManager;
	
	@Autowired
	private BookStoreInterface bookStore;
	
	private UserModel currentUser; // current logged in user
	private DashboardModel userDashboard;	// Dashboard details of the current user
	
	/**
	 * Create a  user session service
	 */
	public UserSessionService() {
		
	}

	@Override
	public String getUserName() {
		if (currentUser != null) {
			logger.debug("Getting username for current user: {}", currentUser.getUserName());
			return currentUser.getUserName();
		}

		logger.warn("No current user found.");
		return null;
	}

	@Override
	public void setCurrentUser(String user) {
		logger.info("Setting current user: {}", user);
		currentUser = userManager.getUser(user);
		if (currentUser != null) {
			logger.info("Current user set successfully: {}", currentUser.getUserName());
			updateUserDashboard();
		} else {
			logger.warn("User not found: {}", user);
		}
	}

	@Override
	public DashboardModel getUserDashboard() {
		if (userDashboard != null) {
			logger.debug("Fetching dashboard for user: {}", currentUser != null ? currentUser.getUserName() : "No user logged in");
		} else {
			logger.warn("No dashboard available for user: {}", currentUser != null ? currentUser.getUserName() : "No user logged in");
		}
		return this.userDashboard;
	}
	
	/**
	 * Create or update dashboard model for the current user
	 */
	@Override
	public void updateUserDashboard() {
		logger.info("Updating dashboard for user: {}", currentUser.getUserName());
		this.userDashboard = new DashboardModel();
		
		// Update books listed by the user
		var books = bookStore.getBooksForUser(currentUser.getUserName());
		if (books!= null) {
			var smallList = books.size() > 5 ?  books.subList(0, 5) : books; 
			userDashboard.setBooksOwned(smallList); // Dashboard only need few books
			logger.info("Dashboard updated with books for user: {}. Books count: {}", currentUser.getUserName(), smallList.size());
		}
		else {
			logger.warn("No books found for user: {}", currentUser.getUserName());
		}
		
		// TODO: Update profit using order service
	}
	
	@Override
	public void logOut() {
		logger.info("Logging out user: {}", currentUser != null ? currentUser.getUserName() : "No user logged in");
		this.currentUser = null;
		this.userDashboard = null;
	}
}
