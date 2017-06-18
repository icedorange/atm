package com.atm.bean;

import java.sql.Timestamp;

public class Record {

	private Integer id;
	private Integer operId;
	private Integer aimId;
	private Integer operType;
	private Integer money;
	private Timestamp date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOperId() {
		return operId;
	}

	public void setOperId(Integer operId) {
		this.operId = operId;
	}

	public Integer getAimId() {
		return aimId;
	}

	public void setAimId(Integer aimId) {
		this.aimId = aimId;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}
