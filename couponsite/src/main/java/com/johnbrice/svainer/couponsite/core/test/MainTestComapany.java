package com.johnbrice.svainer.couponsite.core.test;

import java.util.Collection;

import com.johnbrice.svainer.couponsite.core.database.CompanyDAO;
import com.johnbrice.svainer.couponsite.core.database.CompanyDBDAO;
import com.johnbrice.svainer.couponsite.core.database.ConnectionManagerPool;
import com.johnbrice.svainer.couponsite.core.model.CompanyDO;


public class MainTestComapany {

	
	public static void main(String[] args) {
	CompanyDAO companyDAO = new CompanyDBDAO();
	try {
		int createdCompany = companyDAO.createCompany(new CompanyDO(5L, "arsenal", "6543", "arsenal@gmail.com"));
		System.out.println("number of inserted records: " + createdCompany);
		
		//CompanyDO insertedCompany = companyDAO.getCompany(5L);
		//System.out.println(insertedCompany.toString());	
		
		
		//Company company = new Company (4L, "Alice", "6543", "Alice@gmail.com");	
		//int removeCompany = companyDAO.removeCompany(company);
		//System.out.println("number of removed records: " + removeCompany);
		
		//CompanyDO companyDO = companyDAO.getCompany(4L);
		//System.out.println(companyDO);
			
		Collection <CompanyDO> companies = companyDAO.getAllCompanies();
		System.out.println(companies.toString());


		//CompanyDO companyDO = new CompanyDO (7L, "AliceNew", "9876", "AliceNew@gmail.com");
		//int updateCompany = companyDAO.updateCompany(companyDO);
		//System.out.println("number of update records: " + updateCompany);
		
		
		//boolean login = companyDAO.login("4", "1234");
		//System.out.println(login);

	} finally  {
		ConnectionManagerPool.getInstance().closeAllConnection();	
	}


}
}
