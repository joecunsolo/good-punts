package com.joe.springracing.services;

import com.joe.springracing.objects.Race;

/**
 * Generate the probabilities for a race
 *
 */
public interface ProbabilityService {

	/** 
	 * Assumes the race has some runners
	 * Updates the runners with their unique probabilities 
	 */
	public void generate(Race race) throws Exception;

}
