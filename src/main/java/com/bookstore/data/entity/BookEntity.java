package com.bookstore.data.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Database entity class for book
 */
@Table("BOOKS")
public class BookEntity {
	@Id
	private long id;
	
	@Column("BOOK_ID")
	private String bookId;
	
	@Column("NAME")
	private String name;
	
	@Column("AUTHOR")
	private String author;
	
	@Column("PUBLISH_DATE")
	private Date publishDate;
	
	@Column("DESCRIPTION")
	private String description;
	
	@Column("PRICE")
	private float price;
	
	@Column("QUANTITY")
	private int quantity;
	
	@Column("OWNER")
	private long owner;

	/**
	 * Create BookEntity object from database 
	 * @param id db id field
	 * @param bookId generated book id
	 * @param name name
	 * @param author author
	 * @param publishDate date
	 * @param description description
	 * @param price price
	 * @param quantity quantity
	 * @param owner owner
	 */
	public BookEntity(long id, String bookId, String name, String author, Date publishDate, String description,
			float price, int quantity, long owner) {
		super();
		this.id = id;
		this.bookId = bookId;
		this.name = name;
		this.author = author;
		this.publishDate = publishDate;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.owner = owner;
	}

	/**
	 * Get the id from db index
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the id 
	 * @param id id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get BookID field
	 * @return book id
	 */
	public String getBookId() {
		return bookId;
	}

	/**
	 * Set Book id field
	 * @param bookId id
	 */
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	/**
	 * Get name of the book
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of book
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get author of book
	 * @return author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * set book author
	 * @param author author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Get book publish date
	 * @return date
	 */
	public Date getPublishDate() {
		return publishDate;
	}

	/**
	 * Set Book published date
	 * @param publishDate date
	 */
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	/**
	 * Get book description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set book description
	 * @param description description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the price of the book
	 * @return price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * Set price of book
	 * @param price price
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * Get book quantity
	 * @return quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Set the book quantity
	 * @param quantity quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Get owner id
	 * @return id
	 */
	public long getOwner() {
		return owner;
	}

	/**
	 * Set owner number
	 * @param owner owner id
	 */
	public void setOwner(long owner) {
		this.owner = owner;
	}	
}
