package com.tripayapp.api;

import org.springframework.web.servlet.ModelAndView;

import com.ccavenue.model.CCAvenueRequest;

public interface ICCAvenueApi {

	ModelAndView requestHandler(CCAvenueRequest request, ModelAndView modelMap, String username);
	ModelAndView responseHandler(String encryptedResponse, ModelAndView modelMap, String username);
	String responseHandlerMobile(String encryptedResponse);
	String requestRSAKey(String accessCode, String orderId);
}
