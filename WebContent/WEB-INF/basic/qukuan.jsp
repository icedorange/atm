<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>取款</title>
<script type="text/javascript">
	function check() {
		document.getElementById("submit").disabled = true;
		return true;
	}
</script>
</head>
<body>
	<form
		action="${pageContext.request.contextPath }/QukuanServlet?method=toQukuan"
		onsubmit="return check();" method="post">
		<table>
			<tr>
				<td>请输入取款金额：</td>
				<td><input id="change" name="change" type="text"></td>
				<td><span style="color: red">${SessionScope.msg }</span></td>
			</tr>
			<tr>
				<td><input type="hidden" name="token" value="${token }"></td>
			</tr>
			<tr>
				<td><input type="submit" value="确认"></td>
			</tr>
		</table>
	</form>

</body>
</html>