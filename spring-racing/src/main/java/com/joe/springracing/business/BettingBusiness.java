package com.joe.springracing.business;

import java.io.PrintWriter;
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
	 * Calculates to the stake and places the bet 
	 * @return the punts with stakes updated
	 * @throws Exception 
	 */
	public List<Punt> placeBets(List<Punt> punts) {
		for (Punt punt : punts) {
			Punt existingPunt = getExistingOpenPunt(punt);		
			double accountAmount = SpringRacingServices.getBookieAccount().fetchAccountAmount();
			double stake = SpringRacingServices.getPuntingService().calculateStake(punt, existingPunt, accountAmount);

			try {
				placeBet(punt, stake);
			} catch (Exception ex) {
				throw new RuntimeException("Unable to place bet of " + stake + " for " + punt.getRace().getRaceCode());
			}
		}
		return punts;
	}
	
	/** 
	 * Places the bet for the specified amount 
	 * @return the punts with stakes updated
	 * @throws Exception 
	 */
	public List<Punt> placeBets(List<Punt> punts, double amount) throws Exception {
		for (Punt punt : punts) {
			placeBet(punt, amount);
		}
		return punts;
	}

	/** 
	 * Places a bet with the bookie and updates the stakes
	 * A bet is only paced if the amount > 0
	 * @return the punts with stakes updated
	 * @throws Exception 
	 */
	public Punt placeBet(Punt punt, double amount) throws Exception {
		if (amount > 0) {
			Stake stake = SpringRacingServices.getBookieAccount().placeBet(punt, amount);
			punt.addStake(stake);
			SpringRacingServices.getPuntingDAO().updateStakes(punt);
		}
		return punt;
	}

	/**
	 * Given a Good Punt returns Punt that is open and equal
	 * @param goodPunt something to check
	 * @return an existing open Punt
	 */
	public Punt getExistingOpenPunt(Punt goodPunt) {
		List<Punt> openPunts = SpringRacingServices.getPuntingDAO().fetchOpenPunts();
		for (Punt open : openPunts) {
			if (open.equals(goodPunt)) {
				return open;
			}
		}
		return null;
	}

	public void updateBets() throws Exception {
		Date lastUpdatedTime = SpringRacingServices.getPuntingDAO().getLastBookieUpdateTimestamp();
		List<Stake> settled = SpringRacingServices.getBookieAccount().getSettledBets(lastUpdatedTime);
	}
}
