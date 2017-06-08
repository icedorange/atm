package com.atm.mapper;

import org.apache.ibatis.annotations.Param;

import com.atm.javabean.Card;

public interface CardMapper {

	public Card selectCardIdByCardNum(String cardNum);
	
	public Card selectCardByCardId(int cardId);
	
	//返回值是数据库受影响行数
	public int getMoneyFromCard(@Param("cardId")Integer cardId,@Param("version")Integer version,@Param("money")Integer money);
	
}
