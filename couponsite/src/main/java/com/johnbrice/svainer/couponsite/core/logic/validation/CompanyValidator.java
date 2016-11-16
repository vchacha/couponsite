package com.johnbrice.svainer.couponsite.core.logic.validation;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.johnbrice.svainer.couponsite.core.model.CompanyDO;

/**
 * Validates Company parameters:
 *  <blockquote><pre>
 *     id rules:  expected id should be > 1000
 *     company name rules: expected name should be >= 3 letters
 *     password rules: expected password should be >= 4 letters
 *     email rules: expected email in valid email format
 * </pre></blockquote><p>
 * <p> If parameters meets required standards, throws ValidationResponse with true
 * <p> If parameters does not meet required standards, throws ValidationResponse with false and all error message
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 *
 */
public class CompanyValidator implements DataValidator<CompanyDO> {

	private static final int MINIMUM_ALLOWED_PASSWORD_LENGTH = 4;
	private static final long MINIMUN_NUMBER_ID = 1000;
	private static final int MINIMUN_ALLOWED_COMPANY_NAME_LENGTH = 3;

	@Override
	public ValidationResponse validateData(CompanyDO companyDO) {
		boolean isIdMeetRules = validateId(companyDO.getCompanyId());
		boolean isNameMeetRules = validateString(companyDO.getCompanyName(), MINIMUN_ALLOWED_COMPANY_NAME_LENGTH);
		boolean isPasswordMeetRules = validateString(companyDO.getPassword(), MINIMUM_ALLOWED_PASSWORD_LENGTH);
		boolean isEmailMeetRules = validateEmail(companyDO.getEmail());
		StringBuilder aggregateErrorMessage = new StringBuilder();
		if (!isIdMeetRules) {
			aggregateErrorMessage.append("ID does not meet required standards \n");
		}
		if (!isNameMeetRules) {
			aggregateErrorMessage.append("Company name does not meet required standards \n");
		}
		if (!isPasswordMeetRules) {
			aggregateErrorMessage.append("Password does not meet required standards \n");
		}
		if (!isEmailMeetRules) {
			aggregateErrorMessage.append("Email does not meet required standards \n");
		}
		if (!isIdMeetRules || !isNameMeetRules || !isPasswordMeetRules || !isEmailMeetRules) {
			return new ValidationResponse(false, aggregateErrorMessage.toString());
		}
		return new ValidationResponse(true, null);
	}

	private boolean validateId(long id) {
		if (id >= MINIMUN_NUMBER_ID) {
			return true;
		}
		return false;
	}

	private boolean validateString(String string, int minAllowedLength) {
		if (string == null || string.isEmpty()) {
			return false;
		}
		return string.length() >= minAllowedLength;
	}

	private boolean validateEmail(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&�*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&�*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

}
