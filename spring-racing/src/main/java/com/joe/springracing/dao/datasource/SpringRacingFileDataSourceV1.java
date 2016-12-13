package com.joe.springracing.dao.datasource;

import java.util.List;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RunnerResult;

public class SpringRacingFileDataSourceV1 implements SpringRacingDataSource {

	public List<Meeting> fetchUpcomingMeets() throws Exception {
		throw new UnsupportedOperationException(this.getClass().toString() + "fetchUpcomingMeets()");
	}

	public List<Meeting> fetchMeets(List<String> meets) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<RunnerResult> fetchPastResultsForHorse(Horse horse)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateRaceResults(Race race) {
		// TODO Auto-generated method stub
		
	}

	public Meeting fetchMeet(String meetCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
