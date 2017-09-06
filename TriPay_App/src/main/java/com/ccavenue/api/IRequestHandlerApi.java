package com.ccavenue.api;

import javax.servlet.http.HttpServletRequest;

import com.ccavenue.model.CCAvenueRequest;

public interface IRequestHandlerApi {

	CCAvenueRequest request(HttpServletRequest request);
	CCAvenueRequest request(CCAvenueRequest ccAvenueRequest);
	String requestRSAKey(String accessCode, String orderId);

}
