<%@page import="com.atm.service.CardService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>login_action</title>
</head>
<body>

	<%!private CardService cardService = new CardService();%>

	<%
		String cardNum = request.getParameter("cardNum");
		String password = request.getParameter("password");

		int code = cardService.check(cardNum, password);
	%>

	<%
		String msg = null;
		if (code == 1) {//登录成功
		msg = "登陆成功";
		int cardId = cardService.selectCardIdByCardNum(cardNum);
		session.setAttribute("cardId", cardId);
		out.write("<SCRIPT LANGUAGE=\"JavaScript\">alert(\"" + msg
				+ "\");location.href=\"main.jsp\"</SCRIPT>");
		
	%>
	<%
		} else {//登录失败
			msg = "用户名或密码不正确";
			out.write("<SCRIPT LANGUAGE=\"JavaScript\">alert(\"" + msg
					+ "\");history.back()</SCRIPT>");
		}
	%>

</body>
</html>