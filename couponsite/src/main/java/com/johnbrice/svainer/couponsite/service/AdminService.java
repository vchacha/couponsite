package com.johnbrice.svainer.couponsite.service;

import java.util.Collection;

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
import com.johnbrice.svainer.couponsite.core.model.CompanyDO;
import com.johnbrice.svainer.couponsite.core.model.CustomerDO;

@Path("/admin")
public class AdminService {
	
    private LoginManager loginManagerAdmin = new LoginManager();
    private CouponClientFacade couponClientFacadeAdmin = loginManagerAdmin.login("Admin", "1234", ClientType.ADMIN);
    
	
	@Path("/hello")
	@GET
	@Produces("application/json")	
	public String helloWorld(){
		return "hello world";
	}
	
	@Path("/createcompany/companyid={companyId}&companyname={companyName}&password={password}&email={email}")
	@PUT
	@Produces("application/json")
	public void createCompany(@PathParam ("companyId") long companyId,
		@PathParam ("companyName") String companyName,
		@PathParam ("password") String password,
		@PathParam ("email") String email){
			CompanyDO tempCompanyDO = new CompanyDO(companyId, companyName,password,email);
			couponClientFacadeAdmin.createCompany(tempCompanyDO);
	}
		
	@Path("/deletecompany/companyid={companyId}&companyname={companyName}&password={password}&email={email}")
	@DELETE
	@Produces("application/json")
	public void removeCompany(@PathParam ("companyId") long companyId,
		@PathParam ("companyName") String companyName,
		@PathParam ("password") String password,
		@PathParam ("email") String email){
			CompanyDO tempCompanyDO = new CompanyDO(companyId, companyName,password,email);
			couponClientFacadeAdmin.removeCompany(tempCompanyDO);
	}
	
	@Path("/updatecompany/companyid={companyId}&companyname={companyName}&password={password}&email={email}")
	@POST
	@Produces("application/json")
	public void updateCompany(@PathParam ("companyId") long companyId,
		@PathParam ("companyName") String companyName,
		@PathParam ("password") String password,
		@PathParam ("email") String email){
			CompanyDO tempCompanyDO = new CompanyDO(companyId, companyName,password,email);
			couponClientFacadeAdmin.updateCompany(tempCompanyDO);	
	}

	@Path("/getcompany/companyid={companyId}")
	@GET
	@Produces("application/json")
	public CompanyDO getCompany(@PathParam ("companyId") long companyId){
		return couponClientFacadeAdmin.getCompany(companyId);
	}
	
	@Path("/getallcompanies")
	@GET
	@Produces("application/json")
	public Collection<CompanyDO> getAllCompanies(){
		return couponClientFacadeAdmin.getAllCompanies();
	}
	
	@Path("/createcustomer/customerid={customerId}&customername={customerName}&password={password}&email={email}")
	@PUT
	@Produces("application/json")
	public void createCustomer(@PathParam ("customerId") long customerId,
		@PathParam ("customerName") String customerName,
		@PathParam ("password") String password,
		@PathParam ("email") String email){
			CustomerDO tempCustomerDO = new CustomerDO(customerId, customerName,password,email);
			couponClientFacadeAdmin.createCustomer(tempCustomerDO);
	}
	
	@Path("/deletecustomer/customerid={customerId}&customername={customerName}&password={password}&email={email}")
	@DELETE
	@Produces("application/json")
	public void removeCustomer(@PathParam ("customerId") long customerId,
		@PathParam ("customerName") String customerName,
		@PathParam ("password") String password,
		@PathParam ("email") String email){
			CustomerDO tempCustomerDO = new CustomerDO(customerId, customerName,password,email);
			couponClientFacadeAdmin.removeCustomer(tempCustomerDO);
	}
	
	@Path("/updatecustomer/customerid={customerId}&customername={customerName}&password={password}&email={email}")
	@POST
	@Produces("application/json")
	public void updateCustomer(@PathParam ("customerId") long customerId,
		@PathParam ("customerName") String customerName,
		@PathParam ("password") String password,
		@PathParam ("email") String email){
			CustomerDO tempCustomerDO = new CustomerDO(customerId, customerName,password,email);
			couponClientFacadeAdmin.updateCustomer(tempCustomerDO);	
	}
}
