package com.johnbrice.svainer.couponsite.service.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(/* urlPatterns = {"/admin", "/company", "/customer"} */)
public class SessionFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String requestURI = request.getRequestURI();

		if (requestURI.contains("login")) {
			System.out.println("session login");
			HttpSession session = request.getSession(false);
			boolean isSessionActive = session == null ? false : true;
			System.out.println("isSessionActive: " + isSessionActive);
			if (session != null) {
				System.out.println("sessioId: " + session.getId());
			}
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		HttpSession session = request.getSession(false);

		if (session == null) {
			System.out.println("session is null:-)");
			response.sendRedirect("http://localhost:8080/jerseyservice/login.html");
		} else {
			System.out.println("session Do Filter");
			filterChain.doFilter(servletRequest, servletResponse);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
