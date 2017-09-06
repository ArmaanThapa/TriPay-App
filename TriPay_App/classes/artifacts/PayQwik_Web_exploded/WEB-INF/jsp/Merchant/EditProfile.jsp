<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>PayQwik</title>
</head>
<body onLoad="ActiveHeader('Account');">
	<jsp:include page="/WEB-INF/jsp/Merchant/Header.jsp" />
	<div class="col-md-4"></div>
	<div class="col-md-4">
		<legend>Edit Profile</legend>
		<div class="form-group">
			<p class="error">${msg}</p>
		</div>
		<form action="EditProfile" method="post">
			<input type="text" name="username" value="${user.username}"
				hidden="hidden">
			<div class="form-group">
				<input type="text" name="firstName" class="form-control input-sm"
					value="${user.firstName}" placeholder="First Name"
					required="required">
				<p class="error">${error.firstName}</p>
			</div>
			<div class="form-group">
				<input type="text" name="middleName" class="form-control input-sm"
					value="${user.middleName}" placeholder="Middle Name"
					required="required">
				<p class="error">${error.middleName}</p>
			</div>
			<div class="form-group">
				<input type="text" name="lastName" class="form-control input-sm"
					value="${user.lastName}" placeholder="Last Name"
					required="required">
				<p class="error">${error.lastName}</p>
			</div>
			<div class="form-group">
				<input type="text" name="address" class="form-control input-sm"
					value="${user.address}" placeholder="Address" required="required">
				<p class="error">${error.address}</p>
			</div>
			<div class="form-group">
				<input type="text" name="contactNo" class="form-control input-sm"
					value="${user.contactNo}" placeholder="Contact Number"
					required="required">
				<p class="error">${error.contactNo}</p>
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-md btn-block btncu">Edit
					User</button>
			</div>
		</form>
	</div>
	<div class="col-md-4"></div>
</body>
</html>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap-typeahead.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/header.js"></script>
