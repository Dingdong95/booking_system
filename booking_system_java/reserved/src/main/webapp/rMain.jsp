<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Restaurant Main</title>
</head>
<body>
<input class = "res_Check_Btn" type ="button" placeholder = "예약 대기자 현황" onClick ="showDiv(0)" >
<input class = "res_Check_Btn"  type = "button" placeholder = "금일 예약자 현황" onClick ="showDiv(1)">
<div>${watingList }</div>
<div>${todayList }</div>
</body>
</html>