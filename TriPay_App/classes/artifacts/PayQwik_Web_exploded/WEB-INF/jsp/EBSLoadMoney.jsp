<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>EBS Payment Gateway</title>
</head>
<body>
<table>
	<sf:form modelAttribute="ebs" action="/PayQwikApp/EBSGateway/Test/Process" method="post">
	
		<td>Amount</td>
		<td><sf:input path="amount"/></td>
		</tr>
		<tr>
		<td>Name</td>
		<td><sf:input path="name"/></td>
		</tr>
		<tr>
		<td>State</td>
		<td><sf:input path="state"/></td>
		</tr>
		<tr>
		<td>City</td>
		<td><sf:input path="city"/></td>
		</tr>

		<tr>
		<td>Postal Code</td>
		<td><sf:input path="postal_code"/></td>
		</tr>
		<tr>
		<td>Phone</td>
		<td><sf:input path="phone"/></td>
		</tr>
		<tr>
		<td>Email</td>
		<td><sf:input path="email"/></td>
		</tr>

		<tr>
		<td></td>
		<td><input type="submit" value="Continue" /></td>
 		</tr>
	</sf:form>
</table>
</body>
</html>