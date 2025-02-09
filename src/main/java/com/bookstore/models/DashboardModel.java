package com.bookstore.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent Dashboard data for the user
 * Since it does not accept any form information, validation is not needed.
 */
public class DashboardModel {
	private float profit;
	private List<BookModel> booksOwned = new ArrayList<BookModel>();
	
	//private List<InvoiceModel> shippingInvoice = new ArrayList<InvoiceModel>();
	//private List<OrderModel> ordersPlaced = new ArrayList<OrderModel>();
	
	/**
	 * Create Dashboard model object
	 */
	public DashboardModel() {
		
	}
	
	/**
	 * Get profit for the user
	 * @return profit
	 */
	public float getProfit() {
		return profit;
	}
	
	/**
	 * Set profit for the user
	 * @param profit profit
	 */
	public void setProfit(float profit) {
		this.profit = profit;
	}
	
	/**
	 * Get the list of books owned by this user
	 * @return books list
	 */
	public List<BookModel> getBooksOwned() {
		return booksOwned;
	}
	
	/**
	 * Set the list of books owned
	 * @param booksOwned books list
	 */
	public void setBooksOwned(List<BookModel> booksOwned) {
		this.booksOwned = booksOwned;
	}
}
