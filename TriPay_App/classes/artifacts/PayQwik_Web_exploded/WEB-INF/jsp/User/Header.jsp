<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*"  isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<div class="topline"></div>
	<nav class="navbar navbar-default"
		style="background: #08bbc7; min-height: 40px; margin-bottom: 0px;">
		<div class="container">
			<div class="navbar-header col-md-4 col-xs-8">
				<a class="navbar-brand" href="${pageContext.request.contextPath}/User/Home"><img
					src='<c:url value="/resources/images/logo_ios.png"/>' alt="logo" "></a>
			</div> 
			
			<ul class="nav navbar-nav navbar-right" style="float: left;">

				<li><a href="<c:url value="${pageContext.request.contextPath}/User/LoadMoney"/>"> Rs. ${user.accountDetail.balance}<img
						src='<c:url value="/resources/images/main/wallet.png"/>'
						alt="wallet" style="margin-left: 28px; width: 40px; "><p>Add Money</p>
						 </a></li>
				<div class="dropdown">
				
					<li><a href="#">${user.userDetail.firstName}
					<c:if test="${user.userDetail.image ne null}">
                                    <img  src="<c:url value='${user.userDetail.image}'/>"  class="img-circle" width="60" height="60" />
                    </c:if>
                     <c:if test="${user.userDetail.image eq null}">
                     				<img src='<c:url value="/resources/images/main/account.png"/>'
							style="padding-top: 18px; padding-left: 20px; margin-bottom: 10px;" />
	
                     </c:if>
                     </a></li>
					<div class="dropdown-content">
						<a href="${pageContext.request.contextPath}/User/Home"><i class="fa fa-user"></i> Account</a> <a href="<c:url value="/User/Settings"/>"><i
							class="fa fa-cog"></i> Settings</a> <a href='<c:url value="/User/Receipts"/>'><i
							class="fa fa-files-o"></i> My Receipts</a> <a href="${pageContext.request.contextPath}/User/InviteFriends"><i
							class="fa fa-users"></i> Invite Friends</a> <a href="${pageContext.request.contextPath}/logout">Logout</a>
					</div>
				</div>
			</ul>
		</div>
		<!-- /.container-fluid -->
	</nav>
