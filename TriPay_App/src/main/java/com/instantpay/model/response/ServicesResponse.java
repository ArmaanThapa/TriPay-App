package com.instantpay.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.instantpay.model.Service;
import com.instantpay.model.Validation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicesResponse {

	/**
	 * 
	 * [{ "service_type": "PREPAID", "service_provider": "Aircel",
	 * "service_desc": "pass Mobile Number in 'account'", "provider_key": "ACP",
	 * "margin": "4.10" }, { "service_type": "PREPAID", "service_provider":
	 * "Airtel", "service_desc":
	 * "pass Mobile Number in 'account' and Outlet ID in 'optional5'. Not available in JK"
	 * , "provider_key": "ATP", "margin": "1.20" }, { "service_type": "PREPAID",
	 * "service_provider": "BSNL - Talktime", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "BGP", "margin":
	 * "3.40" }, { "service_type": "PREPAID", "service_provider": "Idea",
	 * "service_desc":
	 * "pass Mobile Number in 'account'. Not available in JK & NE",
	 * "provider_key": "IDP", "margin": "1.60" }, { "service_type": "PREPAID",
	 * "service_provider": "MTNL - Special Tariff", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "MSP", "margin":
	 * "3.80" }, { "service_type": "PREPAID", "service_provider":
	 * "MTNL - Talktime", "service_desc": "pass Mobile Number in 'account'",
	 * "provider_key": "MMP", "margin": "3.80" }, { "service_type": "PREPAID",
	 * "service_provider": "MTS", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "MTP", "margin":
	 * "3.60" }, { "service_type": "PREPAID", "service_provider": "Reliance",
	 * "service_desc": "pass Mobile Number in 'account'", "provider_key": "RGP",
	 * "margin": "3.70" }, { "service_type": "PREPAID", "service_provider":
	 * "Tata Docomo CDMA", "service_desc": "pass Mobile Number in 'account'",
	 * "provider_key": "TCP", "margin": "2.20" }, { "service_type": "PREPAID",
	 * "service_provider": "Tata Docomo GSM - Talktime", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "TGP", "margin":
	 * "2.20" }, { "service_type": "PREPAID", "service_provider":
	 * "Telenor - Talktime", "service_desc": "pass Mobile Number in 'account'",
	 * "provider_key": "UGP", "margin": "3.10" }, { "service_type": "PREPAID",
	 * "service_provider": "Videocon - Talktime", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "VGP", "margin":
	 * "4.00" }, { "service_type": "PREPAID", "service_provider": "Vodafone",
	 * "service_desc": "pass Mobile Number in 'account'", "provider_key": "VFP",
	 * "margin": "1.60" }, { "service_type": "PREPAID", "service_provider":
	 * "BSNL - Special Tariff", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "BVP", "margin":
	 * "3.40" }, { "service_type": "PREPAID", "service_provider":
	 * "Tata Docomo GSM - Special Tariff", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "TSP", "margin":
	 * "2.20" }, { "service_type": "PREPAID", "service_provider":
	 * "Telenor - Special Tariff", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "USP", "margin":
	 * "3.10" }, { "service_type": "PREPAID", "service_provider":
	 * "Videocon - Special Tariff", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "VSP", "margin":
	 * "4.00" }, { "service_type": "PREPAID", "service_provider":
	 * "T24 Mobile - Talktime", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "TMP", "margin":
	 * "2.20" }, { "service_type": "PREPAID", "service_provider":
	 * "T24 Mobile - Special Tariff", "service_desc":
	 * "pass Mobile Number in 'account'", "provider_key": "TVP", "margin":
	 * "2.20" }]
	 *
	 * 
	 * when token is invalid or from incorrect IP { "ipay_errorcode":"IAT",
	 * "ipay_errordesc":"Invalid Access Token - Correct IP - 66.207.206.54" }
	 *
	 */

	private boolean success;
	private Validation validation;
	private List<Service> services;

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

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

}
