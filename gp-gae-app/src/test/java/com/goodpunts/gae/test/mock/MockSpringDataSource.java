package com.goodpunts.gae.test.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joe.springracing.dao.datasource.SpringRacingDataSource;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class MockSpringDataSource implements SpringRacingDataSource {

	private List<Race> races;
	private List<Meeting> meets;
	private Map<String, List<RunnerResult>> pastResultsForHorse;
	private Map<String, Race> raceResults = new HashMap<String, Race>();
	
	public MockSpringDataSource() {
		races = new ArrayList<Race>();
		meets = new ArrayList<Meeting>();
		pastResultsForHorse = new HashMap<String, List<RunnerResult>>();
	}

	public void addRace(Meeting meet, Race race) {
		race.setMeetCode(meet.getMeetCode());
		races.add(race);
	}
	
	public void addMeet(Meeting meet) {
		meets.add(meet);
		for (Race race : meet.getRaces()) {
			races.add(race);
		}
	}
	
	public List<Race> fetchRaces() throws Exception {
		return races;
	}

	public List<RunnerResult> fetchPastResultsForHorse(String horseCode)
			throws Exception {
		return pastResultsForHorse.get(horseCode);
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
		return raceResults.get(raceCode);
	}
	
	public void addRaceResult(Race race) {
		raceResults.put(race.getRaceCode(), race);
	}

	public List<Runner> fetchRunnnersForRace(Race race) throws Exception {
		return getRace(race).getRunners();
	}

	public Horse fetchHorse(Runner runner) throws Exception {
		Horse h = new Horse();
		h.setCode(runner.getHorse());
		h.setName(runner.getHorse());
		h.setId(runner.getHorse());
		h.setName(runner.getHorse());
		return h;
	}

	private Race getRace(Race aRace) {
		for (Race race : races) {
			if (race.equals(aRace)) {
				return race;
			}
		}
		return null;
	}
	
	public void addRunner(Race aRace, Runner aRunner) {
		Race localRace = getRace(aRace);
		if (localRace != null) {
			localRace.getRunners().add(aRunner);
		}
	}

	public void addResult(Race aRace, Runner aRunner, RunnerResult aResult) {
		// TODO Auto-generated method stub
		
	}

	public void addPastResults(Runner aRunner, List<RunnerResult> somePastResults) {
		pastResultsForHorse.put(aRunner.getHorse(), somePastResults);
	}

}
