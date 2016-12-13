package com.joe.springracing.dao;

import java.util.List;

import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public interface RaceDAO {

	public List<Race> fetchRaces() throws Exception;
	public List<Runner> fetchRunnnersForRace(String meeting, int race) throws Exception;
	public void updateRaceResults(Race race) throws Exception;
	public void enrichPastResults(RunnerResult runner) throws Exception;
	public void enrichPastResults(String meetCode, int raceNumber, RunnerResult result) throws Exception;
}
