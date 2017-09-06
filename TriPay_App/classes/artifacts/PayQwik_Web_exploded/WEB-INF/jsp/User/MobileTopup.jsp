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
<title>PayQwik</title>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/header.js"></script>
<link rel="icon" href='<c:url value="/resources/images/favicon.png"/>' type="image/png" />
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
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/r/bs-3.3.5/jq-2.1.4,dt-1.10.8/datatables.min.css"/> 
		<script type="text/javascript" src="https://cdn.datatables.net/r/bs-3.3.5/jqc-1.11.3,dt-1.10.8/datatables.min.js"></script>
</head>

<body
	onLoad="ActiveMenu('MobileTopup');ActiveSubmenu('${menu}');ActiveFadeIn('${menu}');SelectOperator();">
	<jsp:include page="/WEB-INF/jsp/User/Header.jsp" />
	<jsp:include page="/WEB-INF/jsp/User/Menu.jsp" />

	<!-----------------end navbar---------------------->

	<!------------- end main-------------------->


	<div class="background"></div>
	<!---blue box---->

	<div class="container" id="box">
		<div class="row">

			<div class="col-md-4" id="Prepaid">
				<!---form---->
				<div class="box hidden-xs"></div>

				<ul class="nav nav-pills">
					<li id="PrepaidSubMenu"><a data-toggle="pill"
						href="#PrepaidFadeIn">Prepaid</a></li>
					<li id="PostpaidSubMenu"><a data-toggle="pill"
						href="#PostpaidFadeIn">Postpaid</a></li>
					<li id="DataCardSubMenu"><a data-toggle="pill"
						href="#DataCardFadeIn">DataCard</a></li>
				</ul>

				<div class="tab-content">
					<div id="PrepaidFadeIn" class="tab-pane fade">
						<p class="error">${msg}</p>
						<p class="error">${trerror.message}</p>

						<form method="post"
							action="${pageContext.request.contextPath}/User/MobileTopup/ProcessPrepaid">
							<input type="text" name="topupType" value="Prepaid"
								hidden="hidden">

							<div class="group_1">
								<input type="text" name="mobileNo" id="mobile"
									value="${preValue.mobileNo}" required> <span
									class="highlight"></span>
								<p class="error">${preerror.mobileNo}</p>
								<label>Prepaid Mobile No</label>
							</div>
							
							<div class="group_1">
								<select name="serviceProvider" id="operator"
									class="form-control" style="width: 86%;background: #EDEDED;background: transparent;border: transparent;border-bottom: gray;border-style: solid;border-width: 1.8px; font-family: 'Ubuntu', sans-serif; padding-left: 0; border-top: transparent; height: 55px; margin-top: -19px; padding-bottom:-20px;  font-weight: bold; color: #928F8F;">
									<option value="">--select service provider--</option>
									<option value="ACP">Aircel</option>
									<option value="ATP">Airtel</option>
									<option value="BVP">BSNL - Special Tariff</option>
									<option value="BGP">BSNL</option>
									<option value="IDP">Idea</option>
									<option value="MSP">MTNL - Special Tariff</option>
									<option value="MMP">MTNL</option>
									<option value="MTP">MTS</option>
									<option value="RGP">Reliance</option>
									<option value="TVP">T24 Mobile - Special Tariff</option>
									<option value="TMP">T24 Mobile</option>
									<option value="TCP">Tata Docomo CDMA</option>
									<option value="TSP">Tata Docomo GSM - Special Tariff</option>
									<option value="TGP">Tata Docomo GSM</option>
									<option value="USP">Telenor - Special Tariff</option>
									<option value="UGP">Telenor</option>
									<option value="VSP">Videocon - Special Tariff</option>
									<option value="VGP">Videocon</option>
									<option value="VFP">Vodafone</option>
								</select>
								<p>${preerror.serviceProvider}</p>
							</div>
							
							<div class="group_1">
								<select name="area" id="circle" class="form-control" style="width: 86%;background: #EDEDED;background: transparent;border: transparent;border-bottom: gray;border-style: solid;border-width: 1.8px; font-family: 'Ubuntu', sans-serif; padding-left: 0; border-top: transparent; height: 55px; margin-top: -19px; padding-bottom:-20px;  font-weight: bold; color: #928F8F;">
									<option value="">--SELECT YOUR AREA--</option>
									<option value="AP">Andhra Pradesh</option>
									<option value="AS">Assam</option>
									<option value="BR">Bihar and Jharkhand</option>
									<option value="CH">Chennai</option>
									<option value="DL">Delhi</option>
									<option value="GJ">Gujarat</option>
									<option value="HR">Haryana</option>
									<option value="HP">Himachal Pradesh</option>
									<option value="JK">Jammu and Kashmir</option>
									<option value="KN">Karnataka</option>
									<option value="KL">Kerala</option>
									<option value="KO">Kolkata</option>
									<option value="MP">Madhya Pradesh/Chattisgarh</option>
									<option value="MH">Maharashtra</option>
									<option value="MU">Mumbai</option>
									<option value="NE">North East</option>
									<option value="OR">Orissa</option>
									<option value="PB">Punjab</option>
									<option value="RJ">Rajasthan</option>
									<option value="TN">Tamil Nadu</option>
									<option value="UW">Uttar Pradesh(W)/Uttranchal</option>
									<option value="UE">Uttar Pradesh(E)</option>
									<option value="WB">West Bengal</option>
								</select>

								<div id="plan_link"></div>
								<p>${preerror.area}</p>
							</div>
							<div class="group_1">
								<input type="number" id="pre_amount" name="amount" min="10"
									value="${preValue.amount}" required> <span
									class="highlight"></span> <span class="bar"></span> <label>Enter
									Amount</label>
							</div>

							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; margin-top: 10px; color: #FFFFFF;">Pay</button>
						</form>
					</div>
				`	
					<div id="PostpaidFadeIn" class="tab-pane fade">
						<form method="post"	action="${pageContext.request.contextPath}/User/MobileTopup/ProcessPostpaid">
							<p class="error">${msg}</p>
							<p class="error">${trerror.message}</p>
							<input type="text" name="topupType" value="Postpaid"
								hidden="hidden">

							<div class="group_1">
								<input type="text" name="mobileNo" value="${postValue.mobileNo}"
									required> <span class="highlight"></span>
								<p class="error">${posterror.mobileNo}</p>
								<label>Postpaid Mobile No</label>
							</div>

							<div class="group_1">
								<select name="serviceProvider" class="form-control"
									style="width: 86%; background: #EDEDED;">
									<option value="">--select service provider--</option>
									<option value="ACC">Aircel</option>
									<option value="ATC">Airtel</option>
									<option value="BGC">BSNL</option>
									<option value="IDC">Idea</option>
									<option value="MTC">MTS</option>
									<option value="RGC">Reliance</option>
									<option value="TDC">Tata Docomo</option>
									<option value="VFC">Vodafone</option>
								</select>
								<p>${posterror.serviceProvider}</p>
							</div>

							<div class="group_1">
								<select name="serviceProvider" class="form-control"
									style="width: 86%; background: #EDEDED;">
									<option value="">--select service provider--</option>
									<option value="ACP">Aircel</option>
									<option value="ATP">Airtel</option>
									<option value="BVP">BSNL - Special Tariff</option>
									<option value="BGP">BSNL - Talktime</option>
									<option value="IDP">Idea</option>
									<option value="MSP">MTNL - Special Tariff</option>
									<option value="MMP">MTNL - Talktime</option>
									<option value="MTP">MTS</option>
									<option value="RGP">Reliance</option>
									<option value="TVP">T24 Mobile - Special Tariff</option>
									<option value="TMP">T24 Mobile - Talktime</option>
									<option value="TCP">Tata Docomo CDMA</option>
									<option value="TSP">Tata Docomo GSM - Special Tariff</option>
									<option value="TGP">Tata Docomo GSM - Talktime</option>
									<option value="USP">Telenor - Special Tariff</option>
									<option value="UGP">Telenor - Talktime</option>
									<option value="VSP">Videocon - Special Tariff</option>
									<option value="VGP">Videocon - Talktime</option>
									<option value="VFP">Vodafone</option>
								</select>
								<p>${dcerror.area}</p>
							</div>

							<div class="group_1">
								<input type="number" name="amount" min="10"
									value="${postValue.amount}" required> <span
									class="highlight"></span> <label>Enter Amount</label>
								<p>${posterror.amount}</p>
							</div>
							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; margin-top: 10px; color: #FFFFFF;">Pay</button>
						</form>
					</div>
					<div id="DataCardFadeIn" class="tab-pane fade">

						<form method="post"
							action="${pageContext.request.contextPath}/User/MobileTopup/ProcessDataCard">
							<p class="error">${msg}</p>
							<p class="error">${trerror.message}</p>

							<!--  <div class="group_1" style="font-family: 'Ubuntu', sans-serif; margin-left: -40px; border-bottom: thick #000000;"> -->

							<div class="group_1">
								<input type="text" name="mobileNo" value="${preValue.mobileNo}"
									required> <span class="highlight"></span> <span
									class="bar"></span> <label>Customer ID</label>
							</div>
							<div class="group_1">
								<select name="serviceProvider" class="form-control"
									style="width: 86%; background: #EDEDED;">
									<option value="">--select service provider--</option>
									<option value="ACC">Aircel</option>
									<option value="ATC">Airtel</option>
									<option value="BGC">BSNL</option>
									<option value="IDC">Idea</option>
									<option value="MTC">MTS</option>
									<option value="RGC">Reliance</option>
									<option value="TDC">Tata Docomo</option>
									<option value="VFC">Vodafone</option>
								</select>
								<p>${posterror.serviceProvider}</p>
							</div>

							<div class="group_1">
								<select name="serviceProvider" class="form-control"
									style="width: 86%; background: #EDEDED;">
									<option value="">--select service provider--</option>
									<option value="1">Andhra Pradesh</option>
									<option value="2">Arunachal Pradesh</option>
									<option value="3">Assam</option>
									<option value="4">Bihar</option>
									<option value="5">Chhattisgarh</option>
									<option value="6">Goa</option>
									<option value="7">Gujarat</option>
									<option value="8">Haryana</option>
									<option value="9">Himachal Pradesh</option>
									<option value="10">Jammu and Kashmir</option>
									<option value="11">Jharkhand</option>
									<option value="12">Karnataka</option>
									<option value="13">Kerala</option>
									<option value="14">Madhya Pradesh</option>
									<option value="15">Maharashtra</option>
									<option value="16">Manipur</option>
									<option value="17">Meghalaya</option>
									<option value="18">Mizoram</option>
									<option value="19">Nagaland</option>
									<option value="20">Orissa</option>
									<option value="21">Punjab</option>
									<option value="22">Rajasthan</option>
									<option value="23">Sikkim</option>
									<option value="24">Tamil Nadu</option>
									<option value="25">Tripura</option>
									<option value="26">Uttrakhand</option>
									<option value="27">Uttar Pradesh</option>
									<option value="28">West Bengal</option>
								</select>
								<p>${dcerror.area}</p>
							</div>

							<div class="group_1">
								<input type="number" name="amount" min="10"
									value="${preValue.amount}" required> <span
									class="highlight"></span> <span class="bar"></span> <label>Enter
									Amount</label>
								<p>${preerror.amount}</p>
							</div>

							<button type="submit" class="btn"
								style="width: 80%; background: #ff0000; margin-top: 10px; color: #FFFFFF;">Pay</button>
						</form>
					</div>
				</div>

			</div>
			<!----end col-md-4-->

	
			<div class="col-md-8 hidden-xs">
				<div class="slider"  id="slider" style="margin-right: -15px; margin-left: -15px;">
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
				<table class="table table-fixed" id="plan_table">
			
				</table>
			</div>
		</div>
		<!-----end col-md-8-->
		</div>
		<!---end row-->
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



