package com.joe.springracing.test.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.exporter.RunnerHistoriesExporter;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class MockRacingDao implements SpringRacingDAO {

	public List<Meeting> fetchExistingMeets() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Race> fetchRacesForMeet(Meeting meet) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Runner> fetchRunnersForRace(Race race) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean storeMeet(Meeting meet) throws Exception {
		meetings.add(meet);
		return false;
	}

	public List<Meeting> fetchMeetsWithoutResults() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void storeMeets(List<Meeting> meets) throws Exception {
		meetings.addAll(meets);
	}

	public Race fetchRace(String raceCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Race> races = new ArrayList<Race>();
	public void storeRace(Race race) throws Exception {
		races.add(race);
	}

	public void storeHorse(Horse horse) throws Exception {
		// TODO Auto-generated method stub

	}

	public void storeResults(List<RunnerResult> results) throws Exception {
		// TODO Auto-generated method stub

	}

	public Horse fetchHorse(String horse) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Race> fetchRacesWithoutHistories() {
		return racesWithoutHistories;
	}

	public Meeting fetchMeet(String meetCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void exportRunnerHistories(RunnerHistoriesExporter exporter)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public List<Race> fetchRacesWithoutResults() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Runner> fetchRunnersWithoutHistories() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Runner fetchRunner(String raceCode, String horseCode) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Race> racesWithoutHistories;
	public void setRacesWithoutHistories(Race... races2) {
		racesWithoutHistories = Arrays.asList(races2);
		races.addAll(racesWithoutHistories);
	}

	private List<Meeting> meetings = new ArrayList<Meeting>();
	public List<Meeting> fetchUpcomingMeets() throws Exception {
		return meetings;
	}

}
