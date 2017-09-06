package com.instantpay.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.instantpay.model.StatusCheck;
import com.instantpay.model.Validation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusCheckResponse {

	/**
	 * 
	 * response :: { "ipay_id": "1160226111813NQJBD", "agent_id": "7948Y8730",
	 * "opr_id": "2435901786", "account_no": "9449147913", "sp_key": "BGP",
	 * "trans_amt": "10.00", "charged_amt": "9.71", "opening_bal": "21839.65",
	 * "req_dt": "2016-02-26 11:18:13", "status": "SUCCESS", "res_code": "TXN",
	 * "res_msg": "Transaction Successful" }
	 * 
	 */
	private boolean success;
	private Validation validation;
	private StatusCheck statusCheck;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
	}

	public StatusCheck getStatusCheck() {
		return statusCheck;
	}

	public void setStatusCheck(StatusCheck statusCheck) {
		this.statusCheck = statusCheck;
	}

}
