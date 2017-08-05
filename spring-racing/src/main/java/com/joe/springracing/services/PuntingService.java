package com.joe.springracing.services;

import java.util.List;

import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Stake;

/**
 * Determine what to punt on for a race
 */
public interface PuntingService {

	/**
	 * Assumes the probabilities for a race are already generated
	 * @return the list of good punts
	 */
	public List<Punt> generate(Race race) throws Exception;
	
	public double calculateStake(Punt goodPunt, List<Stake> existingPunt, double accountAmount);
}
