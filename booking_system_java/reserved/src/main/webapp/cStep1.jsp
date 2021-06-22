<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ReserveDate Select Page</title>
<script src="js/js.js"></script>
<link href="css/style.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<h1>${uCode } ${rCode } </h1>
		<input type = "hidden" name = "uCode" value = '${uCode }'/>
		<input type = "hidden" name = "rCode" value = '${rCode }'/>
		
	${list }
</body>
</html>