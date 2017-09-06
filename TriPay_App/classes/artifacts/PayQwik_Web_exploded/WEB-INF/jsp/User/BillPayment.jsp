
<!-- ========================================================================================================================= -->

<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Bill Payment</title>
<link rel="icon" href='<c:url value="/resources/images/favicon.png"/>' type="image/png" />
<link rel="stylesheet" href="css_style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.1/animate.min.css">
<link href='https://fonts.googleapis.com/css?family=Ubuntu'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

<link href='<c:url value="/resources/css/style_main.css"/>'
	rel='stylesheet' type='text/css'>
<link href='<c:url value="/resources/css/css_style.css"/>'
	rel='stylesheet' type='text/css'>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/header.js"></script>

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
</head>

<body
	onLoad="ActiveMenu('BillPayment');ActiveSubmenu('${menu}');ActiveFadeIn('${menu}');">
	<jsp:include page="/WEB-INF/jsp/User/Header.jsp" />
	<jsp:include page="/WEB-INF/jsp/User/Menu.jsp" />

	<div class="background"></div>
	<!---blue box---->

	<div class="container" id="bill_box">
		<div class="row">

			<div class="col-md-6" id="Prepaid">
				<!---form---->
				<div class="box hidden-xs"></div>

				<ul class="nav nav-pills">
					<li id="DTHSubMenu"><a data-toggle="pill" href="#DTHFadeIn">DTH</a></li>
					<li id="LandlineSubMenu"><a data-toggle="pill"
						href="#LandlineFadeIn">Landline</a></li>
					<li id="ElectricitySubMenu"><a data-toggle="pill"
						href="#ElectricityFadeIn">Electricity</a></li>
					<li id="GasSubMenu"><a data-toggle="pill" href="#GasFadeIn">Gas</a></li>
					<li id="InsuranceSubMenu"><a data-toggle="pill"
						href="#InsuranceFadeIn">Insurance</a></li>
				</ul>

				<div class="tab-content" id="bill_payment">
					<div id="DTHFadeIn" class="tab-pane fade">
						<p class="error">${msg}</p>
						<p class="error">${trerror.message}</p>
						<form method="post"
							action="${pageContext.request.contextPath}/User/BillPayment/ProcessDTH">

							<div class="group_1">
								<select name="serviceProvider" class="form-control"
									style="width: 86%; background: #EDEDED;">
									<option value="">--select service provider--</option>
									<option value="ATV">Airtel Digital TV</option>
									<option value="DTV">Dish TV</option>
									<option value="RTV">Reliance Digital TV</option>
									<option value="STV">Sun Direct</option>
									<option value="TTV">Tata Sky</option>
									<option value="VTV">Videocon d2h</option>
								</select>
								<p class="error">${errordth.serviceProvider}</p>
							</div>

							<div class="group_1">
								<input type="text" name="dthNo" value="${dth.dthNo}"
									placeholder="<spring:message code="page.user.billPayment.form.input.dthNo"/>"
									required="required"> <span class="highlight"></span>
								<p class="error">${errordth.dthNo}</p>
							</div>

							<div class="group_1">
								<input type="number" name="amount" min="10"
									value="${dth.amount}"
									placeholder="<spring:message code="page.user.billPayment.form.input.amount"/>"
									required> <span class="highlight"></span> <span
									class="bar"></span>
							</div>
							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; margin-top: 10px; color: #FFFFFF;">Pay</button>
						</form>
					</div>

					<div id="LandlineFadeIn" class="tab-pane fade">
						<p class="error">${msg}</p>
						<p class="error">${trerror.message}</p>
						<form method="post"
							action="${pageContext.request.contextPath}/User/BillPayment/ProcessLandline">
							<div class="group_1">

								<select name="serviceProvider" class="form-control" id="landlineProviders"
									style="width: 86%; background: #EDEDED;">
									<option value="#">--select service provider--</option>
									<option value="ATL">Airtel</option>
									<option value="BGL">BSNL</option>
									<option value="MDL">MTNL - Delhi</option>
									<option value="RGL">Reliance</option>
									<option value="TCL">Tata Docomo</option>
						</select>
								<p class="error">${errorlandline.serviceProvider}</p>
							</div>

							<div class="group_1">
								<input type="text" name="stdCode" value="${landline.stdCode}"
									maxlength="4" required> <span class="highlight"></span>
								<span class="bar"></span>
								<p class="error">${errorlandline.stdCode}</p>
								<label>STD Code</label>

							</div>
							<div class="group_1">
								<input type="text" name="landlineNumber"
									value="${landline.landlineNumber}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p class="error">${errorlandline.landlineNumber}</p>
								<label>Landline No</label>

							</div>
							<div class="group_1">
								<input type="text" name="accountNumber"
									value="${landline.accountNumber}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p class="error">${errorlandline.accountNumber}</p>
								<label>Account No</label>
							</div>

							<div class="group_1">
								<input type="number" name="amount" min="10"
									value="${landline.amount}" required> <span
									class="highlight"></span> <span class="bar"></span> <label>Enter
									Amount</label>
							</div>
							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; margin-top: 10px; color: #FFFFFF;">Pay</button>
						</form>
					</div>
					<div id="ElectricityFadeIn" class="tab-pane fade">
						<p class="error">${msg}</p>
						<p class="error">${trerror.message}</p>
						<form method="post"
							action="${pageContext.request.contextPath}/User/BillPayment/ProcessElectricity">


							<div class="group_1">

								<select name="serviceProvider" class="form-control"
									style="width: 86%; background: #EDEDED;">
									<option value="#">--select service provider--</option>
									<option value="BRE">BSES Rajdhani - DELHI</option>
									<option value="BYE">BSES Yamuna - DELHI</option>
									<option value="REE">Reliance Energy - MUMBAI</option>
									<option value="NDE">Tata Power - DELHI</option>
								</select>
								<p class="error">${errorelectricity.serviceProvider}</p>
							</div>


							<div class="group_1">
								<input type="text" name="accountNumber"
									value="${electricity.accountNumber}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p class="error">${errorelectricity.accountNumber}</p>
								<label>Account No</label>
							</div>

							<div class="group_1">
								<input type="text" name="cycleNumber"
									value="${electricity.cycleNumber}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p class="error">${errorelectricity.cycleNumber}</p>
								<label>Cycle No</label>
							</div>

							<div class="group_1">
								<input type="number" name="amount" min="10"
									value="${electricity.amount}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p>${errorelectricity.amount}</p>
								<label>Enter Amount</label>
							</div>
							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; margin-top: 10px; color: #FFFFFF;">Pay</button>
						</form>
					</div>

					<div id="GasFadeIn" class="tab-pane fade">
						<p class="error">${msg}</p>
						<p class="error">${trerror.message}</p>
						<form method="post"
							action="${pageContext.request.contextPath}/User/BillPayment/ProcessGas">
							<div class="group_1">

								<select name="serviceProvider" class="form-control"
									style="width: 86%; background: #EDEDED;">
									<option value="#">--select Gas provider--</option>
									<option value="IPG">Indraprastha Gas</option>
									<option value="MMG">Mahanagar Gas</option>
								</select>
								<p class="error">${errorgas.serviceProvider}</p>
							</div>

							<div class="group_1">
								<input type="text" name="accountNumber"
									value="${gas.accountNumber}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p class="error">${errorgas.accountNumber}</p>
								<label>Account No</label>
							</div>

							<div class="group_1">
								<input type="number" name="amount" min="10"
									value="${electricity.amount}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p>${errorelectricity.amount}</p>
								<label>Enter Amount</label>
							</div>
							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; margin-top: 10px; color: #FFFFFF;">Pay</button>
						</form>
					</div>

					<div id="InsuranceFadeIn" class="tab-pane fade">
						<p class="error">${msg}</p>
						<p class="error">${trerror.message}</p>
						<form method="post"
							action="${pageContext.request.contextPath}/User/BillPayment/ProcessInsurance">
							<div class="group_1">

								<select name="serviceProvider" class="form-control"
									style="width: 86%; background: #EDEDED;">
									<option value="#">--select Insurance provider--</option>
									<option value="IPI">ICICI Prudential Life Insurance</option>
									<option value="TAI">Tata AIA Life Insurance</option>
								</select>
								<p class="error">${errorinsurance.serviceProvider}</p>
							</div>

							<div class="group_1">
								<input type="text" name="policyNumber"
									value="${insurance.policyNumber}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p class="error">${errorinsurance.policyNumber}</p>
								<label>Policy No</label>
							</div>

							<div class="group_1">
								<input type="date" name="policyDate"
									value="${insurance.policyDate}" required="required">
								<p class="error">${errorinsurance.policyDate}</p>
							</div>

							<div class="group_1">
								<input type="number" name="amount" min="10"
									value="${insurance.amount}" required> <span
									class="highlight"></span> <span class="bar"></span>
								<p>${errorinsurance.amount}</p>
								<label>Enter Amount</label>
							</div>
							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; margin-top: 10px; color: #FFFFFF;">Pay</button>
						</form>
					</div>
				</div>

			</div>
			<!----end col-md-4-->

			<!--<div class="col-md-6 hidden-xs">
             <div class="slider" style="margin-right:-15px; margin-left: -15px;">
            	<div class="carousel slide hidden-xs" data-ride="carousel" id="mycarousel">
                       <ol class="carousel-indicators">
                           <li class="active" data-slide-to="0" data-target="#mycarousel"></li>
                           <li data-slide-to="1" data-target="#mycarousel"></li>
                       </ol>
                    
                    	<div class="carousel-inner">
                        	
                            <div class="item active" id="slide1">
                            </div><!---end item---->

			<div class="item"></div>
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
	</div>
	<!----end container-->




	<jsp:include page="/WEB-INF/jsp/User/Footer.jsp" />

	<script src="http://code.jquery.com/jquery-2.2.1.min.js"></script>
	<script type="text/javascript"
		src='<c:url value="/resources/js/wow.js"/>' /></script>
	<!-- <script>
		new WOW().init();
	</script> -->
	<!-- Latest compiled and minified JavaScript -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
		integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
		crossorigin="anonymous"></script>
</body>
</html>