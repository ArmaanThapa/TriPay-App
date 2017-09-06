package com.tripayapp.validation;

import com.tripayapp.entity.User;
import com.tripayapp.model.Status;
import com.tripayapp.model.error.VerifyMobileOTPError;
import com.tripayapp.repositories.UserRepository;

public class MobileOTPValidation {

	private final UserRepository userRepository;

	public MobileOTPValidation(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * For OTP Validation
	 * 
	 * @param OTP
	 * @param phoneNO
	 * @return
	 */
	public VerifyMobileOTPError validateOTP(String OTP, String username) {
		VerifyMobileOTPError error = new VerifyMobileOTPError();
		boolean valid = true;

		if (OTP.length() == 0) {
			error.setOtp("OTP Required");
			valid = false;
		}

		if (OTP.length() != 6) {
			error.setOtp("OTP is 6 digit");
			valid = false;
		}

		User u = userRepository.findByUsername(username);
		if (u != null) {
			if (u.getMobileStatus().equals(Status.Active)) {
				error.setOtp(username + " Mobile Number is already Verified");
				valid = false;
			}
		}
		error.setValid(valid);
		return error;
	}

	public VerifyMobileOTPError validateResendOTP(String username) {
		VerifyMobileOTPError error = new VerifyMobileOTPError();
		boolean valid = true;

		User u = userRepository
				.findByUsernameAndStatus(username, Status.Active);
		if (u == null) {
			valid = true;
		} else {
			error.setOtp(username + " Mobile Number is already verified");
			valid = false;
		}
		error.setValid(valid);
		return error;
	}
}
