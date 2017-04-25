package com.joe.springracing.account;

import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Stake;

public interface BookieAccount {

	/** The stake is the bookie receipt of the placed bet */
	Stake placeBet(Punt punt, double amount);

	double fetchAccountAmount();

}
