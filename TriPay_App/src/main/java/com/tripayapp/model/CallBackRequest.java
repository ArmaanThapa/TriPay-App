package com.tripayapp.model;

public class CallBackRequest {

	private String ipay_id;
	private String agent_id;
	private String opr_id;
	private String status;
	private String res_code;
	private String res_msg;

	public String getIpay_id() {
		return ipay_id;
	}

	public void setIpay_id(String ipay_id) {
		this.ipay_id = ipay_id;
	}

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public String getOpr_id() {
		return opr_id;
	}

	public void setOpr_id(String opr_id) {
		this.opr_id = opr_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRes_code() {
		return res_code;
	}

	public void setRes_code(String res_code) {
		this.res_code = res_code;
	}

	public String getRes_msg() {
		return res_msg;
	}

	public void setRes_msg(String res_msg) {
		this.res_msg = res_msg;
	}

}
