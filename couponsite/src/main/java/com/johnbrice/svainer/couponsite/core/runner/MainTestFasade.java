package com.johnbrice.svainer.couponsite.core.runner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.johnbrice.svainer.couponsite.core.database.ConnectionManagerPool;
import com.johnbrice.svainer.couponsite.core.fasade.ClientType;
import com.johnbrice.svainer.couponsite.core.fasade.CouponClientFacade;
import com.johnbrice.svainer.couponsite.core.fasade.LoginManager;
import com.johnbrice.svainer.couponsite.core.logic.exception.CompanyValidationException;
import com.johnbrice.svainer.couponsite.core.logic.exception.CouponValidationException;
import com.johnbrice.svainer.couponsite.core.logic.exception.CustomerValidationException;
import com.johnbrice.svainer.couponsite.core.logic.exception.DAOException;
import com.johnbrice.svainer.couponsite.core.logic.exception.LoginException;
import com.johnbrice.svainer.couponsite.core.model.CompanyDO;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.CustomerDO;
import com.johnbrice.svainer.couponsite.core.model.Type;


public class MainTestFasade {
	//First, we will create same companies and will activate all methods that relate directly to company
	//Second, we will create customers. At the end, will create coupons and activate all methods that relate
	//directly to them.
	
	public static void main(String[] args) throws ParseException {
	try	{
		try {
			LoginManager loginManagerAdminIsIncorrect = new LoginManager();
			//Login with user Admin (username 'Admin', password '1234'), password is incorrect, expect exception
			CouponClientFacade couponClientFacadeAdminIsIncorrect = loginManagerAdminIsIncorrect.login("Admin", "5432", ClientType.ADMIN);						
		} catch (LoginException e) {
			System.out.println("Error!!!" + e.getMessage() + "\n");
		}
		
		// Login with user Admin (username 'Admin', password '1234'), without exception
		LoginManager loginManagerAdmin = new LoginManager();
		CouponClientFacade couponClientFacadeAdmin = loginManagerAdmin.login("Admin", "1234", ClientType.ADMIN);
		
		handleCompany(couponClientFacadeAdmin);				
		handleCustomers(couponClientFacadeAdmin);			

		
		// Now we login to Company Client Facade
		LoginManager loginManagerCompany = new LoginManager();
		CouponClientFacade couponClientFacadeCompany = loginManagerCompany.login("12345", "eB765", ClientType.COMPANY);
		handleCouponsFlow(couponClientFacadeCompany);

		// Now we login to Customer Client Facade
		LoginManager loginManagerCustomer = new LoginManager();
		CouponClientFacade couponClientFacadeCustomer = loginManagerCustomer.login("423865423", "742MyP", ClientType.CUSTOMER);

		handleCouponsPurchase(couponClientFacadeCustomer);
	}
	finally {
		ConnectionManagerPool.getInstance().closeAllConnection();
		}
}




