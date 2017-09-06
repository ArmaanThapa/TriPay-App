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

<%-- <link
	href="${pageContext.request.contextPath}/resources/css/jquery.dataTables.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/Style.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/css/lightbox.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/css/fotorama.css"
	rel="stylesheet" type="text/css" />
--%>

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

</head>

<body
	onLoad="ActiveMenu('MobileTopup');ActiveSubmenu('${menu}');ActiveFadeIn('${menu}');">
	<jsp:include page="/WEB-INF/jsp/User/Header.jsp" />
	<jsp:include page="/WEB-INF/jsp/User/Menu.jsp" />

	<!-----------------end navbar---------------------->

	<!------------- end main-------------------->

	   <div class="background"></div><!---blue box---->

       <div class="container" id="aboutus">
            <div class="row" style="background: url(img/bg.jpg) no-repeat;"> 
                        <div class="container" id="Account">
								<div class="col-md-2"></div>
	<div class="col-md-8">
		<div class="form-group">
		<h1>Load Money</h1>
		<hr>
			<p class="error">${msg}</p>
		</div>
		<form method="post" name="customerData" action="<c:url value='/User/LoadMoney/Process' />" class="form-inline">
		
			<div class="form-group">
				<div class="input-group">
					<div class="input-group-addon">Rs.</div>
					<input type="number" class="form-control" name="amount"
						value="" placeholder="<spring:message code="page.user.loadMoney.form.input.amount"/>" maxlength="5" size="10" required/>
				</div>
				<p>${error.amount}</p>
			</div>
			<!-- Button -->
			<div class="form-group">
				<div class="col-sm-offset-2">
					<input type="submit" value="<spring:message code="page.user.loadMoney.form.button"/>" class="btn btn-primary btn-md" style="
    width: 100px;     margin-top: -10px; ">
				</div>
			</div>
			<!-- HIDDEN REQUIRED BASIC PARAMETERS -->
			<div id="hidden_fields">
			<input type="hidden" name="tid" id="tid" readonly
				class="form-control" /> <input type="hidden" name="merchantId"
				id="merchant_id" value="47281" class="form-control" /> <input
				type="hidden" name="orderId" value="793732332" class="form-control" />
			<input type="hidden" name="currency" value="INR" class="form-control" />
			<input type="hidden" name="encryptedResponse" value="" class="form-control" />
			<input type="hidden" name="redirectURL"
				value="http://fgmtest.firstglobalmoney.com:8034/User/LoadMoney/Redirect"
				class="form-control" /> <input type="hidden" name="cancelURL"
				value="http://fgmtest.firstglobalmoney.com:8034/User/LoadMoney/Cancel"
				class="form-control" /> <input type="hidden" name="language"
				id="language" class="form-control" value="EN" />
			<div class="panel-group" id="accordion" role="tablist"
				aria-multiselectable="true">
			
				
						<div class="panel-body">
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="billingName" value="${user.userDetail.firstName}"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="billingAddress" value="Santacruz"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="billingCity" value="Mumbai"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="billingState" value="MH"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="billingZip" value="400054"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="billingCountry" value="India"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="billingTel" value="${user.userDetail.contactNo}"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="billingEmail"
										value="${user.userDetail.email }" class="form-control" />
								</div>
							</div>
						</div>
					
				
				
						<div class="panel-body">
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="deliveryName" value="Sam"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="deliveryAddress" value="Vile Parle"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="deliveryCity" value="Mumbai"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="deliveryState" value="Maharashtra"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="deliveryZip" value="400038"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="deliveryCountry" value="India"
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="deliveryTel" value="0221234321"
										class="form-control" />
								</div>
							</div>
						</div>
				
					
					<div id="collapseThree" class="panel-collapse collapse"
						role="tabpanel" aria-labelledby="headingThree">
						<div class="panel-body">
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="merchantParam1"
										value="additional Info." class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="merchantParam2"
										value="additional Info." class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="merchantParam3"
										value="additional Info." class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="merchantParam4"
										value="additional Info." class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="merchantParam5" class="form-control"
										value="additional Info." />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="promoCode" value=""
										class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-10">
									<input type="hidden" name="customerIdentifier" value="${user.username}"
										class="form-control" />
								</div>
							</div>
						</div>
					</div>
				
			</div>
			</div>
			

		</form>
	</div>
	<div class="col-md-2"></div>									
                            </div><!--div account-->
                       </div><!---end row --->
                 </div><!---- end aboutus-->	    
       
	
	
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


<!-- ========================================================================================================================== -->

 