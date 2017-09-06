<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0" />
<head>
<link rel="icon" href='<c:url value="/resources/images/favicon.png"/>' type="image/png" />
<link
	href="${pageContext.request.contextPath}/css/jquery.dataTables.min.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/Style.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/lightbox.css"
	rel="stylesheet" type="text/css" />
<title><spring:message code="page.title.signup">
	</spring:message> <spring:message code="version">
	</spring:message></title>
</head>
<body onLoad="ActiveHeader('SignUp')">
	<jsp:include page="/WEB-INF/jsp/Header.jsp" />
	<div class="col-md-4"></div>
	<div class="col-md-4" style="padding-top: 100px">
		<form action="Register" method="post">
			<legend class="white-font">Sign Up</legend>
			<p class="align-center">${msg}</p>
			<div class="form-group">
				<input type="text" name="firstName" class="form-control input-sm"
					value="${user.firstName}" placeholder="First Name"
					required="required" autofocus>
				<p class="error">${error.firstName}</p>
			</div>
			<div class="form-group">
				<input type="text" name="middleName" class="form-control input-sm"
					value="${user.middleName}" placeholder="Middle Name">
				<p class="error">${error.middleName}</p>
			</div>
			<div class="form-group">
				<input type="text" name="lastName" class="form-control input-sm"
					value="${user.lastName}" placeholder="Last Name"
					required="required">
				<p class="error">${error.lastName}</p>
			</div>
			<div class="form-group">
				<input type="password" name="password" class="form-control input-sm"
					placeholder="Password" required="required">
				<p class="error">${error.password}</p>
			</div>
			<div class="form-group">
				<input type="password" name="confirmPassword"
					class="form-control input-sm" placeholder="Confirm Password"
					required="required">
				<p class="error">${error.confirmPassword}</p>
			</div>
			<div class="form-group">
				<textarea type="text" name="address" rows="3" cols="4"
					class="form-control" value="${user.address}" placeholder="Address"></textarea>
				<p class="error">${error.address}</p>
			</div>
			<div class="form-group">
				<input type="text" name="locationCode" class="form-control input-sm"
					placeholder="Pincode" maxlength="6" value="${user.locationCode}">
				<p class="error">${error.locationCode}</p>
			</div>
			<div class="form-group">
				<input type="text" name="contactNo" class="form-control input-sm"
					value="${user.contactNo}" placeholder="Contact Number"
					maxlength="10" required="required">
				<p class="error">${error.contactNo}</p>
			</div>
			<div class="form-group">
				<input type="email" name="email" class="form-control input-sm"
					value="${user.email}" placeholder="Email Id" required="required">
				<p class="error">${error.email}</p>
			</div>
			<div class="form-group col-md-6"
				style="float: none; margin-left: auto; margin-right: auto;">
				<button class="btn btn-primary btn-md btn-block btncu">Sign
					Up</button>
			</div>
		</form>
	</div>
	<div class="col-md-4"></div>
</body>
</html>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap-typeahead.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/Search/Place.js"></script>