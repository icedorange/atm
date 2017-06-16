package com.atm.controller;

import java.io.IOException;

import javax.annotation.Resource;
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
import com.atm.javabean.Card;
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
	@ResponseBody
	public  JSONObject checkCardNum(Model model, Card card, HttpServletResponse response,HttpSession session) throws IOException {
		logger.debug("用户请求用户名，密码登陆");
		logger.info("info");
		logger.error("error");
		int code = cardService.selectCardIdByCardNum(card.getcardNum());
		logger.debug("用户请求登陆服务器返回结果", code);
		JSONObject jo = new JSONObject();
		if (code != Constant.FORMAT_ERROR || code!=Constant.CARD_ERROR) {
			session.setAttribute("cardId", code);
			jo.put("code", code);
			jo.put("msg", "asdfsfd");
			return jo;
			// return "login";
		}
		return jo;
	}

	@RequestMapping(value = "/check.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String check(Model model, Card card, HttpSession session) {
		logger.debug("用户请求用户名，密码登陆");
		int code = cardService.check(card.getcardNum(), card.getPassword());
		logger.debug("用户请求登陆服务器返回结果", code);
		if (code == 1) {
			
			return "main";
		}
		return "login";
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
	public String qukuanStart(Model model, HttpSession session) {
		return "qukuan";
	}

	@RequestMapping(value = "/qukuan.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String qukuan(Model model, String money, HttpSession session) {
		Integer money1 = Integer.parseInt(money);
		int cardId = Integer.parseInt(String.valueOf(session.getAttribute("cardId")));
		int result = cardService.getMoneyFromCard(cardId, money1);
		if (result == Constant.SUCCESS) {
			return "main";
		}
		return "query";
	}

	/**
	 * 存款操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cunkuanStart.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String cunkaunStart() {
		return "cunkuan";
	}

	@RequestMapping(value = "/cunkuan.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String cunkuan(String money, HttpSession session) {
		Integer money1 = Integer.parseInt(money);
		int cardId = Integer.parseInt(String.valueOf(session.getAttribute("cardId")));
		int result = cardService.putMoneyInCard(cardId, money1);
		if (result == Constant.SUCCESS) {
			return "main";
		}
		return "cunkuan";
	}

	@RequestMapping(value = "/transferStart.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String transferStart() {
		return "transfer";
	}

	@RequestMapping(value = "/transfer.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String transfer(String cardNum, String money,HttpSession session) throws Exception {
		int money1 = Integer.parseInt(money);
		int cardId = Integer.parseInt(String.valueOf(session.getAttribute("cardId")));
		int result = cardService.transferCheck(cardId, cardNum, money1);
		if (result == Constant.SUCCESS) {
			return "main";
		}
		return "transfer";
	}

}
