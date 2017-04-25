package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Punt.Confidence;
import com.joe.springracing.objects.Stake;

public class BettingBusiness extends AbstractSpringRacingBusiness {

	private Model model;
	
	public BettingBusiness() {
		this(new Model(new ModelAttributes()));
	}
	
	public BettingBusiness(Model model) {
		this(model, new PrintWriter(System.out));
	}

	public BettingBusiness(Model model, PrintWriter pw) {
		super(pw);
		this.setModel(model);
	}	
	
	/** 
	 * Calculates to the stake and places the bet 
	 * @return the punts with stakes updated
	 */
	public List<Punt> placeBets(List<Punt> punts) {
		for (Punt punt : punts) {
			double stake = calculateStake(punt);
			placeBet(punt, stake);
		}
		return punts;
	}
	
	/** 
	 * Places the bet for the specified amount 
	 * @return the punts with stakes updated
	 */
	public List<Punt> placeBets(List<Punt> punts, double amount) {
		for (Punt punt : punts) {
			placeBet(punt, amount);
		}
		return punts;
	}

	/** 
	 * Places a bet with the bookie and updates the stakes
	 * A bet is only paced if the amount > 0
	 * @return the punts with stakes updated
	 */
	public Punt placeBet(Punt punt, double amount) {
		if (amount > 0) {
			Stake stake = SpringRacingServices.getBookieAccount().placeBet(punt, amount);
			punt.addStake(stake);
			SpringRacingServices.getPuntingDAO().updateStakes(punt);
		}
		return punt;
	}

	/**
	 * Sets the Confidence {@link #setConfidence(Punt)} of the punt if it is not already set
	 * and uses the value of any existing punts to determine the stake
	 * 
	 * The maximum stake is the lower of the max dollar or max percentage stake
	 * The minimum stake is the max of the min dollar or min percentage stake		
	 * 
	 * The stake amount equals the calculated stake amount minus what is already staked
	 * but not less than the minimum dollar stake
	 * 
	 * @param goodPunt a good Punt that is worth betting on
	 * @return the amount that should be staked on this punt
	 */
	public double calculateStake(Punt goodPunt) {
		ModelAttributes ma = this.getModel().getAttributes();
		Punt existingPunt = getExistingOpenPunt(goodPunt);
		double existingStake = calculateExistingStake(existingPunt);
		double accountAmount = SpringRacingServices.getBookieAccount().fetchAccountAmount();
		double stake = 0;
		
		if (goodPunt.getConfidence() == null) {
			setConfidence(goodPunt);
		}
		if (Confidence.HIGH.equals(goodPunt.getConfidence())) {
			stake = ma.getMaximumPercentageStake() * accountAmount / 100;
			//The maximum stake is the lower of the dollar or percentage stake
			stake = stake > ma.getMaximumDollarStake() ?  
					ma.getMaximumDollarStake() : stake;
		} else {
			stake = ma.getMinimumPercentageStake() * accountAmount / 100;
			//The maximum stake is the max of the dollar or percentage stake
			stake = stake > ma.getMinimumDollarStake() ?  
					stake : ma.getMinimumDollarStake();
		}

		// The stake amount equals the calculated stake amount minus what is already staked
		if (stake > existingStake) {
			stake = stake - existingStake;
			//can't ever bet less than the minimum dollar stake
			if (stake < ma.getMinimumDollarStake()) {
				stake = 0;
			}
		} else {
			//already staked too much
			stake = 0;
		}
		//TODO A stake should be in some worthwhile increment
		return stake;
	}
	
	/** Loops through the existing Stakes for the Punt to determine how much is already staked */
	public double calculateExistingStake(Punt existingPunt) {
		if (existingPunt == null) {
			return 0;
		}
		double amount = 0;
		for (Stake stake : existingPunt.getStakes()) {
			amount += stake.getAmount();
		}
		return amount;
	}

	/** uses {@link #hasHighConfidence(Punt)} to set the confidence of the Punt */
	public void setConfidence(Punt goodPunt) {
		if (hasHighConfidence(goodPunt)) {
			goodPunt.setConfidence(Confidence.HIGH);
		} else {
			goodPunt.setConfidence(Confidence.LOW);
		}
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

	public void setModel(Model model) {
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

	/** 
	 * Determines if the Punt is a High Confidence punt
	 * @param aGoodPunt
	 * @return true if {@link Punt#getJoesOdds()} < {@link ModelAttributes#getHighJoeConfidence()}
	 * and {@link Punt#getBookieOdds()} < {@link ModelAttributes#getHighBookieConfidence()}
	 * false Otherwise
	 */
	public boolean hasHighConfidence(Punt punt) {
		return punt.getJoesOdds() < this.getModel().getAttributes().getHighJoeConfidence() &&
				punt.getBookieOdds() < this.getModel().getAttributes().getHighBookieConfidence();
	}	
}
