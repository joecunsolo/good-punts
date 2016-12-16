package com.joe.springracing.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Jockey;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RaceResult;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.Trainer;

public class LocalRaceDAOImpl implements SpringRacingDAO {

	private static final String FILENAME_MEETING = "meeting.props";
	private static final String FILENAME_RACE = "race.props";
	private static final String FILENAME_RUNNER = "runner.props";
	private static final String FILENAME_HORSE = "horse.props";
	private static final String FILENAME_JOCKEY = "jockey.props";
	private static final String FILENAME_ODDS = "odds.props";
	private static final String FILENAME_TRAINER = "trainer.props";
	private static final String FILENAME_CAREER = "career.props";
	private static final String FILENAME_GOODAT = "goodat.props";
	private static final String FILENAME_RESULT = "result.props";
	private static final String FILENAME_SPLITS = "splits.props";
	
	public static final String KEY_FINISH = "finish";
	public static final String KEY_NUMBER = "number";
	
	private File storageDirectory;
	public LocalRaceDAOImpl(String rootDir) {
		storageDirectory = new File(rootDir);
	}
	
	public List<Meeting> fetchExistingMeets(boolean races, boolean runners, boolean history) throws Exception {
		List<Meeting> meetings = new ArrayList<Meeting>();
		File[] meetFiles = storageDirectory.listFiles();
		for (File meetDirectory : meetFiles) {
			Meeting meet = readMeeting(meetDirectory, races, runners, history);
			meetings.add(meet);			
		}
		
		return meetings;
	}
	
	public List<Meeting> fetchMeetsWithoutResults(boolean races, boolean runners, boolean history) throws Exception {
		List<Meeting> allMeets = fetchExistingMeets(true, false, false);
		List<Meeting> result = new ArrayList<Meeting>();
		for (Meeting meet : allMeets) {
			System.out.println(meet.getDate() + " " + meet.getVenue());
			if (meet.getRaces().get(0).getResult() == null) {
				if (races) {
					fetchRacesForMeet(meet, runners, history);
				}
				result.add(meet);
			}
		}
		return result;
	}
	
	public List<Race> fetchRacesForMeet(Meeting meet, boolean runners, boolean history) throws Exception {
		File[] raceFiles = getMeetDirectory(meet.getMeetCode()).listFiles();
		for (File raceDirectory : raceFiles) {
			if (raceDirectory.isDirectory()) {
				meet.addRace(readRace(raceDirectory, runners, history));
			}
		}
		return meet.getRaces();
	}
	
	private File getMeetDirectory(int meetCode) {
		return new File(storageDirectory, String.valueOf(meetCode));
	}

	private Meeting readMeeting(File meetDirectory, boolean readRaces, boolean runners, boolean history) throws Exception {
		Meeting m = toMeeting(readPropertiesFile(meetDirectory, FILENAME_MEETING));
		
		System.out.println();
		System.out.println(m.getDate() + " "  + m.getVenue());
		
		if (readRaces) {
			List<Race> races = fetchRacesForMeet(m, runners, history);
			m.setRaces(races);
		}
		
		return m;
	}
	
	private Meeting toMeeting(Properties readPropertiesFile) {
		// TODO Auto-generated method stub
		return new Meeting();
	}

	public Race fetchRace(int meetCode, int raceNumber, boolean runners,
			boolean history) throws Exception {
		File raceDirectory = getRaceDirectory(meetCode, raceNumber);
		Race race = readRace(raceDirectory, runners, history);
		
		return race;
	}

	private Race readRace(File raceDirectory, boolean readRunners, boolean history) throws Exception {
		Race race = toRace(readPropertiesFile(raceDirectory, FILENAME_RACE));
		
		Properties props = readPropertiesFile(raceDirectory, FILENAME_RESULT);
		if (props != null && props.size() > 0) {
			RaceResult result = new RaceResult(props);
			race.setResult(result);
		}
		if (readRunners) {
			fetchRunnersForRace(race, history);
		}
		return race;
	}
	
	private Race toRace(Properties props) {
		Race race = new Race();
		return race;
	}

	public List<Runner> fetchRunnersForRace(Race race, boolean history)
			throws Exception {
		List<Runner> runners = new ArrayList<Runner>();
		File raceDirectory = getRaceDirectory(race);
		File[] runnerFiles = raceDirectory.listFiles();
		for (File runnerDirectory : runnerFiles) {
			if (runnerDirectory.isDirectory()) {
				runners.add(readRunner(race, runnerDirectory, history));
			}
		}
		race.setRunners(runners);
		return runners;
	}
	
