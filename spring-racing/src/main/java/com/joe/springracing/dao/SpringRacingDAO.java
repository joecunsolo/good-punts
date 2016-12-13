package com.joe.springracing.dao;

import java.util.List;

import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public interface SpringRacingDAO {

	/** Just the meet - Race details will be null */
	public List<Meeting> fetchExistingMeets(boolean races, boolean runners, boolean history) throws Exception;
	
	/** Will populate the RaceResult if it is known <br>
	 * Runner information will be null*/
	public List<Race> fetchRacesForMeet(Meeting meet, boolean runners, boolean history) throws Exception;
	
	/**
	 * Just the runner - Runner Horse history will be null
	 */
	public List<Runner> fetchRunnersForRace(Race race, boolean history) throws Exception;

	public void storeMeet(Meeting meet) throws Exception;

	public List<Meeting> fetchMeetsWithoutResults(boolean races, boolean runners, boolean history) throws Exception;

	public void storeMeets(List<Meeting> meets) throws Exception;

	public Race fetchRace(int meetCode, int raceNumber, boolean runners, boolean history) throws Exception;

	public void storeRace(Race race) throws Exception;

	public void storeRunners(List<Runner> runners) throws Exception;

}
