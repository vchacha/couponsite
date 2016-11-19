package com.johnbrice.svainer.couponsite.service;

import java.util.Collection;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.johnbrice.svainer.couponsite.core.fasade.ClientType;
import com.johnbrice.svainer.couponsite.core.fasade.CouponClientFacade;
import com.johnbrice.svainer.couponsite.core.fasade.LoginManager;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;

@Path("/customer")
public class CustomerService {
	LoginManager loginManagerCustomer = new LoginManager();
	CouponClientFacade couponClientFacadeCustomer = loginManagerCustomer.login("12345", "eB765", ClientType.CUSTOMER);

	@Path("/purchasecoupon/couponid={couponId}&companyid={companyId}&title={title}&startdate={startDate}&enddate={endDate}&amount={amount}&type={type}&message={message}&price={price}&image={image}&customerId={customerId}")
	@POST
	@Produces("application/json")
	public void purchaseCoupon(@PathParam("couponId") long couponId, @PathParam("companyId") long companyId,
			@PathParam("title") String title, @PathParam("startDate") Date startDate,
			@PathParam("endDate") Date endDate, @PathParam("amount") int amount, @PathParam("type") Type type,
			@PathParam("message") String message, @PathParam("price") int price, @PathParam("image") String image,
			@PathParam("customerId") long customerId) {
		CouponDO tempCouponDO = new CouponDO(couponId, companyId, title, startDate, endDate, amount, type, message,
				price, image);
		couponClientFacadeCustomer.purchaseCoupon(tempCouponDO, customerId);

	}

	@Path("/getallpurchasecouponbytype/type={type}&customerid={customerId}")
	@GET
	@Produces("application/json")
	public Collection<CouponDO> getAllPurchaseCouponsByType(@PathParam("type") Type type,
			@PathParam("customerId") long customerId) {
		return couponClientFacadeCustomer.getAllPurchaseCouponsByType(customerId, type);
	}

	@Path("/getallpurchasecouponsbycustomer/customerid={customerId}")
	@GET
	@Produces("application/json")
	public Collection<CouponDO> getAllPurchaseCouponsByCustomer(@PathParam("customerId") long customerId) {
		return couponClientFacadeCustomer.getAllPurchaseCouponsByCustomer(customerId);
	}

	@Path("/getallpurchasecouponbyprice/couponid={couponId}&companyid={companyId}&title={title}&startdate={startDate}&enddate={endDate}&amount={amount}&type={type}&message={message}&price={price}&image={image}&customerId={customerId}")
	@GET
	@Produces("application/json")
	public Collection<CouponDO> getAllPurchaseCouponsByPrice(@PathParam("price") int price,
			@PathParam("customerId") long customerId) {
		return couponClientFacadeCustomer.getAllPurchaseCouponsByPrice(customerId, price);
	}

}
