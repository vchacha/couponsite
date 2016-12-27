package com.johnbrice.svainer.couponsite.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
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
import com.johnbrice.svainer.couponsite.core.logic.exception.CompanyValidationException;
import com.johnbrice.svainer.couponsite.core.logic.exception.CouponValidationException;
import com.johnbrice.svainer.couponsite.core.logic.exception.DAOException;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;

@Path("/company")
public class CompanyService {
	
	LoginManager loginManagerCompany = new LoginManager();
	CouponClientFacade couponClientFacadeCompany = loginManagerCompany.login("12345", "eB765", ClientType.COMPANY);
	
	@Context
	private HttpServletRequest httpServletRequest;

	@Path("/createcoupon/{couponid}/{companyid}/{title}/{startdate}/{enddate}/{amount}/{type}/{message}/{price}") ///{image}"	
	@POST
	@Produces("application/json")
	public Response createCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId,
			@PathParam("title") String title, @PathParam("startdate") String startDateString,
			@PathParam("enddate") String endDateString, @PathParam("amount") int amount, @PathParam("type") String couponType,
			@PathParam("message") String message, @PathParam("price") double price/* @PathParam("image") String image*/) {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    Date startDate = new Date();
		    Date endDate= new Date();
		    try {
		    	startDate = dateFormat.parse(startDateString);

		    } catch (ParseException e) {
		    	throw new DAOException("cannot parse date from coupon table: " + "\n" + e.getMessage()); 
		    }	
		    
		    try {
		    	endDate = dateFormat.parse(endDateString);

		    } catch (ParseException e) {
		    	throw new DAOException("cannot parse date from coupon table: " + "\n" + e.getMessage()); 
		    }	
		CouponDO couponDO = new CouponDO(couponId, companyId, title, startDate, endDate, amount, Type.valueOf(couponType), message,
				price, "image");
		try {
			couponClientFacadeCompany.createCoupon(couponDO);
			
			return Response.status(200).entity("Successfully created coupon").build();
			
		} catch (CouponValidationException e) {
			return Response.status(500).entity("Wasn't able to create coupon").build();
		}
	}

	@Path("/deletecoupon/{couponid}/{companyid}/{title}/{startdate}/{enddate}/{amount}/{type}/{message}/{price}") ///{image}"
	@DELETE
	@Produces("application/json")
	public Response removeCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId,
			@PathParam("title") String title, @PathParam("startdate") String startDateString,
			@PathParam("enddate") String endDateString, @PathParam("amount") int amount, @PathParam("type") String couponType,
			@PathParam("message") String message, @PathParam("price") double price/* @PathParam("image") String image*/) {
	   			
		CouponDO couponDO = new CouponDO(couponId, companyId, title, formatDateFromString(startDateString), formatDateFromString(endDateString), amount, Type.valueOf(couponType), message,
				price, "image");
		try {
			int numberOfDeletedCoupons = couponClientFacadeCompany.removeCoupon(couponDO);
			if (numberOfDeletedCoupons > 0){
			return Response.status(200).entity("Coupon successfully deleted").build();
			} else {
				return Response.status(201).entity("Coupon doesn't exist").build();
				}
		} catch (CouponValidationException e) {	
			return Response.status(500).entity("Wasn't able to delete coupon").build();
		}
	}

	@Path("/updatecoupon/{couponid}/{companyid}/{title}/{startdate}/{enddate}/{amount}/{type}/{message}/{price}") ///{image}"
	@POST
	@Produces("application/json")
	public Response updateCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId,
			@PathParam("title") String title, @PathParam("startdate") String startDateString,
			@PathParam("enddate") String endDateString, @PathParam("amount") int amount, @PathParam("type") String couponType,
			@PathParam("message") String message, @PathParam("price") double price/* @PathParam("image") String image*/) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date startDate = new Date();
	    Date endDate= new Date();
	    try {
	    	startDate = dateFormat.parse(startDateString);

	    } catch (ParseException e) {
	    	throw new DAOException("cannot parse date from coupon table: " + "\n" + e.getMessage()); 
	    }	
	    
	    try {
	    	endDate = dateFormat.parse(endDateString);

	    } catch (ParseException e) {
	    	throw new DAOException("cannot parse date from coupon table: " + "\n" + e.getMessage()); 
	    }	
		
		CouponDO couponDO = new CouponDO(couponId, companyId, title, startDate, endDate, amount, Type.valueOf(couponType), message,
				price, "image");
		try{
			int numberOfUpdatedCoupons = couponClientFacadeCompany.updateCoupon(couponDO);
			if (numberOfUpdatedCoupons > 0){
			return Response.status(200).entity("Coupon successfully updated").build();
			} else {
				return Response.status(201).entity("Coupon doesn't exist").build();
			}
			
		} catch (CouponValidationException e) {	
			return Response.status(500).entity("Wasn't able to update coupon").build();
		}
	}

	@Path("/getcoupon/{couponid}/{companyid}")
	@GET
	@Produces({ "application/json" })
	public Response getCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId) {
		try{
		CouponDO couponDO = couponClientFacadeCompany.getCoupon(companyId, couponId);
		if (couponDO == null){
		return Response.status(201).entity("Coupon doesn't exist").build();
			} else {		
					return Response.status(200).entity(couponDO).build();
			}
		} catch (CompanyValidationException e) {	
			return Response.status(500).entity("Wasn't able to find coupon").build();
		}
	}

	@Path("/getallcouponsbycompany/{companyid}")
	@GET
	@Produces("application/json")
	public Response getAllCouponsByCompany(@PathParam("companyid") long companyId) {
		Collection<CouponDO> coupons = new ArrayList<>();
		try{
			coupons = couponClientFacadeCompany.getAllCouponsByCompany(companyId);
				if (coupons.isEmpty()){
					return Response.status(201).entity("To this company doesn't exist any coupon").build();
				} else {		
					return Response.status(200).entity(coupons).build();
				}
		} catch (CouponValidationException e) {	
			return Response.status(500).entity("Wasn't able to find any coupon").build();
		}
		
	}

	@Path("/getallcouponsbycompanyandtype/{companyid}/{type}")
	@GET
	@Produces("application/json")
	public Response getAllCouponsByCompanyAndType(@PathParam("companyid") long companyId,
			@PathParam("type") String type) {
		Collection<CouponDO> coupons = new ArrayList<>();
		try{
			coupons = couponClientFacadeCompany.getAllCouponsByCompanyAndType(companyId, Type.valueOf(type));
				if (coupons.isEmpty()){
					return Response.status(201).entity("To this company doesn't exist any coupon").build();
				} else {		
					return Response.status(200).entity(coupons).build();
				}
		
		} catch (CouponValidationException e) {	
			return Response.status(500).entity("Wasn't able to find any coupon").build();
		}
	}
	
	@Path("/createcouponhello/{hi}")
	@GET
	@Produces("application/json")
	public Response helloCoupon(@PathParam("hi") String hi) {
		try {
			return Response.status(200).entity("hi: " + hi).build();			
		} catch (CouponValidationException e) {
			return Response.status(500).entity("Wasn't able to create coupon").build();
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