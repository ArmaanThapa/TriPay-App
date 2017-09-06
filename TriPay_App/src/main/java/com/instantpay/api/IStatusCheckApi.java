package com.instantpay.api;

import com.instantpay.model.request.StatusCheckRequest;
import com.instantpay.model.response.StatusCheckResponse;

public interface IStatusCheckApi {

	StatusCheckResponse request(StatusCheckRequest request);
}
