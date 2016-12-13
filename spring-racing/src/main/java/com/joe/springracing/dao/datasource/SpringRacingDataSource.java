package com.joe.springracing.dao.datasource;

import java.util.List;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RunnerResult;

public interface SpringRacingDataSource {

	public List<Meeting> fetchUpcomingMeets() throws Exception;
	
	public List<Meeting> fetchMeets(List<String> meetCode) throws Exception;
	
	public List<RunnerResult> fetchPastResultsForHorse(Horse horse) throws Exception;

	public void updateRaceResults(Race race);

	public Meeting fetchMeet(String meetCode);
}
