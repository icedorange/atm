package com.atm.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atm.service.CardService;
import com.atm.util.Constant;

/**
 * Servlet implementation class CunkuanServlet
 */
@WebServlet("/CunkuanServlet")
public class CunkuanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CunkuanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		// 跳转路径
		String method = request.getParameter("method");
		if (method == null || method.equals("")) {
			request.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(request, response);
			return;
		}
		if (method.equals("toStart")) {
			//创建令牌
			String token = String.valueOf(UUID.randomUUID());
			session.setAttribute("token", token);
			request.getRequestDispatcher("/WEB-INF/basic/cunkuan.jsp").forward(request, response);
			return;
		}
		if (method.equals("toCunkuan")) {
			String money = request.getParameter("change");
			if (money == null || money.equals("")) {
				request.getRequestDispatcher("/WEB-INF/basic/cunkuan.jsp").forward(request, response);
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
			CardService cardService = new CardService();
			Object obj = session.getAttribute("cardId");
			int result = cardService.putMoneyInCard(Integer.parseInt(String.valueOf(obj)), Integer.parseInt(money));
			session.removeAttribute("token");
			if (result == Constant.FAIL) {
				session.setAttribute("msg", "输入不正确");
				request.getRequestDispatcher("/WEB-INF/basic/cunkuan.jsp").forward(request, response);
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
