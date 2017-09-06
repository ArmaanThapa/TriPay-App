package com.tripayapp.util;

import org.apache.commons.codec.binary.Base64;

public class ImageProcessorUtil {

	public static byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString);
	}

	public static String encodeImage(byte[] imageByteArray) {
		return Base64.encodeBase64URLSafeString(imageByteArray);
	}
}
