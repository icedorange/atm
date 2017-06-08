package com.atm.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.atm.javabean.Card;
import com.atm.javabean.Record;
import com.atm.mapper.CardMapper;
import com.atm.mapper.RecordMapper;
import com.atm.util.Constant;
import com.atm.util.MD5Util;
import com.atm.util.SessionUtil;

public class CardService {

	// 创建数据库连接
	SqlSession se = SessionUtil.getSession(true);

	/**
	 * 根据cardNum，查询ID
	 * 
	 * @param cardNum
	 * @return
	 */
	public int selectCardIdByCardNum(String cardNum) {
		if (cardNum.length() != 16) { // ？判断内容是数字，后者是输入内容不能有空格、字符串。。
			return Constant.FORMAT_ERROR;
		}
		CardMapper mapper = se.getMapper(CardMapper.class); // ？作为全局变量会不会有线程安全问题
		Card card = mapper.selectCardIdByCardNum(cardNum); // ?返回类型是int的话，如果数据库查不到回返回null，出现错误
		if (card == null) {
			return Constant.CARD_ERROR;
		}
		return card.getId();
	}

	/**
	 * 根据ID，账号的所有信息
	 * 
	 * @param cardId
	 * @return
	 */
	public Card selectCardByCardId(int cardId) {
		CardMapper mapper = se.getMapper(CardMapper.class);
		Card card = mapper.selectCardByCardId(cardId);
		return card;
	}
	
	/**
	 * 检查密码是否正确
	 * @param cardNum
	 * @param password
	 * @return
	 */
	public int check(String cardNum,String password){
		if (cardNum.length() != 16) { // ？判断内容是数字，后者是输入内容不能有空格、字符串。。
			return Constant.FORMAT_ERROR;
		}
		if(password.length()!=6){
			return Constant.FORMAT_ERROR;
		}
		CardMapper mapper = se.getMapper(CardMapper.class); // ？作为全局变量会不会有线程安全问题
		Card card = mapper.selectCardIdByCardNum(cardNum); // ?返回类型是int的话，如果数据库查不到回返回null，出现错误
		if (card == null) {
			return Constant.CARD_ERROR;
		}
		String pass = card.getPassword();
		if(pass.equals(MD5Util.MD5(password+"atm"))){
			return Constant.SUCCESS;
		}
		return Constant.PASSWORD_ERROR;
	}
	
	/**
	 * 检查密码是否正确
	 * @param cardId
	 * @param password
	 * @return
	 */
	public int checkPassword(int cardId, String password){
		if(password.length()!=6){
			return Constant.FORMAT_ERROR;
		}
		Card card = selectCardByCardId(cardId);
		String pass = card.getPassword();
		if(pass.equalsIgnoreCase(MD5Util.MD5(password+"atm"))){
			return Constant.SUCCESS;
		}
		return Constant.PASSWORD_ERROR;
	}

	/**
	 * 根据ID，查询余额
	 * 
	 * @param cardId
	 * @return
	 */
	public int getBalanceById(int cardId) {
		Card card = selectCardByCardId(cardId);
		int money = card.getMoney()/100;
		return money;
	}

	/**
	 * 根据ID，取款
	 * 
	 * @param cardId
	 * @param money
	 * @return
	 */
	public int getMoneyFromCard(int cardId, int money) {
		if (money <= 0) {
			return Constant.FAIL;
		}
		money = money * 100;
		int change = 0;
		CardMapper mapper = se.getMapper(CardMapper.class);
		while (true) {
			Card card = selectCardByCardId(cardId);
			change = card.getMoney();
			if (change < money) {
				return Constant.RUNOUT;
			}
			int version = card.getVersion();
			int temp = mapper.getMoneyFromCard(cardId, version, money);
			if (temp > 0) {
				break;
			}
		}
		RecordMapper reMapper = se.getMapper(RecordMapper.class);
		reMapper.insertTrans(cardId, cardId, 1, money);
		
		return Constant.SUCCESS;
	}

	/**
	 * 根据ID，存款
	 * 
	 * @param cardId
	 * @param money
	 * @return
	 */
	public int putMoneyInCard(int cardId, int money) {
		if (money <= 0) {
			return Constant.FAIL;
		}
		money = money * 100;
		CardMapper mapper = se.getMapper(CardMapper.class);
		while (true) {
			Card card = selectCardByCardId(cardId);
			int version = card.getVersion();
			int temp = mapper.getMoneyFromCard(cardId, version, -money);
			if (temp > 0) {
				break;
			}
		}
		RecordMapper reMapper = se.getMapper(RecordMapper.class);
		reMapper.insertTrans(cardId, cardId, 2, money);
		return Constant.SUCCESS;
	}
	
	/**
	 * 转账，判断转账目标是否存在
	 * @param cardId
	 * @param aimNum
	 * @param money
	 * @return
	 */
	public int transferCheck(int cardId,String aimNum,int money){
		if (money <= 0) {
			return Constant.FAIL;
		}
		int aimId = selectCardIdByCardNum(aimNum);
		if (aimId == Constant.FORMAT_ERROR) { 
			return Constant.FORMAT_ERROR;
		}
		if (aimId == Constant.CARD_ERROR) {
			return Constant.CARD_ERROR;
		}
		int temp = transfer(cardId,aimId,money);
		return temp;
	}
	
	public int transfer(int cardId,int aimId,int money){
		money = money * 100;
		int change = 0;
		CardMapper mapper = se.getMapper(CardMapper.class);
		while (true) {
			Card card = selectCardByCardId(cardId);
			change = card.getMoney();
			if (change < money) {
				return Constant.RUNOUT;
			}
			int version = card.getVersion();
			int temp = mapper.getMoneyFromCard(cardId, version, money);
			if (temp > 0) {
				break;
			}
		}
		while (true) {
			Card card = selectCardByCardId(aimId);
			int version = card.getVersion();
			int temp = mapper.getMoneyFromCard(aimId, version, -money);
			if (temp > 0) {
				break;
			}
		}
		RecordMapper reMapper = se.getMapper(RecordMapper.class);
		reMapper.insertTrans(cardId, aimId, 3, money);
		return Constant.SUCCESS;
	}
	
	public List<Record> getTranfer(int operId,int aimId, int operType){
		RecordMapper mapper = se.getMapper(RecordMapper.class);
		return mapper.getTranfer(operId, aimId, operType);
	}
	

	
}
