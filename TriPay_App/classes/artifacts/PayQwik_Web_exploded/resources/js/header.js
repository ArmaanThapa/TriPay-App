var page = 1;
var perPage = 3;

function ActiveHeader(id) {
	$('#' + id).addClass("active");
}

function ActiveMenu(id) {
	$('#' + id).addClass("active");
}

function ActiveSubmenu(id) {
	console.log("SubMenu");
	console.log('#' + id + "SubMenu");
	if (id.length == 0) {
		console.log("menu is null");
		$('#PrepaidSubMenu').addClass("active");
		$('#DTHSubMenu').addClass("active");
		$('#MobileSubMenu').addClass("active");
		$('#IEmailSubMenu').addClass("active");
	} else {
		console.log("Menu is not null");
		$('#' + id + "SubMenu").addClass("active");
	}
}
function enableButton(id) {
	var isChecked = $("#termsConditions").is(":checked");
	console.log(isChecked);
	if (isChecked) {
		console.log(isChecked);
		console.log("disable button with id " + id);
		$("#" + id).removeClass("disabled");
	} else {
		$("#" + id).addClass("disabled")
	}

}

function processMessage(message) {
	console.log("process Message called")
	if (message.includes('Registration Successful')
			|| message.includes("New OTP") || message.includes("Invalid OTP")) {
		$("#regMessage").modal("show");
	}
	if (message.includes(" Your mobile is  successfully verified")
			|| message.includes("Already Verified")) {
		$("#regMessage").modal("hide");
		$("#verifiedMessage").modal("show");
	}
	if (message.includes("Check your SMS for OTP Code to change your password")) {
		$("#forgotPassword").modal("show");
	}
}

function ActiveFadeIn(id) {
	console.log("FadeIn")
	console.log('#' + id + "FadeIn");
	if (id.length == 0) {
		$('#PrepaidFadeIn').addClass("active in");
		$('#DTHFadeIn').addClass("active in");
		$('#MobileFadeIn').addClass("active in");
		$('#IEmailFadeIn').caddClass("active in");

	} else {
		$('#' + id + "FadeIn").addClass("active in");
	}

}

function ActiveClick(id) {
	$("#" + id).click();
}

// Show popup
function showPopup(id) {
	if ($('#' + id).length) {
		$('#' + id).fadeIn();
	}
}

// Hide popup
function hidePopup(id) {
	if ($('#' + id).length) {
		$('#' + id).fadeOut();
	}
}

/* Here Pagination for All Users Starts */

// Load Pagination Menu
function loadPagination(total) {
	console.log("PAGE :: " + total);
	var totalPage = Math.round(total / perPage);
	console.log("PAGE TOTAL :: " + totalPage);
	if (totalPage > 0) {
		$('#pagination').twbsPagination({
			totalPages : totalPage,
			startPage : page,
			visiblePages : 15,
			onPageClick : function(event, pg) {
				console.log("PAGE CLICK :: " + pg);
				checkPage(pg);
			}
		});
	}
}

// Load Page
function checkPage(pg) {
	console.log("CHECK PAGE :: " + pg);
	if (page != pg) {
		page = pg;
		console.log("CHECK PAGE SUBTRACT :: " + page);
		$('#loading').show();
		getUserList();
	}
}

function getUserList() {
	var requestData = 'page=' + (page - 1) + '&perPage=' + perPage;
	console.log("request Data " + requestData);
	$.ajax({

		type : "GET",
		url : "/PayQwik/Admin/Ajax/GetUser",
		contentType : "application/json",
		data : requestData,
		datatype : 'json',

		success : function(response) {
			console.log("Response " + JSON.stringify(response));
			$('#userList').html("");
			var size = response.length
			console.log("SIZE :: " + size);
			console.log("Head Created");

			for (i = 0; i < size; i++) {
				console.log("Body Created " + i);
				var u = response[i];
				$('#userList').append(
						'' + '<tr>' + '<td><b>User Name : </b>'
								+ u.userDetail.firstName + ''
								+ u.userDetail.middleName
								+ u.userDetail.lastName + '<br />'
								+ '<b>Mobile Number :</b> '
								+ u.userDetail.contactNo + '<br /> <b>Email'
								+ 'ID :</b>' + u.userDetail.email + '</td>'
								+ '<td>' + u.emailStatus + '</td>' + '<td>'
								+ u.userDetail.address + '</td>'
								+ '<td>Balance : 1833 -/ Rs.<br />Status :'
								+ u.mobileStatus
								+ '<td>Daily : <br />Monthly : <br /> Yearly :'
								+ '</td>' + '</tr>' + '');
			}
			console.log("Table Closes");
			$("#loading").hide();
		},
		error : function(response) {
			console.log("HERE INSIDE ERROR " + response.responseText);
			location.reload();
		}
	});
}

/* Here Pagination for All Users Ends */

/* Here Pagination for Verified Users Starts */

