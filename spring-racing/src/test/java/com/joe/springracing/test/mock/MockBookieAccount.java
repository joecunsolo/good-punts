package com.joe.springracing.test.mock;

import com.joe.springracing.account.BookieAccount;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Stake;

public class MockBookieAccount implements BookieAccount {

	double amount = 100;
	public void setAmount(int i) {
		this.amount = i;
	}

	public double fetchAccountAmount() {
		return amount;
	}

	/** Creates a stake based on the Punt */
	public Stake placeBet(Punt punt, double amount) {
		Stake stake = new Stake();
		stake.setAmount(amount);
		stake.setOdds(punt.getBookieOdds());
		return stake;
	}

}
