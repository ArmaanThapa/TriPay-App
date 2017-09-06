<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

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
<title><spring:message code="page.title.user.editProfile"/> <spring:message code="version">
	</spring:message></title>
</head>
<body onLoad="ActiveHeader('Settings');">
	<jsp:include page="/WEB-INF/jsp/User/Header.jsp" />
	<jsp:include page="/WEB-INF/jsp/User/Menu.jsp" />

	<div class="col-md-4"></div>
	<div class="col-md-4">
		<div class="form-group">
			<p class="error">${msg}</p>
		</div>
		<form
			action="${pageContext.request.contextPath}/User/EditProfile/Process"
			method="post">

			<div class="form-group">
				<input type="text" name="firstName" class="form-control input-sm"
					value="${user.firstName}" placeholder="<spring:message code="page.signup.form.input.firstName"/>"
					required="required">
				<p class="error">${error.firstName}</p>
			</div>
			<div class="form-group">
				<input type="text" name="middleName" class="form-control input-sm"
					value="${user.middleName}" placeholder="<spring:message code="page.signup.form.input.middleName"/>"
					required="required">
				<p class="error">${error.middleName}</p>
			</div>
			<div class="form-group">
				<input type="text" name="lastName" class="form-control input-sm"
					value="${user.lastName}" placeholder="<spring:message code="page.signup.form.input.lastName"/>"
					required="required">
				<p class="error">${error.lastName}</p>
			</div>
			<div class="form-group">
				<textarea name="address" class="form-control input-sm"
					rows="3" cols="4" placeholder="<spring:message code="page.signup.form.input.address"/>" required="required">${user.address}</textarea>
				<p class="error">${error.address}</p>
			</div>
			<div class="form-group">
				<input type="text" name="locationCode" class="form-control input-sm"
					placeholder="<spring:message code="page.signup.form.input.locationCode"/>" maxlength="6" value="${user.locationCode}">
				<p class="error">${error.locationCode}</p>
			</div>

			<div class="form-group">
				<button class="btn btn-primary btn-md btn-block btncu" type="submit"><spring:message code="page.user.editProfile.form.button"/></button>
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
