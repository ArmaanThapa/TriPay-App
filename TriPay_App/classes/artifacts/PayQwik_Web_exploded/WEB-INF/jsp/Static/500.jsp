<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
<link rel="icon" href='<c:url value="/resources/images/favicon.png"/>' type="image/png" />
<title><spring:message code="page.title.static.505"/></title>
</head>
<div class="jumbotron">
<h1 class="text text-warning">Sorry <span class="glyphicon glyphicon-exclamation-sign"></span></h1>
<h2>It's not you</h2>
<h2>It's us</h2>
<p class="text text-warning">We are experiencing and internal server problem</p>
<p class="text text-warning">Please try again later</p>
</div>
</html>