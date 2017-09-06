package com.instantpay.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Validation {

	@JsonProperty("ipay_errorcode")
	private String ipayErrorCode;
	@JsonProperty("ipay_errordesc")
	private String ipayErrorDesc;

	public String getIpayErrorCode() {
		return ipayErrorCode;
	}

	public void setIpayErrorCode(String ipayErrorCode) {
		this.ipayErrorCode = ipayErrorCode;
	}

	public String getIpayErrorDesc() {
		return ipayErrorDesc;
	}

	public void setIpayErrorDesc(String ipayErrorDesc) {
		this.ipayErrorDesc = ipayErrorDesc;
	}

	@Override
	public String toString() {
		return "Validation [ipayErrorCode=" + ipayErrorCode + ", ipayErrorDesc=" + ipayErrorDesc + "]";
	}

}
