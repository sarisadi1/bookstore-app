package com.bookstore.business;

import com.bookstore.models.UserModel;

/**
 * User Management business service interface
 */
public interface UserManagementInterface {
	/**
	 * Authenticate a user
	 * @param user user
	 * @param password password
	 * @return success or fail
	 */
	public boolean authenticate(String user, String password);
	
	/**
	 * Check if user is duplicate
	 * @param user user
	 * @return duplicate or not
	 */
	public boolean isDuplicateUser(String user);
	
	/**
	 * Add a new user
	 * @param user user
	 * @return success or fail
	 */
	public boolean addUser(UserModel user);
	
	/**
	 * Update user details
	 * @param user user
	 * @return success or fail
	 */
	public boolean updateUser(UserModel user);
	
	/**
	 *  Remove  a user from store
	 * @param user user
	 * @return success/fail
	 */
	public boolean deleteUser(UserModel user);
}
