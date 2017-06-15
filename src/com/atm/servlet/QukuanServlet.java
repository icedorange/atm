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
		
		HttpSession session = request.getSession(true);
		
		if (method == null || method.equals("")) {
			request.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(request, response);
			return;
		}
		if (method.equals("toStart")) {
			//创建令牌
			String token = String.valueOf(UUID.randomUUID());
			session.setAttribute("token", token);
			
			request.getRequestDispatcher("/WEB-INF/basic/qukuan.jsp").forward(request, response);
			return;
		}
		if (method.equals("toQukuan")) {
			String money = request.getParameter("change");
			//获取表单令牌
			String token = request.getParameter("token");
			if (money == null) {
				session.setAttribute("msg", "输入不正确");
				request.getRequestDispatcher("/WEB-INF/basic/qukuan.jsp").forward(request, response);
				return;
			}
			//获取session令牌
			String token1 = String.valueOf(session.getAttribute("token"));
			//校验令牌
			if(!(token!=null && token.equals(token1))){
				request.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(request, response);
				return;
			}
			CardService cardService = new CardService();
			Object obj = session.getAttribute("cardId");
			int result = cardService.getMoneyFromCard(Integer.parseInt(String.valueOf(obj)), Integer.parseInt(money));
			//移除令牌
			session.removeAttribute("token");
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
