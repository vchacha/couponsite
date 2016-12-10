package com.johnbrice.svainer.couponsite.service;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.johnbrice.svainer.couponsite.core.fasade.ClientType;
import com.johnbrice.svainer.couponsite.core.fasade.CouponClientFacade;
import com.johnbrice.svainer.couponsite.core.fasade.LoginManager;
import com.johnbrice.svainer.couponsite.core.logic.exception.CompanyValidationException;
import com.johnbrice.svainer.couponsite.core.model.CompanyDO;
import com.johnbrice.svainer.couponsite.core.model.CustomerDO;

@Path("/admin")
public class AdminService {

	private LoginManager loginManagerAdmin = new LoginManager();
	private CouponClientFacade couponClientFacadeAdmin = loginManagerAdmin.login("Admin", "1234", ClientType.ADMIN);

	@Context
	private HttpServletRequest httpServletRequest;

	@Path("/hello")
	@GET
	@Produces("application/json")
	public String helloWorld() {
		HttpSession session = httpServletRequest.getSession();
		return "hello world " + "session.id: " + session.getId();
	}

	@Path("/createcompany/{companyid}/{companyname}/{password}/{email}")
	@POST
	@Produces("application/json")
	public Response createCompany(@PathParam("companyid") long companyId, @PathParam("companyname") String companyName,
			@PathParam("password") String password, @PathParam("email") String email) {
		CompanyDO companyDO = new CompanyDO(companyId, companyName, password, email);
		
		try {
			couponClientFacadeAdmin.createCompany(companyDO);
			return Response.status(200).entity("Successfully created company").build();
			
		} catch (CompanyValidationException e) {
			return Response.status(500).entity("Wasn't able to create company").build();
		}
	}

	@Path("/deletecompany/{companyid}/{companyname}/{password}/{email}")
	@DELETE
	@Produces("application/json")
	public Response removeCompany(@PathParam("companyid") long companyId, @PathParam("companyname") String companyName,
			@PathParam("password") String password, @PathParam("email") String email) {
		CompanyDO companyDO = new CompanyDO(companyId, companyName, password, email);
		try {
			int numberOfDeletedCompanies = couponClientFacadeAdmin.removeCompany(companyDO);
			if (numberOfDeletedCompanies > 0){
			return Response.status(200).entity("Company successfully deleted").build();
			} else {
				return Response.status(201).entity("Company doesn't exist").build();
				}
		} catch (CompanyValidationException e) {	
			return Response.status(500).entity("Wasn't able to delete company").build();
		}
	}

	@Path("/updatecompany/{companyid}/{companyname}/{password}/{email}")
	@POST
	@Produces("application/json")
	public Response updateCompany(@PathParam("companyid") long companyId, @PathParam("companyname") String companyName,
			@PathParam("password") String password, @PathParam("email") String email) {
			CompanyDO companyDO = new CompanyDO(companyId, companyName, password, email);
		try{
			int numberOfUpdatedCompanies = couponClientFacadeAdmin.updateCompany(companyDO);
			if (numberOfUpdatedCompanies > 0){
			return Response.status(200).entity("Company successfully updated").build();
			} else {
				return Response.status(201).entity("Company doesn't exist").build();
			}
			
		} catch (CompanyValidationException e) {	
			return Response.status(500).entity("Wasn't able to update company").build();
		}
	}

	@Path("/getcompany/{companyid}")
	@GET
	@Produces("application/json")
	public Response getCompany(@PathParam("companyid") long companyId) {
		
		try{
			couponClientFacadeAdmin.getCompany(companyId);
			return Response.status(200).entity("Successfully find company").build();
			
		} catch (CompanyValidationException e) {	
			return Response.status(500).entity("Wasn't able to find company").build();
		}
	}

	@Path("/getallcompanies")
	@GET
	@Produces("application/json")
	public Collection<CompanyDO> getAllCompanies() {
		return couponClientFacadeAdmin.getAllCompanies();
	}

	@Path("/createcustomer/{customerid}/{customername}/{password}/{email}")
	@PUT
	@Produces("application/json")
	public void createCustomer(@PathParam("customerid") long customerId, @PathParam("customername") String customerName,
			@PathParam("password") String password, @PathParam("email") String email) {
		CustomerDO tempCustomerDO = new CustomerDO(customerId, customerName, password, email);
		couponClientFacadeAdmin.createCustomer(tempCustomerDO);
	}

	@Path("/deletecustomer/{customerid}/{customername}/{password}/{email}")
	@DELETE
	@Produces("application/json")
	public void removeCustomer(@PathParam("customerid") long customerId, @PathParam("customername") String customerName,
			@PathParam("password") String password, @PathParam("email") String email) {
		CustomerDO tempCustomerDO = new CustomerDO(customerId, customerName, password, email);
		couponClientFacadeAdmin.removeCustomer(tempCustomerDO);
	}

	@Path("/updatecustomer/{customerId}/{customerName}/{password}/{email}")
	@POST
	@Produces("application/json")
	public void updateCustomer(@PathParam("customerid") long customerId, @PathParam("customername") String customerName,
			@PathParam("password") String password, @PathParam("email") String email) {
		CustomerDO tempCustomerDO = new CustomerDO(customerId, customerName, password, email);
		couponClientFacadeAdmin.updateCustomer(tempCustomerDO);
	}
}
