package com.joe.springracing.dao.datasource;

import java.util.List;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

//TODO This shouldnt exist - really should mock the API
public class GoodPuntsDataSource implements SpringRacingDataSource {

	public List<Race> fetchRaces() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<RunnerResult> fetchPastResultsForHorse(String horseCode)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Meeting fetchMeet(String meetCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public Race fetchRace(String racecode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Race fetchRaceResult(String raceCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Runner> fetchRunnnersForRace(Race race) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Horse fetchHorse(Runner runner) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
