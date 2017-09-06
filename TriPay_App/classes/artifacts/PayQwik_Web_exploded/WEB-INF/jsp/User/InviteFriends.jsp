<%-- <!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
<title><spring:message code="page.title.user.inviteFriends"/> <spring:message code="version">
	</spring:message></title>
</head>
<body
	onLoad="ActiveHeader('Account');ActiveSubmenu('${menu}');ActiveFadeIn('${menu}');">
	<jsp:include page="/WEB-INF/jsp/User/Header.jsp" />
	<jsp:include page="/WEB-INF/jsp/User/Menu.jsp" />

	<div class="col-md-4"></div>
	<div class="col-md-4">
		<legend>Invite Friend</legend>
		<div class="form-group">
			<p class="error">${msg}</p>
		</div>

		<ul class="nav nav-pills">
			<li id="IEmailSubMenu"><a data-toggle="pill"
				href="#IEmailFadeIn"><spring:message code="page.user.pills.email"/></a></li>
			<li id="IMobileSubMenu"><a data-toggle="pill"
				href="#IMobileFadeIn"><spring:message code="page.user.pills.mobile"/></a></li>
		</ul>

		<div class="tab-content">
			<div id="IEmailFadeIn" class="tab-pane fade">
				<h3><spring:message code="page.user.pills.email"/></h3>
				<form
					action="${pageContext.request.contextPath}/User/InviteFriends/ProcessByEmail"
					method="post">
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">@</div>
							<input type="email" class="form-control" name="email"
								value="${friend.email}" placeholder="<spring:message code="page.user.inviteFriend.form.input.email"/>" required="required" />
						</div>
						<p>${error.email}</p>
					</div>
					<div class="form-group">
						<input type="text" name="receiversName"
							class="form-control input-sm" value="${friend.receiversName}"
							placeholder="<spring:message code="page.user.inviteFriend.form.input.receiver"/>" required="required">
						<p class="error">${error.receiversName}</p>
					</div>
					<div class="form-group">
						<textarea cols="4" rows="3" placeholder="<spring:message code="page.user.inviteFriend.form.input.message"/>" name="Message"
							class="form-control">${friend.message}</textarea>
						<p class="error">${error.message}</p>
					</div>

					<div class="form-group">
						<button class="btn btn-primary btn-md btn-block btncu" type="submit"><spring:message code="page.user.inviteFriend.form.button"/></button>
					</div>
				</form>

			</div>
			<div id="IMobileFadeIn" class="tab-pane fade">
				<h3><spring:message code="page.user.pills.mobile"/></h3>
				<form
					action="${pageContext.request.contextPath}/User/InviteFriends/ProcessByMobile"
					method="post">


					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">+91</div>
							<input type="text" class="form-control" name="mobileNo"
								value="${friendm.mobileNo}" placeholder="<spring:message code="page.user.inviteFriend.form.input.mobileNumber"/>">
						</div>
						<p class="error">${errorm.mobileNo}</p>
					</div>


					<div class="form-group">
						<input type="text" name="receiversName"
							class="form-control input-sm" value="${friendm.receiversName}"
							placeholder="<spring:message code="page.user.inviteFriend.form.input.receiver"/>" required="required">
						<p class="error">${errorm.receiversName}</p>
					</div>

					<div class="form-group">
						<textarea cols="4" rows="3" placeholder="<spring:message code="page.user.inviteFriend.form.input.message"/>" name="message"
							class="form-control">${friendm.message}</textarea>
						<p class="error">${errorm.message}</p>
					</div>
					${merror.message}
					<div class="form-group">
						<button class="btn btn-primary btn-md btn-block btncu" type="submit"><spring:message code="page.user.inviteFriend.form.button"/></button>
					</div>
				</form>
			</div>

		</div>
	</div>
	<div class="col-md-4"></div>
</body>
</html>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/header.js"></script> --%>

<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>PayQwik</title>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/header.js"></script>
<link rel="icon" href='<c:url value="/resources/images/favicon.png"/>'
	type="image/png" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.1/animate.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

<link rel='stylesheet'
	href="https://fonts.googleapis.com/css?family=Ubuntu" type='text/css'>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
	integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r"
	crossorigin="anonymous">

<%-- <link href='<c:url value="/resources/css/style_main.css"/>'
	rel='stylesheet' type='text/css'> --%>
<link href='<c:url value="/resources/css/css_style.css"/>'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/r/bs-3.3.5/jq-2.1.4,dt-1.10.8/datatables.min.css" />
<script type="text/javascript"
	src="https://cdn.datatables.net/r/bs-3.3.5/jqc-1.11.3,dt-1.10.8/datatables.min.js"></script>
</head>

