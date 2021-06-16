<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Restaurant Main</title>
</head>
<body>
	<h1>${info.memberId }</h1>
	<h1>${info.memberName }</h1>
	<h1>${info.memberEtc }</h1>
	<h1>${info.categoryCode }</h1>
	<h1>${info.category }</h1>
	<h1>${info.location }</h1>
	<h1>${sessionScope.access }</h1>
</body>
</html>