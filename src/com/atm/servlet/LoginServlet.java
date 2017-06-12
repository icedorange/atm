package com.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atm.service.CardService;
import com.atm.util.Constant;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cardNum = request.getParameter("cardNum");
		String password = request.getParameter("password");
		HttpSession session = request.getSession(true);

		CardService cardService = new CardService();
		int result = cardService.check(cardNum, password);
		if (result != Constant.SUCCESS) {
			session.setAttribute("msg", "用户名或密码不正确");
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		int cardId = cardService.selectCardIdByCardNum(cardNum);
		session.setAttribute("cardNum", cardNum);
		session.setAttribute("cardId", cardId);
		request.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
