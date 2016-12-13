package com.joe.springracing.dao;

import java.io.File;
import java.util.List;

import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class SpringRacingFileDAOV1 implements SpringRacingDAO {

	private File storageDirectory;
	public SpringRacingFileDAOV1(String rootDir) {
		storageDirectory = new File(rootDir);
	}

	public List<Meeting> fetchExistingMeets(boolean races, boolean runners, boolean history) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Race> fetchRacesForMeet(Meeting meet, boolean runners, boolean history) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Runner> fetchRunnersForRace(Race race, boolean history) {
		// TODO Auto-generated method stub
		return null;
	}

	public void storeMeets(List<Meeting> meets) {
		// TODO Auto-generated method stub
	}

	public void storeMeet(Meeting meet) {
		// TODO Auto-generated method stub
	}

	public List<Meeting> fetchMeetsWithoutResults(boolean races, boolean runners, boolean history) {
		// TODO Auto-generated method stub
		return null;
	}

	public Race fetchRace(String meetCode, int raceNumber, boolean runners, boolean history) throws Exception {
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
