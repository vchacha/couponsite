package com.johnbrice.svainer.couponsite.core.database;

import java.util.Collection;

import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;

public interface CouponDAO {

	int createCoupon(CouponDO couponDO);

	int removeCoupon(CouponDO couponDO);

	int updateCoupon(CouponDO couponDO);
	
	CouponDO getCoupon(long companyId, long couponId);
	
	Collection<CouponDO> getAllCoupons();

	Collection<CouponDO> getAllCouponsByType(Type type);
	
	Collection<CouponDO> getAllPurchaseCoupons();

}
