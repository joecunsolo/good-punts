package com.joe.springracing.test.mock;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.dao.datasource.SpringRacingDataSource;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class MockSpringDataSource implements SpringRacingDataSource {

	private List<Race> races;
	public MockSpringDataSource() {
		races = new ArrayList<Race>();
	}
	
	public List<Race> fetchRaces() throws Exception {
		return races;
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

	public void addRace(Race race) {
		races.add(race);
	}

}