// Load Pagination Menu for verified Users
function loadPaginationVerifiedUsers(total) {
	console.log(" Verified Users PAGE :: " + total);
	var totalPage = Math.round(total / perPage);
	console.log(" Verified Users PAGE TOTAL :: " + totalPage);
	if (totalPage > 0) {
		$('#pagination').twbsPagination({
			totalPages : totalPage,
			startPage : page,
			visiblePages : 15,
			onPageClick : function(event, pg) {
				console.log(" Verified Users PAGE CLICK :: " + pg);
				checkPageVerifiedUsers(pg);
			}
		});
	}
}

// Load Page for verified Users
function checkPageVerifiedUsers(pg) {
	console.log(" Verified Users CHECK PAGE :: " + pg);
	if (page != pg) {
		page = pg;
		console.log("Verified Users CHECK PAGE SUBTRACT :: " + page);
		$('#loading').show();
		getUserListVerifiedUsers();
	}
}

function getUserListVerifiedUsers() {
	var requestData = 'page=' + (page - 1) + '&perPage=' + perPage;
	console.log("request Data " + requestData);
	$
			.ajax({

				type : "GET",
				url : "/PayQwik/Admin/Ajax/VerifiedUser",
				contentType : "application/json",
				data : requestData,
				datatype : 'json',

				success : function(response) {
					console.log("Verified Users Response "
							+ JSON.stringify(response));
					$('#userList').html("");
					var size = response.length
					console.log("SIZE :: " + size);
					console.log("Head Created");

					for (i = 0; i < size; i++) {
						console.log("Body Created " + i);
						var u = response[i];
						$('#userList')
								.append(
										'' + '<tr>' + '<td><b>User Name : </b>'
												+ u.userDetail.firstName
												+ ''
												+ u.userDetail.middleName
												+ u.userDetail.lastName
												+ '<br />'
												+ '<b>Mobile Number :</b> '
												+ u.userDetail.contactNo
												+ '<br /> <b>Email'
												+ 'ID :</b>'
												+ u.userDetail.email
												+ '</td>'
												+ '<td>'
												+ u.emailStatus
												+ '</td>'
												+ '<td>'
												+ u.userDetail.address
												+ '</td>'
												+ '<td>Balance : 1833 -/ Rs.<br />Status :'
												+ u.mobileStatus + '' + '</td>'
												+ '</tr>' + '');
					}
					console.log("Table Closes");
					$("#loading").hide();
				},
				error : function(response) {
					console.log("HERE INSIDE ERROR " + response.responseText);
					location.reload();
				}
			});
}

/* Here Pagination for Unverified Users Starts */

// Load Pagination Menu for Unverified Users
function loadPaginationUnverifiedUsers(total) {
	console.log(" Verified Users PAGE :: " + total);
	var totalPage = Math.round(total / perPage);
	console.log(" Verified Users PAGE TOTAL :: " + totalPage);
	if (totalPage > 0) {
		$('#pagination').twbsPagination({
			totalPages : totalPage,
			startPage : page,
			visiblePages : 15,
			onPageClick : function(event, pg) {
				console.log(" Verified Users PAGE CLICK :: " + pg);
				checkPageUnverifiedUsers(pg);
			}
		});
	}
}

// Load Page for verified Users
function checkPageUnverifiedUsers(pg) {
	console.log(" Verified Users CHECK PAGE :: " + pg);
	if (page != pg) {
		page = pg;
		console.log("Verified Users CHECK PAGE SUBTRACT :: " + page);
		$('#loading').show();
		getUserListUnverifiedUsers();
	}
}

function SelectOperator() {
	var i = 0;
	$("#mobile")
			.keypress(
					function(event) {
						if (event.which >= 48 && event.which <= 57) {
							i = i + 1;
							console.log(event.which);
							console.log(i);
							console.log($("#mobile").val());
							var mobilePart = $("#mobile").val();
							if (mobilePart >= 7000) {
								$
										.ajax({

											type : "GET",
											url : "/Guest/Ajax/GetTelco?number="
													+ mobilePart,
											contentType : "application/json",
											datatype : 'json',
											success : function(response) {
												console
														.log("Response "
																+ JSON
																		.stringify(response));
												var u = response;
												console.log(response);
												$("#operator").val(
														u.operator.serviceCode);
												$("#circle").val(u.circle.code);
												var link = "<a href='#' id='planzz'>Browse Plans</a>"
												$("#plan_link").html(link);
												$("#planzz")
														.click(
																function() {
																	console
																			.log("clicked");
																	$
																			.ajax({
																				type : "GET",
																				url : "/Guest/Ajax/GetPlans?operatorCode="
																						+ u.operator.code
																						+ "&circleCode="
																						+ u.circle.code,
																				contentType : "application/json",
																				datatype : 'json',
																				success : function(
																						response) {
																					console
																							.log("Response "
																									+ JSON
																											.stringify(response));
																					console
																							.log(response);
																					var plans = response.plans;
																					generateTable(plans);

																				}

																			});

																});
											},

										});
							}
						}
					});
}
function processForgotPassword() {
	console.log("Inside Process Forgot Password");
	var oldVal = $("#fp_submit").val();
	$("#fp_submit").addClass("disabled");
	$("#fp_submit").val("Please wait ....");
	$.ajax({
		type : "POST",
		contentType : "application/x-www-form-urlencoded",
		url : "/ForgotPassword",
		data : {
			username : $("#fp_username").val()
		},
		success : function(response) {
			$("#fp_submit").val(oldVal)
			$("#fp_submit").removeClass("disabled");
			console.log("Reponse Is ::");
			console.log(response.details);
			if (response.message.includes("success")) {
				$("#forgotPassword").modal("hide");
				$("#fpOTP").modal("show");
				$("#fpOTP_message").html(response.details);

			} else {
				$("#fp_message").html(response.details);
			}
		}
	});
}
function processForgotPasswordOTP() {
	console.log("inside forgot password OTP");
	var key = $("#fpOTP_key").val();
	$.ajax({
		type : "POST",
		contentType : "application/x-www-form-urlencoded",
		url : "/Change/" + key,
		success : function(response) {
			console.log(response);
			if (response.message.includes("success")) {
				console.log("success");
				$("#fpOTP").modal("hide");
				$("#changePassword").modal("show");
				$("#change_password_username").val(response.details.username);
				$("#change_password_key").val(response.details.key);
			} else {
				$("#fpOTP_message").html(response.details.message);
			}
		}
	});
}

