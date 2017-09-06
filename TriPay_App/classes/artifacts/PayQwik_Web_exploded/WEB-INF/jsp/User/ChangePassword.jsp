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
<title><spring:message code="page.title.user.changePassword"/><spring:message code="version">
	</spring:message></title>
</head>
<body onLoad="ActiveHeader('Settings')">
	<jsp:include page="/WEB-INF/jsp/User/Header.jsp" />
	<jsp:include page="/WEB-INF/jsp/User/Menu.jsp" />

	<div class="col-md-4"></div>
	<div class="col-md-4">
		<div class="form-group">
			<br><br>
			<p class="error">${msg}</p>
		</div>
		<form
			action="${pageContext.request.contextPath}/User/UpdatePassword/Process"
			method="post">
			<div class="form-group">
				<input type="password" name="password" class="form-control input-sm"
					placeholder="<spring:message code="page.user.changePassword.form.input.password"/>" required="required">
				<p class="error">${error.password}</p>
			</div>
			<div class="form-group">
				<input type="password" name="newPassword"
					class="form-control input-sm" placeholder="<spring:message code="page.user.changePassword.form.input.newPassword"/>"
					required="required">
				<p class="error">${error.newPassword}</p>
			</div>
			<div class="form-group">
				<input type="password" name="confirmPassword"
					class="form-control input-sm"
					placeholder="<spring:message code="page.user.changePassword.form.input.confirmPassword"/>" required="required">
				<p class="error">${error.confirmPassword}</p>
			</div>
			<div class="form-group">
				<input type="submit" class="btn btn-primary btn-block"
					value="<spring:message code="page.user.changePassword.form.button"/>">
			</div>
		</form>
	</div>
	<div class="col-md-4"></div>
</body>
</html>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/header.js"></script>
