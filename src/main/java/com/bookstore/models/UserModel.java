package com.bookstore.models;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Model class for Sign up User details 
 */
public class UserModel {
	
	@NotNull(message="User name is a required field")
	@Size(min=1, max=20, message="User name must be between 1 and 20 characters")
	private String userName; // User Id unique
	
	@NotNull(message="Password is a required field")
	@Size(min=1, max=20, message="Password must be between 1 and 20 characters")
	private String password;	
	
	@NotNull(message="First name is a required field")
	@Size(min=1, max=20, message="First name must be between 1 and 20 characters")
	private String firstName;
	
	@NotNull(message="Last name is a required field")
	@Size(min=1, max=20, message="Last name must be between 1 and 20 characters")
	private String lastName;
	
	@NotNull(message="Email is a required field")
	@Email(message="Email is not valid",regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags= Pattern.Flag.CASE_INSENSITIVE)
	@Size(min=1, max=50, message="Last name must be between 1 and 50 characters")
	private String email;
	
	@NotNull(message="Phone number is a required field")
	@Size(min=10, max=10,message="Phone number must be 10 digits")
	@Digits(fraction=0, integer=10, message="Phone number must be 10 digits")
	private String phone;

	/**
	 * Create a UserModel object
	 */
	public UserModel() {
		
	}
	
	/**
	 * Get the unique user name for the user 
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set the User name for user
	 * @param userName user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Get the password for the user
	 * @return password password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password for the user 
	 * @param password password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Get the first name of the user
	 * @return first name name string
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set first name to the user 
	 * @param firstName name string
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get the last name of the user
	 * @return last name last name string
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set the last name for the user
	 * @param lastName last name string
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get the e-mail id of the user
	 * @return mail id mail id
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the e-mail id for user
	 * @param email mail id
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get phone number of user 
	 * @return phone number
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set phone number for the user
	 * @param phone phone number
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
