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
	href="${pageContext.request.contextPath}/resources/css/jquery.dataTables.min.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/bootstrap.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/Style.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/lightbox.css"
	rel="stylesheet" type="text/css" />
<title><spring:message code="page.title.verifyMobile">
	</spring:message> <spring:message code="version">
	</spring:message></title>
</head>
<body onLoad="ActiveHeader('Home')">
	<jsp:include page="/WEB-INF/jsp/Header.jsp" />
	<div class="col-md-4"></div>
	<div class="col-md-4" style="padding-top: 100px">
		<form action="${pageContext.request.contextPath}/Activate/Mobile"
			method="post">
			<legend class="align-center">Verify Mobile Number
				${mobileNumber}</legend>
			<p class="align-center">${msg}</p>
			<div class="form-group">
				<input type="text" name="key" class="form-control input-sm"
					placeholder="Mobile Token" required="required">
			</div>
			<div class="form-group">
				<input type="hidden" name="mobileNumber" class="form-control input-sm"
					value="${mobileNumber}" placeholder="Mobile Number"
					required="required"  />
			</div>
			<div class="form-group col-md-6"
				style="float: none; margin-left: auto; margin-right: auto;">
				<button class="btn btn-primary btn-md btn-block btncu"
					style="margin-bottom: 5px">Verify Mobile</button>
			</div>
		</form>
		<form action="${pageContext.request.contextPath}/Resend/Mobile/OTP"
			method="post">
			<div class="form-group">
				<input type="hidden" name="mobileNumber"
					class="form-control input-sm" value="${mobileNumber}"
					placeholder="Mobile Number" required="required" hidden="hidden" />
			</div>
			<div class="form-group col-md-6"
				style="float: none; margin-left: auto; margin-right: auto;">
				<button class="btn btn-primary btn-md btn-block btncu"
					style="margin-bottom: 5px">Resend OTP</button>
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

