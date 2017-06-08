<%@page import="com.atm.service.CardService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>存款</title>
</head>
<body>

	<form action="cunkuan_action.jsp">
		<table>
			<tr>
				<td>请输入存款金额：</td>
				<td><input id="change" name="change" type="text"></td>
			</tr>
			<tr>
				<td><input type="submit" value="确认"></td>
			</tr>
		</table>
	</form>
	
</body>
</html>