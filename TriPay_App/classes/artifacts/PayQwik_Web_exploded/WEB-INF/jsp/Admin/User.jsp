<!DOCTYPE html>
<html lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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

<link
	href="${pageContext.request.contextPath}/resources/admin/js/datatables/jquery.dataTables.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/admin/js/datatables/buttons.bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/admin/js/datatables/fixedHeader.bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/admin/js/datatables/responsive.bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/admin/js/datatables/scroller.bootstrap.min.css"
	rel="stylesheet" type="text/css" />

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
						<a href="../Home" class="site_title"><i class="fa fa-paw"></i>
							<span>PayQwik</span></a>
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
							<h2>Admin</h2>
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
										<li><a href='<c:url value="../UserList"/>'>All</a></li>
										<li><a href='<c:url value="../VerifiedUsers"/>'>Verified</a></li>
										<li><a href='<c:url value="../UnverifiedUsers"/>'>Unverified</a></li>
										<li><a href='<c:url value="../BlockedUsers"/>'>Blocked</a></li>
										<li><a href='<c:url value="../ActiveUsers"/>'>Online</a></li>
									</ul></li>
								<li><a><i class="fa fa-edit"></i> Reports <span
										class="fa fa-chevron-down"></span></a>
									<ul class="nav child_menu" style="display: none">
										<li><a href='<c:url value="../DailyReport"/>'>Daily
												Reports</a></li>
										<li><a href='<c:url value="../MonthlyReport"/>'>Monthly
												Reports</a></li>
									</ul></li>
								<li><a><i class="fa fa-desktop"></i> Logs <span
										class="fa fa-chevron-down"></span></a>
									<ul class="nav child_menu" style="display: none">
										<li><a href='<c:url value="../SMSLog"/>'>Message Logs</a></li>
										<li><a href='<c:url value="../EmailLog"/>'>Email Logs</a></li>
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
										<li><a href='<c:url value="../ListMerchant"/>'>Merchant
												List</a></li>
										<li><a href='<c:url value="../AddMerchant"/>'>Add
												Merchant</a></li>

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
									alt="">PayQwik <span class=" fa fa-angle-down"></span>
							</a>
								<ul
									class="dropdown-menu dropdown-usermenu animated fadeInDown pull-right">
									<li><a href="javascript:;"> Profile</a></li>
									<li><a href="javascript:;"> <span
											class="badge bg-red pull-right">50%</span> <span>Settings</span>
									</a></li>
									<li><a href="javascript:;">Help</a></li>
									<li><a href="${pageContext.request.contextPath}/logout"><i
											class="fa fa-sign-out pull-right"></i>Log Out</a></li>
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
												src="${pageContext.request.contextPath}/resources/admin/images/img.jpg"
												alt="Profile Image" />
										</span> <span> <span>PayQwik</span> <span class="time">3
													mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"> <img
												src="${pageContext.request.contextPath}/resources/admin/images/img.jpg"
												alt="Profile Image" />
										</span> <span> <span>PayQwik</span> <span class="time">3
													mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"> <img
												src="${pageContext.request.contextPath}/resources/admin/images/img.jpg"
												alt="Profile Image" />
										</span> <span> <span>PayQwik</span> <span class="time">3
													mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"> <img
												src="${pageContext.request.contextPath}/resources/admin/images/img.jpg"
												alt="Profile Image" />
										</span> <span> <span>PayQwik</span> <span class="time">3
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
							<h3>
								Profile Type : <i>${user.userType}</i>
							</h3>
							<h6>
								Mobile OTP Code :
								<code>${user.mobileToken}</code>
							</h6>
						</div>

						<div class="title_right">
							<!-- <div
								class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
								<div class="input-group">
									<input type="text" class="form-control"
										placeholder="Search for..."> <span
										class="input-group-btn">
										<button class="btn btn-default" type="button">Go!</button>
									</span>
								</div>
							</div> -->
						</div>
					</div>
					<div class="clearfix"></div>

					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="x_panel">
								<div class="x_title">
									<h2>
										Authority :
										<code>${user.authority}</code>
									</h2>

									<br /> <br />
									<h2>
										Mobile Status :
										<code>${user.mobileStatus}</code>
									</h2>

									<br /> <br />
									<h2>
										Email Status :
										<code>${user.emailStatus}</code>
									</h2>
									<ul class="nav navbar-right panel_toolbox">
										<li><a href="#"><i class="fa fa-chevron-up"></i></a></li>
										<li class="dropdown"><a href="#" class="dropdown-toggle"
											data-toggle="dropdown" role="button" aria-expanded="false"><i
												class="fa fa-wrench"></i></a>
											<ul class="dropdown-menu" role="menu">
												<li><a href="#">Settings 1</a></li>
												<li><a href="#">Settings 2</a></li>
											</ul></li>
										<li><a href="#"><i class="fa fa-close"></i></a></li>
									</ul>
									<div class="clearfix"></div>
								</div>
								<div class="x_content">

									<div class="col-md-3 col-sm-3 col-xs-12 profile_left">

										<div class="profile_img">

											<!-- end of image cropping -->
											<div id="crop-avatar">
												<!-- Current avatar -->
												<div class="avatar-view" title="Change the avatar">
													<img src='<c:url value="${user.userDetail.image}"/>'
														alt="Avatar">
												</div>

												<!-- Cropping modal -->
												<div class="modal fade" id="avatar-modal" aria-hidden="true"
													aria-labelledby="avatar-modal-label" role="dialog"
													tabindex="-1">
													<div class="modal-dialog modal-lg">
														<div class="modal-content">
															<form class="avatar-form" action="crop.php"
																enctype="multipart/form-data" method="post">
																<div class="modal-header">
																	<button class="close" data-dismiss="modal"
																		type="button">&times;</button>
																	<h4 class="modal-title" id="avatar-modal-label">Change
																		Avatar</h4>
																</div>
																<div class="modal-body">
																	<div class="avatar-body">

																		<!-- Upload image and data -->
																		<div class="avatar-upload">
																			<input class="avatar-src" name="avatar_src"
																				type="hidden"> <input class="avatar-data"
																				name="avatar_data" type="hidden"> <label
																				for="avatarInput">Local upload</label> <input
																				class="avatar-input" id="avatarInput"
																				name="avatar_file" type="file">
																		</div>

																		<!-- Crop and preview -->
																		<div class="row">
																			<div class="col-md-9">
																				<div class="avatar-wrapper"></div>
																			</div>
																			<div class="col-md-3">
																				<div class="avatar-preview preview-lg"></div>
																				<div class="avatar-preview preview-md"></div>
																				<div class="avatar-preview preview-sm"></div>
																			</div>
																		</div>

																		<div class="row avatar-btns">
																			<div class="col-md-9">
																				<div class="btn-group">
																					<button class="btn btn-primary"
																						data-method="rotate" data-option="-90"
																						type="button" title="Rotate -90 degrees">Rotate
																						Left</button>
																					<button class="btn btn-primary"
																						data-method="rotate" data-option="-15"
																						type="button">-15deg</button>
																					<button class="btn btn-primary"
																						data-method="rotate" data-option="-30"
																						type="button">-30deg</button>
																					<button class="btn btn-primary"
																						data-method="rotate" data-option="-45"
																						type="button">-45deg</button>
																				</div>
																				<div class="btn-group">
																					<button class="btn btn-primary"
																						data-method="rotate" data-option="90"
																						type="button" title="Rotate 90 degrees">Rotate
																						Right</button>
																					<button class="btn btn-primary"
																						data-method="rotate" data-option="15"
																						type="button">15deg</button>
																					<button class="btn btn-primary"
																						data-method="rotate" data-option="30"
																						type="button">30deg</button>
																					<button class="btn btn-primary"
																						data-method="rotate" data-option="45"
																						type="button">45deg</button>
																				</div>
																			</div>
																			<div class="col-md-3">
																				<button
																					class="btn btn-primary btn-block avatar-save"
																					type="submit">Done</button>
																			</div>
																		</div>
																	</div>
																</div>
																<!-- <div class="modal-footer">
                                                  <button class="btn btn-default" data-dismiss="modal" type="button">Close</button>
                                                </div> -->
															</form>
														</div>
													</div>
												</div>
												<!-- /.modal -->

												<!-- Loading state -->
												<div class="loading" aria-label="Loading" role="img"
													tabindex="-1"></div>
											</div>
											<!-- end of image cropping -->

										</div>
										<h3>${user.userDetail.firstName}
											${user.userDetail.lastName}</h3>

										<ul class="list-unstyled user_data">
											<li><i class="fa fa-map-marker user-profile-icon"></i>
												${user.userDetail.email}</li>

											<li><i class="fa fa-briefcase user-profile-icon"></i>
												${user.userDetail.contactNo}</li>

											<li class="m-top-xs"><i
												class="fa fa-external-link user-profile-icon"></i> <a
												href="#">${user.userDetail.created}</a></li>
										</ul>

										<a class="btn btn-danger"><i class="fa fa-edit m-right-xs"></i>Block
											${user.userDetail.firstName}</a> <br />

										<!-- start skills -->
										<!-- <h4>Skills</h4>
                    <ul class="list-unstyled user_data">
                      <li>
                        <p>Web Applications</p>
                        <div class="progress progress_sm">
                          <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="50"></div>
                        </div>
                      </li>
                      <li>
                        <p>Website Design</p>
                        <div class="progress progress_sm">
                          <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="70"></div>
                        </div>
                      </li>
                      <li>
                        <p>Automation & Testing</p>
                        <div class="progress progress_sm">
                          <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="30"></div>
                        </div>
                      </li>
                      <li>
                        <p>UI / UX</p>
                        <div class="progress progress_sm">
                          <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="50"></div>
                        </div>
                      </li>
                    </ul> -->
										<!-- end of skills -->

									</div>
									<div class="col-md-9 col-sm-9 col-xs-12">

										<div class="profile_title">
											<div class="col-md-6">
												<h2>User Activity Report</h2>
											</div>
											<div class="col-md-6">
												<!-- <div id="reportrange" class="pull-right"
													style="margin-top: 5px; background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #E6E9ED">
													<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
													<span>December 30, 2014 - January 28, 2015</span> <b
														class="caret"></b>
												</div> -->

											</div>
										</div>


										<!-- start of user-activity-graph -->
										<!-- <div id="lineChart" style="height: 280px;"></div> -->
										<!-- end of user-activity-graph -->




										<canvas id="lineChart"></canvas>













										<div class="" role="tabpanel" data-example-id="togglable-tabs">
											<ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
												<li role="presentation" class="active"><a
													href="#tab_content1" id="home-tab" role="tab"
													data-toggle="tab" aria-expanded="true">${user.userDetail.firstName}
														Transactions</a></li>
												<!-- <li role="presentation" class=""><a href="#tab_content2" role="tab" id="profile-tab" data-toggle="tab" aria-expanded="false">Projects Worked on</a>
                        </li>
                        <li role="presentation" class=""><a href="#tab_content3" role="tab" id="profile-tab2" data-toggle="tab" aria-expanded="false">Profile</a>
                        </li> -->
											</ul>
											<div id="myTabContent" class="tab-content">
												<div role="tabpanel" class="tab-pane fade active in"
													id="tab_content1" aria-labelledby="home-tab">

													<!-- start recent activity -->
													<div class="x_content">
														<p class="text-muted font-13 m-b-30">
															<!--  DataTables has most features enabled by default, so all you need to do to use it with your own tables is to call the construction function: <code>$().DataTable();</code> -->
														</p>
														<table id="datatable"
															class="table table-striped table-bordered">
															<thead>
																<tr>
																	<th>Date of Transaction</th>
																	<th>Amount</th>
																	<th>Current Balance</th>
																	<th>Status</th>
																</tr>
															</thead>
															<tbody>
																<c:forEach items="${pQTransaction}" var="pQTransaction">
																	<tr>
																		<td>${pQTransaction.created}</td>
																		<td>${pQTransaction.amount}</td>
																		<td>${pQTransaction.currentBalance}</td>
																		<td>${pQTransaction.status}</td>

																	</tr>
																</c:forEach>
															</tbody>
														</table>
													</div>
													<!-- end recent activity -->

												</div>
												<div role="tabpanel" class="tab-pane fade" id="tab_content2"
													aria-labelledby="profile-tab">

													<!-- start user projects -->
													<table class="data table table-striped no-margin">
														<thead>
															<tr>
																<th>#</th>
																<th>Project Name</th>
																<th>Client Company</th>
																<th class="hidden-phone">Hours Spent</th>
																<th>Contribution</th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td>1</td>
																<td>New Company Takeover Review</td>
																<td>Deveint Inc</td>
																<td class="hidden-phone">18</td>
																<td class="vertical-align-mid">
																	<div class="progress">
																		<div class="progress-bar progress-bar-success"
																			data-transitiongoal="35"></div>
																	</div>
																</td>
															</tr>
															<tr>
																<td>2</td>
																<td>New Partner Contracts Consultanci</td>
																<td>Deveint Inc</td>
																<td class="hidden-phone">13</td>
																<td class="vertical-align-mid">
																	<div class="progress">
																		<div class="progress-bar progress-bar-danger"
																			data-transitiongoal="15"></div>
																	</div>
																</td>
															</tr>
															<tr>
																<td>3</td>
																<td>Partners and Inverstors report</td>
																<td>Deveint Inc</td>
																<td class="hidden-phone">30</td>
																<td class="vertical-align-mid">
																	<div class="progress">
																		<div class="progress-bar progress-bar-success"
																			data-transitiongoal="45"></div>
																	</div>
																</td>
															</tr>
															<tr>
																<td>4</td>
																<td>New Company Takeover Review</td>
																<td>Deveint Inc</td>
																<td class="hidden-phone">28</td>
																<td class="vertical-align-mid">
																	<div class="progress">
																		<div class="progress-bar progress-bar-success"
																			data-transitiongoal="75"></div>
																	</div>
																</td>
															</tr>
														</tbody>
													</table>
													<!-- end user projects -->

												</div>
												<div role="tabpanel" class="tab-pane fade" id="tab_content3"
													aria-labelledby="profile-tab">
													<p>xxFood truck fixie locavore, accusamus mcsweeney's
														marfa nulla single-origin coffee squid. Exercitation +1
														labore velit, blog sartorial PBR leggings next level wes
														anderson artisan four loko farm-to-table craft beer twee.
														Qui photo booth letterpress, commodo enim craft beer
														mlkshk</p>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- footer content -->
				<footer>
					<div class="copyright-info">
						<!-- <p class="pull-right">
							Gentelella - Bootstrap Admin Template by <a
								href="https://colorlib.com">Colorlib</a>
						</p> -->
					</div>
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


	<!-- Datatables -->
	<!-- <script src="js/datatables/js/jquery.dataTables.js"></script>
  <script src="js/datatables/tools/js/dataTables.tableTools.js"></script> -->

	<!-- Datatables-->
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/jquery.dataTables.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/dataTables.bootstrap.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/dataTables.buttons.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/buttons.bootstrap.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/jszip.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/pdfmake.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/vfs_fonts.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/buttons.html5.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/buttons.print.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/dataTables.fixedHeader.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/dataTables.keyTable.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/dataTables.responsive.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/responsive.bootstrap.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/dataTables.scroller.min.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/admin/js/cropping/cropper.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/cropping/main.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/moment/moment.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datepicker/daterangepicker.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/chartjs/chart.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/datatables/dataTables.scroller.min.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/admin/js/moris/raphael-min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/admin/js/moris/morris.min.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/admin/js/pace/pace.min.js"></script>

	<script>
		Chart.defaults.global.legend = {
			enabled : false
		};

		// Line chart
		var ctx = document.getElementById("lineChart");
		console.log("Hello");
		console.log("${gDto.day}-${gDto.month}-${gDto.year}");
		console.log("${bb}");
		console.log("${cc}");
		console.log("${dd}");
		console.log("${ee}");
		console.log("${ff}");
		console.log("${gg}");
		console.log("Last");
		var lineChart = new Chart(
				ctx,
				{
					type : 'line',
					data : {
						labels : [
								"${gDto.day} - ${gDto.month} - ${gDto.year}",
								"${gDto.day}" - 1
										+ " - ${gDto.month} - ${gDto.year}",
								"${gDto.day}" - 2
										+ " - ${gDto.month} - ${gDto.year}",
								"${gDto.day}" - 3
										+ " - ${gDto.month} - ${gDto.year}",
								"${gDto.day}" - 4
										+ " - ${gDto.month} - ${gDto.year}",
								"${gDto.day}" - 5	
										+ " - ${gDto.month} - ${gDto.year}", ],
						datasets : [
								{
									label : "",
									backgroundColor : "",
									borderColor : "",
									pointBorderColor : "",
									pointBackgroundColor : "",
									pointHoverBackgroundColor : "",
									pointHoverBorderColor : "",
									pointBorderWidth : 1,
									data : [ 0, 0, 0, 0, 0, 0, 0 ]
								},
								{
									label : "Transaction",
									backgroundColor : "#0099ff",
									borderColor : "rgba(3, 88, 106, 0.70)",
									pointBorderColor : "rgba(3, 88, 106, 0.70)",
									pointBackgroundColor : "rgba(3, 88, 106, 0.70)",
									pointHoverBackgroundColor : "#fff",
									pointHoverBorderColor : "rgba(151,187,205,1)",
									pointBorderWidth : 1,
									data : [ "${gDto.aa}", "${gDto.bb}",
											"${gDto.cc}", "${gDto.dd}",
											"${gDto.ee}", "${gDto.ff}",
											"${gDto.gg}" ]
								} ]
					},
				});

		// Bar chart

		// Doughnut chart

		// Radar chart

		// Pie chart

		// PolarArea chart
	</script>
	
	<script>
		var handleDataTableButtons = function() {
			"use strict";
			0 !== $("#datatable-buttons").length
					&& $("#datatable-buttons").DataTable({
						dom : "Bfrtip",
						buttons : [ {
							extend : "copy",
							className : "btn-sm"
						}, {
							extend : "csv",
							className : "btn-sm"
						}, {
							extend : "excel",
							className : "btn-sm"
						}, {
							extend : "pdf",
							className : "btn-sm"
						}, {
							extend : "print",
							className : "btn-sm"
						} ],
						responsive : !0
					})
		}, TableManageButtons = function() {
			"use strict";
			return {
				init : function() {
					handleDataTableButtons()
				}
			}
		}();
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#datatable').dataTable();
			$('#datatable-keytable').DataTable({
				keys : true
			});
			$('#datatable-responsive').DataTable();
			$('#datatable-scroller').DataTable({
				ajax : "js/datatables/json/scroller-demo.json",
				deferRender : true,
				scrollY : 380,
				scrollCollapse : true,
				scroller : true
			});
			var table = $('#datatable-fixed-header').DataTable({
				fixedHeader : true
			});
		});
		TableManageButtons.init();
	</script>
	<script>
		$(function() {
			var day_data = [ {
				"period" : "Jan",
				"Hours worked" : 80
			}, {
				"period" : "Feb",
				"Hours worked" : 125
			}, {
				"period" : "Mar",
				"Hours worked" : 176
			}, {
				"period" : "Apr",
				"Hours worked" : 224
			}, {
				"period" : "May",
				"Hours worked" : 265
			}, {
				"period" : "Jun",
				"Hours worked" : 314
			}, {
				"period" : "Jul",
				"Hours worked" : 347
			}, {
				"period" : "Aug",
				"Hours worked" : 287
			}, {
				"period" : "Sep",
				"Hours worked" : 240
			}, {
				"period" : "Oct",
				"Hours worked" : 211
			} ];
			Morris.Bar({
				element : 'graph_bar',
				data : day_data,
				xkey : 'period',
				hideHover : 'auto',
				barColors : [ '#26B99A', '#34495E', '#ACADAC', '#3498DB' ],
				ykeys : [ 'Hours worked', 'sorned' ],
				labels : [ 'Hours worked', 'SORN' ],
				xLabelAngle : 60
			});
		});
	</script>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {

							var cb = function(start, end, label) {
								console.log(start.toISOString(), end
										.toISOString(), label);
								$('#reportrange span').html(
										start.format('MMMM D, YYYY') + ' - '
												+ end.format('MMMM D, YYYY'));
								//alert("Callback has fired: [" + start.format('MMMM D, YYYY') + " to " + end.format('MMMM D, YYYY') + ", label = " + label + "]");
							}

							var optionSet1 = {
								startDate : moment().subtract(29, 'days'),
								endDate : moment(),
								minDate : '01/01/2012',
								maxDate : '12/31/2015',
								dateLimit : {
									days : 60
								},
								showDropdowns : true,
								showWeekNumbers : true,
								timePicker : false,
								timePickerIncrement : 1,
								timePicker12Hour : true,
								ranges : {
									'Today' : [ moment(), moment() ],
									'Yesterday' : [
											moment().subtract(1, 'days'),
											moment().subtract(1, 'days') ],
									'Last 7 Days' : [
											moment().subtract(6, 'days'),
											moment() ],
									'Last 30 Days' : [
											moment().subtract(29, 'days'),
											moment() ],
									'This Month' : [ moment().startOf('month'),
											moment().endOf('month') ],
									'Last Month' : [
											moment().subtract(1, 'month')
													.startOf('month'),
											moment().subtract(1, 'month')
													.endOf('month') ]
								},
								opens : 'left',
								buttonClasses : [ 'btn btn-default' ],
								applyClass : 'btn-small btn-primary',
								cancelClass : 'btn-small',
								format : 'MM/DD/YYYY',
								separator : ' to ',
								locale : {
									applyLabel : 'Submit',
									cancelLabel : 'Clear',
									fromLabel : 'From',
									toLabel : 'To',
									customRangeLabel : 'Custom',
									daysOfWeek : [ 'Su', 'Mo', 'Tu', 'We',
											'Th', 'Fr', 'Sa' ],
									monthNames : [ 'January', 'February',
											'March', 'April', 'May', 'June',
											'July', 'August', 'September',
											'October', 'November', 'December' ],
									firstDay : 1
								}
							};
							$('#reportrange span').html(
									moment().subtract(29, 'days').format(
											'MMMM D, YYYY')
											+ ' - '
											+ moment().format('MMMM D, YYYY'));
							$('#reportrange').daterangepicker(optionSet1, cb);
							$('#reportrange').on('show.daterangepicker',
									function() {
										console.log("show event fired");
									});
							$('#reportrange').on('hide.daterangepicker',
									function() {
										console.log("hide event fired");
									});
							$('#reportrange')
									.on(
											'apply.daterangepicker',
											function(ev, picker) {
												console
														.log("apply event fired, start/end dates are "
																+ picker.startDate
																		.format('MMMM D, YYYY')
																+ " to "
																+ picker.endDate
																		.format('MMMM D, YYYY'));
											});
							$('#reportrange').on('cancel.daterangepicker',
									function(ev, picker) {
										console.log("cancel event fired");
									});
							$('#options1').click(
									function() {
										$('#reportrange').data(
												'daterangepicker').setOptions(
												optionSet1, cb);
									});
							$('#options2').click(
									function() {
										$('#reportrange').data(
												'daterangepicker').setOptions(
												optionSet2, cb);
									});
							$('#destroy').click(
									function() {
										$('#reportrange').data(
												'daterangepicker').remove();
									});
						});
	</script>

</body>

</html>
