package com.johnbrice.svainer.couponsite.core.logic.validation;

import java.util.Date;

import com.johnbrice.svainer.couponsite.core.model.CouponDO;

/**
 * Validates Coupon parameters:
 *  <blockquote><pre>
 *     coupon id and company id rules:  expected id should be > 1000
 *     title, message and image rules: expected String should be >= 4 letters
 *     start date rules: expected start date should not be older current date
 *     end date rules: expected end date should not be older current date and start date
 *     amount and price rules: expected amount and price should be > 1
 *     
 * </pre></blockquote><p>
 * <p> If parameters meets required standards, throws ValidationResponse with true
 * <p> If parameters does not meet required standards, throws ValidationResponse with false and all error message
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 *
 */
public class CouponValidator implements DataValidator<CouponDO> {

	private static final int MINIMUN_ALLOWED_STRING_LENGTH = 4;
	private static final long MINIMUN_NUMBER_ID = 1000;

	@Override
	public ValidationResponse validateData(CouponDO couponDO) {
		boolean isCouponIdMeetRules = validateId(couponDO.getCouponId());
		boolean isCompanyIdMeetRules = validateId(couponDO.getCompanyId());
		boolean isTitleMeetRules = validateString(couponDO.getTitle(), MINIMUN_ALLOWED_STRING_LENGTH);
		boolean isDateMeetRules = validateDate(couponDO.getStartDate(), couponDO.getEndDate());
		boolean isAmountMeetRules = validateAmountAndPrice(couponDO.getAmount());
		boolean isMessageMeetRules = validateString(couponDO.getMessage(), MINIMUN_ALLOWED_STRING_LENGTH); 
		boolean isPriceMeetRules = validateAmountAndPrice(couponDO.getPrice());
		boolean isImageMeetRules = validateString(couponDO.getImage(), MINIMUN_ALLOWED_STRING_LENGTH);
		StringBuilder aggregateErrorMessage = new StringBuilder();
		
		if (!isCouponIdMeetRules) {
			aggregateErrorMessage.append("Coupon ID does not meet required standards \n");
		}
		if (!isCompanyIdMeetRules) {
			aggregateErrorMessage.append("Company ID does not meet required standards \n");
		}
		if (!isTitleMeetRules) {
			aggregateErrorMessage.append("Title of coupon does not meet required standards \n");
		}		
		if (!isDateMeetRules) {
			aggregateErrorMessage.append("Start Date or/and End Date does not meet required standards \n");
		}
		if (!isAmountMeetRules) {
			aggregateErrorMessage.append("Amount of coupons does not meet required standards \n");
		}
		if (!isMessageMeetRules) {
			aggregateErrorMessage.append("Message/Information of coupon does not meet required standards \n");
		}
		if (!isPriceMeetRules) {
			aggregateErrorMessage.append("Price does not meet required standards \n");
		}
		if (!isImageMeetRules) {
			aggregateErrorMessage.append("Image does not meet required standards \n");
		}
		
		if (!isCouponIdMeetRules || !isCompanyIdMeetRules || !isTitleMeetRules || !isDateMeetRules 
			|| !isAmountMeetRules || !isMessageMeetRules || !isPriceMeetRules || !isImageMeetRules){
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

	private boolean validateDate(Date startDate, Date endDate) {
		Date currentDate = new Date();
		if (startDate.before(currentDate) || endDate.before(currentDate)) {
			return false;
		}
		if (endDate.before(startDate)) {
			return false;
		}
		return true;
	}

	private boolean validateAmountAndPrice(double price) {
		if (price >= 1) {
			return true;
		}
		return false;
	}
	
	private boolean validateAmountAndPrice(int amount) {
		if (amount >= 1) {
			return true;
		}
		return false;
	}
}
