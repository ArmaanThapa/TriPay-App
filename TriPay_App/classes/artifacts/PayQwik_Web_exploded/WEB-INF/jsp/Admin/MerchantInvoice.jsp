<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>PayQwik</title>
<!-- Bootstrap core CSS -->
<link rel="icon" href='<c:url value="/resources/images/favicon.png"/>' type="image/png" />

<link
	href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/admin/fonts/css/font-awesome.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/admin/css/animate.min.css"
	rel="stylesheet">

<!-- Custom styling plus plugins -->
<link
	href="${pageContext.request.contextPath}/resources/admin/css/custom.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/admin/css/icheck/flat/green.css"
	rel="stylesheet">
<script
	src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js"></script>

<!--[if lt IE 9]>
        <script src="../assets/js/ie8-responsive-file-warning.js"></script>
        <![endif]-->

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
</head>
<body class="nav-md">
	<div class="container body">
		<div class="main_container">
			<div class="col-md-3 left_col">
				<div class="left_col scroll-view">
					<div class="navbar nav_title" style="border: 0;">
						<a href="Home" class="site_title"><i class="fa fa-paw"></i> <span>PayQwik</span></a>
					</div>
					<div class="clearfix"></div>
					<!-- menu prile quick info -->
					<div class="profile">
						<div class="profile_pic">
							<img
								src="${pageContext.request.contextPath}/resources/admin/images/pic.png"
								alt="..." class="img-circle profile_img">
						</div>
						<div class="profile_info">
							<span>Welcome,</span>
							<h2>${dto.userName}</h2>
						</div>
					</div>
					<!-- /menu prile quick info -->
					<br />
					<!-- sidebar menu -->
					<div id="sidebar-menu"
						class="main_menu_side hidden-print main_menu">
						<div class="menu_section">
							<h3>General</h3>
							<ul class="nav side-menu">
								<li><a><i class="fa fa-home"></i> Users <span
										class="fa fa-chevron-down"></span></a>
									<ul class="nav child_menu" style="display: none">
										<li><a href="UserList">All</a></li>
										<li><a href="VerifiedUsers">Verified</a></li>
										<li><a href="UnverifiedUsers">Unverified</a></li>
										<li><a href="BlockedUsers">Blocked</a></li>
										<li><a href="ActiveUsers">Online</a></li>
									</ul></li>
								<li><a><i class="fa fa-edit"></i> Reports <span
										class="fa fa-chevron-down"></span></a>
									<ul class="nav child_menu" style="display: none">
										<li><a href="DailyReport">Daily Reports</a></li>
										<li><a href="MonthlyReport">Monthly Reports</a></li>
									</ul></li>
								<li><a><i class="fa fa-desktop"></i> Logs <span
										class="fa fa-chevron-down"></span></a>
									<ul class="nav child_menu" style="display: none">
										<li><a href="SMSLog">Message Logs</a></li>
										<li><a href="EmailLog">Email Logs</a></li>
									</ul></li>
							</ul>

						</div>
						<div class="menu_section">
							<h3>Others</h3>
							<ul class="nav side-menu">
								<li><a><i class="fa fa-bug"></i> GCM Notification </a></li>
								<li><a><i class="fa fa-windows"></i> Service <span
										class="fa fa-chevron-down"></span></a>
									<ul class="nav child_menu" style="display: none">
										<li><a href="#">Email</a></li>
										<li><a href="#">Bulk Email</a></li>
										<li><a href="SendPromotionalSMS">SMS</a></li>
										<li><a href="#">Bulk SMS</a></li>
									</ul></li>
								<li><a><i class="fa fa-bar-chart-o"></i> Merchant
										Service <span class="fa fa-chevron-down"></span></a>
									<ul class="nav child_menu" style="display: none">
										<li><a href="ListMerchant">Merchant List</a></li>
										<li><a href="AddMerchant">Add Merchant</a></li>

									</ul></li>
							</ul>
						</div>
					</div>
					<!-- /sidebar menu -->
					<!-- /menu footer buttons -->
					<div class="sidebar-footer hidden-small">
						<a data-toggle="tooltip" data-placement="top" title="Settings">
							<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
						</a> <a data-toggle="tooltip" data-placement="top" title="FullScreen">
							<span class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
						</a> <a data-toggle="tooltip" data-placement="top" title="Lock"> <span
							class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
						</a> <a data-toggle="tooltip" data-placement="top" title="Logout">
							<span class="glyphicon glyphicon-off" aria-hidden="true"></span>
						</a>
					</div>
					<!-- /menu footer buttons -->
				</div>
			</div>
			<!-- top navigation -->
			<div class="top_nav">
				<div class="nav_menu">
					<nav class="" role="navigation">
						<div class="nav toggle">
							<a id="menu_toggle"><i class="fa fa-bars"></i></a>
						</div>
						<ul class="nav navbar-nav navbar-right">
							<li class=""><a href="javascript:;"
								class="user-profile dropdown-toggle" data-toggle="dropdown"
								aria-expanded="false"> <img
									src="${pageContext.request.contextPath}/resources/admin/images/pic.png"
									alt="">PayQwik <span class=" fa fa-angle-down"></span>
							</a>
								<ul
									class="dropdown-menu dropdown-usermenu animated fadeInDown pull-right">
									<li><a href="javascript:;"> Profile</a></li>
									<li><a href="javascript:;"> <span
											class="badge bg-red pull-right">50%</span> <span>Settings</span>
									</a></li>
									<li><a href="javascript:;">Help</a></li>
									<li><a href="login.html"><i
											class="fa fa-sign-out pull-right"></i> Log Out</a></li>
								</ul></li>
							<!-- <li role="presentation" class="dropdown"><a
								href="javascript:;" class="dropdown-toggle info-number"
								data-toggle="dropdown" aria-expanded="false"> <i
									class="fa fa-envelope-o"></i> <span class="badge bg-green">6</span>
							</a>
								<ul id="menu1"
									class="dropdown-menu list-unstyled msg_list animated fadeInDown"
									role="menu">
									<li><a> <span class="image"> <img
												src="images/img.jpg" alt="Profile Image" />
										</span> <span> <span>John Smith</span> <span class="time">3
													mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"> <img
												src="images/img.jpg" alt="Profile Image" />
										</span> <span> <span>John Smith</span> <span class="time">3
													mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"> <img
												src="images/img.jpg" alt="Profile Image" />
										</span> <span> <span>John Smith</span> <span class="time">3
													mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"> <img
												src="images/img.jpg" alt="Profile Image" />
										</span> <span> <span>John Smith</span> <span class="time">3
													mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li>
										<div class="text-center">
											<a> <strong>See All Alerts</strong> <i
												class="fa fa-angle-right"></i>
											</a>
										</div>
									</li>
								</ul></li> -->
						</ul>
					</nav>
				</div>
			</div>
			<div class="right_col" role="main">

				<div class="">
					<!-- <div class="page-title">
						<div class="title_right">
							
						</div>
					</div> -->
					<div class="clearfix"></div>
					<div class="row">
						<div class="col-md-12">
							<div class="page-title">
								<div class="x_title">
									<h2>
										Merchant Invoice Design <small></small>
									</h2>
									<!-- <ul class="nav navbar-right panel_toolbox">
										<li><a class="collapse-link"><i
												class="fa fa-chevron-up"></i></a></li>
										<li class="dropdown"><a href="#" class="dropdown-toggle"
											data-toggle="dropdown" role="button" aria-expanded="false"><i
												class="fa fa-wrench"></i></a>
											<ul class="dropdown-menu" role="menu">
												<li><a href="#">Settings 1</a></li>
												<li><a href="#">Settings 2</a></li>
											</ul></li>
										<li><a class="close-link"><i class="fa fa-close"></i></a>
										</li>
									</ul> -->
									<div class="clearfix"></div>
								</div>
								<div class="x_content">

									<section class="content invoice">
										<!-- title row -->
										<div class="row">
											<div class="col-xs-12 invoice-header">
												<h1>
													<i class="fa fa-globe"></i> Invoice. <small
														class="pull-right">Date: <%
														Date date = new Date();
														out.print("<h6 align=\"center\">" + date.toString() + "</h6>");
													%></small>
												</h1>
											</div>
											<!-- /.col -->
										</div>
										<!-- info row -->
										<div class="row invoice-info">
											<div class="col-sm-4 invoice-col">
												From
												<address>
													<strong>PAYQWIK SOLUTION</strong> <br>106, 4th Cross <br>Koramangala,
													Bengaluru <br>Phone: 080 2553 5857 <br>Email:
													care@payqwik.in
												</address>
											</div>
											<!-- /.col -->
											<div class="col-sm-4 invoice-col">
												To
												<address>
													<strong>Asiadedu</strong> <br>Noida Sector-5 <br>U.P
													<br>Phone: 1234567890 <br>Email: asiad@gmail.com
												</address>
											</div>
											<!-- /.col -->
											<div class="col-sm-4 invoice-col">
												<b>Invoice #007612</b> <br> <br> <b>Order ID:</b>
												4F3S8J <br> <b>Payment Due:</b> 2/22/2014 <br> <b>Account:</b>
												968-34567
											</div>
											<!-- /.col -->
										</div>
										<!-- /.row -->

										<!-- Table row -->
										<div class="row">
											<div class="col-xs-12 table">
												<table class="table table-striped">
													<thead>
														<tr>
															<th>Qty</th>
															<th>Product</th>
															<th>Serial #</th>
															<th style="width: 59%">Description</th>
															<th>Subtotal</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td>1</td>
															<td>Call of Duty</td>
															<td>455-981-221</td>
															<td>El snort testosterone trophy driving gloves
																handsome gerry Richardson helvetica tousled street art
																master testosterone trophy driving gloves handsome gerry
																Richardson</td>
															<td>64.50 /- Rs.</td>
														</tr>
														<tr>
															<td>1</td>
															<td>Need for Speed IV</td>
															<td>247-925-726</td>
															<td>Wes Anderson umami biodiesel</td>
															<td>50.00 /- Rs.</td>
														</tr>
														<tr>
															<td>1</td>
															<td>Monsters DVD</td>
															<td>735-845-642</td>
															<td>Terry Richardson helvetica tousled street art
																master, El snort testosterone trophy driving gloves
																handsome letterpress erry Richardson helvetica tousled</td>
															<td>10.70 /- Rs.</td>
														</tr>
														<tr>
															<td>1</td>
															<td>Grown Ups Blue Ray</td>
															<td>422-568-642</td>
															<td>Tousled lomo letterpress erry Richardson
																helvetica tousled street art master helvetica tousled
																street art master, El snort testosterone</td>
															<td>25.99 /- Rs.</td>
														</tr>
													</tbody>
												</table>
											</div>
											<!-- /.col -->
										</div>
										<!-- /.row -->

										<div class="row">
											<!-- accepted payments column -->
											<div class="col-xs-6">
												<p class="lead">
													Payment Methods: <i>PayQwik Payment Gateway</i>
												</p>
												<img
													src="${pageContext.request.contextPath}/resources/admin/images/logo_ios.png"
													alt="Visa">
												<p class="text-muted well well-sm no-shadow"
													style="margin-top: 10px;">PayQwik is a Mobile wallet
													which allows you to make online payments to various
													merchants, transfer funds to various banks and many more.</p>
											</div>
											<!-- /.col -->
											<div class="col-xs-6">
												<p class="lead">Amount Due 2/22/2014</p>
												<div class="table-responsive">
													<table class="table">
														<tbody>
															<tr>
																<th style="width: 50%">Subtotal:</th>
																<td>250.30 /- Rs.</td>
															</tr>
															<tr>
																<th>Tax (9.3%)</th>
																<td>10.34 /- Rs.</td>
															</tr>
															<tr>
																<th>Shipping:</th>
																<td>5.80 /- Rs.</td>
															</tr>
															<tr>
																<th>Total:</th>
																<td>265.24 /- Rs.</td>
															</tr>
														</tbody>
													</table>
												</div>
											</div>
											<!-- /.col -->
										</div>
										
										<!-- /.row -->

										<!-- this row will not appear when printing -->
										<div class="row no-print">
											<div class="col-xs-12">
												<button class="btn btn-default" onclick="window.print();">
													<i class="fa fa-print"></i> Print
												</button>
												<!-- <button class="btn btn-success pull-right">
													<i class="fa fa-credit-card"></i> Submit Payment
												</button>
												<button class="btn btn-primary pull-right"
													style="margin-right: 5px;">
													<i class="fa fa-download"></i> Generate PDF
												</button> -->
											</div>
										</div>
									</section>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- footer content -->
				<footer>
					<div class="copyright-info"></div>
					<div class="clearfix"></div>
				</footer>
				<!-- /footer content -->

			</div>
		</div>
	</div>

	<!-- footer content -->
	<footer>
		<div class="copyright-info"></div>
		<div class="clearfix"></div>
	</footer>
	<!-- /footer content -->
	<!-- /page content -->

	<div id="custom_notifications" class="custom-notifications dsp_none">
		<ul class="list-unstyled notifications clearfix"
			data-tabbed_notifications="notif-group">
		</ul>
		<div class="clearfix"></div>
		<div id="notif-group" class="tabbed_notifications"></div>
	</div>

	<script
		src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js"></script>

	<!-- bootstrap progress js -->
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/progressbar/bootstrap-progressbar.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/nicescroll/jquery.nicescroll.min.js"></script>
	<!-- icheck -->
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/icheck/icheck.min.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/admin/js/custom.js"></script>
	<!-- form wizard -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/admin/js/wizard/jquery.smartWizard.js"></script>
	<!-- pace -->
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/pace/pace.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			// Smart Wizard
			$('#wizard').smartWizard();

			function onFinishCallback() {
				$('#wizard').smartWizard('showMessage', 'Finish Clicked');
				//alert('Finish Clicked');
			}
		});

		$(document).ready(function() {
			// Smart Wizard
			$('#wizard_verticle').smartWizard({
				transitionEffect : 'slide'
			});

		});
	</script>
</body>
</html>
