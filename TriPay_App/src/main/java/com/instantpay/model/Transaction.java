package com.instantpay.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

	@JsonProperty("ipay_id")
	private String ipayId;
	@JsonProperty("agent_id")
	private String agentId;
	@JsonProperty("opr_id")
	private String oprId;
	@JsonProperty("account_no")
	private String accountNo;
	@JsonProperty("sp_key")
	private String spKey;
	@JsonProperty("trans_amt")
	private String transAmt;
	@JsonProperty("charged_amt")
	private String chargedAmt;
	@JsonProperty("opening_bal")
	private String openingBal;

	@JsonProperty("datetime")
	private String dateTime;

	@JsonProperty("status")
	private String status;
	@JsonProperty("res_code")
	private String resCode;
	@JsonProperty("res_msg")
	private String resMsg;

	public String getIpayId() {
		return ipayId;
	}

	public void setIpayId(String ipayId) {
		this.ipayId = ipayId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getOprId() {
		return oprId;
	}

	public void setOprId(String oprId) {
		this.oprId = oprId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getSpKey() {
		return spKey;
	}

	public void setSpKey(String spKey) {
		this.spKey = spKey;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getChargedAmt() {
		return chargedAmt;
	}

	public void setChargedAmt(String chargedAmt) {
		this.chargedAmt = chargedAmt;
	}

	public String getOpeningBal() {
		return openingBal;
	}

	public void setOpeningBal(String openingBal) {
		this.openingBal = openingBal;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	@Override
	public String toString() {
		return "Transaction [ipayId=" + ipayId + ", agentId=" + agentId + ", oprId=" + oprId + ", accountNo="
				+ accountNo + ", spKey=" + spKey + ", transAmt=" + transAmt + ", chargedAmt=" + chargedAmt
				+ ", openingBal=" + openingBal + ", dateTime=" + dateTime + ", status=" + status + ", resCode="
				+ resCode + ", resMsg=" + resMsg + "]";
	}

}
