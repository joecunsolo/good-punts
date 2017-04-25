package com.joe.springracing.objects;

/** 
 * An amount wagered on a #Punt 
 * This is the bookies
 */
public class Stake {
	/** The amount staked */
	private double amount;
	/** The amount that the stake returned */
	private double retrn;
	/** The odds taken on the punt */
	private double odds;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getReturn() {
		return retrn;
	}

	public void setReturn(double retrn) {
		this.retrn = retrn;
	}

	public double getOdds() {
		return odds;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}
}
