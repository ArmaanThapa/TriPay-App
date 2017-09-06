package com.instantpay.api;

import com.instantpay.model.request.ServicesRequest;
import com.instantpay.model.response.ServicesResponse;

public interface IServicesApi {

	ServicesResponse request(ServicesRequest request);
	
}
