package com.joe.springracing.dao;

import java.util.List;

import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class SpringRacingFileDAOV1 implements SpringRacingDAO {

	public SpringRacingFileDAOV1(String offlineDirectory) {
		// TODO Auto-generated constructor stub
	}

	public List<Meeting> fetchExistingMeets(boolean races, boolean runners,
			boolean history) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Race> fetchRacesForMeet(Meeting meet, boolean runners,
			boolean history) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Runner> fetchRunnersForRace(Race race, boolean history)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void storeMeet(Meeting meet) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public List<Meeting> fetchMeetsWithoutResults(boolean races,
			boolean runners, boolean history) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void storeMeets(List<Meeting> meets) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public Race fetchRace(int meetCode, int raceNumber, boolean runners,
			boolean history) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void storeRace(Race race) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void storeRunners(List<Runner> runners) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
