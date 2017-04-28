package com.joe.springracing.account;

import java.util.Date;
import java.util.List;

import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Stake;

public interface BookieAccount {

	/** The stake is the bookie receipt of the placed bet */
	Stake placeBet(Punt punt, double amount) throws Exception;

	double fetchAccountAmount();
	
	List<Stake> getAllBets(Date from, Date to) throws Exception;

	List<Stake> getOpenBets() throws Exception;

	List<Stake> getSettledBets(Date from) throws Exception;

}
