<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>主页</title>
</head>
<body>
	<h3>
		欢迎<%=session.getAttribute("cardNum")%>使用ATM取款机
	</h3>
	<a href="<%=request.getContextPath()%>/QueryServlet">查询余额</a>
	<br />
	<a href="<%=request.getContextPath()%>/QukuanServlet?method=toStart">取款</a>
	<br />
	<a href="<%=request.getContextPath()%>/CunkuanServlet?method=toStart">存款</a>
	<br />
	<a href="<%=request.getContextPath()%>/TransferServlet?method=toStart">转账</a>
	<br />
	<a href="<%=request.getContextPath()%>/login.jsp">退出</a>
</body>
</html>