	private File getRaceDirectory(Race race) {
		return getRaceDirectory(race.getMeetCode(), race.getRaceNumber());
	}
	
	private File getRaceDirectory(int meetCode, int raceNumber) {
		File meetDirectory = getMeetDirectory(meetCode);
		File raceDirectory = new File(meetDirectory, String.valueOf(raceNumber));
		return raceDirectory;
	}

	//TODO dont take a race -
	//Make the runner have the runner result
	private Runner readRunner(Race race, File runnerDirectory, boolean history) throws Exception {
		Runner runner = new Runner(readPropertiesFile(runnerDirectory, FILENAME_RUNNER));
		runner.setCareer(readPropertiesFile(runnerDirectory, FILENAME_CAREER));
		runner.setGoodAt(readPropertiesFile(runnerDirectory, FILENAME_GOODAT));
		
		runner.setHorse(new Horse(readPropertiesFile(runnerDirectory, FILENAME_HORSE)));
		runner.setTrainer(new Trainer(readPropertiesFile(runnerDirectory, FILENAME_TRAINER)));
		runner.setJockey(new Jockey(readPropertiesFile(runnerDirectory, FILENAME_JOCKEY)));
		runner.setOdds(new Odds(readPropertiesFile(runnerDirectory, FILENAME_ODDS)));
		
		if (race.getResult() != null) {
			RunnerResult result = new RunnerResult(readPropertiesFile(runnerDirectory, FILENAME_RESULT));
			result.getProperties().putAll(runner.getProperties());
			race.getResult().addRunner(result);
		}
		
		if (history) {
			File[] raceFiles = runnerDirectory.listFiles();
			for (File raceDirectory : raceFiles) {
				if (raceDirectory.isDirectory()) {
					runner.getHorse().addPastResult(readRunnerResult(raceDirectory));
				}
			}
		}
		return runner;
	}

	private RunnerResult readRunnerResult(File raceDirectory) throws Exception {
		RunnerResult result = new RunnerResult(readPropertiesFile(raceDirectory, FILENAME_RESULT));
		result.setRace(toRace(readPropertiesFile(raceDirectory, FILENAME_RACE)));
		result.setHorse(new Horse(readPropertiesFile(raceDirectory, FILENAME_HORSE)));
		result.setJockey(new Jockey(readPropertiesFile(raceDirectory, FILENAME_JOCKEY)));
		result.setTrainer(new Trainer(readPropertiesFile(raceDirectory, FILENAME_TRAINER)));
		
		return result;
	}

	public void storeMeets(List<Meeting> meets) throws Exception {
		for (Meeting meet : meets) {
			storeMeet(meet);
		}
	}

	public void storeMeet(Meeting meet) throws Exception {
		File meetFolder = new File(storageDirectory, String.valueOf(meet.getMeetCode()));
		meetFolder.mkdir();
		
		File meetingFile = new File(meetFolder, FILENAME_MEETING);
		writePropertiesFile(meetingFile, meet.getProperties());
		
		for (Race race : meet.getRaces()) {
			storeRace(meetFolder, race);
		}
	}

	private void storeRace(File meetFolder, Race race) throws Exception {
		File raceFolder = new File(meetFolder, String.valueOf(race.getRaceNumber()));
		raceFolder.mkdir();
		
		File raceFile = new File(raceFolder, FILENAME_RACE);
		writePropertiesFile(raceFile, race.getProperties());
		
		if (race.getResult() != null) {
			storRaceResult(raceFolder, race);
		}
		
		for (Runner runner : race.getRunners()) {
			storeRunner(raceFolder, runner);
		}
		
	}
	
	private void storRaceResult(File raceFolder, Race race) throws Exception {
		RaceResult result = race.getResult();
		File resultFile = new File(raceFolder, FILENAME_RESULT);
		writePropertiesFile(resultFile, result.getProperties());
	
		for (RunnerResult runner : result.getRunners()) {
			storeRunnerResult(raceFolder, runner);
		}
		
	}

	private void storeRunnerResult(File raceFolder, RunnerResult runner) throws Exception {
		File runnerFolder = new File(raceFolder, String.valueOf(runner.getNumber()));
		
		File resultFile = new File(runnerFolder, FILENAME_RESULT);
		writePropertiesFile(resultFile, runner.getProperties());	
	}

