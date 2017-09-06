<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.security.MessageDigest,
                 java.math.*,
				 java.util.*,
				 java.io.*"%>

<%! 
	private String md5(String str) throws Exception {
		MessageDigest m = MessageDigest.getInstance("MD5");

		byte[] data = str.getBytes();
		
		m.update(data,0,data.length);
		
		BigInteger i = new BigInteger(1,m.digest());
		
		String hash = String.format("%1$032X", i);
		
		return hash;
	}
%>
<%! 
	private String sha1(String str) throws Exception {
		MessageDigest m = MessageDigest.getInstance("SHA-1");

		byte[] data = str.getBytes();
		
		m.update(data,0,data.length);
		
		BigInteger i = new BigInteger(1,m.digest());
		
		String hash = String.format("%1$032X", i);
		
		return hash;
	}
%>


<%! 
	private String sha512(String str) throws Exception {
		MessageDigest m = MessageDigest.getInstance("SHA-512");

		byte[] data = str.getBytes();
		
		m.update(data,0,data.length);
		
		BigInteger i = new BigInteger(1,m.digest());
		
		String hash = String.format("%1$032X", i);
		
		return hash;
	}
%>

<%
    String SECURE_SECRET = "Your Registered Secret Key"; // your secret key;
	
	String md5HashData = SECURE_SECRET;
    // retrieve all the parameters into a hash map
   
	HashMap testMap = new HashMap();
    Enumeration en = request.getParameterNames();

	while(en.hasMoreElements()) {
        String fieldName = (String) en.nextElement();
        String fieldValue = request.getParameter(fieldName);
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
            testMap.put(fieldName, fieldValue);
        }
    }

	//Sort the HashMap
    Map requestFields = new TreeMap(testMap);
    
    String V3URL = (String) requestFields.remove("V3URL");
    requestFields.remove("submit");
    
%>  <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="no-store, no-cache, must-revalidate">
<META HTTP-EQUIV="PRAGMA" CONTENT="no-store, no-cache, must-revalidate">
<body onLoad="document.order.submit()">
	<form name="order" action="<%=V3URL%>" method="post">
	<p>Please wait while your payment is being processed...</p>
<%	
	
	//For Each item In Request.Form
	 // append all fields in a data string
        for (Iterator i = requestFields.keySet().iterator(); i.hasNext(); ) {
            
            String key = (String)i.next();
            String value = (String)requestFields.get(key);
			md5HashData += "|"+value;
%>
        	<input type="hidden" name="<%=key%>" value="<%=value%>"/><br>
<%             
    	}
String hvalue = (String)requestFields.get("algo");

String hashedvalue ="";

if ( hvalue.equals("MD5") )
{
    hashedvalue = md5(md5HashData);
}

else if ( hvalue.equals("SHA1"))
{
    hashedvalue = sha1(md5HashData);
}
else 
{
   hashedvalue = sha512(md5HashData);
}
%>
    <input type="hidden" name="secure_hash" value="<%=hashedvalue%>"/>
    
 </form>
</body>
</html>
