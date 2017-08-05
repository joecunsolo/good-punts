package com.joe.springracing.business;

import java.util.List;

import com.joe.springracing.business.probability.distributions.GatheredDistribution;

/**
 * Simulates a race
 * @author joe
 *
 */
public interface Simulator {

	/**
	 * Runs a simulation of a race i times
	 * @param race A race with runners
	 * @param i The number of simulations to run
	 * @param distribution A distribution to compare against
	 * @param desc True - low is a good result; False - high is a good result
	 */
	public void simulate(List<? extends Simulatable> race, int i, GatheredDistribution distribution, boolean desc);

}
