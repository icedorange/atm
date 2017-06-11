<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>取款</title>
</head>
<body>
	<form
		action="<%=request.getContextPath()%>/QukuanServlet?method=toQukuan" method="get">
		<table>
			<%-- <tr>
				<td>您的余额为：<%=session.getAttribute("balance")%>
				</td>
			</tr> --%>
			<tr>
				<td>请输入取款金额：</td>
				<td><input id="change" name="change" type="text"></td>
				<td>
					<%
						Object msg = session.getAttribute("msg");
						if (msg != null) {
							out.print("<span style = \"color:red\">");
							out.print(String.valueOf(msg));
							out.print("</span>");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="确认"></td>
			</tr>
		</table>
	</form>

</body>
</html>