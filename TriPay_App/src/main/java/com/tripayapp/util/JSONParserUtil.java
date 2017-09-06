package com.tripayapp.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONParserUtil {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static String getString(JSONObject object, String key) {
		String value = null;
		try {
			value = (String) object.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static int getInt(JSONObject object, String key) {
		int value = 0;
		try {
			value = (int) object.getInt(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static boolean checkKey(JSONObject object, String key) {
		return object.has(key);
	}
	
	public static String convertToJSON(Object obj) {
		String json = "";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		 try {
			json = ow.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

}
