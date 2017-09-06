package com.ebs.api.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ebs.api.IEBSRequestHandlerApi;
import com.ebs.model.EBSRequest;
import com.ebs.util.EBSConstants;
import com.tripayapp.util.SecurityUtil;

public class EBSRequestHandlerApi implements IEBSRequestHandlerApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public EBSRequest request(EBSRequest ebsRequest) {

		EBSRequest newRequest = new EBSRequest();
		String md5HashData = EBSConstants.SECRET_KEY;
		HashMap<String, String> map = new HashMap<>();

		map.put(EBSConstants.KEY_CHANNEL, EBSConstants.CHANNEL_STANDARD);
		newRequest.setChannel(EBSConstants.CHANNEL_STANDARD);

		map.put(EBSConstants.KEY_ACCOUNTID, EBSConstants.ACCOUNT_ID);
		newRequest.setAccount_id(EBSConstants.ACCOUNT_ID);

		map.put(EBSConstants.KEY_REFERENCENO, ebsRequest.getReference_no());
		newRequest.setReference_no(ebsRequest.getReference_no());

		map.put(EBSConstants.KEY_AMOUNT, ebsRequest.getAmount());
		newRequest.setAmount(ebsRequest.getAmount());

		map.put(EBSConstants.KEY_MODE, EBSConstants.MODE_LIVE);
		newRequest.setMode(EBSConstants.MODE_LIVE);

		map.put(EBSConstants.KEY_CURRENCY, EBSConstants.CURRENCY);
		newRequest.setCurrency(EBSConstants.CURRENCY);

		map.put(EBSConstants.KEY_DESCRIPTION, EBSConstants.DESCRIPTION+" "+ebsRequest.getPhone());
		newRequest.setDescription(EBSConstants.DESCRIPTION+" "+ebsRequest.getPhone());

		map.put(EBSConstants.KEY_RETURN_URL, ebsRequest.getReturn_url());
		newRequest.setReturn_url(ebsRequest.getReturn_url());

		map.put(EBSConstants.KEY_NAME, ebsRequest.getName());
		newRequest.setName(ebsRequest.getName());

		map.put(EBSConstants.KEY_ADDRESS, EBSConstants.SHIP_ADDRESS);
		newRequest.setAddress(EBSConstants.SHIP_ADDRESS);

		map.put(EBSConstants.KEY_CITY, EBSConstants.SHIP_CITY);
		newRequest.setCity(EBSConstants.SHIP_CITY);

		map.put(EBSConstants.KEY_STATE, EBSConstants.SHIP_STATE);
		newRequest.setState(EBSConstants.SHIP_STATE);

		map.put(EBSConstants.KEY_COUNTRY, EBSConstants.SHIP_COUNTRY);
		newRequest.setCountry(EBSConstants.SHIP_COUNTRY);

		map.put(EBSConstants.KEY_POSTAL_CODE, EBSConstants.SHIP_POSTAL_CODE);
		newRequest.setPostal_code(EBSConstants.SHIP_POSTAL_CODE);

		map.put(EBSConstants.KEY_PHONE, ebsRequest.getPhone());
		newRequest.setPhone(ebsRequest.getPhone());

		map.put(EBSConstants.KEY_EMAIL, ebsRequest.getEmail());
		newRequest.setEmail(ebsRequest.getEmail());

		map.put(EBSConstants.KEY_SHIP_NAME, ebsRequest.getName());
		newRequest.setShip_name(ebsRequest.getName());

		map.put(EBSConstants.KEY_SHIP_ADDRESS, ebsRequest.getSessionId());
		newRequest.setShip_address(ebsRequest.getSessionId());

		map.put(EBSConstants.KEY_SHIP_CITY, EBSConstants.SHIP_CITY);
		newRequest.setShip_city(EBSConstants.SHIP_CITY);

		map.put(EBSConstants.KEY_SHIP_STATE, EBSConstants.SHIP_STATE);
		newRequest.setShip_state(EBSConstants.SHIP_STATE);

		map.put(EBSConstants.KEY_SHIP_COUNTRY, EBSConstants.SHIP_COUNTRY);
		newRequest.setShip_country(EBSConstants.SHIP_COUNTRY);

		map.put(EBSConstants.KEY_SHIP_PHONE, ebsRequest.getPhone());
		newRequest.setShip_phone(ebsRequest.getPhone());

		map.put(EBSConstants.KEY_SHIP_POSTAL_CODE, EBSConstants.SHIP_POSTAL_CODE);
		newRequest.setShip_postal_code(EBSConstants.SHIP_POSTAL_CODE);

		Map<String, String> sortedMap = new TreeMap<>(map);
		Iterator<String> i = sortedMap.keySet().iterator();

		while (i.hasNext()) {
			String key = i.next();
			String value = sortedMap.get(key);
			System.err.println("key ::"+key+" value :: "+value);
			md5HashData += "|" + value;
		}

		logger.info("md5 hash data" + md5HashData);
		String algoName = "MD5";
		String encryptedText = "";
		if (algoName.equals(EBSConstants.ALGO_SHA1)) {
			try {
				encryptedText = SecurityUtil.sha1(md5HashData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (algoName.equals(EBSConstants.ALGO_MD5)) {
			try {
				encryptedText = SecurityUtil.md5(md5HashData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (algoName.equals(EBSConstants.ALGO_SHA512)) {
			try {
				encryptedText = SecurityUtil.sha512(md5HashData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		newRequest.setSecure_hash(encryptedText.toUpperCase());
		return newRequest;

	}

}
