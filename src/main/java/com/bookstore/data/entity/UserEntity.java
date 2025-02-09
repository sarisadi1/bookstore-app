package com.bookstore.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


/**
 * Database entity class for User
 */
@Table("USERS")
public class UserEntity {
	
	@Id
	private long id;
	
	@Column("NAME")
	private String userName; // User Id unique
	
	@Column("PASSWORD")
	private String password;	
	
	@Column("FIRST_NAME")
	private String firstName;
	
	@Column("LAST_NAME")
	private String lastName;
	
	@Column("EMAIL")
	private String email;
	
	@Column("PHONE")
	private String phone;

	/**
	 * Create a user entity db object
	 * 
	 * @param id id
	 * @param userName name
	 * @param password password
	 * @param firstName first name
	 * @param lastName last name
	 * @param email mail
	 * @param phone number
	 */
	public UserEntity(long id, String userName, String password, String firstName, String lastName, String email,
			String phone) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	/**
	 * Get database id
	 * @return id id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set database id
	 * @param id id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get user name
	 * @return name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set user name
	 * @param userName name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Get password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set password
	 * @param password password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get first name
	 * @return first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set first name
	 * @param firstName first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get last name
	 * @return last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set last name
	 * @param lastName last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get user email
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set user email
	 * @param email mail id
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get user phone number
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set user phone number
	 * @param phone number
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
