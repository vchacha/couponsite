package com.johnbrice.svainer.couponsite.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.johnbrice.svainer.couponsite.core.fasade.ClientType;
import com.johnbrice.svainer.couponsite.core.fasade.CouponClientFacade;
import com.johnbrice.svainer.couponsite.core.fasade.LoginManager;
import com.johnbrice.svainer.couponsite.core.logic.exception.CouponValidationException;
import com.johnbrice.svainer.couponsite.core.logic.exception.CustomerValidationException;
import com.johnbrice.svainer.couponsite.core.logic.exception.DAOException;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;

@Path("/customer")
public class CustomerService {
	LoginManager loginManagerCustomer = new LoginManager();
	CouponClientFacade couponClientFacadeCustomer = loginManagerCustomer.login("314567892", "PasW432", ClientType.CUSTOMER);
	
	@Context
	private HttpServletRequest httpServletRequest;

	@Path("/purchasecoupon/{customerid}/{couponid}/{companyid}/{title}/{startdate}/{enddate}/{amount}/{type}/{message}/{price}") ///{image}"
	@POST
	@Produces("application/json")
	public Response purchaseCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId,
			@PathParam("title") String title, @PathParam("startdate") String startDateString,
			@PathParam("enddate") String endDateString, @PathParam("amount") int amount, @PathParam("type") String couponType,
			@PathParam("message") String message, @PathParam("price") double price, @PathParam("image") String image,
			@PathParam("customerid") long customerId) {
		
	CouponDO couponDO = new CouponDO(couponId, companyId, title, formatDateFromString(startDateString), formatDateFromString(endDateString), amount, Type.valueOf(couponType), message,
			price, "image");
	try {
		couponClientFacadeCustomer.purchaseCoupon(couponDO, customerId);
		
		return Response.status(200).entity("Successfully purchase coupon").build();
		
	} catch (CustomerValidationException e) {
		return Response.status(500).entity("Wasn't able to purchase coupon").build();
	}
}

	@Path("/getallpurchasecouponbytype/{customerid}/{type}")
	@GET
	@Produces("application/json")
	public Response getAllPurchaseCouponsByType(@PathParam("type") String couponType,
			@PathParam("customerid") long customerId) {
		Collection<CouponDO> coupons = new ArrayList<>();
		try{
			coupons = couponClientFacadeCustomer.getAllPurchaseCouponsByType(customerId, Type.valueOf(couponType));
				if (coupons.isEmpty()){
					return Response.status(201).entity("To this type doesn't exist any purchase coupon").build();
				} else {		
					return Response.status(200).entity(coupons).build();
				}
		} catch (CustomerValidationException e) {	
			return Response.status(500).entity("Wasn't able to find any coupon").build();
		}	
	}

	@Path("/getallpurchasecouponsbycustomer/{customerid}")
	@GET
	@Produces("application/json")
	public Response getAllPurchaseCouponsByCustomer(@PathParam("customerid") long customerId) {
		Collection<CouponDO> coupons = new ArrayList<>();
		try{
			coupons = couponClientFacadeCustomer.getAllPurchaseCouponsByCustomer(customerId);
				if (coupons.isEmpty()){
					return Response.status(201).entity("To this customer doesn't exist any purchase coupon").build();
				} else {		
					return Response.status(200).entity(coupons).build();
				}
		} catch (CustomerValidationException e) {	
			return Response.status(500).entity("Wasn't able to find any coupon").build();
		}	
	}

	@Path("/getallpurchasecouponbyprice/{customerid}/{price}")
	@GET
	@Produces("application/json")
	public Response getAllPurchaseCouponsByPrice(@PathParam("price") int price,
			@PathParam("customerid") long customerId) {
		Collection<CouponDO> coupons = new ArrayList<>();
		try{
			coupons = couponClientFacadeCustomer.getAllPurchaseCouponsByPrice(customerId, price);
				if (coupons.isEmpty()){
					return Response.status(201).entity("To this customer doesn't exist any purchase coupon with such price").build();
				} else {		
					return Response.status(200).entity(coupons).build();
				}
		} catch (CustomerValidationException e) {	
			return Response.status(500).entity("Wasn't able to find any coupon").build();
		}	
	}

	private static Date formatDateFromString(String fromString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd yyyy");
		try {
			 Date firstDate = simpleDateFormat.parse(fromString);
			 SimpleDateFormat anotherDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			 String format = anotherDateFormat.format(firstDate);
			 return anotherDateFormat.parse(format);
		} catch (ParseException e) {
			throw new DAOException("cannot parse date: " + fromString + " from coupon table: " + "\n" + e.getMessage()); 
		}
	}
}
