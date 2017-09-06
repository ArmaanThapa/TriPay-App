package com.tripayapp.model.error;

public class ForgotPasswordError {

	private String errorLength;
	private boolean valid;

	public String getErrorLength() {
		return errorLength;
	}

	public void setErrorLength(String errorLength) {
		this.errorLength = errorLength;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	

}
