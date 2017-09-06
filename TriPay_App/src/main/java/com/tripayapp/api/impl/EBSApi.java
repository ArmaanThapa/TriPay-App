package com.tripayapp.api.impl;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.ebs.api.IEBSRequestHandlerApi;
import com.ebs.model.EBSRedirectResponse;
import com.ebs.model.EBSRequest;
import com.tripayapp.api.IEBSApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;

public class EBSApi implements IEBSApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final IEBSRequestHandlerApi ebsRequestHandlerApi;
	private final ITransactionApi transactionApi;

	public EBSApi(IEBSRequestHandlerApi ebsRequestHandlerApi, ITransactionApi transactionApi) {
		this.ebsRequestHandlerApi = ebsRequestHandlerApi;
		this.transactionApi = transactionApi;
	}

	@Override
	public EBSRequest requestHandler(EBSRequest request, String username, PQService service) {
		String transactionRefNo = "" + System.currentTimeMillis();
		double amount = Double.parseDouble(request.getAmount());
		request.setReference_no(transactionRefNo);
		request = ebsRequestHandlerApi.request(request);
		transactionApi.initiateLoadMoney(amount, request.getDescription(), service, transactionRefNo, username, request.toJSON().toString());
		return request;
	}

	@Override
	public ModelAndView responseHandlerView(HttpServletRequest request, String username) {
		ModelAndView mv = new ModelAndView();
		boolean isSuccess = false;
		String transactionRefNo = null;
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			mv.addObject(paramName, paramValue);
			if (paramName.equalsIgnoreCase("ResponseCode") && paramValue.equalsIgnoreCase("0")) {
				isSuccess = true;
			}
			if (paramName.equalsIgnoreCase("MerchantRefNo")) {
				transactionRefNo = paramValue;
			}
		}
		if (isSuccess) {
			transactionApi.successLoadMoney(transactionRefNo);
		} else {
			transactionApi.failedLoadMoney(transactionRefNo);
		}
		return mv;
	}

	@Override
	public ResponseDTO responseHandler(EBSRedirectResponse redirectResponse, String username) {
		ResponseDTO result = new ResponseDTO();
		boolean isSuccess = false;
		String responseCode = redirectResponse.getResponseCode();
		String transactionRefNo = redirectResponse.getMerchantRefNo();

		if (responseCode.equalsIgnoreCase("0")) {
				isSuccess = true;
		}
		if (isSuccess) {
			result.setStatus(ResponseStatus.SUCCESS);
			result.setMessage("Success");
			result.setDetails(redirectResponse);
			transactionApi.successLoadMoney(transactionRefNo);
		} else {
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("Failure");
			result.setDetails(redirectResponse);
			transactionApi.failedLoadMoney(transactionRefNo);
		}
		return result;
	}

	
}
