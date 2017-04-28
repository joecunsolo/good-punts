package com.goodpunts.objectify;

import com.googlecode.objectify.annotation.Id;

public class ObjAccount {

	private double amount;
	@Id
	private String id = "good-punts";

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
