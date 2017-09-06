package com.tripayapp.util;

import com.tripayapp.model.KycDTO;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URLEncoder;

public class TriPayUtil {

	public static String Host_URL = "http://localhost:8080/";

	public static String Image_URL = Host_URL + "PayQwik-Images/";

	public static String No_Image_Folder = Host_URL + "images/noimage.jpg";

	public static String Image_Folder = "D:/Projects/Java/payqwikapp/WebContent/PayQwik-Images/";

	public static final String MAIL_TEMPLATE = "com/tripayapp/mail/template/";

	public static final String SMS_TEMPLATE = "com/tripayapp/sms/template/";

	public static final long BASE_ACCOUNT_NUMBER = 999900910000000L;

	public static final String INSTANTPAY_MAIL = "instantpay@msewa.com";

	public static final String PROMOCODE_MAIL = "promocode@msewa.com";

	public static final String KYC_URL = "http://172.16.3.48:9037/Default.aspx";

	public static final String CHANNEL_ID = "MOB";

	public static final String USER_NAME = "vpayqwik";

	public static final String PASSWORD = "vijayavpayQwikvalidation";

	public static final String TXN_ID = "42144534";

	public static JSONObject getInJSON(KycDTO request){
		JSONObject json = new JSONObject();
		try {
			System.err.println("in get in json method");
			json.put("OPCODE","KYC");
			json.put("MOBILE",request.getMobileNumber());
			json.put("ACCOUNT_NO",request.getAccountNumber());
			json.put("TXN_ID",String.valueOf(System.currentTimeMillis()));
			json.put("USER_NAME",USER_NAME);
			json.put("PASSWORD",PASSWORD);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}


	public static String getInString(KycDTO request){
		System.err.println("in string method");
		JSONObject json = getInJSON(request);
		String response = json.toString();
		System.err.println(URLEncoder.encode(response));
		return response;
	}

	public static String getKycRequestToSoap(KycDTO request)  {

		try {
			DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docfactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			//Envelope
			Element envelope = doc.createElement("soapenv:Envelope");
			doc.appendChild(envelope);
			envelope.setAttribute("xmlns:tem", "http://tempuri.org");
			envelope.setAttribute("xmlns:soapenv", "http://schemas.xmlsoap.org/soap/envelope/");

			Element body = doc.createElement("soapenv:Body");
			Element apiName = doc.createElement("tem:validateVPayQwikUser");
			Element accountNo = doc.createElement("tem:accountNo");
			accountNo.appendChild(doc.createTextNode(request.getAccountNumber()));

			Element mobileNo = doc.createElement("tem:mobileNo");
			mobileNo.appendChild(doc.createTextNode(request.getMobileNumber()));

			Element channelId = doc.createElement("tem:channelId");
			channelId.appendChild(doc.createTextNode((CHANNEL_ID)));

			Element username = doc.createElement("tem:userName");
			username.appendChild(doc.createTextNode((USER_NAME)));

			Element password = doc.createElement("tem:password");
			password.appendChild(doc.createTextNode((PASSWORD)));

			Element txnId = doc.createElement("tem:txnId");
			txnId.appendChild(doc.createTextNode((TXN_ID)));

			apiName.appendChild(accountNo);
			apiName.appendChild(mobileNo);
			apiName.appendChild(channelId);
			apiName.appendChild(username);
			apiName.appendChild(password);
			apiName.appendChild(txnId);
			body.appendChild(apiName);
			envelope.appendChild(body);

			String payload = toString(doc);
			System.out.println("Payload : : ");
			System.out.println(payload);

			//Parse to JSON Object
			/*JSONObject xmlJSONObj = XML.toJSONObject(payload);
			System.out.println("JSON: :"+xmlJSONObj);*/
			return payload;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Document getKycRequestToSoapInDocument(KycDTO request)  {

		try {
			DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docfactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			//Envelope
			Element envelope = doc.createElement("soapenv:Envelope");
			doc.appendChild(envelope);
			envelope.setAttribute("xmlns:tem", "http://tempuri.org");
			envelope.setAttribute("xmlns:soapenv", "http://schemas.xmlsoap.org/soap/envelope/");

			//Header
			Element header = doc.createElement("soapenv:Header");
			envelope.appendChild(header);
			Element body = doc.createElement("soapenv:Body");
			Element apiName = doc.createElement("tem:validateVPayQwikUser");
			Element accountNo = doc.createElement("tem:accountNo");

			accountNo.appendChild(doc.createTextNode(request.getAccountNumber()));

			Element mobileNo = doc.createElement("tem:mobileNo");
			mobileNo.appendChild(doc.createTextNode(request.getMobileNumber()));

			Element channelId = doc.createElement("tem:channelId");
			channelId.appendChild(doc.createTextNode((CHANNEL_ID)));

			Element username = doc.createElement("tem:userName");
			username.appendChild(doc.createTextNode((USER_NAME)));

			Element password = doc.createElement("tem:password");
			password.appendChild(doc.createTextNode((PASSWORD)));

			Element txnId = doc.createElement("tem:txnId");
			txnId.appendChild(doc.createTextNode((TXN_ID)));

			apiName.appendChild(accountNo);
			apiName.appendChild(mobileNo);
			apiName.appendChild(channelId);
			apiName.appendChild(username);
			apiName.appendChild(password);
			apiName.appendChild(txnId);
			body.appendChild(apiName);
			envelope.appendChild(body);

			return doc;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toString(Document doc) {

		try {
			StringWriter sw = new StringWriter();
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
//			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//				transformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml; charset=utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

//	public static void main(String... args){
//		KycDTO dto = new KycDTO();
//		dto.setAccountNumber("111111111111111");
//		dto.setMobileNumber("8769986881");
//		System.err.print(getInString(dto));
////		System.err.print(getKycRequestToSoapInDocument(dto));
//	}

}
