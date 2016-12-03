package com.johnbrice.svainer.couponsite.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.johnbrice.svainer.couponsite.core.fasade.ClientType;
import com.johnbrice.svainer.couponsite.core.fasade.LoginManager;
import com.johnbrice.svainer.couponsite.core.logic.exception.LoginException;

@Path("/")
public class LoginService {

	private LoginManager loginManager = new LoginManager();

	@Context
	private HttpServletRequest httpServletRequest;

	@Path("/login/{userid}/{password}/{clientTypeParam}")
	@GET()
	@Produces("application/json")
	public boolean login(@PathParam("userid") String userid, @PathParam("password") String password,
			@PathParam("clientTypeParam") String clientTypeParam) {
		Optional<ClientType> clientType = toClientType(clientTypeParam);
		if (!clientType.isPresent()) {
			System.err.println("ClientType is invalid: " + clientTypeParam);
			return false;
		}
		try {
			loginManager.login(userid, password, clientType.get());
			System.out.println("starting from login");
			HttpSession session = httpServletRequest.getSession(true);
			session.setMaxInactiveInterval(20);
			if (session != null) {
				System.out.println("login page: sessioId: " + session.getId());
			}
			System.out.println("this is printed from login page - line after session id print");

			return true;
		} catch (LoginException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private Optional<ClientType> toClientType(String clientTypeParam) {
		try {
			return Optional.of(ClientType.valueOf(clientTypeParam));
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
	}

}
