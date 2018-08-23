package com.joe.springracing.dao;

import java.util.Date;
import java.util.List;

import com.joe.springracing.exporter.CSVExporter;
import com.joe.springracing.exporter.Exporter;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public interface SpringRacingDAO {

	/** Just the meet - Race details will be null */
	public List<Meeting> fetchExistingMeets() throws Exception;
	
	/** Will populate the RaceResult if it is known <br>
	 * Runner information will be null*/
	public List<Race> fetchRacesForMeet(Meeting meet) throws Exception;
	
	/**
	 * Just the runner - Runner Horse history will be null
	 */
	public List<Runner> fetchRunnersForRace(Race race) throws Exception;

	/** 
	 * Store the meet
	 * @return true if the meet didn't already exist
	 */
	public boolean storeMeet(Meeting meet) throws Exception;
	
	/** Meets that start after now less 24 hrs */
	public List<Meeting> fetchUpcomingMeets() throws Exception;

	public List<Race> fetchRacesWithoutResults() throws Exception;

	public void storeMeets(List<Meeting> meets) throws Exception;

	public Race fetchRace(String raceCode) throws Exception;

	public void storeRace(Race race) throws Exception;

	public void storeHorse(Horse horse) throws Exception;

	public void storeResults(List<RunnerResult> results) throws Exception;

	public Horse fetchHorse(String horse) throws Exception;

	public List<Race> fetchRacesWithoutHistories();

	public Meeting fetchMeet(String meetCode) throws Exception;

	public void exportRunnerHistories(Exporter<RunnerResult> exporter) throws Exception;

	public List<Runner> fetchRunnersWithoutHistories() throws Exception;

	public void exportRunners(Exporter<Runner> exporter) throws Exception;

	public List<Race> fetchRacesWithResults() throws Exception;

	public List<Race> fetchRacesWithoutResults(Date from, Date to) throws Exception;

	public List<Race> fetchRacesWithResults(Date from, Date to) throws Exception;

	public List<Horse> fetchHorses(boolean histories, boolean splits) throws Exception;
}
