<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>PayQwik</title>

<!-- Latest compiled and minified CSS -->
<link rel="icon" href='<c:url value="/resources/images/favicon.png"/>'
	type="image/png" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">

<link href="<c:url value="/resources/css/style_main.css"/>"
	rel='stylesheet' type='text/css'>
<link href="<c:url value="/resources/css/css_style.css"/>"
	rel='stylesheet' type='text/css'>
</head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/header.js"></script>
<body onload="processMessage('${msg}');">
	<div class="topline"></div>
	<c:if test="${msge ne null}">
		<div class="alert alert-info">${msge}</div>
	</c:if>
	<nav class="navbar navbar-default"
		style="background: #08bbc7; min-height: 80px;">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#"><img
					src="resources/images/logo_ios.png" alt=""></a>
			</div>
			
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="container" style="margin-top: 8px;">
				<ul class="nav navbar-nav navbar-right">
					<form id="login" action="j_spring_security_check" method="post">
						<div class="group">
							<input type="text" name="j_username" autocomplete="off"  maxlength="10" required>
							<span class="highlight"></span> <span class="bar"></span> <label>Mobile
								No</label>
						</div>

						<div class="group">
							<input type="password" name="j_password" autocomplete="off" maxlength="50"
								required> <span class="highlight"></span> <span
								class="bar"></span> <label>Password</label> <a href="#" data-toggle="modal" data-target="#forgotPassword">Forgot
								Password? </a>
						</div>
						<button type="submit" class="btn"
							style="margin-top: 12px; margin-left: 20px; background: #ec2029; color: #FFFFFF;">Submit</button>
					</form>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
	<!-----------------end navbar---------------------->


	<div class="carousel slide" data-ride="carousel" id="mycarousel">

		<div class="carousel-inner" role="listbox">

			<div class="item active">
				<img src="resources/images/slider_2.jpg" alt="slider">
			</div>
			<!-----end item active------>

			<!-----end item ------>

		</div>
		<!----end carousel inner------>
	</div>
	<!---end carousel------>

	<div class="container">
		<div class="col-xs-12 col-sm-6  col-md-8 " style="height: 10em"></div>

		<!-------signup-------->
		<div class="col-xs-12 col-sm-6  col-md-4 "
			style="background: #FFFFFF; height: 518px;" id="signup">

			<h3 style="color: #7e7e7e;">Sign Up</h3>
			<hr style="border: solid .1px #949494;">

			<form id="signup" action="Register" method="post">
				<p class="align-center">${msg}</p>
				<div class="group">
					<input type="text" name="firstName" value="${user.firstName}"
						required>
					<p class="error">${error.firstName}</p>
					<span class="highlight"></span> <label>First Name</label>
				</div>

				<div class="group">
					<input type="text" name="lastName" value="${user.lastName}"
						required>
					<p class="error">${error.lastName}</p>
					<span class="highlight"></span> <label>Last Name</label>
				</div>

				<div class="group">

					<input type=password name="password" required>
					<p class="error">${error.password}</p>
					<span class="highlight"></span> <label>Password</label>
				</div>

				<div class="group">
					<input type="password" name="confirmPassword" required>
					<p class="error">${error.confirmPassword}</p>
					<span class="highlight"></span> <label>Confirm Password</label>
				</div>

				<div class="group">
					<input type="text" name="contactNo" value="${user.contactNo}"
						required> <span class="highlight"></span>
					<p class="error">${error.contactNo}</p>
					<label>Mobile Number</label>
				</div>

				<div class="group">
					<input type="email" name="email" value="${user.email}" required>
					<span class="highlight"></span>
					<p class="error">${error.email}</p>
					<label>Email Address</label>
				</div>

				<input type="checkbox" class="check" value="termsConditions"  id="termsConditions" onclick="enableButton('registerButton');" 
					style="width: 10%; text-align: left; margin-top: 20px; float: left; margin-right:;">
				<p style="font-size: 12px; margin-top: 12px; margin-bottom: -22px;">
					By clicking submit i hereby agree all the <a href="<c:url  value='/Terms&Conditions'/>">terms and
						conditions</a>.
				</p>
				<br>

				<button type="submit" class="btn disabled" id="registerButton"
					style="margin-top: 12px; margin-bottom: 2%; margin-left: 8%; background: #ec2029; color: #FFFFFF; width: 80%;">Submit</button>

			</form>
			</ul>
		</div>
		<!----end col----->
	</div>
	<!-------end container------------>
	<!-----modal after Registration Successful -->
	<div id="regMessage" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h5>${msg}</h5>
				</div>
				<div class="modal-body">
				<form action="${pageContext.request.contextPath}/Activate/Mobile"
			method="post">
			<div class="group_1">
				<input type="text" name="key"  required="required">
					 <span class="highlight"></span> <label>OTP</label>
			</div>
			<div class="group_1">
				<input type="hidden" name="mobileNumber" class="form-control input-sm"
					value="${mobileNumber}" 
					required="required"  />
			</div>
			<div class="col-md-6"
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
			</div>
		</div>
	</div>
	<!-- modal after successful verification -->
	<div id="verifiedMessage" role="dialog" class="modal fade">
		<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>
					<div class="modal-body">
							<div class="alert alert-success">${msg}</div>
					</div>
				</div>
		</div>
	</div>