	private static void handleCompany(CouponClientFacade couponClientFacadeAdmin) {
		//Creation a new Company (without exception)
		couponClientFacadeAdmin.createCompany(new CompanyDO(12345, "ebay", "eB765", "ebay@gmail.com"));				
		couponClientFacadeAdmin.createCompany(new CompanyDO(67890, "microsoft", "45M78", "microsoft@gmail.com"));				
		couponClientFacadeAdmin.createCompany(new CompanyDO(3152849, "google", "98765", "google@gmail.com"));
		System.out.println("Already create 3 companies" + "\n");
		
		//Duplicate ID Company,this ID already exist, expect SQLException DAOException
		try {
			couponClientFacadeAdmin.createCompany(new CompanyDO(12345, "ebay", "eB765", "ebay@gmail.com"));				
		} catch (DAOException e) {
			System.err.println(e.getMessage());	
		}
		
		//When Company ID is less then 1000 expect Company Validation Exception
		try {
			couponClientFacadeAdmin.createCompany(new CompanyDO(83, "reebok", "1234", "reebok@gmail.com"));				
		} catch (CompanyValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//When length of Company Name is less then 3 letters, expect Company Validation Exception
		try {
			couponClientFacadeAdmin.createCompany(new CompanyDO(836429, "re", "Nike5", "reebok@gmail.com"));				
		} catch (CompanyValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//When length of Password is less then 4 letters, expect Company Validation Exception
		try {
			couponClientFacadeAdmin.createCompany(new CompanyDO(836429, "reebok", "RBK", "reebok@gmail.com"));				
		} catch (CompanyValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//When Email in invalid format, expect Company Validation Exception
		try {
			couponClientFacadeAdmin.createCompany(new CompanyDO(836429, "reebok", "Nike5", "re.@ebok.gmail.com"));				
		} catch (CompanyValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//Update details of Company according ID, possibly change company name, password and email. 
		//You cann't change company ID. Without Exception
		CompanyDO companyMicrosoft = new CompanyDO (67890, "microsoftName", "45M78SoFt", "misoft@gmail.com");
		//(67890, "microsoft", "45M78", "microsoft@gmail.com") company that exist in database
		couponClientFacadeAdmin.updateCompany(companyMicrosoft);
		System.out.println("Already update company"+ "\n");
		
		//Removing exist Company from database. Without Exception
		couponClientFacadeAdmin.removeCompany(companyMicrosoft);
		System.out.println("Already remove company"+ "\n");
		
		//return Collection of all Companies that exist in database. Without Exception
		System.out.println("All Comapanies: " + "\n"  + couponClientFacadeAdmin.getAllCompanies()+ "\n");
		
		
		//return details eBay company that exist in database. Without Exception
		System.out.println("Company that you search: "  + "\n" + couponClientFacadeAdmin.getCompany(12345)+ "\n");
	}
	
	private static void handleCustomers(CouponClientFacade couponClientFacadeAdmin) {
		//Creation some new Customer (without exception)
		couponClientFacadeAdmin.createCustomer(new CustomerDO(314567892, "Sveta", "PasW432", "sveta@gmail.com"));				
		couponClientFacadeAdmin.createCustomer(new CustomerDO(423865423, "Alisa", "742MyP", "alisa@mail.ru"));				
		couponClientFacadeAdmin.createCustomer(new CustomerDO(227844560, "Moshe", "PoY67", "Moshe@gmail.com"));	
		System.out.println("Already create 3 customers"+ "\n");
		
		//Duplicate customer ID,this ID already exist, expect SQLException DAOException
		try {
			couponClientFacadeAdmin.createCustomer(new CustomerDO(227844560, "Moshe", "PoY67", "Moshe@gmail.com"));				
		} catch (DAOException e) {
			System.err.println(e.getMessage());
		}
		
		//When Customer ID is less then 1000 expect Customer Validation Exception
		try {
			couponClientFacadeAdmin.createCustomer(new CustomerDO(036, "Rivka", "87654", "rivka@gmail.com"));				
		} catch (CustomerValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//When length of Customer Name is less then 2 letters, expect Customer Validation Exception
		try {
			couponClientFacadeAdmin.createCustomer(new CustomerDO(036764, "R", "87654", "rivka@gmail.com"));				
		} catch (CustomerValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//When length of Password is less then 4 letters, expect Customer Validation Exception
		try {
			couponClientFacadeAdmin.createCustomer(new CustomerDO(036764, "Rivka", "8", "rivka@gmail.com"));				
		} catch (CustomerValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//When Email in invalid format, expect Customer Validation Exception
		try {
			couponClientFacadeAdmin.createCustomer(new CustomerDO(036764, "Rivka", "87654s", "rivka@gmail."));				
		} catch (CustomerValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//Update details of Customer according ID, possibly change customer name, password and email. 
		//You cann't change customer ID. Without  Exceptions.
		CustomerDO customerMoshe = new CustomerDO(227844560, "Moysha", "PoY67", "Moysha@gmail.com");
		//(227844560, "Moshe", "PoY67", "Moshe@gmail.com") company that exist in database
		couponClientFacadeAdmin.updateCustomer(customerMoshe);
		System.out.println("Already update customer"+ "\n");
		
		//Removing exist Customer from database. Without  Exceptions.
		couponClientFacadeAdmin.removeCustomer(customerMoshe);
		System.out.println("Already remove customer"+ "\n");
		
		//return Collection of all Customers that exist in database. Without  Exceptions.
		System.out.println("All Customers: " + "\n" + couponClientFacadeAdmin.getAllCustomers()+ "\n");
		
		
		//return details eBay company that exist in database. Without  Exceptions.
		System.out.println("Customer that you search: "  + "\n" + couponClientFacadeAdmin.getCustomer(423865423)+ "\n");
	}

	private static void handleCouponsFlow(CouponClientFacade couponClientFacadeCompany) throws ParseException {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = date.parse("2016-11-30 09:45:00");
		Date endDate = date.parse("2016-12-30 09:45:00");
		Date startDateIncorect = date.parse("2016-10-11 09:45:00");
		
		CouponDO couponVeryHealthy = new CouponDO(61234, 12345, "VeryHealthy", startDate, endDate, 100, Type.HEALTH, "A healthy coupon", 60, "image");
		CouponDO couponEat = new CouponDO(98765, 12345, "Eating", startDate, endDate, 75, Type.FOOD, "Buy a food", 10, "imageFood");
		CouponDO couponEatGood =new CouponDO(75362, 12345, "EatGood", startDate, endDate, 50, Type.FOOD, "Buy a very good food", 50, "imageGoodFood");
		CouponDO couponFitness = new CouponDO(31648, 12345, "Fitness", startDate, endDate, 150, Type.SPORTS, "Fitness", 200, "imageFitness");
		
		
		//Creation some new Coupon (without exception)
		couponClientFacadeCompany.createCoupon(couponVeryHealthy);
		couponClientFacadeCompany.createCoupon(couponEat);
		couponClientFacadeCompany.createCoupon(couponEatGood);
		couponClientFacadeCompany.createCoupon(couponFitness);
		System.out.println("Already create 4 coupons"+ "\n");

		try {
			//Duplicate ID Coupon,this ID already exist, expect SQLException DAOException
			couponClientFacadeCompany.createCoupon(couponFitness);
		} catch (DAOException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			//When Coupon ID is less then 1000 expect Coupon Validation Exception
			couponClientFacadeCompany.createCoupon(new CouponDO(65, 12345, "Television", startDate, endDate, 20, Type.ELECTRICITY, "Television 45'", 3000, "imageTelevision"));
		} catch (CouponValidationException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			//When Company ID is less then 1000 expect Coupon Validation Exception
			couponClientFacadeCompany.createCoupon(new CouponDO(654321, 21, "Television", startDate, endDate, 20, Type.ELECTRICITY, "Television 45'", 3000, "imageTelevision"));
		} catch (CouponValidationException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			//When length of Coupon Title is less then 4 letters, expect Coupon Validation Exception
			couponClientFacadeCompany.createCoupon(new CouponDO(654321, 12345, "Tel", startDate, endDate, 20, Type.ELECTRICITY, "Television 45'", 3000, "imageTelevision"));
		} catch (CouponValidationException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			//When StartDate or/and EndDate of Coupon are overdue or incorrect, expect Coupon Validation Exception
			couponClientFacadeCompany.createCoupon(new CouponDO(654321, 12345, "Television", startDateIncorect, endDate, 20, Type.ELECTRICITY, "Television 45'", 3000, "imageTelevision"));
		} catch (CouponValidationException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			//When Amount of Coupon is negative or is 0, expect Coupon Validation Exception
			couponClientFacadeCompany.createCoupon(new CouponDO(654321, 12345, "Television", startDate, endDate, -1, Type.ELECTRICITY, "Television 45'", 3000, "imageTelevision"));
		} catch (CouponValidationException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			//When length of Coupon Message is less then 4 letters, expect Coupon Validation Exception
			couponClientFacadeCompany.createCoupon(new CouponDO(654321, 12345, "Television", startDate, endDate, 20, Type.ELECTRICITY, "Tel", 3000, "imageTelevision"));
		} catch (CouponValidationException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			//When Price of Coupon is negative or is 0, expect Coupon Validation Exception
			couponClientFacadeCompany.createCoupon(new CouponDO(654321, 12345, "Television", startDate, endDate, 20, Type.ELECTRICITY, "Television 45", 0, "imageTelevision"));
		} catch (CouponValidationException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			//When length of Coupon Image is less then 4 letters, expect Coupon Validation Exception
			couponClientFacadeCompany.createCoupon(new CouponDO(654321, 12345, "Television", startDate, endDate, 20, Type.ELECTRICITY, "Television 45", 3000, "ima"));
		} catch (CouponValidationException e) {
			System.err.println(e.getMessage());
		}
		
		//Update details of Coupon according Coupon ID, possibly change all variables, except Coupon Id.
		//Without Exceptions
		CouponDO couponChangeFitness = new CouponDO(31648, 12345, "Fitness and Exercises", startDate, endDate, 150, Type.SPORTS, "Fitness and Exercises", 200, "imageFitnessAndExercises");		
		//(31648, 12345, "Fitness", startDate, endDate, 150, Type.SPORTS, "Fitness", 200, "imageFitness") coupon that exist in database
		couponClientFacadeCompany.updateCoupon(couponChangeFitness);
		System.out.println("Already update coupon"+ "\n");
		
		//Removing exist Coupon from database, also removing all purchase of this coupons. Without Exceptions
		couponClientFacadeCompany.removeCoupon(couponChangeFitness);
		System.out.println("Already remove coupon"+ "\n");
		
		//return a Coupon that belong to the specific Company. Without Exceptions
		System.out.println("Coupon that you search: "  + "\n" + couponClientFacadeCompany.getCoupon(12345, 61234)+ "\n");
		
		//return Collection of all Coupons that belong to the specific Company. Without Exceptions
		System.out.println("All Coupons specific company: " + "\n" + couponClientFacadeCompany.getAllCouponsByCompany(12345)+ "\n");
		
		//return Collection of all Coupons with specific Type that belong to the specific Company. Without Exceptions
		System.out.println("All Coupons with specific type, specific company: " + "\n" + couponClientFacadeCompany.getAllCouponsByCompanyAndType(12345, Type.FOOD)+ "\n");
	}

	
	private static void handleCouponsPurchase(CouponClientFacade couponClientFacadeCustomer) throws ParseException {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = date.parse("2016-11-30 09:45:00");
		Date endDate = date.parse("2016-12-30 09:45:00");
		
		//All Coupons that already exist in database
		CouponDO couponVeryHealthy = new CouponDO(61234, 12345, "VeryHealthy", startDate, endDate, 100, Type.HEALTH, "A healthy coupon", 60, "image");
		CouponDO couponEat = new CouponDO(98765, 12345, "Eat", startDate, endDate, 75, Type.FOOD, "Buy a food", 10, "imageFood");
		CouponDO couponEatGood =new CouponDO(75362, 12345, "EatGood", startDate, endDate, 50, Type.FOOD, "Buy a very good food", 50, "imageGoodFood");
		CouponDO couponFitness = new CouponDO(31648, 12345, "Fitness", startDate, endDate, 150, Type.SPORTS, "Fitness", 200, "imageFitness");
		
		
		//purchase some Coupon (without exception)
		couponClientFacadeCustomer.purchaseCoupon(couponVeryHealthy, 423865423);
		couponClientFacadeCustomer.purchaseCoupon(couponEat, 423865423);
		couponClientFacadeCustomer.purchaseCoupon(couponEat, 314567892);
		couponClientFacadeCustomer.purchaseCoupon(couponEatGood, 314567892);
		couponClientFacadeCustomer.purchaseCoupon(couponFitness, 314567892);
		System.out.println("Already purchase 5 coupons"+ "\n");
		
		//Duplicate purchase. Customer already purchase this coupon, expect SQLException DAOException	
		try{
			couponClientFacadeCustomer.purchaseCoupon(couponFitness, 314567892);
		} catch (DAOException e) {
			System.err.println(e.getMessage());
		}
		
		//return Collection of Coupons that specific Customer purchase. Without Exceptions
		System.out.println("All Coupons that specific Customer purchase: " + "\n" 
		+ couponClientFacadeCustomer.getAllPurchaseCouponsByCustomer(314567892)+ "\n");
		
		
		//return Collection of Coupons with specific Price, that specific Customer purchase. Without Exceptions
		System.out.println("All Coupons with specific price that specific Customer purchase: " + "\n" 
		+ couponClientFacadeCustomer.getAllPurchaseCouponsByPrice(314567892, 10)+ "\n");
		
		//return Collection of Coupons with specific Type, that specific Customer purchase. Without Exceptions
		System.out.println("All Coupons with specific type that specific Customer purchase: " + "\n" 
		+ couponClientFacadeCustomer.getAllPurchaseCouponsByType(314567892, Type.FOOD)+ "\n");
	}

}
