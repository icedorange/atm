package com.atm.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.atm.javabean.Card;
import com.atm.service.CardService;

@Controller
@RequestMapping("/card")
public class CardController {
	
	private static Logger logger =LoggerFactory.getLogger(CardController.class);

	@Resource
	CardService cardService;

	@RequestMapping(value = "/login.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(ModelMap model,Card card) {
		return "login";
	}

	@RequestMapping(value = "/checkCardNum.do", method = { RequestMethod.GET, RequestMethod.POST })
	public void checkCardNum(Model model, Card card,HttpServletResponse response) throws IOException {
		logger.debug("用户请求用户名，密码登陆");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
		int code = cardService.selectCardIdByCardNum(card.getcardNum());
		logger.debug("用户请求登陆服务器返回结果",code);
		if (code != 1) {
			PrintWriter out = response.getWriter();
			JSONObject jo = new JSONObject();
			jo.put("code", code);
			jo.put("msg", "asdfsfd");
			out.print(jo.toJSONString());
//			return "login";
		}
//		return "login";
	}

	@RequestMapping(value = "/check.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String check(Model model, Card card) {
		logger.debug("用户请求用户名，密码登陆");
		int code = cardService.check(card.getcardNum(), card.getPassword());
		logger.debug("用户请求登陆服务器返回结果",code);
		if(code== 1){
			return "main";
		}
		return "login";
	}

}
