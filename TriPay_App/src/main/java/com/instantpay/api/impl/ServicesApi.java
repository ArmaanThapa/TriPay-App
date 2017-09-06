package com.instantpay.api.impl;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.instantpay.api.IServicesApi;
import com.instantpay.model.Service;
import com.instantpay.model.Validation;
import com.instantpay.model.request.ServicesRequest;
import com.instantpay.model.response.ServicesResponse;
import com.instantpay.util.InstantPayConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ServicesApi implements IServicesApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public ServicesResponse request(ServicesRequest request) {
		ServicesResponse response = new ServicesResponse();
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		try {
			String stringResponse = "";
			WebResource resource = Client.create().resource(InstantPayConstants.URL_SERVICE)
					.queryParam(InstantPayConstants.API_KEY_TOKEN, request.getToken())
					.queryParam(InstantPayConstants.API_KEY_TYPE, request.getType())
					.queryParam(InstantPayConstants.API_KEY_FORMAT, request.getFormat());
			ClientResponse clientResponse = resource.get(ClientResponse.class);
			if (clientResponse.getStatus() == 200) {
				stringResponse = clientResponse.getEntity(String.class);
				print("String response :: " + stringResponse);
				Object json = new JSONTokener(stringResponse).nextValue();
				if (json instanceof JSONObject) {
					Validation v = mapper.readValue(stringResponse, Validation.class);
					print("CODE :: " + v.getIpayErrorCode());
					print("DESC :: " + v.getIpayErrorDesc());
					response.setValidation(mapper.readValue(stringResponse, Validation.class));
					response.setSuccess(false);
				} else if (json instanceof JSONArray) {
					response.setServices((List<Service>) mapper.readValue(stringResponse,
							typeFactory.constructCollectionType(List.class, Service.class)));
					response.setSuccess(true);
				}
			} else {
				response.setSuccess(false);
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
