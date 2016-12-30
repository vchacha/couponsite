package com.johnbrice.svainer.couponsite.service;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.johnbrice.svainer.couponsite.core.fasade.ClientType;
import com.johnbrice.svainer.couponsite.core.fasade.LoginManager;
import com.johnbrice.svainer.couponsite.core.logic.exception.LoginException;
import javax.ws.rs.core.Response;

@Path("/")
public class LoginService {

	private LoginManager loginManager = new LoginManager();

	@Context
	private HttpServletRequest httpServletRequest;	
	@Context 
    private HttpServletResponse httpServletResponse;

	@Path("/login/{userid}/{password}/{clienttype}")
	@GET
	@Produces("application/json")
	public Response login(@PathParam("userid") String userid, @PathParam("password") String password,
			@PathParam("clienttype") String clientTypeParam) {
		Optional<ClientType> clientType = toClientType(clientTypeParam);
		if (!clientType.isPresent()) {
			System.err.println("ClientType is invalid: " + clientTypeParam);
			return Response.status(500).entity("failure in client type").build();
		}
		try {
			loginManager.login(userid, password, clientType.get());
			//System.out.println("starting from login");
			HttpSession session = httpServletRequest.getSession(true);
			session.setMaxInactiveInterval(3000);
			/*if (session != null) {
				System.out.println("login page: sessioId: " + session.getId());
			}
			System.out.println("this is printed from login page - line after session id print");*/
			
			return  Response.status(200).entity("success").build();
		} catch (LoginException e) {
			System.err.println(e.getMessage());
			return Response.status(500).entity("failure").build();
		}
	}
	
	@Path("/logout")
	@POST
	@Produces("application/json")
	public Response logout() {
			HttpSession session = httpServletRequest.getSession();
			session.invalidate();
			return  Response.status(200).entity("success").build();
		}

	private Optional<ClientType> toClientType(String clientTypeParam) {
		try {
			return Optional.of(ClientType.valueOf(clientTypeParam));
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
	}

}
