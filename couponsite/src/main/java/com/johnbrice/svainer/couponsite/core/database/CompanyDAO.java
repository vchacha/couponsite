package com.johnbrice.svainer.couponsite.core.database;

import java.util.Collection;

import com.johnbrice.svainer.couponsite.core.model.CompanyDO;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;

public interface CompanyDAO {

	int createCompany(CompanyDO company);

	int removeCompany(CompanyDO company);

	int updateCompany(CompanyDO company);

	CompanyDO getCompany(long companyId);

	Collection<CompanyDO> getAllCompanies();

	Collection<CouponDO> getAllCouponsByCompany(long companyId);
	
	Collection<CouponDO> getAllCouponsByCompanyAndType(long companyId, Type type);
	
	boolean login(long companyId, String password);

}
