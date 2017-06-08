package com.atm.util;

/**
 * 1XXX 卡相关
 * 2XXX 用户相关
 * 3XXX 转账相关
 * 4XXX 金额相关
 * @author Jack
 *
 */
public interface Constant {

	//操作成功
	int SUCCESS = 1;
	
	//操作失败
	int FAIL = 0;

	//输入格式错误
	int FORMAT_ERROR = -1;
	
	//卡号不存在
	int CARD_ERROR = 1001;
	
	//密码错误
	int PASSWORD_ERROR = 1002;
	
	//金额不足
	int RUNOUT = 4001;
	
	//超出每天能去的最大金额
	int MAXOUT = 4002;
	
	//超出每天能存的最大金额
	int MAXINPUT = 4003;
}
