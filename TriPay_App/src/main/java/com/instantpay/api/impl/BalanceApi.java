package com.instantpay.api.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.instantpay.api.IBalanceApi;
import com.instantpay.model.Balance;
import com.instantpay.model.request.BalanceRequest;
import com.instantpay.model.response.BalanceResponse;
import com.instantpay.util.InstantPayConstants;
import com.tripayapp.util.JSONParserUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class BalanceApi implements IBalanceApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public BalanceResponse request(BalanceRequest request) {
		BalanceResponse response = new BalanceResponse();
		try {
			String stringResponse = "";
			WebResource resource = Client.create().resource(InstantPayConstants.URL_BALANCE)
					.queryParam(InstantPayConstants.API_KEY_TOKEN, request.getToken())
					.queryParam(InstantPayConstants.API_KEY_FORMAT, request.getFormat());
			ClientResponse clientResponse = resource.get(ClientResponse.class);
			if (clientResponse.getStatus() == 200) {
				Balance balanceResponse = new Balance();
				stringResponse = clientResponse.getEntity(String.class);
				JSONObject o = new JSONObject(stringResponse);
				String wallet = JSONParserUtil.getString(o, "Wallet");
				if (wallet != null) {
					response.setSuccess(true);
					balanceResponse.setWallet(wallet);
				}
				response.setBalance(balanceResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
