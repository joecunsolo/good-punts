package com.joe.springracing.business.probability;

import com.joe.springracing.objects.RunnerResult;

/**
 * Uses the weights of RunnerResult as a handicap to the prize money
 * Running under #MIN_WEIGHT decreases the value of the result
 * Anything over increases the value of the result
 * 
 * @author joe.cunsolo
 *
 */
public class WeightedPrizeMoneyStatistics extends PrizeMoneyStatistics {

	private static double MIN_WEIGHT = 53;
	private static double WEIGHT_HANDICAP = 0.07;
	
	/** 
	 * The prize money of the result with the weight as handicap
	 */
	@Override
	public Double getResult(RunnerResult runnerResult) {
		double weight = runnerResult.getWeight();
		double handicap = 1 + getHandicap(weight);
		return handicap * runnerResult.getPrizeMoney();
	}
	
	/** 
	 * The handicap
	 */
	@Override
	public double getInfluence() {
		double weight = this.getRunner().getWeight(); 
		return 1 - getHandicap(weight);
	}
	
	/**
	 * Converts the weight into a handicap
	 * @param weight
	 * @return the fractional handicap
	 */
	private double getHandicap(double weight) {
		if (weight == 0) {
			return 0;
		}
		double handicap = (weight - MIN_WEIGHT) * WEIGHT_HANDICAP;
		return handicap;
	}

}
