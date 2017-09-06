<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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

<link href='<c:url value="/resources/css/css_style.css"/>'
	rel='stylesheet' type='text/css'>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/modernizr/2.8.2/modernizr.js"></script>
<script src="<c:url value='/resources/js/spinner.js'/>"></script>
</head>

<body
	onLoad="ActiveMenu('MobileTopup');ActiveSubmenu('${menu}');ActiveFadeIn('${menu}');browsePic();">

	<jsp:include page="/WEB-INF/jsp/User/Header.jsp" />
	<jsp:include page="/WEB-INF/jsp/User/Menu.jsp" />

	<!-----------------end navbar---------------------->

	<!------------- end main-------------------->

	<div class="background"></div>
	<!---blue box---->

	<div class="container" id="aboutus">
		<div class="row"">
			<div class="container" id="Account">
				<div class="row" id="foto">
					<div class="col-xs-12 col-sm-6 text-center">
						<!-- Your foto -->
						<div class="foto">
							<a id="profilePic" href="#" data-toggle="modal"
								data-target="#profilePicture"> <c:if
									test="${user.userDetail.image ne null}">
									<img src="<c:url value='${user.userDetail.image}'/>"
										width="230px" height="230px" />
								</c:if> <img
								src="${pageContext.request.contextPath}/resources/images/sample.jpg"
								width="230px" height="230px" /> Change Image
							</a>
							<div id="profilePicture" class="modal fade" role="dialog">
								<div class="modal-dialog modal-sm">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal">&times;</button>
											<h5 class="modal-title">Upload Picture</h5>
										</div>
										<div class="modal-body">
											<form method="post"
												action="${pageContext.request.contextPath}/User/UploadPicture/Process"
												enctype="multipart/form-data" class="form form-inline">
												<input type="file" class="form-control"
													style="width: 86%; background: #EDEDED;"
													accept="image/jpeg" name="profilePicture" required />
												<button type="submit" class="btn btn-primary">Upload</button>

											</form>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- end your foto -->
						<hr style="width: 50%; margin-top: 20px;">
						<h1 class="title">Welcome</h1>
						<!-- Your Profession -->
						<h3 class="sub-title">${user.userDetail.firstName}
							${user.userDetail.middleName} ${user.userDetail.lastName}</h3>
					</div>

					<div class="col-md-5">
						<div>
							<div>
								<h4 class="">PROFILE DETAILS</h4>
								<p class="">Mobile Number : ${user.userDetail.contactNo}</p>
								<p class="">Email : ${user.userDetail.email}</p>
								<p class="">Address : ${user.userDetail.address }</p>
								<h4 class="">ACCOUNT DETAILS</h4>
								<p class="">Account Number :
									${user.accountDetail.accountNumber}</p>
								<p class="">Balance : ${user.accountDetail.balance}</p>
								<h4 class="">ACCOUNT TYPE</h4>
								<p class="">Name : ${user.accountDetail.accountType.name}</p>
								<p class="">Description :
									${user.accountDetail.accountType.name}</p>
								<p class="">Daily Transaction Limit : Rs.
									${user.accountDetail.accountType.dailyLimit}</p>
								<p class="">Monthly Transaction Limit : Rs.
									${user.accountDetail.accountType.monthlyLimit}</p>
							</div>
						</div>

					</div>
					<!---end col-md-5-->
				</div>
				<!---end foto-->
			</div>
			<!--div account-->
		</div>
		<!---end row --->
	</div>
	<!---- end aboutus-->



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


