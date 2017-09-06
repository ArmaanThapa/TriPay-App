package com.instantpay.api.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.instantpay.api.IValidationApi;
import com.instantpay.model.Validation;
import com.instantpay.model.request.ValidationRequest;
import com.instantpay.model.response.ValidationResponse;
import com.instantpay.util.InstantPayConstants;
import com.tripayapp.util.JSONParserUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ValidationApi implements IValidationApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public ValidationResponse request(ValidationRequest request) {
		ValidationResponse response = new ValidationResponse();
		Validation validationResponse = new Validation();
		try {
			String stringResponse = "";
			WebResource resource = Client.create().resource(InstantPayConstants.URL_VALIDATION)
					.queryParam(InstantPayConstants.API_KEY_TOKEN, request.getToken())
					.queryParam(InstantPayConstants.API_KEY_MODE, request.getMode())
					.queryParam(InstantPayConstants.API_KEY_SPKEY, request.getSpKey())
					.queryParam(InstantPayConstants.API_KEY_AGENTID, request.getAgentId())
					.queryParam(InstantPayConstants.API_KEY_ACCOUNT, request.getAccount())
					.queryParam(InstantPayConstants.API_KEY_AMOUNT, request.getAmount())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL1, request.getOptional1())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL2, request.getOptional2())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL3, request.getOptional3())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL4, request.getOptional4())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL5, request.getOptional5())
					.queryParam(InstantPayConstants.API_KEY_FORMAT, request.getFormat());
			ClientResponse clientResponse = resource.get(ClientResponse.class);
			if (clientResponse.getStatus() == 200) {
				stringResponse = clientResponse.getEntity(String.class);
				print("response ::"+stringResponse);
				JSONObject o = new JSONObject();
				String ipay_errorcode = JSONParserUtil.getString(o, "ipay_errorcode");
				response.setSuccess(false);
				if(ipay_errorcode != null) {
					String ipay_errordesc = JSONParserUtil.getString(o, "​ipay_errordesc");
					validationResponse.setIpayErrorCode(ipay_errorcode);
					validationResponse.setIpayErrorDesc(ipay_errordesc);
					if(ipay_errorcode.equalsIgnoreCase("TXN")) {
						response.setSuccess(true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	private void print(String message) {
		System.out.println(message);
	}

}
