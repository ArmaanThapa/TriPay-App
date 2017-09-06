package com.tripayapp.util;

import java.util.Random;

public class CommonUtil {

	/**
	 * Random Number Generator for MObile OTP return 6 digit random number
	 * 
	 * @return
	 */
	public static String generateSixDigitNumericString() {
		Random rnd = new Random();
		int n = 100000 + rnd.nextInt(900000);
		return "" + n;
	}

	/**
	 * random n digit number generator
	 * @param n
	 * @return
     */

	public static String generateNDigitNumericString(long n){
		double mul = Math.pow(10,n);
		long result = (long) (Math.random() * mul);
		String number = String.valueOf(result);
		return number;
	}


	public static String generateNineDigitNumericString() {
		return "" + (int) (Math.random() * 1000000000);
	}

	public static void sleep(long timeInMS) {
		try {
			Thread.sleep(timeInMS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
