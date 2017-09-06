package com.ccavenue.api.impl;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccavenue.api.IResponseHandlerApi;
import com.ccavenue.model.CCAvenueResponse;
import com.ccavenue.util.CCAvenueConstants;

public class ResponseHandlerApi implements IResponseHandlerApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public CCAvenueResponse response(String encrypted) {
		CCAvenueResponse response = new CCAvenueResponse();
		String decResp = decrypt(encrypted);
		StringTokenizer tokenizer = new StringTokenizer(decResp, "&");
		Hashtable hs = new Hashtable();
		String pair = null, pname = null, pvalue = null;
		while (tokenizer.hasMoreTokens()) {
			pair = (String) tokenizer.nextToken();
			if (pair != null) {
				StringTokenizer strTok = new StringTokenizer(pair, "=");
				pname = "";
				pvalue = "";
				if (strTok.hasMoreTokens()) {
					pname = (String) strTok.nextToken();
					if (strTok.hasMoreTokens())
						pvalue = (String) strTok.nextToken();
					hs.put(pname, pvalue);
				}
			}
		}
		String orderId = (String) hs.get("order_id");
		response.setOrderId(orderId);
		String trackingId = (String) hs.get("tracking_id");
		response.setTrackingId(trackingId);
		String bankRefNo = (String) hs.get("bank_ref_no");
		response.setBankRefNo(bankRefNo);
		String orderStatus = (String) hs.get("order_status");
		response.setOrderStatus(orderStatus);
		String failureMessage = (String) hs.get("failure_message");
		response.setFailureMessage(failureMessage);
		String paymentMode = (String) hs.get("payment_mode");
		response.setPaymentMode(paymentMode);
		String cardName = (String) hs.get("card_name");
		response.setCardName(cardName);
		String statusCode = (String) hs.get("status_code");
		response.setStatusCode(statusCode);
		String statusMessage = (String) hs.get("status_message");
		response.setStatusMessage(statusMessage);
		String currency = (String) hs.get("currency");
		response.setCurrency(currency);
		String amount = (String) hs.get("amount");
		response.setAmount(amount);
		String billingName = (String) hs.get("billing_name");
		response.setBillingName(billingName);
		String billingAddress = (String) hs.get("billing_address");
		response.setBillingAddress(billingAddress);
		String billingCity = (String) hs.get("billing_city");
		response.setBillingCity(billingCity);
		String billingState = (String) hs.get("billing_state");
		response.setBillingState(billingState);
		String billingZip = (String) hs.get("billing_zip");
		response.setBillingZip(billingZip);
		String billingCountry = (String) hs.get("billing_country");
		response.setBillingCountry(billingCountry);
		String billingTel = (String) hs.get("billing_tel");
		response.setBillingTel(billingTel);
		String billingEmail = (String) hs.get("billing_email");
		response.setBillingEmail(billingEmail);
		String deliveryName = (String) hs.get("delivery_name");
		response.setDeliveryName(deliveryName);
		String deliveryAddress = (String) hs.get("delivery_address");
		response.setDeliveryAddress(deliveryAddress);
		String deliveryCity = (String) hs.get("delivery_city");
		response.setDeliveryCity(deliveryCity);
		String deliveryState = (String) hs.get("delivery_state");
		response.setDeliveryState(deliveryState);
		String deliveryZip = (String) hs.get("delivery_zip");
		response.setDeliveryZip(deliveryZip);
		String deliveryCountry = (String) hs.get("delivery_country");
		response.setDeliveryCountry(deliveryCountry);
		String deliveryTel = (String) hs.get("delivery_tel");
		response.setDeliveryTel(deliveryTel);
		String merchantParam1 = (String) hs.get("merchant_param1");
		response.setMerchantParam1(merchantParam1);
		String merchantParam2 = (String) hs.get("merchant_param2");
		response.setMerchantParam2(merchantParam2);
		String merchantParam3 = (String) hs.get("merchant_param3");
		response.setMerchantParam3(merchantParam3);
		String merchantParam4 = (String) hs.get("merchant_param4");
		response.setMerchantParam4(merchantParam4);
		String merchantParam5 = (String) hs.get("merchant_param5");
		response.setMerchantParam5(merchantParam5);
		String vault = (String) hs.get("vault");
		response.setVault(vault);
		String offerType = (String) hs.get("offer_type");
		response.setOfferType(offerType);
		String offerCode = (String) hs.get("offer_code");
		response.setOfferCode(offerCode);
		String discountValue = (String) hs.get("discount_value");
		response.setDiscountValue(discountValue);

		return response;
	}

	private String decrypt(String hexCipherText) {
		try {
			SecretKeySpec skey = new SecretKeySpec(
					getMD5(CCAvenueConstants.WORKING_KEY), "AES");
			// byte[] buf = new byte[1024];
			byte[] iv = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
					0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// CBC requires an initialization vector
			dcipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);

			return new String(dcipher.doFinal(hexToByte(hexCipherText)),
					"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getMD5(String input) {
		try {
			byte[] bytesOfMessage = input.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(bytesOfMessage);
		} catch (Exception e) {
			return null;
		}
	}

	private byte[] hexToByte(String hexString) {
		int len = hexString.length();
		byte[] ba = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			ba[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
					.digit(hexString.charAt(i + 1), 16));
		}
		return ba;
	}
}
