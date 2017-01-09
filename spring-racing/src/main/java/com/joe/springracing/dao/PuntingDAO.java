package com.joe.springracing.dao;

import java.util.List;

import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public interface PuntingDAO {

	void storeProbabilities(Meeting meeting) throws Exception;

	/** Runners with probabilities populated */
	List<Runner> fetchProbabilitiesForRace(Race race) throws Exception;
	
	void storePunts(List<Punt> punts) throws Exception;

	List<Punt> fetchPuntsForMeet(Meeting meet) throws Exception;

}