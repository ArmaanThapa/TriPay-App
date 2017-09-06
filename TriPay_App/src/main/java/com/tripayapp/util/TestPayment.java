package com.tripayapp.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class TestPayment {

	public static void main(String[] args) {
		String secret="F7293EED91CDED600B66144FFD079972";
		String amount= "10";
		long id=1978;
		String transactionId = "1242";
		String str = secret.trim()+"|"+amount.toString().trim()+"|"+id+"|"+transactionId.trim();
		System.out.println(str);
		try {
			String hash = md5(str);
			System.err.println(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String md5(String str) throws Exception {

		MessageDigest m = MessageDigest.getInstance("MD5");

		byte[] data = str.getBytes();

		m.update(data, 0, data.length);

		BigInteger i = new BigInteger(1, m.digest());

		String hash = String.format("%1$032X", i);

		return hash;
	}

}
