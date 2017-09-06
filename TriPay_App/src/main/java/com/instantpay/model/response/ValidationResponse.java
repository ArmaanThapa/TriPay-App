package com.instantpay.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.instantpay.model.Validation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationResponse {

	private boolean success;
	private Validation validation;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
	}
}
