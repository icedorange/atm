package com.atm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atm.bean.Card;
import com.atm.bean.Record;
import com.atm.mapper.CardMapper;
import com.atm.mapper.RecordMapper;
import com.atm.util.Constant;
import com.atm.util.MD5Util;

@Service
@Transactional
public class CardService {

	@Resource
	CardMapper cardMapper;
	
	@Resource
	RecordMapper recordMapper;
	
	/**
	 * 根据cardNum，查询ID
	 * 
	 * @param cardNum
	 * @return cardId
	 */
	public int selectCardIdByCardNum(String cardNum) {
		// 判断输入长度是否正确
		if (cardNum.equals("")||cardNum.length() != 16) {
			return Constant.FORMAT_ERROR;
		}

		Card card = cardMapper.selectCardIdByCardNum(cardNum);
		if (card == null) {
			return Constant.CARD_ERROR;
		}
		return card.getId();
	}

	/**
	 * 根据ID，获取账号的所有信息
	 * 
	 * @param cardId
	 * @return
	 */
	public Card selectCardByCardId(int cardId) {
		// cardId是用selectCardIdByCardNum(String cardNum)方法查询数据库得到
		// 所以默认cardId不会出错
		Card card = cardMapper.selectCardByCardId(cardId);
		return card;
	}

	/**
	 * 检查密码是否正确
	 * 
	 * @param cardNum
	 * @param password
	 * @return
	 */
	public int check(String cardNum, String password) {

		// 判断输入的数据长度是否正确
		if (cardNum.length() != 16 || password.length() != 6) {
			return Constant.FORMAT_ERROR;
		}


		// 判断卡号是否存在
		Card card = cardMapper.selectCardIdByCardNum(cardNum);
		if (card == null) {
			return Constant.CARD_ERROR;
		}

		// 验证密码是否正确
		String pass = card.getPassword();
		if (pass.equals(MD5Util.MD5(password + "atm"))) {
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
		int money = card.getMoney() / 100;
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
		// 判断输入是否正确
		if (money <= 0) {
			return Constant.FORMAT_ERROR;
		}

		money = money * 100;
		// 每天取钱不能超过1万(包括转账给别人)
		int max = maxGetMoney(cardId, money);
		if (max > 1000000) {
			return Constant.MAXOUT;
		}

		int change = 0;
		// 使用乐观锁
		while (true) {
			Card card = selectCardByCardId(cardId);
			change = card.getMoney();
			if (change < money) {
				// 余额不足，结束
				return Constant.RUNOUT;
			}

			// version：乐观锁
			int version = card.getVersion();
			int temp = cardMapper.getMoneyFromCard(cardId, version, money);
			if (temp > 0) {
				break;
			}
		}

		// 插入流水
		recordMapper.insertTrans(cardId, cardId, 1, money);

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
			return Constant.FORMAT_ERROR;
		}

		money = money * 100;
		// 每天存款不能超过1万(包括别人转给你的)
		int max = maxInputMoney(cardId, money);
		if (max > 1000000) {
			return Constant.MAXINPUT;
		}

		while (true) {
			Card card = selectCardByCardId(cardId);
			int version = card.getVersion();
			int temp = cardMapper.getMoneyFromCard(cardId, version, -money);
			if (temp > 0) {
				break;
			}
		}

		recordMapper.insertTrans(cardId, cardId, 2, money);
		return Constant.SUCCESS;
	}

	/**
	 * 转账，判断转账目标是否存在
	 * 
	 * @param cardId
	 * @param aimNum
	 * @param money
	 * @return
	 */
	public int transferCheck(int cardId, String aimNum, int money) {

		if (money <= 0) {
			return Constant.FORMAT_ERROR;
		}

		int aimId = selectCardIdByCardNum(aimNum);
		if (aimId == Constant.FORMAT_ERROR) {
			return Constant.FORMAT_ERROR;
		}
		if (aimId == Constant.CARD_ERROR) {
			return Constant.CARD_ERROR;
		}

		money = money * 100;
		int max = maxGetMoney(cardId, money);
		if (max > 1000000) {
			return Constant.MAXOUT;
		}
		
		int max2 = maxInputMoney(aimId, money);
		if (max2 > 1000000) {
			return Constant.MAXINPUT;
		}
		int result = transfer(cardId, aimId, money);
		return result;
	}

	/**
	 * 转账操作
	 * 
	 * @param cardId
	 * @param aimId
	 * @param money
	 * @return
	 */
	private int transfer(int cardId, int aimId, int money) {
		int change = 0;

		while (true) {
			Card card = selectCardByCardId(cardId);
			change = card.getMoney();
			if (change < money) {
				return Constant.RUNOUT;
			}
			int version = card.getVersion();
			int temp = cardMapper.getMoneyFromCard(cardId, version, money);
			if (temp > 0) {
				break;
			}
		}

		while (true) {
			Card card = selectCardByCardId(aimId);
			int version = card.getVersion();
			int temp = cardMapper.getMoneyFromCard(aimId, version, -money);
			if (temp > 0) {
				break;
			}
		}

		recordMapper.insertTrans(cardId, aimId, 3, money);
		return Constant.SUCCESS;
	}

	/**
	 * 获取流水信息
	 * @param operId
	 * @param aimId
	 * @param operType
	 * @param day
	 * @return
	 */
	public List<Record> getTranfer(int operId, int aimId, int operType, int day) {
		return recordMapper.getTranfer(operId, aimId, operType, day);
	}

	/**
	 * 当天已取款数目
	 * @param cardId
	 * @param money
	 * @return
	 */
	public int maxGetMoney(int cardId, int money) {
		// 取款流水
		List<Record> list = getTranfer(cardId, 0, 1, 1);
		// 转账流水
		List<Record> list2 = getTranfer(cardId, 0, 3, 1);
		int sum = 0;
		for (Record record : list) {
			sum = sum + record.getMoney();
		}
		for (Record record : list2) {
			sum = sum + record.getMoney();
		}
		sum = sum + money;
		return sum;
	}

	/**
	 * 当天已存款数目
	 * @param cardId
	 * @param money
	 * @return
	 */
	public int maxInputMoney(int cardId, int money) {
		// 存款流水
		List<Record> list = getTranfer(cardId, 0, 2, 1);
		// 转账流水
		List<Record> list2 = getTranfer(0, cardId, 3, 1);
		int sum = 0;
		for (Record record : list) {
			sum = sum + record.getMoney();
		}
		for (Record record : list2) {
			sum = sum + record.getMoney();
		}
		sum = sum + money;
		return sum;
	}

}
