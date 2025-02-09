package com.bookstore.business;

import com.bookstore.models.DashboardModel;

/**
 * Represent a user session and actions
 */
public interface UserSessionInterface {
	
	/**
	 * Get current session user name
	 * @return user name
	 */
	public String getUserName();
	
	/**
	 * Set current session user
	 * @param user user
	 */
	public void setCurrentUser(String user);
	
	/**
	 * Get current user dash board object
	 * @return model
	 */
	public DashboardModel getUserDashboard();
	
	/**
	 * Update the dashborad model details for current user
	 */
	public void updateUserDashboard();
	
	/**
	 * Log out current user
	 */
	public void logOut();
}
