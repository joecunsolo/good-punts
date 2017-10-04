package com.joe.springracing.objects;

import java.util.Date;

/** 
 * An amount wagered on a #Punt 
 * This is the bookies receipt
 */
public class Stake extends Punt {
	/** The account that the Stake was wagered with */
	private String account;
	/** The transaction no from the account*/
	private String txnNo;
	/** The amount staked */
	private double amount;
	/** The amount that the stake returned */
	private double retrn;
	/** The odds taken on the punt */
	private double odds;
	/** The date the stake was made */
	private Date date;
	/** Has the bookie paid out */
	private boolean settled;
	/** The balance when the stake is settled */
	private double balance;
	
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTxnNo() {
		return txnNo;
	}

	public void setTxnNo(String txnNo) {
		this.txnNo = txnNo;
	}
		
	@Override
	public int hashCode() {
		return (account + txnNo).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Stake) {
			return o.hashCode() == this.hashCode();
		}
		if (o instanceof Punt) {
			return super.equals(o);
		}
		return false;
	}

	public boolean isSettled() {
		return settled;
	}
	
	public void setSettled(boolean settled) {
		this.settled = settled;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
