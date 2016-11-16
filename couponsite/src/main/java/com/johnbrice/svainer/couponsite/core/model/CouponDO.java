package com.johnbrice.svainer.couponsite.core.model;

import java.util.Date;

public class CouponDO {
	
	private long companyId;
	private long couponId;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private Type type;
	private String message;
	private double price;
	private String image;
	


	public CouponDO(long couponId, long companyId, String title, Date startDate, Date endDate, int amount, Type type, String message,
			double price, String image) {
		super();
		this.couponId = couponId;
		this.companyId = companyId;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
	}



	public long getCompanyId() {
		return companyId;
	}



	public long getCouponId() {
		return couponId;
	}



	public void setTitle(String title) {
		this.title = title;
	}




	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}




	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}




	public void setAmount(int amount) {
		this.amount = amount;
	}




	public void setType(Type type) {
		this.type = type;
	}




	public void setMessage(String message) {
		this.message = message;
	}




	public void setPrice(double price) {
		this.price = price;
	}




	public void setImage(String image) {
		this.image = image;
	}




	public String getTitle() {
		return title;
	}




	public Date getStartDate() {
		return startDate;
	}




	public Date getEndDate() {
		return endDate;
	}




	public int getAmount() {
		return amount;
	}




	public Type getType() {
		return type;
	}




	public String getMessage() {
		return message;
	}




	public double getPrice() {
		return price;
	}




	public String getImage() {
		return image;
	}

	@Override
	public String toString() {
		String coupon = 
		"Coupon Id:" + getCouponId() + "\n" +
		"Company Id:" + getCompanyId() + "\n" +
		"Title:    " + getTitle() + "\n" +
		"Start Day:" + getStartDate() + "\n" +
		"End Day:  " + getEndDate() + "\n" + 
		"Amount:   " + getAmount() + "\n" +
		"Type:     " + getType() + "\n" +
		"Messege:  " + getMessage() + "\n" + 
		"Price:    " + getPrice() + "\n" +
		"Image:    " + getImage() + "\n"; 
			return coupon;
	}
}
