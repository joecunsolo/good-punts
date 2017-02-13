package com.joe.springracing.dao.datasource;

import java.util.List;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public interface SpringRacingDataSource {

//	public List<Meeting> fetchUpcomingMeets() throws Exception;
	
	public List<Race> fetchRaces() throws Exception;
	
//	public List<Meeting> fetchMeet(String meetCode) throws Exception;
	
//	public List<RunnerResult> fetchPastResultsForHorse(Horse horse) throws Exception;
	public List<RunnerResult> fetchPastResultsForHorse(String horseCode) throws Exception;
	
//	public void updateRaceResults(Race race);

	public Meeting fetchMeet(String meetCode);
	
	public Race fetchRace(String racecode) throws Exception;
	
	public Race fetchRaceResult(String raceCode) throws Exception;
	
	public List<Runner> fetchRunnnersForRace(Race race) throws Exception;

	public Horse fetchHorse(Runner runner) throws Exception;
}
