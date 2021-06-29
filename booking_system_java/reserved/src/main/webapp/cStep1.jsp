<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Reserve Step 1</title>
<script src="js/js.js"></script>
<link href="css/style.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<div id="reservezone" data-recode="">
	${dayList }
		<div id ="menuList"></div>
		<div id ="orderList"></div>
	</div>
	<input type ="button" value="예약하기" onClick= "callData()">
</body>
</html>