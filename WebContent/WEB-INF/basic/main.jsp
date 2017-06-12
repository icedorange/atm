<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>主页</title>
</head>
<body>
	<h3>
		欢迎${cardNum}使用ATM取款机
	</h3>
	<a href="${pageContext.request.contextPath}/QueryServlet">查询余额</a>
	<br />
	<a href="${pageContext.request.contextPath}/QukuanServlet?method=toStart">取款</a>
	<br />
	<a href="${pageContext.request.contextPath}/CunkuanServlet?method=toStart">存款</a>
	<br />
	<a href="${pageContext.request.contextPath}/TransferServlet?method=toStart">转账</a>
	<br />
	<a href="${pageContext.request.contextPath}/login.jsp">退出</a>
</body>
</html>