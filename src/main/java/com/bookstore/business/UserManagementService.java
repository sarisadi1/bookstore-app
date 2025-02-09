package com.bookstore.business;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookstore.data.UserDataService;
import com.bookstore.data.entity.UserEntity;
import com.bookstore.models.UserModel;

/**
 * User management business service to manipulate users 
 */
@Service
public class UserManagementService implements UserManagementInterface, UserDetailsService {
	private static final Logger logger = LogManager.getLogger(UserManagementService.class);

	@Autowired
	private UserDataService userManager;
	
	/**
	 * Create a user management service object
	 */
	public UserManagementService() {
		
	}
	
	/**
	 * Find user entity based on user name
	 * @param userName user name
	 * @return entity or null
	 */
	private UserEntity findUserEntity(String userName) {
		logger.debug("Finding user entity for userName: {}", userName);
		var users = userManager.findAll();
		UserEntity userObj = null;
		for (var user : users) {
			if (user.getUserName().compareTo(userName) == 0) {
				logger.debug("User found: {}", userName);
				userObj = user;
				break;
			}
		}
		if(userObj == null){
			logger.warn("User not found for userName: {}", userName);
		}
		return userObj;
	}

	@Override
	public boolean authenticate(String userName, String password) {
		logger.info("Authenticating user: {}", userName);
		// Get user list and find the matching one
		var users = userManager.findAll();
		for (var user : users) {
			if (user.getUserName().compareTo(userName) == 0 && user.getPassword().compareTo(password) == 0) {
				// valid user if user name and password matches
				logger.info("Authentication successful for user: {}", userName);
				return true;
			}
		}

		// return false if not found
		logger.warn("Authentication failed for user: {}", userName);
		return false;
	}

	@Override
	public boolean isDuplicateUser(String userName) {
		logger.info("Checking if user is duplicate for userName: {}", userName);
		var users = userManager.findAll();
		for (var user : users) {
			if (user.getUserName().compareTo(userName) == 0) {
				// duplicate user if a match is found on name
				logger.warn("Duplicate user found: {}", userName);
				return true;
			}
		}

		// return false if not found
		logger.info("No duplicate user found for userName: {}", userName);
		return false;
	}

	/**
	 * Get UserModel object for a user name string
	 * @param userName username string
	 * @return UserModel object or null
	 */
	public UserModel getUser(String userName) {
		logger.info("Fetching user details for userName: {}", userName);
		var userObj = findUserEntity(userName);		

		if (userObj != null) {
			var user = new UserModel();
			user.setUserName(userObj.getUserName());
			user.setPassword(userObj.getPassword());
			user.setFirstName(userObj.getFirstName());
			user.setLastName(userObj.getLastName());
			user.setEmail(userObj.getEmail());
			user.setPhone(userObj.getPhone());
			logger.info("User details fetched successfully for userName: {}", userName);
			return user;
		} else {
			logger.warn("User not found for userName: {}", userName);
			return null;
		}
	}

	@Override
	public boolean addUser(UserModel user) {
		logger.info("Adding new user: {}", user.getUserName());
		var entity = new UserEntity(0, user.getUserName(), user.getPassword(), user.getFirstName(), user.getLastName(),
				user.getEmail(), user.getPhone());
		boolean success = userManager.create(entity) != null;
		if (success) {
			logger.info("User added successfully: {}", user.getUserName());
		} else {
			logger.warn("Failed to add user: {}", user.getUserName());
		}
		return success;
	}
	
	@Override
	public boolean updateUser(UserModel userModel) {
		// Find existing user id
		logger.info("Updating user: {}", userModel.getUserName());
		var userObj = findUserEntity(userModel.getUserName());

		if(userObj != null) {
			// Update the details from new model
			var entity = new UserEntity(userObj.getId(), userModel.getUserName(), userModel.getPassword(), userModel.getFirstName(), userModel.getLastName(),
					userModel.getEmail(), userModel.getPhone());
			boolean success = userManager.update(entity);
			if (success) {
				logger.info("User updated successfully: {}", userModel.getUserName());
			} else {
				logger.warn("Failed to update user: {}", userModel.getUserName());
			}
			return success;
		}
		logger.warn("User not found to update: {}", userModel.getUserName());
		return false;
	}
	
	@Override
	public boolean deleteUser(UserModel userModel) {
		// Find the user and initiate delete
		logger.info("Deleting : {}", userModel.getUserName());
		var userObj= findUserEntity(userModel.getUserName());
		if (userObj != null) {
			boolean success = userManager.delete(userObj);
			if (success) {
				logger.info("User deleted successfully: {}", userModel.getUserName());
			} else {
				logger.warn("Failed to delete user: {}", userModel.getUserName());
			}
			return success;
		}
		logger.warn("User not found to delete: {}", userModel.getUserName());
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Find existing user by name
		logger.info("Loading user by username: {}", username);
		var user = findUserEntity(username);
		if (user != null) {
			// Once found give the User authority. Our app only needs one for now
			var authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("USER"));
			logger.info("User loaded successfully: {}", username);
			return new User(user.getUserName(), user.getPassword(), authorities);
		}
		else {
			logger.error("User not found for username: {}", username);
			throw new UsernameNotFoundException("User not found");
		}
	}
}
