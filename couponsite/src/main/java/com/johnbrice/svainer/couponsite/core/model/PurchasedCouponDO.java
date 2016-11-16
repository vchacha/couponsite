package com.johnbrice.svainer.couponsite.core.model;

public class PurchasedCouponDO {

	private long customerId;
	private long couponId;

	public PurchasedCouponDO(long customerId, long couponId) {
		this.customerId = customerId;
		this.couponId = couponId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public long getCouponId() {
		return couponId;
	}

}
