package com.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atm.service.CardService;
import com.atm.util.Constant;

/**
 * Servlet implementation class TransferServlet
 */
public class TransferServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TransferServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 跳转路径
		String method = request.getParameter("method");
		if (method == null || method.equals("")) {
			request.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(request, response);
			return;
		}
		if (method.equals("toStart")) {
			request.getRequestDispatcher("/WEB-INF/basic/transfer.jsp").forward(request, response);
			return;
		}
		if (method.equals("transfer")) {
			String cardNum = request.getParameter("cardNum");
			String money = request.getParameter("money");
			if (money == null || money.equals("") || cardNum == null || cardNum.equals("")) {
				return;
			}
			HttpSession session = request.getSession(true);
			Object obj = session.getAttribute("cardId");
			CardService cardService = new CardService();
			int result = cardService.transferCheck(Integer.parseInt(String.valueOf(obj)), cardNum,
					Integer.parseInt(money));
			if (result == Constant.FAIL || result == Constant.RUNOUT || result == Constant.CARD_ERROR) {
				session.setAttribute("msg", "余额不足、输入不正确或对方卡号不存在");
				request.getRequestDispatcher("/WEB-INF/basic/transfer.jsp").forward(request, response);
				return;
			}
			request.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(request, response);
			return;
		}
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
