package com.atm.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseServlet
 */
@WebServlet("/BaseServlet")
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String method = req.getParameter("method");
		Class<?> clazz = this.getClass();
		try {
			Method mt = clazz.getMethod(method, HttpServletRequest.class, 
					HttpServletResponse.class);
			mt.invoke(this, req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
