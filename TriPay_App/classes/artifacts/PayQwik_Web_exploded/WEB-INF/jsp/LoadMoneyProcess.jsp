<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body onLoad="document.pay.submit()">
	<sf:form modelAttribute="pay" action="${url}">

		<sf:hidden path="account_id" />
		<sf:hidden path="address" />
		<sf:hidden path="amount" />
		<sf:hidden path="channel" />
		<sf:hidden path="city" />
		<sf:hidden path="country" />
		<sf:hidden path="currency" />
		<sf:hidden path="description" />
		<sf:hidden path="email" />
		<sf:hidden path="mode" />
		<sf:hidden path="name" />
		<sf:hidden path="phone" />
		<sf:hidden path="postal_code" />
		<sf:hidden path="reference_no" />
		<sf:hidden path="return_url" />
		<sf:hidden path="ship_address" />
		<sf:hidden path="ship_city" />
		<sf:hidden path="ship_country" />
		<sf:hidden path="ship_name" />
		<sf:hidden path="ship_phone" />
		<sf:hidden path="ship_postal_code" />
		<sf:hidden path="ship_state" />
		<sf:hidden path="state" />

		<sf:hidden path="secure_hash" />
		<!-- <input type="submit" value="submit" /> -->
	</sf:form>
	<script>
		document.getElementById('pay').submit();
	</script>

</body>
</html>