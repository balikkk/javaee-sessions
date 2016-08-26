<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home Page</title>
</head>
<body>
	<c:set var="currentUser" value="${sessionScope['User']}" />

	<h3>Hi ${currentUser.name}</h3>
	<strong>Your Email</strong>: ${currentUser.email}
	<br>
	<strong>Your Country</strong>: ${currentUser.country}
	<br>
	<strong>Your Id</strong>: ${currentUser.id}
	<br>
	<br>
	
</body>
</html>