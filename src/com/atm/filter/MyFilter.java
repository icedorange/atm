package com.atm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class MyFilter
 */

public class MyFilter implements Filter {

    /**
     * Default constructor. 
     */
    public MyFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		System.out.println("destory...");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String url = req.getServletPath();
		//判断session是否有效
		if(url.equals("/QueryServlet") 
				|| url.equals("/QukuanServlet")
				|| url.equals("/CunkuanServlet")
				|| url.equals("/MainServlet")
				|| url.equals("/TransferServlet")
				){
			if(session.getAttribute("cardId")==null){
				if(session.getAttribute("msg") == null){
					session.setAttribute("msg", "登陆超时，请重新登陆");
				}
				res.sendRedirect("login.jsp");
				return;
			}
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("init...");
	}

}
