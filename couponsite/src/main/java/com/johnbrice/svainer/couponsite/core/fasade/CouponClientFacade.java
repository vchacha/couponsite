package com.johnbrice.svainer.couponsite.core.fasade;

import java.util.Collection;

import com.johnbrice.svainer.couponsite.core.model.CompanyDO;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.CustomerDO;
import com.johnbrice.svainer.couponsite.core.model.Type;



public interface CouponClientFacade {
	
	void createCompany(CompanyDO company);
	
	void removeCompany(CompanyDO company);
	
	void updateCompany(CompanyDO company);
	
	CompanyDO getCompany( long companyId);
	
	Collection <CompanyDO> getAllCompanies();

	void createCustomer(CustomerDO customer);
	
	void removeCustomer(CustomerDO customer);
	
	void updateCustomer(CustomerDO customer);
	
	CustomerDO getCustomer(long customerId);
	
	Collection <CustomerDO> getAllCustomers();
	
	void createCoupon(CouponDO coupon);
	
	void removeCoupon(CouponDO coupon);
	
	void updateCoupon(CouponDO coupon);
	
	CouponDO getCoupon(long companyId, long couponId);
	
	Collection <CouponDO> getAllCoupons();
	
	Collection<CouponDO> getAllCouponsByCompany(long companyId);
	
	Collection <CouponDO> getAllCouponsByCompanyAndType(long companyId, Type type);
	
	void purchaseCoupon(CouponDO coupon, long customerId);
	
	Collection <CouponDO> getAllPurchaseCouponsByCustomer(long customerId);
	
	Collection <CouponDO> getAllPurchaseCouponsByType(long customerId, Type type);
	
	Collection <CouponDO> getAllPurchaseCouponsByPrice(long customerId, int price);
	
	Collection<CouponDO> getAllCouponsByType(Type type);
	
	Collection<CouponDO> getAllPurchaseCoupons();
}
