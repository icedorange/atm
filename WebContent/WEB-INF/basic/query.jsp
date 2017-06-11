<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查询余额</title>
</head>
<body>


	<h3><%=session.getAttribute("cardNum")%>你好
	</h3>
	<h3>
		您的余额为：<%=session.getAttribute("balance")%></h3>
	<a href="<%=request.getContextPath()%>/MainServlet">返回主页</a>
</body>
</html>