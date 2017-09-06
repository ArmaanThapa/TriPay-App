package com.instantpay.api;

import com.instantpay.model.request.BalanceRequest;
import com.instantpay.model.response.BalanceResponse;

public interface IBalanceApi {

	BalanceResponse request(BalanceRequest request);
}
