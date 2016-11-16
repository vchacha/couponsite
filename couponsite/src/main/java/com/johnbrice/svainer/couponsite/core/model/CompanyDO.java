package com.johnbrice.svainer.couponsite.core.model;

public class CompanyDO {

	private long companyId;
	private String companyName;
	private String password;
	private String email;

	public CompanyDO(long companyId, String companyName, String password, String email) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.password = password;
		this.email = email;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}


	@Override
	public String toString() {
		String company = 
		"Company Id:   " + companyId + "\n" +
		"Company Name: " + companyName + "\n" +
		"Password:     " + password + "\n" +
		"Email:        " + email + "\n";
		return company;
	}
}
