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
	<a href="${pageContext.request.contextPath}/card/query.htm">查询余额</a>
	<br />
	<a href="${pageContext.request.contextPath}/card/qukuanStart.htm">取款</a>
	<br />
	<a href="${pageContext.request.contextPath}/card/cunkuanStart.htm">存款</a>
	<br />
	<a href="${pageContext.request.contextPath}/card/transferStart.htm">转账</a>
	<br />
	<a href="${pageContext.request.contextPath}/card/login.htm">退出</a>
</body>
</html>