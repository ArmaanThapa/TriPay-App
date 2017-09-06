package com.instantpay.api.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.instantpay.api.ITransactionApi;
import com.instantpay.model.Transaction;
import com.instantpay.model.Validation;
import com.instantpay.model.request.TransactionRequest;
import com.instantpay.model.response.TransactionResponse;
import com.instantpay.util.InstantPayConstants;
import com.tripayapp.entity.PQService;
import com.tripayapp.util.JSONParserUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;

public class TransactionApi implements ITransactionApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public TransactionResponse request(TransactionRequest request, PQService service) {
		TransactionResponse response = new TransactionResponse();
		try {
			
			String stringResponse = "";
			Client c = Client.create();
			c.addFilter(new LoggingFilter(System.out));
			
			WebResource resource = c.resource(InstantPayConstants.URL_TRANSACTION)
					.queryParam(InstantPayConstants.API_KEY_TOKEN,
							(request.getToken() == null) ? "" : request.getToken())
					.queryParam(InstantPayConstants.API_KEY_SPKEY,
							(request.getSpKey() == null) ? "" : service.getOperatorCode())
					.queryParam(InstantPayConstants.API_KEY_AGENTID,
							(request.getAgentId() == null) ? "" : request.getAgentId())
					.queryParam(InstantPayConstants.API_KEY_ACCOUNT,
							(request.getAccount() == null) ? "" : request.getAccount())
					.queryParam(InstantPayConstants.API_KEY_AMOUNT,
							(request.getAmount() == null) ? "" : request.getAmount())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL1,
							(request.getOptional1() == null) ? "" : request.getOptional1())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL2,
							(request.getOptional2() == null) ? "" : request.getOptional2())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL3,
							(request.getOptional3() == null) ? "" : request.getOptional3())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL4,
							(request.getOptional4() == null) ? "" : request.getOptional4())
					.queryParam(InstantPayConstants.API_KEY_OPTIONAL5,
							(request.getOptional5() == null) ? "" : request.getOptional5())
					.queryParam(InstantPayConstants.API_KEY_FORMAT,
							(request.getFormat() == null) ? "" : request.getFormat());
			
			ClientResponse clientResponse = resource.get(ClientResponse.class);
			
			if (clientResponse.getStatus() == 200) {
				stringResponse = clientResponse.getEntity(String.class);
				logger.info("RESPONSE :: " + stringResponse);
				JSONObject o = new JSONObject(stringResponse);
				if (JSONParserUtil.checkKey(o, "res_code")) {
					Transaction transactionResponse = new Transaction();
					String res_code = JSONParserUtil.getString(o, "res_code");
					transactionResponse.setResCode(res_code);
					String res_msg = JSONParserUtil.getString(o, "res_msg");
					transactionResponse.setResMsg(res_msg);
					String ipay_id = JSONParserUtil.getString(o, "ipay_id");
					transactionResponse.setIpayId(ipay_id);
					String agent_id = JSONParserUtil.getString(o, "agent_id");
					transactionResponse.setAgentId(agent_id);
					String opr_id = JSONParserUtil.getString(o, "opr_id");
					transactionResponse.setOprId(opr_id);
					String account_no = JSONParserUtil.getString(o, "account_no");
					transactionResponse.setAccountNo(account_no);
					String sp_key = JSONParserUtil.getString(o, "sp_key");
					transactionResponse.setSpKey(sp_key);
					String trans_amt = JSONParserUtil.getString(o, "trans_amt");
					transactionResponse.setTransAmt(trans_amt);
					String charged_amt = JSONParserUtil.getString(o, "charged_amt");
					transactionResponse.setChargedAmt(charged_amt);
					String opening_bal = JSONParserUtil.getString(o, "opening_bal");
					transactionResponse.setOpeningBal(opening_bal);
					String datetime = JSONParserUtil.getString(o, "datetime");
					transactionResponse.setDateTime(datetime);
					String status = JSONParserUtil.getString(o, "status");
					transactionResponse.setStatus(status);
					response.setTransaction(transactionResponse);
					response.setValidation(null);
					response.setSuccess(true);
				}
				if (JSONParserUtil.checkKey(o, "ipay_errorcode")) {
					Validation validationResponse = new Validation();
					String ipay_errorcode = JSONParserUtil.getString(o, "ipay_errorcode");
					validationResponse.setIpayErrorCode(ipay_errorcode);
					String ipay_errordesc = (ipay_errorcode.equalsIgnoreCase("IAB"))?"Recharge services are temporarily down":JSONParserUtil.getString(o, "ipay_errordesc");

					validationResponse.setIpayErrorDesc(ipay_errordesc);
					response.setValidation(validationResponse);
					response.setTransaction(null);
					response.setSuccess(false);
				}
			}else {
				response.setSuccess(false);
				Validation validation = new Validation();
				validation.setIpayErrorCode("ERROR");
				validation.setIpayErrorDesc("Service down");
				response.setValidation(validation);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			Validation validation = new Validation();
			validation.setIpayErrorCode("ERROR");
			validation.setIpayErrorDesc("Service down");
			response.setValidation(validation);
		}
		return response;
	}

}
