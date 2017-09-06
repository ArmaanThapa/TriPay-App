<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
<link rel="icon" href='<c:url value="/resources/images/favicon.png"/>' type="image/png" />
<title><spring:message code="page.title.static.404"/></title>
</head>
<div class="jumbotron">
<h1 class="text text-warning">Error 404 <span class="glyphicon glyphicon-warning-sign"></span></h1>
<p class="text text-warning">The resource you are trying to access is either not available or no longer exists</p>
</div>
</html>