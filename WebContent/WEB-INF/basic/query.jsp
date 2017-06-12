<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查询余额</title>
</head>
<body>


	<h3>${cardNum }你好
	</h3>
	<h3>
		您的余额为：${balance }</h3>
	<a href="${pageContext.request.contextPath }/MainServlet">返回主页</a>
</body>
</html>