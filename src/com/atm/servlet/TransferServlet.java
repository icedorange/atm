package com.atm.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atm.service.CardService;
import com.atm.util.Constant;

/**
 * Servlet implementation class TransferServlet
 */
@WebServlet("/TransferServlet")
public class TransferServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public void toStart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		//创建令牌
		String token = String.valueOf(UUID.randomUUID());
		session.setAttribute("token", token);
		request.getRequestDispatcher("/WEB-INF/basic/transfer.jsp").forward(request, response);
		return;
	}
	
	public void toTransfer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		String cardNum = request.getParameter("aimNum");
		String money = request.getParameter("change");
		if (money == null || money.equals("") || cardNum == null || cardNum.equals("")) {
			request.getRequestDispatcher("/WEB-INF/basic/transfer.jsp").forward(request, response);
			return;
		}
		//获取表单令牌
		String token = request.getParameter("token");
		//获取session令牌
		String token1 = String.valueOf(session.getAttribute("token"));
		//校验令牌
		if(!(token!=null && token.equals(token1))){
			request.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(request, response);
			return;
		}
		Object obj = session.getAttribute("cardId");
		CardService cardService = new CardService();
		int result = cardService.transferCheck(Integer.parseInt(String.valueOf(obj)), cardNum,
				Integer.parseInt(money));
		session.removeAttribute("token");
		if (result == Constant.FAIL || result == Constant.RUNOUT || result == Constant.CARD_ERROR) {
			session.setAttribute("msg", "余额不足、输入不正确或对方卡号不存在");
			request.getRequestDispatcher("/WEB-INF/basic/transfer.jsp").forward(request, response);
			return;
		}
		request.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(request, response);
		return;
	}

}
