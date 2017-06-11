package com.atm.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class MyListener
 *
 */

public class MyListener implements HttpSessionAttributeListener, HttpSessionListener {

	/**
	 * Default constructor.
	 */
	public MyListener() {
	}

	private static int count = 0;

	private Set<Integer> set = new HashSet<>();

	private Map<Integer, Object> userMap = new HashMap<>();

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent se) {
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
	}

	/**
	 * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent se) {
		String name = se.getName();
		if (name.equals("cardId")) {
			int value = Integer.parseInt(String.valueOf(se.getValue()));
			count++;
			set.add(value);
			// 获得cardId的session
			Object obj = userMap.get(value);
			if (obj == null) {
				userMap.put(value, se.getSession());
			} else {
				// 将obj转为session类型
				HttpSession session = (HttpSession) obj;
				// 将此session中的属性删除
				session.removeAttribute("cardId");
				set.add(value);
				userMap.put(value, se.getSession());
				System.out.println(se.getValue() + "用户被迫下线");
				session.setAttribute("msg", "您的账户在其它浏览器登录，此会话被下线");
			}

			System.out.println("人气（30分钟内活跃的用户）A：" + count);
			System.out.println("在线人数A：" + set.size());
			System.out.println("在线账户数A：" + userMap.size());
		}
	}

	/**
	 * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent se) {
		String name = se.getName();
		if (name == "cardId") {
			int value = Integer.parseInt(String.valueOf(se.getValue()));
			count--;
			set.remove(value);
			// 获得cardId的session
			Object obj = userMap.get(value);
			// 将obj转为session类型
			HttpSession session = (HttpSession) obj;
			// 将此session中的属性删除
			session.removeAttribute("cardId");
			System.out.println("人气（30分钟内活跃的用户）R：" + count);
			System.out.println("在线账户数目R：" + set.size());
			System.out.println("在线用户数R：" + userMap.size());
		}
	}

	/**
	 * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent se) {
	}

}
