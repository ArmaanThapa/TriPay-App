package com.tripayapp.model.error;

public class VerifyMobileOTPError {

	private boolean valid;

	private String otp;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}
