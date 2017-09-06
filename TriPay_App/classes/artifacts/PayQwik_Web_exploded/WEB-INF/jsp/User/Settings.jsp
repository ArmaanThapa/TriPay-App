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
					<li class="active"><a data-toggle="pill" href="#home">Edit
							Profile</a></li>
					<li><a data-toggle="pill" href="#menu1">Change Password</a></li>
				</ul>

				<div class="tab-content" id="sendmoney">
					<div id="home" class="tab-pane fade in active">
						<form modelAttribute="editUser" method="post"
							action="${pageContext.request.contextPath}/User/EditProfile/Process">

							<div class="group_1">
								<input name="firstName" type="text" value="${user.userDetail.firstName }" required="required" />
								<span class="highlight"></span> <span class="bar"></span> <label>First
									Name</label>
								<p class="error">${error.firstName}</p>
							</div>
							<div class="group_1">
								<input name="lastName" type="text" value="${user.userDetail.lastName }" required="required" />
								<span class="highlight"></span> <span class="bar"></span> <label>Last
									Name</label>
								<p class="error">${error.lastName}</p>
							</div>
							<div class="group_1">
								<input type="text" name="address" value="${user.userDetail.address }" required="required" />

								<span class="highlight"></span> <span class="bar"></span> <label>Address</label>
								<p class="error">${error.address}</p>
							</div>

							<div class="group_1">
								<input type="email"  value="${user.userDetail.email }"  name="email" required="required" />

								<span class="highlight"></span> <span class="bar"></span> <label>Email</label>
								<p class="error">${error.email}</p>
							</div>

							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; color: #FFFFFF;">Update</button>

						</form>


					</div>

					<div id="menu1" class="tab-pane fade">
						<form method="post"
							action="${pageContext.request.contextPath}/User/UpdatePassword/Process">
							${msg}
							<div class="group_1">
								<input type="password" name="password" required> <span
									class="highlight"></span> <span class="bar"></span> <label>Current
									Password</label>
								<p>${cherror.password}</p>
							</div>

							<div class="group_1">
								<input type="password" name="newPassword" required> <span
									class="highlight"></span> <span class="bar"></span> <label>New
									Password</label>
								<p>${cherror.newPassword }</p>
							</div>

							<div class="group_1">
								<input type="password" name="confirmPassword" required>
								<span class="highlight"></span> <span class="bar"></span> <label>Confirm
									New Password</label>
								<p>${cherror.confirmPassword}</p>
							</div>


							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; color: #FFFFFF;">Update Password</button>
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



