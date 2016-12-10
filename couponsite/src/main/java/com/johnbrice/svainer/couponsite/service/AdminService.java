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
import com.johnbrice.svainer.couponsite.core.logic.exception.CustomerValidationException;
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
			CompanyDO company = couponClientFacadeAdmin.getCompany(companyId);
			if (company == null){
				return Response.status(201).entity("Company doesn't exist").build();
			} else {		
			return Response.status(200).entity(company).build();
			}
			
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
	@POST
	@Produces("application/json")
	public Response createCustomer(@PathParam("customerid") long customerId, @PathParam("customername") String customerName,
			@PathParam("password") String password, @PathParam("email") String email) {
		CustomerDO tempCustomerDO = new CustomerDO(customerId, customerName, password, email);
		try {
		couponClientFacadeAdmin.createCustomer(tempCustomerDO);
		return Response.status(200).entity("Successfully created customer").build();
		} catch (CustomerValidationException e) {
		  return Response.status(500).entity("Wasn't able to create customer").build();
		}
	}

	@Path("/deletecustomer/{customerid}/{customername}/{password}/{email}")
	@DELETE
	@Produces("application/json")
	public Response removeCustomer(@PathParam("customerid") long customerId, @PathParam("customername") String customerName,
			@PathParam("password") String password, @PathParam("email") String email) {
		CustomerDO tempCustomerDO = new CustomerDO(customerId, customerName, password, email);
		try {
			int numberOfDeletedCustomers = couponClientFacadeAdmin.removeCustomer(tempCustomerDO);
			if (numberOfDeletedCustomers > 0){
				return Response.status(200).entity("Customer successfully updated").build();
			} else {
				return Response.status(201).entity("Customer doesn't exist").build();
			}
		} catch (CustomerValidationException e) {	
			return Response.status(500).entity("Wasn't able to delete customer").build();
		}	
}

	@Path("/updatecustomer/{customerid}/{customername}/{password}/{email}")
	@POST
	@Produces("application/json")
	public Response updateCustomer(@PathParam("customerid") long customerId, @PathParam("customername") String customerName,
			@PathParam("password") String password, @PathParam("email") String email) {
		CustomerDO customerDO = new CustomerDO(customerId, customerName, password, email);
		try{
			int numberOfUpdatedcustomers = couponClientFacadeAdmin.updateCustomer(customerDO);
			if (numberOfUpdatedcustomers > 0){
			return Response.status(200).entity("Customer successfully updated").build();
			} else {
				return Response.status(201).entity("Customer doesn't exist").build();
				}
		} catch (CustomerValidationException e) {	
			return Response.status(500).entity("Wasn't able to update customer").build();
		}
	}
	
	@Path("/getcustomer/{customerid}")
	@GET
	@Produces("application/json")
	public Response getCustomer(@PathParam("customerid") long customerId) {
		
		try{
			CustomerDO customer = couponClientFacadeAdmin.getCustomer(customerId);
			if (customer == null){
				return Response.status(201).entity("Customer doesn't exist").build();
			} else {		
			return Response.status(200).entity(customer).build();
			}
			
		} catch (CustomerValidationException e) {	
			return Response.status(500).entity("Wasn't able to find customer").build();
		}
	}

}
