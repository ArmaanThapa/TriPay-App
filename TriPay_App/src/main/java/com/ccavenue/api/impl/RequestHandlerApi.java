package com.ccavenue.api.impl;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccavenue.api.IRequestHandlerApi;
import com.ccavenue.model.CCAvenueRequest;
import com.ccavenue.security.AesCryptUtil;
import com.ccavenue.util.CCAvenueConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RequestHandlerApi implements IRequestHandlerApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public CCAvenueRequest request(HttpServletRequest request) {
		CCAvenueRequest ccavenueRequest = new CCAvenueRequest();
		Enumeration enumeration = request.getParameterNames();
		Hashtable hs = new Hashtable();
		String ccaRequest = "", pname = "", pvalue = "";
		while (enumeration.hasMoreElements()) {
			pname = "" + enumeration.nextElement();
			pvalue = request.getParameter(pname);
			hs.put(pname, pvalue);
			// ccaRequest = ccaRequest + pname + "=" + pvalue + "&";
		}

		// order_id=793732332&
		// amount=1&
		// tid=&
		// language=EN&
		// promo_code=&
		// currency=INR&
		// customer_identifier=&
		// merchant_id=47281&
		// cancel_url=http://localhost:8080/PayQwik/User/LoadMoney/Cancel&
		// redirect_url=http://localhost:8080/PayQwik/User/LoadMoney/Redirect&
		// String orderId = (String) hs.get("order_id");
		ccavenueRequest.setOrderId("" + System.currentTimeMillis());
		ccaRequest = ccaRequest + "order_id=" + ccavenueRequest.getOrderId() + "&";

		String amount = (String) hs.get("amount");
		ccavenueRequest.setAmount(amount);
		ccaRequest = ccaRequest + "amount=" + ccavenueRequest.getAmount() + "&";

		String tid = (String) hs.get("tid");
		ccavenueRequest.setTid(tid);
		ccaRequest = ccaRequest + "tid=" + ccavenueRequest.getTid() + "&";

		String language = (String) hs.get("language");
		ccavenueRequest.setLanguage(language);
		ccaRequest = ccaRequest + "language=" + ccavenueRequest.getLanguage() + "&";

		String promoCode = (String) hs.get("promo_code");
		ccavenueRequest.setPromoCode(promoCode);
		ccaRequest = ccaRequest + "promo_code=" + ccavenueRequest.getPromoCode() + "&";

		String currency = (String) hs.get("currency");
		ccavenueRequest.setCurrency(currency);
		ccaRequest = ccaRequest + "currency=" + ccavenueRequest.getCurrency() + "&";

		String customerIdentifier = (String) hs.get("customer_identifier");
		ccavenueRequest.setCustomerIdentifier(customerIdentifier);
		ccaRequest = ccaRequest + "customer_identifier=" + ccavenueRequest.getCustomerIdentifier() + "&";

		String merchantId = (String) hs.get("merchant_id");
		ccavenueRequest.setMerchantId(merchantId);
		ccaRequest = ccaRequest + "merchant_id=" + ccavenueRequest.getMerchantId() + "&";

		String cancelURL = (String) hs.get("cancel_url");
		ccavenueRequest.setCancelURL(cancelURL);
		ccaRequest = ccaRequest + "cancel_url=" + ccavenueRequest.getCancelURL() + "&";

		String redirectURL = (String) hs.get("redirect_url");
		ccavenueRequest.setRedirectURL(redirectURL);
		ccaRequest = ccaRequest + "redirect_url=" + ccavenueRequest.getRedirectURL() + "&";

		// delivery_name=Sam&
		// delivery_address=Vile Parle&
		// delivery_tel=0221234321&
		// delivery_city=Mumbai&
		// delivery_state=Maharashtra&
		// delivery_zip=400038&
		// delivery_country=India&
		String deliveryName = (String) hs.get("delivery_name");
		ccavenueRequest.setDeliveryName(deliveryName);
		ccaRequest = ccaRequest + "delivery_name=" + ccavenueRequest.getDeliveryName() + "&";

		String deliveryAddress = (String) hs.get("delivery_address");
		ccavenueRequest.setDeliveryAddress(deliveryAddress);
		ccaRequest = ccaRequest + "delivery_address=" + ccavenueRequest.getDeliveryAddress() + "&";

		String deliveryTel = (String) hs.get("delivery_tel");
		ccavenueRequest.setDeliveryTel(deliveryTel);
		ccaRequest = ccaRequest + "delivery_tel=" + ccavenueRequest.getDeliveryTel() + "&";

		String deliveryCity = (String) hs.get("delivery_city");
		ccavenueRequest.setDeliveryCity(deliveryCity);
		ccaRequest = ccaRequest + "delivery_city=" + ccavenueRequest.getDeliveryCity() + "&";

		String deliveryState = (String) hs.get("delivery_state");
		ccavenueRequest.setDeliveryState(deliveryState);
		ccaRequest = ccaRequest + "delivery_state=" + ccavenueRequest.getDeliveryState() + "&";

		String deliveryZip = (String) hs.get("delivery_zip");
		ccavenueRequest.setDeliveryZip(deliveryZip);
		ccaRequest = ccaRequest + "delivery_zip=" + ccavenueRequest.getDeliveryZip() + "&";

		String deliveryCountry = (String) hs.get("delivery_country");
		ccavenueRequest.setDeliveryCountry(deliveryCountry);
		ccaRequest = ccaRequest + "delivery_country=" + ccavenueRequest.getDeliveryCountry() + "&";

		// billing_name=Peter&
		// billing_address=Santacruz&
		// billing_city=Mumbai&
		// billing_state=MH&
		// billing_zip=400054&
		// billing_country=India&
		// billing_tel=0229874789&
		// billing_email=testing@domain.com&
		String billingName = (String) hs.get("billing_name");
		ccavenueRequest.setBillingName(billingName);
		ccaRequest = ccaRequest + "billing_name=" + ccavenueRequest.getBillingName() + "&";

		String billingAddress = (String) hs.get("billing_address");
		ccavenueRequest.setBillingAddress(billingAddress);
		ccaRequest = ccaRequest + "billing_address=" + ccavenueRequest.getBillingAddress() + "&";

		String billingCity = (String) hs.get("billing_city");
		ccavenueRequest.setBillingCity(billingCity);
		ccaRequest = ccaRequest + "billing_city=" + ccavenueRequest.getBillingCity() + "&";

		String billingState = (String) hs.get("billing_state");
		ccavenueRequest.setBillingState(billingState);
		ccaRequest = ccaRequest + "billing_state=" + ccavenueRequest.getBillingState() + "&";

		String billingZip = (String) hs.get("billing_zip");
		ccavenueRequest.setBillingZip(billingZip);
		ccaRequest = ccaRequest + "billing_zip=" + ccavenueRequest.getBillingZip() + "&";

		String billingCountry = (String) hs.get("billing_country");
		ccavenueRequest.setBillingCountry(billingCountry);
		ccaRequest = ccaRequest + "billing_country=" + ccavenueRequest.getBillingCountry() + "&";

		String billingTel = (String) hs.get("billing_tel");
		ccavenueRequest.setBillingTel(billingTel);
		ccaRequest = ccaRequest + "billing_tel=" + ccavenueRequest.getBillingTel() + "&";

		String billingEmail = (String) hs.get("billing_email");
		ccavenueRequest.setBillingEmail(billingEmail);
		ccaRequest = ccaRequest + "billing_email=" + ccavenueRequest.getBillingEmail() + "&";

		String merchantParam1 = (String) hs.get("merchant_param1");
		ccavenueRequest.setMerchantParam1(merchantParam1);
		ccaRequest = ccaRequest + "merchant_param1=" + ccavenueRequest.getMerchantParam1() + "&";

		String merchantParam2 = (String) hs.get("merchant_param2");
		ccavenueRequest.setMerchantParam2(merchantParam2);
		ccaRequest = ccaRequest + "merchant_param2=" + ccavenueRequest.getMerchantParam2() + "&";

		String merchantParam3 = (String) hs.get("merchant_param3");
		ccavenueRequest.setMerchantParam3(merchantParam3);
		ccaRequest = ccaRequest + "merchant_param3=" + ccavenueRequest.getMerchantParam3() + "&";

		String merchantParam4 = (String) hs.get("merchant_param4");
		ccavenueRequest.setMerchantParam4(merchantParam4);
		ccaRequest = ccaRequest + "merchant_param4=" + ccavenueRequest.getMerchantParam4() + "&";

		String merchantParam5 = (String) hs.get("merchant_param5");
		ccavenueRequest.setMerchantParam5(merchantParam5);
		ccaRequest = ccaRequest + "merchant_param5=" + ccavenueRequest.getMerchantParam5() + "&";

		AesCryptUtil aesUtil = new AesCryptUtil(CCAvenueConstants.WORKING_KEY);

		String encRequest = aesUtil.encrypt(ccaRequest);
		ccavenueRequest.setEncryptedResponse(encRequest);

		return ccavenueRequest;
	}

	@Override
	public CCAvenueRequest request(CCAvenueRequest ccAvenueRequest) {
		String ccaRequest = "";
		ccAvenueRequest.setOrderId("" + System.currentTimeMillis());
		ccaRequest = ccaRequest + "order_id=" + ccAvenueRequest.getOrderId() + "&";
		ccaRequest = ccaRequest + "amount=" + ccAvenueRequest.getAmount() + "&";
		ccaRequest = ccaRequest + "tid=" + ccAvenueRequest.getTid() + "&";
		ccaRequest = ccaRequest + "language=" + ccAvenueRequest.getLanguage() + "&";
		ccaRequest = ccaRequest + "promo_code=" + ccAvenueRequest.getPromoCode() + "&";
		ccaRequest = ccaRequest + "currency=" + ccAvenueRequest.getCurrency() + "&";
		ccaRequest = ccaRequest + "customer_identifier=" + ccAvenueRequest.getCustomerIdentifier() + "&";
		ccaRequest = ccaRequest + "merchant_id=" + ccAvenueRequest.getMerchantId() + "&";
		ccaRequest = ccaRequest + "cancel_url=" + ccAvenueRequest.getCancelURL() + "&";
		ccaRequest = ccaRequest + "redirect_url=" + ccAvenueRequest.getRedirectURL() + "&";
		ccaRequest = ccaRequest + "delivery_name=" + ccAvenueRequest.getDeliveryName() + "&";
		ccaRequest = ccaRequest + "delivery_address=" + ccAvenueRequest.getDeliveryAddress() + "&";
		ccaRequest = ccaRequest + "delivery_tel=" + ccAvenueRequest.getDeliveryTel() + "&";
		ccaRequest = ccaRequest + "delivery_city=" + ccAvenueRequest.getDeliveryCity() + "&";
		ccaRequest = ccaRequest + "delivery_state=" + ccAvenueRequest.getDeliveryState() + "&";
		ccaRequest = ccaRequest + "delivery_zip=" + ccAvenueRequest.getDeliveryZip() + "&";
		ccaRequest = ccaRequest + "delivery_country=" + ccAvenueRequest.getDeliveryCountry() + "&";
		ccaRequest = ccaRequest + "billing_name=" + ccAvenueRequest.getBillingName() + "&";
		ccaRequest = ccaRequest + "billing_address=" + ccAvenueRequest.getBillingAddress() + "&";
		ccaRequest = ccaRequest + "billing_city=" + ccAvenueRequest.getBillingCity() + "&";
		ccaRequest = ccaRequest + "billing_state=" + ccAvenueRequest.getBillingState() + "&";
		ccaRequest = ccaRequest + "billing_zip=" + ccAvenueRequest.getBillingZip() + "&";
		ccaRequest = ccaRequest + "billing_country=" + ccAvenueRequest.getBillingCountry() + "&";
		ccaRequest = ccaRequest + "billing_tel=" + ccAvenueRequest.getBillingTel() + "&";
		ccaRequest = ccaRequest + "billing_email=" + ccAvenueRequest.getBillingEmail() + "&";
		ccaRequest = ccaRequest + "merchant_param1=" + ccAvenueRequest.getMerchantParam1() + "&";
		ccaRequest = ccaRequest + "merchant_param2=" + ccAvenueRequest.getMerchantParam2() + "&";
		ccaRequest = ccaRequest + "merchant_param3=" + ccAvenueRequest.getMerchantParam3() + "&";
		ccaRequest = ccaRequest + "merchant_param4=" + ccAvenueRequest.getMerchantParam4() + "&";
		ccaRequest = ccaRequest + "merchant_param5=" + ccAvenueRequest.getMerchantParam5() + "&";

		AesCryptUtil aesUtil = new AesCryptUtil(CCAvenueConstants.WORKING_KEY);
		String encRequest = aesUtil.encrypt(ccaRequest);
		ccAvenueRequest.setEncryptedResponse(encRequest);
		return ccAvenueRequest;
	}

	@Override
	public String requestRSAKey(String accessCode, String orderId) {
		String stringResponse = "";
		try {
			MultivaluedMapImpl formData = new MultivaluedMapImpl();
			formData.add("access_code", accessCode);
			formData.add("order_id", orderId);
			WebResource resource = Client.create().resource(CCAvenueConstants.URL_RSA);
			ClientResponse clientResponse = resource.post(ClientResponse.class,formData);
			if (clientResponse.getStatus() == 200) {
				stringResponse = clientResponse.getEntity(String.class);
			} else {
				stringResponse = clientResponse.getEntity(String.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringResponse;
	}

}
