package com.johnbrice.svainer.couponsite.core.fasade;

import java.util.Collection;

import com.johnbrice.svainer.couponsite.core.database.CompanyDAO;
import com.johnbrice.svainer.couponsite.core.database.CouponDAO;
import com.johnbrice.svainer.couponsite.core.logic.exception.CouponValidationException;
import com.johnbrice.svainer.couponsite.core.logic.validation.CouponValidator;
import com.johnbrice.svainer.couponsite.core.logic.validation.DataValidator;
import com.johnbrice.svainer.couponsite.core.logic.validation.ValidationResponse;
import com.johnbrice.svainer.couponsite.core.model.CompanyDO;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.CustomerDO;
import com.johnbrice.svainer.couponsite.core.model.Type;


/**
 * Exposes methods for user of type Company. Abstracts two database tables: {Company, Coupon}
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 *  
 *  @throws UnsupportedOperationException for following methods:
 *  {createCompany,
 *   removeCompany,
 *   updateCompany, 
 *   getCompany,
 *   getAllCompanies,
 *   createCustomer,
 *   removeCustomer,
 *   updateCustomer,
 *   getCustomer,
 *   getAllCustomers,
 *   getAllCoupons,
 *   purchaseCoupon,
 *   getAllPurchaseCouponsByCustomer,
 *   getAllPurchaseCouponsByType
 *   getAllPurchaseCouponsByPrice,
 *   getAllCouponsByType,
 *   getAllPurchaseCoupons}
 *   
 *   @throws CouponValidationException
 *   		 if Coupon parameters does not meet required standards
 */
public class CompanyFacade implements CouponClientFacade {
	
	private CouponDAO couponDAO;
	private CompanyDAO companyDAO;

	public CompanyFacade(CouponDAO couponDAO, CompanyDAO companyDAO) {
		super();
		this.couponDAO = couponDAO;
		this.companyDAO = companyDAO;
	}

	@Override
	public void createCompany(CompanyDO company) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int removeCompany(CompanyDO company) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int updateCompany(CompanyDO company) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CompanyDO getCompany(long companyId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<CompanyDO> getAllCompanies() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createCustomer(CustomerDO customer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int removeCustomer(CustomerDO customer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int updateCustomer(CustomerDO customer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CustomerDO getCustomer(long customerId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<CustomerDO> getAllCustomers() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createCoupon(CouponDO couponDO) {
		DataValidator<CouponDO> couponValidator = new CouponValidator();
		ValidationResponse couponValidatorResponse = couponValidator.validateData(couponDO);
		if (couponValidatorResponse.isOk()) {
			couponDAO.createCoupon(couponDO);
			return;
		}
		throw new CouponValidationException(couponValidatorResponse.getErrorMessage());
	}

	@Override
	public int removeCoupon(CouponDO couponDO) {
		return couponDAO.removeCoupon(couponDO);
	}

	@Override
	public int updateCoupon(CouponDO couponDO) {
		DataValidator<CouponDO> couponValidator = new CouponValidator();
		ValidationResponse couponValidatorResponse = couponValidator.validateData(couponDO);
		if (couponValidatorResponse.isOk()) {
			return couponDAO.updateCoupon(couponDO);
		}
		throw new CouponValidationException(couponValidatorResponse.getErrorMessage());
	}

	@Override
	public CouponDO getCoupon(long companyId, long couponId) {
		return couponDAO.getCoupon(companyId, couponId);
	}

	@Override
	public Collection<CouponDO> getAllCoupons() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<CouponDO> getAllCouponsByCompany(long companyId){
		return companyDAO.getAllCouponsByCompany(companyId);
	}
	
	@Override
	public Collection<CouponDO> getAllCouponsByCompanyAndType(long companyId, Type type) {
		return companyDAO.getAllCouponsByCompanyAndType(companyId, type);
	}

	@Override
	public void purchaseCoupon(CouponDO coupon, long customerId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<CouponDO> getAllPurchaseCouponsByCustomer(long customerId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<CouponDO> getAllPurchaseCouponsByType(long customerId, Type type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<CouponDO> getAllPurchaseCouponsByPrice(long customerId, int price) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<CouponDO> getAllCouponsByType(Type type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<CouponDO> getAllPurchaseCoupons() {
		throw new UnsupportedOperationException();
	}

	
}
