package com.atm;

import java.util.List;

import com.atm.javabean.Record;
import com.atm.service.CardService;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CardService cardService = new CardService();
		
//		int temp = cardService.putMoneyInCard(1, 10);
//		System.out.println(temp);
//		
		List<Record> temp = cardService.getTranfer(1,1,2);
		for (Record r : temp) {
			int t = r.getMoney();
			System.out.println(t);
		}
		
		
	/*	RecordService re = new RecordService();
		int */
	}
	
	

}
