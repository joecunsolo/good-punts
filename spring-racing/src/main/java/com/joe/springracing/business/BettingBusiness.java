package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Stake;

public class BettingBusiness extends AbstractSpringRacingBusiness {

	public BettingBusiness() {
		this(new PrintWriter(System.out));
	}

	public BettingBusiness(PrintWriter pw) {
		super(pw);
	}	
	
	/** 
	 * Calculates the stake and places the bet 
	 * @return the punts with stakes updated
	 * @throws Exception 
	 */
	public List<Stake> placeBets(List<Punt> punts) {
		List<Stake> result = new ArrayList<Stake>();
		for (Punt punt : punts) {
			List<Stake> existingPunt = getExistingOpenStakesForPunt(punt);		
			double accountAmount = SpringRacingServices.getBookieAccount().fetchAccountAmount();
			double stake = SpringRacingServices.getPuntingService().calculateStake(punt, existingPunt, accountAmount);

			try {
				result.add(placeBet(punt, stake));
			} catch (Exception ex) {
				throw new RuntimeException("Unable to place bet of " + stake + " for " + punt.getRaceCode(), ex);
			}
		}
		return result;
	}
	
	/** 
	 * Places the bet for the specified amount 
	 * @return the punts with stakes updated
	 * @throws Exception 
	 */
	public List<Stake> placeBets(List<Punt> punts, double amount) throws Exception {
		List<Stake> stakes = new ArrayList<Stake>();
		for (Punt punt : punts) {
			Stake stake = placeBet(punt, amount);
			if (stake != null) {
				stakes.add(stake);
			}
		}
		return stakes;
	}

	/** 
	 * Places a bet with the bookie and updates the stakes
	 * A bet is only paced if the amount > 0
	 * @return the punts with stakes updated
	 * @throws Exception 
	 */
	public Stake placeBet(Punt punt, double amount) throws Exception {
		if (amount > 0) {
			Stake stake = SpringRacingServices.getBookieAccount().placeBet(punt, amount);
			SpringRacingServices.getPuntingDAO().storeStake(stake);
			return stake;
		}
		return null;
	}

	/**
	 * Given a Good Punt returns Punt that is open and equal
	 * @param goodPunt something to check
	 * @return an existing open Punt
	 */
	public List<Stake> getExistingOpenStakesForPunt(Punt goodPunt) {
		List<Stake> result = new ArrayList<Stake>();
		List<? extends Stake> openPunts = SpringRacingServices.getPuntingDAO().fetchOpenStakes();
		for (Stake open : openPunts) {
			if (open.equals(goodPunt)) {
				result.add(open);
			}
		}
		return result;
	}
	
	/**
	 * Asks the bookie for everything they've settled recently
	 * And then settles the stakes in the punting dao
	 * @throws Exception
	 */
	public void settleBets() throws Exception {
		Date lastUpdatedTime = SpringRacingServices.getPuntingDAO().getLastBookieUpdateTimestamp();
		List<Stake> settled = SpringRacingServices.getBookieAccount().getSettledBets(lastUpdatedTime);
		for (Stake stake : settled) {
			stake.setSettled(true);
			SpringRacingServices.getPuntingDAO().updateStake(stake);
		}
	}
	
	public double fetchAccountAmount(String account) {
		return SpringRacingServices.getBookieAccount().fetchAccountAmount();
	}
}
