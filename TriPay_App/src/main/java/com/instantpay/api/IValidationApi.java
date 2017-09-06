package com.instantpay.api;

import com.instantpay.model.request.ValidationRequest;
import com.instantpay.model.response.ValidationResponse;

public interface IValidationApi {

	ValidationResponse request(ValidationRequest request);
	
}
