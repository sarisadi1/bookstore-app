package com.bookstore.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.bookstore.data.entity.UserEntity;
import com.bookstore.data.repository.UserRepository;

/**
 * Class for UserData repo handling management
 */
@Service("UserDataService")
public class UserDataService implements DataAccessInterface<UserEntity>{

	private static final Logger logger = LogManager.getLogger(UserDataService.class);

	@Autowired
	private UserRepository userRepo;
	
	/**
	 * Create data service for user object 
	 */
	public UserDataService() {
		
	}


	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;

	public String signUp(UserEntity userEntity) {
		if (userRepo.findByUserName(userEntity.getUserName()).isPresent()) {
			return "User already exists!";
		}
		// Hash the password before saving
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		userRepo.save(userEntity);
		return "User registered successfully!";
	}

	public String logIn(String userName, String password) {
		Optional<UserEntity> user = userRepo.findByUserName(userName);
		if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
			return "Login successful!";
		}
		return "Invalid credentials!";
	}
	
	/**
	 * Find all users
	 */
	@Override
	public List<UserEntity> findAll() {
		var users = new ArrayList<UserEntity>();
		try {
			var iter = userRepo.findAll();
			iter.forEach(users::add);
			logger.info("Fetched all users, total count: {}", users.size());
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error("Error occurred while fetching all users", ex);
		}
		
		return users;
	}

	/**
	 * find user with id 
	 */
	@Override
	public UserEntity findById(long id) {
		try {
			var user = userRepo.findById(id);
			if (user.isPresent()) {
				logger.info("User found with ID: {}", id);
				return user.get();
			} else {
				logger.warn("No user found with ID: {}", id);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error occurred while fetching user with ID: {}", id, ex);
		}

		return null;
	}

	/**
	 * create new user in db
	 */
	@Override
	public UserEntity create(UserEntity user) {
		try {
			logger.info("Successfully created new user: {}", user.getUserName());
			return userRepo.save(user);
		}catch(Exception ex) {
			logger.error("Error occurred while creating user: {}", user.getUserName(), ex);
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Update an existing user
	 */
	@Override
	public boolean update(UserEntity user) {
		try {
			userRepo.save(user);
			logger.info("Successfully updated user: {}", user.getUserName());
		}catch(Exception ex) {
			logger.error("Error occurred while updating user: {}", user.getUserName(), ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * delete a user from  db
	 */
	@Override
	public boolean delete(UserEntity user) {
		try {
			userRepo.delete(user);
			logger.info("Successfully deleted user: {}", user.getUserName());
		}catch(Exception ex) {
			logger.error("Error occurred while deleting user: {}", user.getUserName(), ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	public void saveUser(UserEntity user) {
		userRepo.save(user);
	}
//	@Repository
//	public interface UserRepository extends JpaRepository<UserEntity, Long> {
//		Optional<UserEntity> findByUserName(String userName);
//	}



}
