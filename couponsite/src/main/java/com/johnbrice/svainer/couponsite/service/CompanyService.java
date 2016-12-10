package com.johnbrice.svainer.couponsite.service;

import java.util.Collection;
import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.johnbrice.svainer.couponsite.core.fasade.ClientType;
import com.johnbrice.svainer.couponsite.core.fasade.CouponClientFacade;
import com.johnbrice.svainer.couponsite.core.fasade.LoginManager;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;

@Path("/company")
public class CompanyService {
	LoginManager loginManagerCompany = new LoginManager();
	CouponClientFacade couponClientFacadeCompany = loginManagerCompany.login("12345", "eB765", ClientType.COMPANY);

	@Path("/createcoupon/{couponid}/{companyid}/{title}/{startdate}/{endDate}/{amount}/{type}/{message}/{price}/{image}")
	@PUT
	@Produces({ "application/json" })
	public void createCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId,
			@PathParam("title") String title, @PathParam("startdate") Date startDate,
			@PathParam("enddate") Date endDate, @PathParam("amount") int amount, @PathParam("type") Type type,
			@PathParam("message") String message, @PathParam("price") double price, @PathParam("image") String image) {
		CouponDO tempCouponDO = new CouponDO(couponId, companyId, title, startDate, endDate, amount, type, message,
				price, image);
		couponClientFacadeCompany.createCoupon(tempCouponDO);
	}

	@Path("/removecoupon/{couponid}/{companyid}/{title}/{startdate}/{endDate}/{amount}/{type}/{message}/{price}/{image}")
	@DELETE
	@Produces({ "application/json" })
	public void removeCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId,
			@PathParam("title") String title, @PathParam("startdate") Date startDate,
			@PathParam("enddate") Date endDate, @PathParam("amount") int amount, @PathParam("type") Type type,
			@PathParam("message") String message, @PathParam("price") double price, @PathParam("image") String image) {
		CouponDO tempCouponDO = new CouponDO(couponId, companyId, title, startDate, endDate, amount, type, message,
				price, image);
		couponClientFacadeCompany.removeCoupon(tempCouponDO);
	}

	@Path("/updatecoupon/{couponid}/{companyid}/{title}/{startdate}/{enddate}/{amount}/{type}/{message}/{price}/{image}")
	@POST
	@Produces({ "application/json" })
	public void updateCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId,
			@PathParam("title") String title, @PathParam("startdate") Date startDate,
			@PathParam("enddate") Date endDate, @PathParam("amount") int amount, @PathParam("type") Type type,
			@PathParam("message") String message, @PathParam("price") double price, @PathParam("image") String image) {
		CouponDO tempCouponDO = new CouponDO(couponId, companyId, title, startDate, endDate, amount, type, message,
				price, image);
		couponClientFacadeCompany.updateCoupon(tempCouponDO);
	}

	@Path("/getcoupon/{couponid}/{companyid}")
	@GET
	@Produces({ "application/json" })
	public CouponDO getCoupon(@PathParam("couponid") long couponId, @PathParam("companyid") long companyId) {
		return couponClientFacadeCompany.getCoupon(companyId, couponId);
	}

	@Path("/getallcoupon/{companyid}")
	@GET
	@Produces({ "application/json" })
	public Collection<CouponDO> getAllCouponsByCompany(@PathParam("companyid") long companyId) {
		return couponClientFacadeCompany.getAllCouponsByCompany(companyId);
	}

	@Path("/getcouponbytype/{companyid}/{type}")
	@GET
	@Produces("application/json")
	public Collection<CouponDO> getAllCouponsByCompanyAndType(@PathParam("companyid") long companyId,
			@PathParam("type") Type type) {
		return couponClientFacadeCompany.getAllCouponsByCompanyAndType(companyId, type);
	}

}