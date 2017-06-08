<%@page import="com.atm.service.CardService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%!private CardService cardService = new CardService();%>
	<%
		int change = Integer.parseInt(request.getParameter("change"));
		Integer cardId = (Integer) session.getAttribute("cardId");
		int temp = cardService.getMoneyFromCard(cardId, change);
		int money = cardService.getBalanceById(cardId);
	%>
	<h3>余额为：</h3>
	<%=money%>
	<br>
	<a href="main.jsp">返回主页</a>
</body>
</html>