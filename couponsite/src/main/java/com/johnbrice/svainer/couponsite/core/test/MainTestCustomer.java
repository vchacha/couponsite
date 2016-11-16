package com.johnbrice.svainer.couponsite.core.test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.johnbrice.svainer.couponsite.core.database.ConnectionManagerPool;
import com.johnbrice.svainer.couponsite.core.database.CustomerDAO;
import com.johnbrice.svainer.couponsite.core.database.CustomerDBDAO;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.CustomerDO;
import com.johnbrice.svainer.couponsite.core.model.Type;


public class MainTestCustomer {

	public static void main(String[] args) throws ParseException {
		try {
			CustomerDAO customerDAO = new CustomerDBDAO();
		
		
			int numberOfCreateCustomer = 0;
			//System.out.println(numberOfCreateCustomer =  customerDAO.createCustomer(new CustomerDO(317147593, "Sveta Vainer", "12345", "sveta@gmail.com")));
			//System.out.println(numberOfCreateCustomer =  customerDAO.createCustomer(new CustomerDO(98765432, "Alisa", "8765", "alisa@gmail.com")));
			//System.out.println(customerDAO.getCustomer(317147593));
		
			
			CustomerDO customerDO = new CustomerDO(317147593, "Vainer Sveta", "12345", "svetaV@gmail.com");
			
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = date.parse("2016-10-11 09:46:00");
			Date endDate = date.parse("2016-10-21 09:46:00");
		
			CouponDO couponDO = new CouponDO(61234, 12345, "VeryHealthy", startDate, endDate, 100, Type.HEALTH, "A healthy coupon", 60, "image");
			CouponDO couponDO2 = new CouponDO(98765, 65432, "Eat", startDate, endDate, 75, Type.FOOD, "Buy food", 10, "imageFood");
			//System.out.println(customerDAO.updateCustomer(customerDO));
	
			//System.out.println(customerDAO.getCustomer(317147593));
			
			//System.out.println(customerDAO.getAllCustomers());
			
			
			//System.out.println(customerDAO.removeCustomer(customerDO));
						
			//customerDAO.purchaseCoupon(couponDO, 456789);		
			//customerDAO.purchaseCoupon(couponDO, 23234343);	
			//customerDAO.purchaseCoupon(couponDO2, 23234343);
			
			//System.out.println(customerDAO.getAllPurchaseCouponsByCustomer(23234343));
			
			//System.out.println(customerDAO.getAllPurchaseCouponsByType(23234343, Type.FOOD));
			
			//System.out.println(customerDAO.getAllPurchaseCouponsByPrice(23234343, 60));
			
			//boolean login = customerDAO.login(98765432, "8765");
			//System.out.println(login);
				
		} finally {
			ConnectionManagerPool.getInstance().closeAllConnection();
		}
		
		
	}

}
