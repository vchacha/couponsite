package com.johnbrice.svainer.couponsite.core.test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.johnbrice.svainer.couponsite.core.database.ConnectionManagerPool;
import com.johnbrice.svainer.couponsite.core.database.CouponDAO;
import com.johnbrice.svainer.couponsite.core.database.CouponDBDAO;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;


public class MainTestCoupon {

	public static void main(String[] args) throws ParseException {
		try {
			CouponDAO couponDAO = new CouponDBDAO();
			//System.out.println(couponDAO.getAllCouponsByCompany(12345));
			//System.out.println(couponDAO.getAllCoupons().toString());
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = date.parse("2016-10-11 09:46:00");
			Date endDate = date.parse("2016-10-21 09:46:00");
		
			//CouponDO couponDO = new CouponDO(61234, 12345, "VeryHealthy", startDate, endDate, 100, Type.HEALTH, "A healthy coupon", 60, "image");
			CouponDO couponDO2 = new CouponDO(98765, 65432, "Eat", startDate, endDate, 75, Type.FOOD, "Buy food", 10, "imageFood");
			//couponDAO.removeCoupon(couponDO);
			
			//System.out.println(couponDAO.createCoupon(couponDO2));
			
			//System.out.println(couponDAO.updateCoupon(couponDO));
			
			//System.out.println(couponDAO.getAllCoupons());
			
			//System.out.println(couponDAO.getAllCouponsByType(Type.HEALTH));
			
			//System.out.println(couponDAO.getCoupon(12345, 61234));
			
			//System.out.println(couponDAO.getAllPurchaseCoupons());		
			
		} finally {
			ConnectionManagerPool.getInstance().closeAllConnection();
		}

	}

}
