package com.instantpay.api;

import com.instantpay.model.request.TransactionRequest;
import com.instantpay.model.response.TransactionResponse;
import com.tripayapp.entity.PQService;

public interface ITransactionApi {
	
	TransactionResponse request(TransactionRequest request, PQService service);

}
