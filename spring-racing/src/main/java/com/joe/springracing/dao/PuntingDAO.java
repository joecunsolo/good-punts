package com.joe.springracing.dao;

import java.util.Date;
import java.util.List;

import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Pick;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.Stake;

public interface PuntingDAO {

	/** Runners with probabilities populated */
	List<Runner> fetchProbabilitiesForRace(Race race) throws Exception;
	
	/**
	 * 
	 * @param meet a competition with Punts
	 * @return the Open, Settled and Live Punts for the meet
	 * @throws Exception
	 */
	List<Punt> fetchPuntsForMeet(Meeting meet) throws Exception;

	void storePunts(Race race, List<Punt> punts);

	void storeProbabilities(Race race);
	
	List<Punt> fetchPuntsForRace(Race race) throws Exception;

	Date fetchLastPuntEvent(Race r);
	
	List<Punt> fetchPuntResults() throws Exception;

//	List<Punt> fetchSettledPunts();
//	
//	List<Punt> fetchOpenPunts();

	Date getLastBookieUpdateTimestamp();

	List<Stake> fetchOpenStakes();

	List<Stake> fetchSettledStakes();

	void storeStake(Stake stake);
	
	void updateStake(Stake stake);

	List<Stake> fetchStakesForRace(Race race);

	void storePicks(Race race, List<Pick> picks);

}
