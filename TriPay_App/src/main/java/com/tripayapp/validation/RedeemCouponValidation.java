package com.tripayapp.validation;

import com.tripayapp.model.CouponDTO;
import com.tripayapp.model.error.CouponError;

public class RedeemCouponValidation {

	public CouponError isValidCoupon(CouponDTO coupon) {
		CouponError error = new CouponError();
		boolean valid = true;
//		if (CommonValidation.isNull(coupon.getCouponNumber())) {
//			error.setCouponNumber("Please enter Coupon Number");
//			valid = false;
//		}
//		if (!CommonValidation.isAlphanumeric(coupon.getCouponNumber())) {
//			error.setCouponNumber("Enter valid coupon number");
//			valid = false;
//		}
		error.setValid(valid);
		return error;
	}
}
