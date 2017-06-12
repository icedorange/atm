package com.atm.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.atm.service.CardService;
import com.atm.util.Constant;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	CardService cardService = new CardService();
	
	/**
	 * 验证卡号是否存在（ajax）
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void checkCardNum(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException{
		String cardNum = req.getParameter("cardNum");
		
		int result = cardService.selectCardIdByCardNum(cardNum);
		if(result==Constant.CARD_ERROR ||result==Constant.FORMAT_ERROR){
			PrintWriter out = resp.getWriter();
			JSONObject jo = new JSONObject();
			jo.put("code", result);
			jo.put("msg", "您输入的卡号不存在，请重新输入！");
			out.print(jo.toJSONString());
		}
	}

	/**
	 * 提交表单验证
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	public void login(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException{
		String cardNum = req.getParameter("cardNum");
		String password = req.getParameter("password");
		HttpSession session = req.getSession(true);

		int result = cardService.check(cardNum, password);
		if (result != Constant.SUCCESS) {
			session.setAttribute("msg", "用户名或密码不正确");
			resp.sendRedirect(req.getContextPath() + "/login.jsp");
			return;
		}
		int cardId = cardService.selectCardIdByCardNum(cardNum);
		session.setAttribute("cardNum", cardNum);
		session.setAttribute("cardId", cardId);
		req.getRequestDispatcher("/WEB-INF/basic/main.jsp").forward(req, resp);

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
