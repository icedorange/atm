<%@page import="com.atm.service.CardService"%>
<%@page import="com.atm.util.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>存款操作</title>
</head>
<body>

	<%!private CardService cardService = new CardService();%>
	<%
		int change = Integer.parseInt(request.getParameter("change"));

		int cardId = Integer.parseInt(String.valueOf(session.getAttribute("cardId")));
		int result = cardService.putMoneyInCard(cardId, change);
		if(result!= Constant.SUCCESS){
			response.sendRedirect("cunkuan.jsp");
			return;
		}
		int money = cardService.getBalanceById(cardId);
	%>
	<h3>
		余额为：<%=money%></h3>
	<a href="main.jsp">返回主页</a>
</body>
</html>