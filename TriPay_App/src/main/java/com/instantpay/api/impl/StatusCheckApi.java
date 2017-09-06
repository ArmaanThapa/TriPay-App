package com.instantpay.api.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.instantpay.api.IStatusCheckApi;
import com.instantpay.model.StatusCheck;
import com.instantpay.model.request.StatusCheckRequest;
import com.instantpay.model.response.StatusCheckResponse;
import com.instantpay.util.InstantPayConstants;
import com.tripayapp.util.JSONParserUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class StatusCheckApi implements IStatusCheckApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public StatusCheckResponse request(StatusCheckRequest request) {
		StatusCheckResponse response = new StatusCheckResponse();
		StatusCheck statusCheck = new StatusCheck();
		try {
			String stringResponse = "";
			WebResource resource = Client.create().resource(InstantPayConstants.URL_STATUS)
					.queryParam(InstantPayConstants.API_KEY_TOKEN, request.getToken())
					.queryParam(InstantPayConstants.API_KEY_AGENTID, request.getAgentId())
					.queryParam(InstantPayConstants.API_KEY_FORMAT, request.getFormat());
			ClientResponse clientResponse = resource.get(ClientResponse.class);
			if (clientResponse.getStatus() == 200) {
				stringResponse = clientResponse.getEntity(String.class);
			    print("response ::"+stringResponse);
			    JSONObject o = new JSONObject();
			    String ipay_id = JSONParserUtil.getString(o, "ipay_id");
			    String agent_id = JSONParserUtil.getString(o, "agent_id");
			    String opr_id = JSONParserUtil.getString(o, "opr_id");
			    String account_no = JSONParserUtil.getString(o, "account_no");
			    String sp_key = JSONParserUtil.getString(o, "sp_key");
			    String trans_amt = JSONParserUtil.getString(o, "trans_amt");
			    String charged_amt = JSONParserUtil.getString(o, "charged_amt");
			    String opening_bal = JSONParserUtil.getString(o, "opening_bal");
			    String req_dt = JSONParserUtil.getString(o, "req_dt");
			    String status = JSONParserUtil.getString(o, "status");
			    String res_code = JSONParserUtil.getString(o, "res_code");
			    String res_msg = JSONParserUtil.getString(o, "res_msg");
			    response.setSuccess(false);
			    if(res_code != null) {
			    	response.setSuccess(true);
			    	statusCheck.setAccountNo(account_no);
			    	statusCheck.setAgentId(agent_id);
			    	statusCheck.setChargedAmt(charged_amt);
			    	statusCheck.setIpayId(ipay_id);
			    	statusCheck.setOpeningBal(opening_bal);
			    	statusCheck.setOprId(opr_id);
			    	statusCheck.setReqDt(req_dt);
			    	statusCheck.setResCode(res_code);
			    	statusCheck.setResMsg(res_msg);
			    	statusCheck.setSpKey(sp_key);
			    	statusCheck.setStatus(status);
			    	statusCheck.setTransAmt(trans_amt);
			    }
			    response.setStatusCheck(statusCheck);
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
