package com.joe.springracing.dao.datasource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

/**
 * The datasource is the glue between good-punts and racing.com
 * @author joecunsolo
 *
 */
public class RacingDotComDataSource implements SpringRacingDataSource {

	public static final int RACE_RESULTS_PER_PAGE = 5;
	public static final int NUM_PAGES = 1;
	public static final int HORSE_RESULTS_PER_PAGE = 30;
	
	public static final String PREFIX_RACE_RESULTS_URL = "v1/en-au/race/results/";
	public static final String PREFIX_RACE_DAY_URL = "api/meet/RacesByDay/";
	public static final String PREFIX_RUNNER_URL = "v1/en-au/race/entries/";
	public static final String PREFIX_SPLIT_SECTION_URL = "v1/en-au/race/splitsandsectionals/";
	public static final String PREFIX_RACE_URL = "api/race/details/";
	public static final String PREFIX_FORM_URL = "api/form/horse/";

	private RacingDotComReader reader;
	
	public RacingDotComDataSource() {
		reader = new RacingDotComReader();
	}
	
	public List<Race> fetchRaces() throws Exception {
		return fetchRaces(10, 5);
	}
	
	public List<Race> fetchRaces(int daysAgo, int daysTo) throws Exception {
		List<Race> races = new ArrayList<Race>();
		String urlToRead = getRaceDayURL(daysAgo, daysTo);
		System.out.println(urlToRead);
		races.addAll(reader.readRaces(urlToRead));
		return races;
	}
	
	public List<Runner> fetchRunnnersForRace(Race race) throws Exception {
		
		String urlToRead = getRunnerURL(race.getMeetCode(), race.getRaceNumber());
		List<Runner> runners = reader.readRunners(urlToRead);
		if (runners != null) {
			//set the race code - racing.com doesn't have that detail
			for (Runner runner : runners) {
				runner.setRaceCode(race.getRaceCode());
			}
		}
		return runners;
	}
			
	public List<RunnerResult> fetchPastResultsForHorse(String horseCode) throws Exception {
		String urlToRead = getHorseURL(horseCode);
		List<RunnerResult> result = reader.readResults(urlToRead);
		
		return result;
	}
	
	public Map<Integer, List<Double>> fetchSplitsAndSectionals(String meetCode, int raceNumber) throws Exception {
		String splitsURL = getSplitsAndSectionalsURL(meetCode, raceNumber);
		return reader.readSplits(splitsURL);
	}
			
	public Race fetchRace(String racecode) throws Exception {
		String urlToRead = getRaceURL(racecode);
		return reader.readRace(urlToRead);
	}
	
	public Meeting fetchMeet(String meetCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Update the runners 
	 */
	public Race fetchRaceResult(String raceCode) throws Exception {
		Race race = fetchRace(raceCode);
		String urlToRead = getRaceResultURL(race);
		
		race.setRunners(reader.readRaceResult(urlToRead));
		System.out.println("Runners " + race.getRunners().size());
		System.out.println("Has Runners " + (race.getRunners() != null && race.getRunners().size() > 0));
		
		int[] results = new int[race.getRunners().size()];
		int scratchings = 0;
		for (Runner runner : race.getRunners()) {
			System.out.println(runner.getHorse());
			int number = runner.getNumber();
			int finish = runner.getResult();
			
			//Racing.com's method of handling exceptions
			if (finish <= results.length && finish > 0) {
				results[finish-1] = number;
			} else {
				scratchings++;
			}
		}

		race.setResult(Arrays.copyOf(results, results.length - scratchings));
		race.setSplits(fetchSplitsAndSectionals(race.getMeetCode(), race.getRaceNumber()));
		return race;
	}
	
	public Horse fetchHorse(Runner runner) throws Exception {
		RacingDotComRunner rdRunner = (RacingDotComRunner)runner;
		return rdRunner.getHorseObject();
	}
	
	public String getRaceResultURL(Race race) {
		return SpringRacingServices.getRacingDotComURL() + getRaceResulSuffix(race);
	}
	
	public String getRaceResulSuffix(Race race) {
		return PREFIX_RACE_RESULTS_URL + race.getMeetCode() + "/" + race.getRaceNumber();
	}

	public String getSplitsAndSectionalsURL(String meeting, int race) {
		return SpringRacingServices.getRacingDotComURL() + PREFIX_SPLIT_SECTION_URL + meeting + "/" + race;
	}

	public String getRaceDayURL(int page, int resultsPerPage) {
		return SpringRacingServices.getRacingDotComURL() + PREFIX_RACE_DAY_URL + page + "/" + resultsPerPage;
	}
	
	public String getRunnerURL(String meeting, int race) {
		return SpringRacingServices.getRacingDotComURL() + PREFIX_RUNNER_URL + meeting + "/" + race;
	}

	public String getRaceURL(String raceCode) {
		return SpringRacingServices.getRacingDotComURL() + PREFIX_RACE_URL + raceCode;
	}
	
	public String getHorseURL(String horseCode) {
		return SpringRacingServices.getRacingDotComURL() + PREFIX_FORM_URL + horseCode + "/" + NUM_PAGES + "/" + HORSE_RESULTS_PER_PAGE;
	}

	public RacingDotComReader getReader() {
		return reader;
	}
	
	public void setReader(RacingDotComReader reader) {
		this.reader = reader;
	}
}
