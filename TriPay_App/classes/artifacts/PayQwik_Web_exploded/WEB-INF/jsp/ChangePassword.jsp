<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0">
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
<title><spring:message code="page.title.changePassword">
	</spring:message> <spring:message code="version">
	</spring:message></title>
</head>
<body onLoad="ActiveHeader('Home')">
	<jsp:include page="/WEB-INF/jsp/Header.jsp" />
	<div class="row" style="padding: 90px">
		<div class="col-md-4"></div>
		<div class="col-md-4">
			<form action="RenewPassword" method="post">
				<legend class="white-font">Change password</legend>
				<p class="align-center">${msg}</p>
				<input type="hidden" name="username" value="${user}"
					required="required" hidden="hidden">
				<div class="form-group">
					<input type="text" name="key" class="form-control input-sm" placeholder="Enter OTP"
						required="required">
				</div>
				<div class="form-group">
					<input type="password" name="newPassword"
						class="form-control input-sm" placeholder="New Password"
						required="required">
				</div>
				<div class="form-group">
					<input type="password" name="confirmPassword"
						class="form-control input-sm" placeholder="Confirm Password"
						required="required">
					<p class="error">${error}</p>
				</div>
				<div class="form-group col-md-6"
					style="float: none; margin-left: auto; margin-right: auto;">
					<button class="btn btn-primary btn-md btn-block btncu"
						style="margin-bottom: 5px">Change Password</button>
				</div>
			</form>
		</div>
		<div class="col-md-4"></div>
	</div>
</body>
</html>