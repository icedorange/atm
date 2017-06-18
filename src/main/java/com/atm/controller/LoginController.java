package com.atm.controller;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.atm.bean.Card;
import com.atm.service.CardService;
import com.atm.util.Constant;

@Controller
@RequestMapping("/card")
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Resource
	CardService cardService;

	@RequestMapping(value = "/login.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(ModelMap model, Card card) {
		return "login";
	}

	@RequestMapping(value = "/checkCardNum.htm", method = { RequestMethod.GET, RequestMethod.POST })
	
	public @ResponseBody JSONObject checkCardNum(Model model, String cardNum, HttpServletResponse response, HttpSession session)
			throws IOException {
		int code = cardService.selectCardIdByCardNum(cardNum);
		JSONObject jo = new JSONObject();
		if (code == Constant.FORMAT_ERROR || code == Constant.CARD_ERROR) {
			jo.put("code", code);
			jo.put("msg", "卡号不正确");
			return jo;
			// return "login";
		}
		return jo;
	}

	@RequestMapping(value = "/check.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String check(Model model, Card card, HttpSession session) {
		int code = cardService.check(card.getcardNum(), card.getPassword());
		logger.debug("用户请求登陆服务器返回结果", code);
		if (code == 1) {
			session.setAttribute("cardId", code);
			session.setAttribute("cardNum", card.getcardNum());
			return "main";
		}
		JSONObject jo = new JSONObject();
		jo.put("code", code);
		jo.put("msg", "卡号不正确");
		return "login";
	}

	@RequestMapping(value = "/main.htm")
	public String main() {
		return "main";
	}

	/**
	 * 查询余额
	 * 
	 * @param model
	 * @param card
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/query.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String query(Model model, HttpSession session) {
		int cardId = Integer.parseInt(String.valueOf(session.getAttribute("cardId")));
		int balance = cardService.getBalanceById(cardId);
		session.setAttribute("balance", balance);
		return "query";
	}

	/**
	 * 取款操作
	 * 
	 * @param model
	 * @param card
	 * @return
	 */
	@RequestMapping(value = "/qukuanStart.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String qukuanStart(HttpSession session) {
		String token = String.valueOf(UUID.randomUUID());
		session.setAttribute("token", token);
		return "qukuan";
	}

	@RequestMapping(value = "/qukuan.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String qukuan(Model model, String change, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {

		// 获取表单令牌
		String token = request.getParameter("token");
		if (change == null) {
			session.setAttribute("msg", "输入不正确");
			return "qukuan";
		}
		// 获取session令牌
		String token1 = String.valueOf(session.getAttribute("token"));
		// 校验令牌
		if (!(token != null && token.equals(token1))) {
			return "main";
		}
		Object obj = session.getAttribute("cardId");
		int result = cardService.getMoneyFromCard(Integer.parseInt(String.valueOf(obj)), Integer.parseInt(change));
		// 移除令牌
		session.removeAttribute("token");
		if (result == Constant.FAIL || result == Constant.RUNOUT) {
			session.setAttribute("msg", "余额不足或输入不正确");
			return "qukuan";
		}
		return "main";
	}

	/**
	 * 存款操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cunkuanStart.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String cunkaunStart(HttpSession session) {
		String token = String.valueOf(UUID.randomUUID());
		session.setAttribute("token", token);
		return "cunkuan";
	}

	@RequestMapping(value = "/cunkuan.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String cunkuan(String change, HttpSession session,HttpServletResponse response,
			HttpServletRequest request) {
		
		if (change == null || change.equals("")) {
			return "cunkuan";
		}
		//获取表单令牌
		String token = request.getParameter("token");
		//获取session令牌
		String token1 = String.valueOf(session.getAttribute("token"));
		//校验令牌
		if(!(token!=null && token.equals(token1))){
			return "main";
		}
		Object obj = session.getAttribute("cardId");
		int result = cardService.putMoneyInCard(Integer.parseInt(String.valueOf(obj)), Integer.parseInt(change));
		session.removeAttribute("token");
		if (result == Constant.FAIL) {
			session.setAttribute("msg", "输入不正确");
			return "cunkuan";
		}
		return "main";
	}

	@RequestMapping(value = "/transferStart.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String transferStart(HttpSession session) {
		String token = String.valueOf(UUID.randomUUID());
		session.setAttribute("token", token);
		return "transfer";
	}

	@RequestMapping(value = "/transfer.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String transfer(String aimNum, String change, HttpSession session,HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		if (change == null || change.equals("") || aimNum == null || aimNum.equals("")) {
			return "transfer";
		}
		//获取表单令牌
		String token = request.getParameter("token");
		//获取session令牌
		String token1 = String.valueOf(session.getAttribute("token"));
		//校验令牌
		if(!(token!=null && token.equals(token1))){
			return "main";
		}
		Object obj = session.getAttribute("cardId");
		int result = cardService.transferCheck(Integer.parseInt(String.valueOf(obj)), aimNum,
				Integer.parseInt(change));
		session.removeAttribute("token");
		if (result == Constant.FAIL || result == Constant.RUNOUT || result == Constant.CARD_ERROR) {
			session.setAttribute("msg", "余额不足、输入不正确或对方卡号不存在");
			return "transfer";
		}
		return "main";
	}

}
