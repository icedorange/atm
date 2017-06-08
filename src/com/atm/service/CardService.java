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
	 * @return cardId
	 */
	public int selectCardIdByCardNum(String cardNum) {
		// 判断输入长度是否正确
		if (cardNum.length() != 16) {
			return Constant.FORMAT_ERROR;
		}

		CardMapper mapper = se.getMapper(CardMapper.class);
		Card card = mapper.selectCardIdByCardNum(cardNum);
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
		CardMapper mapper = se.getMapper(CardMapper.class);
		Card card = mapper.selectCardByCardId(cardId);
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

		CardMapper mapper = se.getMapper(CardMapper.class);

		// 判断卡号是否存在
		Card card = mapper.selectCardIdByCardNum(cardNum);
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
		CardMapper mapper = se.getMapper(CardMapper.class);
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
			int temp = mapper.getMoneyFromCard(cardId, version, money);
			if (temp > 0) {
				break;
			}
		}

		// 插入流水
		RecordMapper recordMapper = se.getMapper(RecordMapper.class);
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

	public List<Record> getTranfer(int operId, int aimId, int operType, int day) {
		RecordMapper mapper = se.getMapper(RecordMapper.class);
		return mapper.getTranfer(operId, aimId, operType, 0);
	}

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