function renewPassword() {
	console.log("inside renew password");
	var new_password = $("#new_password").val();
	var confirm_password = $("#confirm_password").val();
	var key = $("#change_password_key").val();
	console.log(confirm_password);
	var username = $("#change_password_username").val();
	console.log("Key ::"+key);
	console.log("Username ::"+username);
	$.ajax({
		type : "POST",
		contentType : "application/x-www-form-urlencoded",
		url : "/RenewPassword",
		data : {
			username : username,
			key : key,
			newPassword : new_password,
			confirmPassword : confirm_password
		},
		success : function(response) {
			if (response.message.includes("success")) {
				$("#changePassword").modal("hide");
				$("#successNotification").modal("show");
				$("#success_alert").html("Password Changed Successfully");
			} else {
				console.log("in else condition");
				$("#changePassword_message").html(response.details.message);
			}
		}
	});
}

function generateTable(plans) {
	console.log("inside generation of table");
	$("#mycarousel").hide();
	$("#plan_table")
			.append(
					"<thead><tr><th class='col-xs-8'>Description</th><th class='col-xs-3'>Validity</th><th class='col-xs-1'>Amount</th></tr></thead>");
	$("#plan_table").append("<tbody id='tbodyOne'>");
	for ( var i in plans) {
		if (plans[i].planName.includes("Talktime")
				|| plans[i].planName.includes("Topup")) {
			$("#tbodyOne").append("<tr id='bplanrow" + i + "'>");
			$("#bplanrow" + i).append(
					"<td class='col-xs-9'>" + plans[i].description + "</td>");
			$("#bplanrow" + i).append(
					"<td class='col-xs-3'>" + plans[i].validity + "</td>");
			$("#bplanrow" + i).append(
					"<td class='col-xs-2'><a class='btn btn-primary' onclick=enterAmount("
							+ plans[i].amount + ") href='#';>"
							+ plans[i].amount + "</a></td>");
			$("#tbodyOne").append("</tr>");
		}
	}
	$("#plan_table").append("</tbody>");

}

function browsePic() {
	$("#profilePic").append("<h1>Change Image</h1>")

}

function removePic() {

	$("#picz").hide();
}

function enterAmount(amount) {
	console.log("inside enter amount");
	$("#pre_amount").val(amount);

}

function hideID(id) {
	$("#" + id).hide();
}
function getUserListUnverifiedUsers() {
	var requestData = 'page=' + (page - 1) + '&perPage=' + perPage;
	console.log("request Data " + requestData);

	$
			.ajax({

				type : "GET",
				url : "/Admin/Ajax/UnverifiedUser",
				contentType : "application/json",
				data : requestData,
				datatype : 'json',

				success : function(response) {
					console.log("Verified Users Response "
							+ JSON.stringify(response));
					$('#userList').html("");
					var size = response.length
					console.log("SIZE :: " + size);
					console.log("Head Created");

					for (i = 0; i < size; i++) {
						console.log("Body Created " + i);
						var u = response[i];
						$('#userList')
								.append(
										'' + '<tr>' + '<td><b>User Name : </b>'
												+ u.userDetail.firstName
												+ ''
												+ u.userDetail.middleName
												+ u.userDetail.lastName
												+ '<br />'
												+ '<b>Mobile Number :</b> '
												+ u.userDetail.contactNo
												+ '<br /> <b>Email'
												+ 'ID :</b>'
												+ u.userDetail.email
												+ '</td>'
												+ '<td>'
												+ u.emailStatus
												+ '</td>'
												+ '<td>'
												+ u.userDetail.address
												+ '</td>'
												+ '<td>Balance : 1833 -/ Rs.<br />Status :'
												+ u.mobileStatus + '' + '</td>'
												+ '</tr>' + '');
					}
					console.log("Table Closes");
					$("#loading").hide();
				},
				error : function(response) {
					console.log("HERE INSIDE ERROR " + response.responseText);
					location.reload();
				}
			});
}