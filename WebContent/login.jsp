<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>login</title>
</head>
<body>
	<form action="LoginServlet" method="post">
		<table>
			<tr>
				<td>卡号：</td>
				<td><input type="text" name="cardNum" id="cardNum" /></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input type="text" name="password" id="password" /></td>
			</tr>
			<tr>
				<td></td>
				<td><span style="color: red">${SessionScope.msg }</span></td>
			</tr>
			<tr>
				<td><input type="submit" value="提交" /></td>
			</tr>
		</table>
	</form>
</body>
</html>