package com.instantpay.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {

	/*
	 * 
	 * {"service_type":"PREPAID","service_provider":"Aircel","service_desc":"pass Mobile Number in 'account'","provider_key":"ACP","margin":"4.10"}
	 */
	@JsonProperty("service_type")
	private String serviceType;
	@JsonProperty("service_provider")
	private String serviceProvider;
	@JsonProperty("service_desc")
	private String serviceDesc;
	@JsonProperty("provider_key")
	private String providerKey;
	@JsonProperty("margin")
	private String margin;

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getProviderKey() {
		return providerKey;
	}

	public void setProviderKey(String providerKey) {
		this.providerKey = providerKey;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

}