<!-- modal for forgot password -->
<div class="modal fade" role="dialog" id="forgotPassword">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" data-dismiss="modal" class="close">&times;</button>
				<h5>Forgot Password</h5>
			</div>
			<div class="modal-body">
				
				<div class="group_1">
					<input type="text" name="username"  id="fp_username"
						 required="required" autofocus>
						 <span class="highlight"></span> <label>Mobile Number</label>
				</div>
				<div class="col-md-6"
					style="float: none; margin-left: auto; margin-right: auto;">
					<button class="btn btn-primary btn-sm btn-block btncu" id="fp_submit" type="submit" id="fp_button"
						style="margin-bottom: 5px" onclick="processForgotPassword()">Continue</button>
						</div>
				
			</div>
			<div class="modal-footer">
					<center  id="fp_message"></center>
			</div>
		</div>
	</div>
</div>
<!-- modal OTP for forget password -->
<div class="modal fade" role="dialog" id="fpOTP">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
							<button type="button" data-dismiss="modal" class="close">&times;</button>
			</div>
			<div class="modal-body">
				<div class="group_1">
					<input type="text" name=key id="fpOTP_key" autofocus/>
					<span class="highlight"></span><label>OTP</label>
				</div>
				<button class="btn btn-primary btn-sm  btncu" id="fpOTP_submit" type="submit" 
						style="margin-bottom: 5px" onclick="processForgotPasswordOTP()">Continue</button>
				
			</div>
			<div class="modal-footer">
				<center id="fpOTP_message"></center>
			</div>
		
		</div>
	</div>

</div>

<!-- renew password modal -->
<div class="modal fade" role="dialog" id="changePassword">
	<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
							<button type="button" data-dismiss="modal" class="close">&times;</button>
				</div>
				<input type="hidden" id="change_password_username"/>
				<input type="hidden" id="change_password_key" />
				<div class="modal-body">
					<div class="group_1">
						<input type="password" id="new_password" />
						<span class="highlight"></span><label>New Password</label>
					</div>
					<div class="group_1">
						<input type="password" id="confirm_password"/>
						<span class="highlight"></span><label>Confirm Password</label>
					</div>
				<button class="btn btn-primary   btncu" id="changePassword_submit" type="submit" 
						style="margin-bottom: 5px" onclick="renewPassword()">Update Password</button>
				</div>
				<div class="modal-footer">
					<center id="changePassword_message"></center>
				</div>
			
			</div>
	</div>
</div>
<div id="successNotification" class="modal fade" role="dialog">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
							<button type="button" data-dismiss="modal" class="close">&times;</button>
		</div>
		<div class="modal-body">
			<div class="alert alert-success" id="success_alert"></div>
		</div>
	</div>
</div>

</div>
	<footer class="footer" style="margin-top: 20px;">
		<div class="container">
			<div class="row" style="margin-top: 8px;">
				<a href="AboutUs">About Us</a> <a href="PartnersWithUs">Partner
					with us</a> <a href="Terms&Conditions">Terms & Conditions</a> <a
					href="#">Customer service</a> <a href="Grievance">Grievance
					policy</a> <a href="#">Recharge Partners</a>
			</div>
		</div>
	</footer>

	<!----------end footer---------->

	<script src="http://code.jquery.com/jquery-2.2.1.min.js"></script>
	<!-- Latest compiled and minified JavaScript -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
		integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
		crossorigin="anonymous"></script>
</body>
</html>
<script type="text/javascript"
	src='<c:url value="/resources/js/wow.js"/>' /></script>



<!-- ========================================================================================================================== -->