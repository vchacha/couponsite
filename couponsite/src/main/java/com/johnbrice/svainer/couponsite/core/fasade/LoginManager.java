package com.johnbrice.svainer.couponsite.core.fasade;

import com.johnbrice.svainer.couponsite.core.database.CompanyDAO;
import com.johnbrice.svainer.couponsite.core.database.CompanyDBDAO;
import com.johnbrice.svainer.couponsite.core.database.CouponDBDAO;
import com.johnbrice.svainer.couponsite.core.database.CustomerDAO;
import com.johnbrice.svainer.couponsite.core.database.CustomerDBDAO;
import com.johnbrice.svainer.couponsite.core.logic.exception.LoginException;



/**
 * Enables to login to three client type: {Admin, Company, Customer}
 * by providing ID and password.
 * <p>If Id and password does not found throws {@code LoginException}
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 *  
 */
public class LoginManager {
	
	private CouponClientFacade couponClientFacade;

	public CouponClientFacade login(String id, String password, ClientType clientType) {
		if (isLoginSucceed(id, password, clientType)) {
			if (clientType.equals(ClientType.ADMIN)) {
				//System.out.println("ADMIN SUCCESS");
				couponClientFacade = new AdminFacade(new CompanyDBDAO(), new CustomerDBDAO(), new CouponDBDAO());
				return couponClientFacade;
			}

			if (clientType.equals(ClientType.COMPANY)) {
				//System.out.println("COMPANY SUCCESS");
				couponClientFacade = new CompanyFacade(new CouponDBDAO(), new CompanyDBDAO());
				return couponClientFacade;
			}

			if (clientType.equals(ClientType.CUSTOMER)) {
				couponClientFacade = new CustomerFacade(new CustomerDBDAO());
				return couponClientFacade;
			}
		}
		throw new LoginException(
				"id or/and password are not valid! Please try again");
	}
	
	public CouponClientFacade getCouponClientFacade() {
		return couponClientFacade;
	}

	private boolean isLoginSucceed(String id, String password, ClientType clientType) {
		boolean result = false;
		switch (clientType) {
		case ADMIN:
			if (id.equals("Admin") && password.equals("1234")) {
				result = true;
			}
			break;

		case COMPANY:
			CompanyDAO companyDAO = new CompanyDBDAO();
			result = companyDAO.login(Long.parseLong(id), password);
			break;

		case CUSTOMER:
			CustomerDAO customerDAO = new CustomerDBDAO();
			return customerDAO.login(Long.parseLong(id), password);

		default:
			result = false;
		}
		return result;
	}
}
