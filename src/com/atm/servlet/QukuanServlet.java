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
 * Servlet implementation class QukuanServlet
 */
@WebServlet("/QukuanServlet")
public class QukuanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QukuanServlet() {
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
			request.getRequestDispatcher("/WEB-INF/basic/qukuan.jsp").forward(request, response);
			return;
		}
		if (method.equals("toQukuan")) {
			HttpSession session = request.getSession(true);
			String money = request.getParameter("change");
			if (money == null) {
				session.setAttribute("msg", "输入不正确");
				request.getRequestDispatcher("/WEB-INF/basic/qukuan.jsp").forward(request, response);
				return;
			}

			CardService cardService = new CardService();
			Object obj = session.getAttribute("cardId");
			int result = cardService.getMoneyFromCard(Integer.parseInt(String.valueOf(obj)), Integer.parseInt(money));
			if (result == Constant.FAIL || result == Constant.RUNOUT) {
				session.setAttribute("msg", "余额不足或输入不正确");
				request.getRequestDispatcher("/WEB-INF/basic/qukuan.jsp").forward(request, response);
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
