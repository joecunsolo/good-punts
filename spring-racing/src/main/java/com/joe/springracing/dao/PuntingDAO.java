package com.joe.springracing.dao;

import java.util.List;

import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public interface PuntingDAO {

	/** Runners with probabilities populated */
	List<Runner> fetchProbabilitiesForRace(Race race) throws Exception;
	
	List<Punt> fetchPuntsForMeet(Meeting meet) throws Exception;

	void storePunts(Meeting meet, List<Punt> punts);

	void storeProbabilities(Race race);

}
