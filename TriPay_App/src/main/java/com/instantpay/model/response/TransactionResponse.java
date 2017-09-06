package com.instantpay.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.instantpay.model.Transaction;
import com.instantpay.model.Validation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse {

	/**
	 * Transaction Successful response :: { "ipay_id": "1160226111813NQJBD",
	 * "agent_id": "7948Y8730", "opr_id": "2435901786", "account_no":
	 * "9449147913", "sp_key": "BGP", "trans_amt": "10.00", "charged_amt":
	 * "9.71", "opening_bal": "21839.65", "datetime": "2016-02-26 11:18:13",
	 * "status": "SUCCESS", "res_code": "TXN", "res_msg":
	 * "Transaction Successful" } When Amount is less than 10 response :: {
	 * "ipay_errorcode": "IRA", "ipay_errordesc": "Invalid Refill Amount" } for
	 * Airtel need Outlet ID in optional 5 response :: { "ipay_errorcode":
	 * "OUI", "ipay_errordesc": "Outlet Unauthorized or Inactive" }
	 * 
	 * 
	 */
	private boolean success;
	private Transaction transaction;
	private Validation validation;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
	}

	@Override
	public String toString() {
		return "TransactionResponse [success = " + success + ", transaction = " + ((transaction==null)?null:(transaction.toString())) + ", validation = "
				+ ((validation==null)?null:(validation.toString())) + "]";
	}

}