<body
	onLoad="ActiveMenu('MobileTopup');ActiveSubmenu('${menu}');ActiveFadeIn('${menu}');SelectOperator();">
	<jsp:include page="/WEB-INF/jsp/User/Header.jsp" />
	<jsp:include page="/WEB-INF/jsp/User/Menu.jsp" />

	<!-----------------end navbar---------------------->

	<!------------- end main-------------------->

	<div class="background"></div>
	<!---blue box---->

	<div class="container" id="money_box">
		<div class="row">
			<div class="col-md-4" id="Prepaid">
				<!---form---->
				<div class="box hidden-xs"></div>

				<ul class="nav nav-pills">
					<li class="active"><a data-toggle="pill" href="#IEmailFadeIn">Invite
							By Email</a></li>
					<li><a data-toggle="pill" href="#IMobileFadeIn">Invite By
							Mobile</a></li>
				</ul>

				<div class="tab-content" id="sendmoney">
					<div id="IEmailFadeIn" class="tab-pane fade in active">

						<form
							action="${pageContext.request.contextPath}/User/InviteFriends/ProcessByEmail"
							method="post">
							<div class="group_1">

								<input type="email" name="email" value="${friend.email}"
									required="required" /> <span class="highlight"></span> <span
									class="bar"></span> <label><spring:message
										code="page.user.inviteFriend.form.input.email" /></label>

								<p>${error.email}</p>
							</div>
							<div class="group_1">
								<input type="text" name="receiversName"
									value="${friend.receiversName}" required="required"> <span
									class="highlight"></span> <span class="bar"></span> <label><spring:message
										code="page.user.inviteFriend.form.input.receiver" /></label>

								<p class="error">${error.receiversName}</p>
							</div>
							<div class="group_1">
								<input type="text" name="message" value="${friend.message}"
									required /> <span class="highlight"></span> <span class="bar"></span>
								<label><spring:message
										code="page.user.inviteFriend.form.input.message" /></label>

								<p class="error">${error.message}</p>
							</div>

							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; color: #FFFFFF;">
								<spring:message code="page.user.inviteFriend.form.input.message" />
							</button>
						</form>


					</div>

					<div id="IMobileFadeIn" class="tab-pane fade">
						<form
							action="${pageContext.request.contextPath}/User/InviteFriends/ProcessByEmail"
							method="post">
							<div class="group_1">
								<input type="number" name="mobileNo" value="${friendm.mobileNo}"
									required="required" /> <span class="highlight"></span> <span
									class="bar"></span> <label><spring:message
										code="page.user.inviteFriend.form.input.mobileNumber" /></label>

								<p>${errorm.mobileNo}</p>
							</div>
							<div class="group_1">
								<input type="text" name="receiversName"
									value="${friendm.receiversName}" required="required"> <span
									class="highlight"></span> <span class="bar"></span> <label><spring:message
										code="page.user.inviteFriend.form.input.receiver" /></label>

								<p class="error">${errorm.recieversName}</p>
							</div>
							<div class="group_1">
								<input type="text" name="message" value="${friendm.message}"
									required /> <span class="highlight"></span> <span class="bar"></span>
								<label><spring:message
										code="page.user.inviteFriend.form.input.message" /></label>

								<p class="error">${errorm.message}</p>
							</div>

							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; color: #FFFFFF;">
								<spring:message code="page.user.inviteFriend.form.input.message" />
							</button>
						</form>

					</div>
				</div>

			</div>
			<!----end col-md-4-->

			<div class="col-md-8 hidden-xs">
				<div class="slider" style="margin-right: -15px; margin-left: -15px;">
					<div class="carousel slide hidden-xs" data-ride="carousel"
						id="mycarousel">
						<ol class="carousel-indicators">
							<li class="active" data-slide-to="0" data-target="#mycarousel"></li>
							<li data-slide-to="1" data-target="#mycarousel"></li>
						</ol>

						<div class="carousel-inner">

							<div class="item active" id="slide1">
								<img src='<c:url value="/resources/images/main/slider_4.jpg"/>' />
							</div>
							<!---end item---->

							<div class="item">

								<img src='<c:url value="/resources/images/main/slider_4.jpg"/>'
									style="width:">
							</div>
							<!---end item---->
						</div>
						<!--end carousel inner------>

					</div>
					<!---end caeousel slider---->
				</div>
				<!---end slider----->
			</div>
		</div>
		<!-----end col-md-8-->
	</div>
	<!---end row-->
	<!----end container-->
	<jsp:include page="/WEB-INF/jsp/User/Footer.jsp" />

	<script src="http://code.jquery.com/jquery-2.2.1.min.js"></script>
	<script type="text/javascript"
		src='<c:url value="/resources/js/wow.js"/>' /></script>
	<script>
		new WOW().init();
	</script>
	<!-- Latest compiled and minified JavaScript -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
		integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
		crossorigin="anonymous"></script>
</body>
</html>



