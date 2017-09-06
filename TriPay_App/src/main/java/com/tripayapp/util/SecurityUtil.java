package com.tripayapp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtil {

	protected final static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	private static final String HASH_KEY = generateKey("VIBHANSHU_VYAS");
	// C1B4246A70E8C22CE85CA472AB90A3DF

	private static final byte[] BYTE_KEY = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public static void main(String[] args) {
		String text  = "1000000008";
		String input = "aaa";
		try {
			System.err.println("md5 hash ::"+md5(text));
			String[] encrypted = encryptObject(input);
			Object obj = decryptObject(encrypted[0], encrypted[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Encrypts and encodes the Object and IV for url inclusion
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String[] encryptObject(Object obj) throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(stream);
		try {
			// Serialize the object
			out.writeObject(obj);
			byte[] serialized = stream.toByteArray();

			// Setup the cipher and Init Vector
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecureRandom random = new SecureRandom();
			byte[] iv = random.generateSeed(16);
			// byte[] iv = new byte[cipher.getBlockSize()];
			logger.info("number of IV bytes is " + iv.length + " " + iv);
			new SecureRandom().nextBytes(iv);
			IvParameterSpec ivSpec = new IvParameterSpec(iv);

			// Hash the key with SHA-256 and trim the output to 128-bit for the
			// key
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			// digest.update(KEY.getBytes());
			digest.update(BYTE_KEY);
			byte[] key = new byte[16];
			System.arraycopy(digest.digest(), 0, key, 0, key.length);
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

			// encrypt
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			// Encrypt & Encode the input
			byte[] encrypted = cipher.doFinal(serialized);
			byte[] base64Encoded = Base64.encodeBase64(encrypted);
			String base64String = new String(base64Encoded);
			String urlEncodedData = URLEncoder.encode(base64String, "UTF-8");

			// Encode the Init Vector
			byte[] base64IV = Base64.encodeBase64(iv);
			String base64IVString = new String(base64IV);
			String urlEncodedIV = URLEncoder.encode(base64IVString, "UTF-8");

			return new String[] { urlEncodedData, urlEncodedIV };
		} finally {
			stream.close();
			out.close();
		}
	}

	/**
	 * Decrypts the String and serializes the object
	 * 
	 * @param base64Data
	 * @param base64IV
	 * @return
	 * @throws Exception
	 */
	public static Object decryptObject(String base64Data, String base64IV) throws Exception {
		// Decode the data
		byte[] encryptedData = Base64.decodeBase64(base64Data.getBytes());

		// Decode the Init Vector
		byte[] rawIV = Base64.decodeBase64(base64IV.getBytes());

		// Configure the Cipher
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec ivSpec = new IvParameterSpec(rawIV);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		// digest.update(KEY.getBytes());
		digest.update(BYTE_KEY);
		// System.out.println(KEY.getBytes() + " BYTE SIZE");
		byte[] key = new byte[16];
		System.arraycopy(digest.digest(), 0, key, 0, key.length);
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		// Decrypt the data..
		byte[] decrypted = cipher.doFinal(encryptedData);

		// Deserialize the object
		ByteArrayInputStream stream = new ByteArrayInputStream(decrypted);
		ObjectInput in = new ObjectInputStream(stream);
		Object obj = null;
		try {
			obj = in.readObject();
		} finally {
			stream.close();
			in.close();
		}
		return obj;
	}

	public static String generateKey(String str) {

		String hashKey = "";
		try {
			hashKey = md5(str);
		} catch (Exception e) {
			logger.info("Key Not Generated");
		}
		return hashKey;

	}

	public static String md5(String str) throws Exception {

		MessageDigest m = MessageDigest.getInstance("MD5");

		byte[] data = str.getBytes();

		m.update(data, 0, data.length);

		BigInteger i = new BigInteger(1, m.digest());

		String hash = String.format("%1$032X", i);

		return hash;
	}

	public static String sha1(String str) throws Exception {

		MessageDigest m = MessageDigest.getInstance("SHA-1");

		byte[] data = str.getBytes();

		m.update(data, 0, data.length);

		BigInteger i = new BigInteger(1, m.digest());

		String hash = String.format("%1$032X", i);

		return hash;
	}

	public static String sha512(String str) throws Exception {

		MessageDigest m = MessageDigest.getInstance("SHA-512");

		byte[] data = str.getBytes();

		m.update(data, 0, data.length);

		BigInteger i = new BigInteger(1, m.digest());

		String hash = String.format("%1$032X", i);

		return hash;
	}

	public static boolean isHashMatches(Object obj, String hash) {
		boolean isValid = true;
		ObjectWriter ow = new ObjectMapper().writer();
		try {
			String json = ow.writeValueAsString(obj) + HASH_KEY;
			logger.info("JSON ::" + json);
			String encryptedHash = "";
			try {
				encryptedHash = md5(json);
				logger.info("Calculated hash ::" + encryptedHash);
			} catch (Exception e) {
				logger.info("Internal Server Error While Using MD5");
			}
			if (encryptedHash.equals(hash)) {
				logger.info("Both hash are equal");
				isValid = true;
			} else {
				logger.info("Hash are not equal ::" + hash);
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isValid;
	}

}
