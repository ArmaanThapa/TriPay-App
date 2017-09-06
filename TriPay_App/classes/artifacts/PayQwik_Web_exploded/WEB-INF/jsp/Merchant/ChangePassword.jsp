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
<body onLoad="ActiveHeader('Account')">
	<jsp:include page="/WEB-INF/jsp/Merchant/Header.jsp" />
	<div class="col-md-4"></div>
	<div class="col-md-4">
		<legend>Change password</legend>
		<div class="form-group">
			<p class="error">${msg}</p>
		</div>
		<form action="ChangePassword" method="post">
			<div class="form-group">
				<input type="password" name="password" class="form-control input-sm"
					placeholder="New Password....." required="required">
				<p class="error">${error.password}</p>
			</div>
			<div class="form-group">
				<input type="password" name="confirmPassword"
					class="form-control input-sm" placeholder="Confirm Password....."
					required="required">
				<p class="error">${error.confirmPassword}</p>
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-md btn-block btncu">Change</button>
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
	src="${pageContext.request.contextPath}/js/header.js"></script>
