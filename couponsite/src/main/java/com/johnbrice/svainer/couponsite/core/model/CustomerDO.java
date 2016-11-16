package com.johnbrice.svainer.couponsite.core.model;

public class CustomerDO {

	private long customerId;
	private String customerName;
	private String password;
	private String email;

	public CustomerDO(long customerId, String customerName, String password, String email) {
		this.customerId = customerId;
		this.customerName = customerName;
		this.password = password;
		this.email = email;
	}

	public long getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		String customer = 
		"Customer Id:  " + getCustomerId() + "\n" +
		"Customer Name: " + getCustomerName() + "\n" +
		"Password:     " + getPassword() + "\n" +
		"Email:        " + getEmail() + "\n";
			return customer;
	}
}