	private void storeRunner(File raceFolder, Runner runner) throws Exception {
		File runnerFolder = new File(raceFolder, String.valueOf(runner.getNumber()));
		runnerFolder.mkdir();
		
		File runnerFile = new File(runnerFolder, FILENAME_RUNNER);
		File horseFile = new File(runnerFolder, FILENAME_HORSE);
		File jockeyFile = new File(runnerFolder, FILENAME_JOCKEY);
		File trainerFile = new File(runnerFolder, FILENAME_TRAINER);
		File oddsFile = new File(runnerFolder, FILENAME_ODDS);
		File careerFile = new File(runnerFolder, FILENAME_CAREER);
		File goodAtFile = new File(runnerFolder, FILENAME_GOODAT);
		
		writePropertiesFile(runnerFile, runner.getProperties());
		writePropertiesFile(horseFile, runner.getHorse().getProperties());
		writePropertiesFile(jockeyFile, runner.getJockey().getProperties());
		writePropertiesFile(trainerFile, runner.getTrainer().getProperties());
		writePropertiesFile(oddsFile, runner.getOdds().getProperties());
		writePropertiesFile(careerFile, runner.getCareer());
		writePropertiesFile(goodAtFile, runner.getGoodAt());
		
		for (RunnerResult result : runner.getHorse().getPastResults()) {
			storePastRace(runnerFolder, result);
		}
	}


	private void storePastRace(File runnerFolder, RunnerResult result) throws Exception {
		File resultFolder = new File(runnerFolder, result.getRaceEntryCode());
		resultFolder.mkdir();
		
		File resultFile = new File(resultFolder, FILENAME_RESULT);
		File raceFile = new File(resultFolder, FILENAME_RACE);
		File horseFile = new File(resultFolder, FILENAME_HORSE);
		File jockeyFile = new File(resultFolder, FILENAME_JOCKEY);
		File trainerFile = new File(resultFolder, FILENAME_TRAINER);

		writePropertiesFile(resultFile, result.getProperties());
		writePropertiesFile(raceFile, result.getRaceProperties());
		writePropertiesFile(horseFile, result.getHorse().getProperties());
		writePropertiesFile(jockeyFile, result.getJockey().getProperties());
		writePropertiesFile(trainerFile, result.getTrainer().getProperties());
		
		storeSplits(resultFolder, result.getSplits());
	}

	private void storeSplits(File resultFolder, List<Double> splits) throws Exception {
		if (splits != null) {
			Properties props = new Properties();
			for (int i = 0; i < splits.size(); i++) {
				props.setProperty(String.valueOf(i), String.valueOf(splits.get(i)));
			}
			
			File splitFile = new File(resultFolder, FILENAME_SPLITS);
			writePropertiesFile(splitFile, props);
		}
	}

	private void writePropertiesFile(File aFile, Properties someProps) throws Exception {
		FileOutputStream fos = new FileOutputStream(aFile);
		if (someProps != null) {
			someProps.store(fos, "");
		}
		fos.flush();
		fos.close();
	}
	
	private Properties readPropertiesFile(File directory, String filename) throws Exception {
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(new File(directory, filename));
			props.load(fis);
		} catch (FileNotFoundException ex) {
			return null;
		}
		return props;
	}

	public int[] fetchRaceResult(String meetCode, int raceNumber) throws Exception {
		int[] result = new int[3];
		File meetFolder = new File(storageDirectory, meetCode);
		File raceFolder = new File(meetFolder, String.valueOf(raceNumber));
		File[] runnerFolders = raceFolder.listFiles();

		for (File runnerDir : runnerFolders) {
			Properties runnerProps = readPropertiesFile(runnerDir, FILENAME_RUNNER);
			String sNumber = runnerProps.getProperty(KEY_NUMBER);
			
			Properties props = readPropertiesFile(runnerDir, FILENAME_RESULT);
			String sPosition = props.getProperty(KEY_FINISH);
			if (sPosition != null) {
				int position = Integer.parseInt(sPosition);
				if (position <= result.length) {
					result[position - 1] = Integer.parseInt(sNumber);
				}
			}
		}
		
		return result;
	}

	public void storeRace(Race race) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void storeRunners(List<Runner> runners) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
