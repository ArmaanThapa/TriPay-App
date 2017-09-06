<!DOCTYPE html>
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

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
								<!-- <li><a><i class="fa fa-laptop"></i> Landing Page <span class="label label-success pull-right">Coming Soon</span></a>
                </li> -->
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
									alt="">John Doe <span class=" fa fa-angle-down"></span>
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

							<li role="presentation" class="dropdown"><a
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
								</ul></li>

						</ul>
					</nav>
				</div>

			</div>
			<!-- /top navigation -->

			<!-- page content -->
			<div class="right_col" role="main">

				<div class="">
					<div class="page-title">
						<div class="title_left">
							<h3>Add Merchant</h3>
						</div>

						<div class="title_right">
							<div
								class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
								<div class="input-group">
									<input type="text" class="form-control"
										placeholder="Search for..."> <span
										class="input-group-btn">
										<button class="btn btn-default" type="button">Go!</button>
									</span>
								</div>
							</div>
						</div>
					</div>
					<div class="clearfix"></div>

					<div class="row">

						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="x_panel">
								<div class="x_title">
									<ul class="nav navbar-right panel_toolbox">
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
									</ul>
									<div class="clearfix"></div>
								</div>
								<div class="x_content">

									<!-- Smart Wizard -->
									<p>${message}</p>

									<!-- <ul class="wizard_steps">
											<li><a href="#step-1"> <span class="step_no">1</span>
													<span class="step_descr"> Step 1<br /> <small>Step
															1 description</small>
												</span>
											</a></li>
											<li><a href="#step-2"> <span class="step_no">2</span>
													<span class="step_descr"> Step 2<br /> <small>Step
															2 description</small>
												</span>
											</a></li>
											<li><a href="#step-3"> <span class="step_no">3</span>
													<span class="step_descr"> Step 3<br /> <small>Step
															3 description</small>
												</span>
											</a></li>
												<li><a href="#step-4"> <span class="step_no">4</span>
													<span class="step_descr"> Step 4<br /> <small>Step
															4 description</small>
												</span>
											</a></li>
										</ul> -->
									<form class="form-horizontal form-label-left"
										action="AddMerchant" method="POST">

										<div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="fNname">Merchant Name <span class="required">*</span>
											</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
												<input type="text" id="mName" name="mName"
													required="required" class="form-control col-md-7 col-xs-12">
											</div>
										</div>
										<!-- <div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="lName">Last Name <span class="required">*</span>
											</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
												<input type="text" id="lName" name="lName"
													required="required" class="form-control col-md-7 col-xs-12">
											</div>
										</div> -->
										<div class="form-group">
											<label for="middle-name"
												class="control-label col-md-3 col-sm-3 col-xs-12">Authority
											</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
												<input id="authority" placeholder="Role_Merchant"
													class="form-control col-md-7 col-xs-12" type="text"
													name="authority" />
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="first-name">Enter Password <span
												class="required">*</span>
											</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
												<input type="password" id="password" name="mpassword"
													required="required" class="form-control col-md-7 col-xs-12">
											</div>
										</div>
										<!-- <div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="last-name">User Type <span class="required">*</span>
											</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
												<input type="text" id="userType" name="userType"
													required="required" value="Merchant" placeholder="Merchant"
													class="form-control col-md-7 col-xs-12">
											</div>
										</div> -->
										<div class="form-group">
											<label for="middle-name"
												class="control-label col-md-3 col-sm-3 col-xs-12">Mobile
												Number </label>
											<div class="col-md-6 col-sm-6 col-xs-12">
												<input id="mNumber" class="form-control col-md-7 col-xs-12"
													type="text" name="contactNo" id="contactNo">
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="first-name">Email ID <span class="required">*</span>
											</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
												<input type="email" id="email" name="email"
													required="required" class="form-control col-md-7 col-xs-12">
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="last-name">Address <span class="required">*</span>
											</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
												<input type="text" id="address" name="address"
													required="required" class="form-control col-md-7 col-xs-12">
											</div>
										</div>
										<div id="col-sm-offset-5 col-sm-2 text-center"
											style="margin-left: 45%">
											<button type="submit" class="btn btn-primary ">Register</button>
										</div>
									</form>
								</div>
								<!-- <div id="step-2">
												<h2 class="StepTitle">
													 
												</h2>

											 <div class="form-group">
													<label class="control-label col-md-3 col-sm-3 col-xs-12"
														for="first-name">Enter Password <span
														class="required">*</span>
													</label>
													<div class="col-md-6 col-sm-6 col-xs-12">
														<input type="password" id="password" required="required"
															class="form-control col-md-7 col-xs-12">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-3 col-sm-3 col-xs-12"
														for="last-name">User Type <span class="required">*</span>
													</label>
													<div class="col-md-6 col-sm-6 col-xs-12">
														<input type="text" id="userType" name="userType"
															required="required" value="Merchant"
															placeholder="Merchant"
															class="form-control col-md-7 col-xs-12">
													</div>
												</div>
												<div class="form-group">
													<label for="middle-name"
														class="control-label col-md-3 col-sm-3 col-xs-12">Mobile
														Number </label>
													<div class="col-md-6 col-sm-6 col-xs-12">
														<input id="mNumber"
															class="form-control col-md-7 col-xs-12" type="text"
															name="mNumber">
													</div>
												</div>
 
											</div>
											<div id="step-3">
												<h2 class="StepTitle">
													<!-- Step 3 Content -->
								<!-- <div class="form-group">
													<label class="control-label col-md-3 col-sm-3 col-xs-12"
														for="first-name">Email ID <span class="required">*</span>
													</label>
													<div class="col-md-6 col-sm-6 col-xs-12">
														<input type="email" id="email" required="required"
															class="form-control col-md-7 col-xs-12">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-3 col-sm-3 col-xs-12"
														for="last-name">Address <span class="required">*</span>
													</label>
													<div class="col-md-6 col-sm-6 col-xs-12">
														<input type="text" id="address" name="address"
															required="required"
															class="form-control col-md-7 col-xs-12">
													</div>
												</div> -->


								<!-- <div class="form-group">
													<label for="middle-name"
														class="control-label col-md-3 col-sm-3 col-xs-12">Middle
														Name / Initial</label>
													<div class="col-md-6 col-sm-6 col-xs-12">
														<input id="middle-name"
															class="form-control col-md-7 col-xs-12" type="text"
															name="middle-name">
													</div>
												</div> -->
							</div>
							<!--<div id="step-4">
												<h2 class="StepTitle">Step 4 Content</h2>
												<form class="form-horizontal form-label-left">

													<div class="form-group">
														<label class="control-label col-md-3 col-sm-3 col-xs-12"
															for="first-name">First Name <span
															class="required">*</span>
														</label>
														<div class="col-md-6 col-sm-6 col-xs-12">
															<input type="text" id="first-name" required="required"
																class="form-control col-md-7 col-xs-12">
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-md-3 col-sm-3 col-xs-12"
															for="last-name">Last Name <span class="required">*</span>
														</label>
														<div class="col-md-6 col-sm-6 col-xs-12">
															<input type="text" id="last-name" name="last-name"
																required="required"
																class="form-control col-md-7 col-xs-12">
														</div>
													</div>
													<div class="form-group">
														<label for="middle-name"
															class="control-label col-md-3 col-sm-3 col-xs-12">Middle
															Name / Initial</label>
														<div class="col-md-6 col-sm-6 col-xs-12">
															<input id="middle-name"
																class="form-control col-md-7 col-xs-12" type="text"
																name="middle-name">
														</div>
													</div>
											</div> -->


						</div>
						<!-- End SmartWizard Content -->

						<!-- Tabs -->


						<!-- End SmartWizard Content -->
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
	<!-- /page content -->
	</div>
	</div>

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
