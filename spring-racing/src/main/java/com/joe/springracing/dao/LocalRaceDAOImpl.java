package com.joe.springracing.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.joe.springracing.exporter.RunnerHistoriesExporter;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.objects.Runner;

public class LocalRaceDAOImpl implements SpringRacingDAO {

	private static final String FILENAME_MEETING = "meeting.props";
	private static final String FILENAME_RACE = "race.props";
	private static final String FILENAME_RUNNER = "runner.props";
	private static final String FILENAME_HORSE = "horse.props";
	private static final String FILENAME_ODDS = "odds.props";
	private static final String FILENAME_RESULT = "result.props";
	private static final String FILENAME_SPLITS = "splits.props";
	
	private static final String DIRECTORY_MEETS = "meets";
	private static final String DIRECTORY_RACES = "races";
	private static final String DIRECTORY_HORSE = "horses";
	
	public static final String KEY_MEET_TIME = "time";
	public static final String KEY_MEET_CODE = "meetcode";
	public static final String KEY_MEET_VENUENAME = "venuename";
	
	public static final String KEY_RACE_NAME = "name";
	public static final String KEY_RACE_CODE = "racecode";
	public static final String KEY_RACE_VENUE = "venuename";
	public static final String KEY_RACE_DATE = "date";
	public static final String KEY_RACE_PRIZEMONEY = "prizemoney";
	public static final String KEY_RACE_NUMBER = "number";
	public static final String KEY_RACE_RESULT = "result";
	public static final String KEY_RACE_RUNNERS = "runners";
	public static final String KEY_RACE_DISTANCE = "distance";

	public static final String KEY_JOCKEY_ID = "jockeyid";
	public static final String KEY_TRAINER_ID = "trainerid";
	private static final String KEY_RUNNER_EMERGENCY = "emergency";
	private static final String KEY_RUNNER_SCRATCHED = "scratched";

	private static final String KEY_HORSE = "horse";
	private static final String KEY_HORSE_CODE = "code";
	private static final String KEY_HORSE_URL = "urlsegment";
	private static final String KEY_HORSE_NAME = "fullname";

	public static final String KEY_FINISH = "finish";
	public static final String KEY_NUMBER = "number";
	private static final String KEY_ODDS_WIN = "win";
	private static final String KEY_ODDS_PLACE = "place";
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
	private File storageDirectory;
	public LocalRaceDAOImpl(String rootDir) {
		storageDirectory = new File(rootDir);
	}
	
	public List<Meeting> fetchExistingMeets() throws Exception {
		List<Meeting> meetings = new ArrayList<Meeting>();
		File[] meetFiles = new File(storageDirectory, DIRECTORY_MEETS).listFiles();
		for (File meetDirectory : meetFiles) {
			Meeting meet = readMeeting(meetDirectory);
			
			meetings.add(meet);			
		}
		
		return meetings;
	}
	
	public List<Meeting> fetchMeetsWithoutResults() throws Exception {
		List<Meeting> allMeets = fetchExistingMeets();
		List<Meeting> result = new ArrayList<Meeting>();
		for (Meeting meet : allMeets) {
			System.out.println(meet.getDate() + " " + meet.getVenue());
			if (!meet.hasResults()) {
//				if (races) {
//					fetchRacesForMeet(meet, runners, history);
//				}
				result.add(meet);
			}
		}
		return result;
	}
	
	public List<Race> fetchRacesForMeet(Meeting meet) throws Exception {
		File[] raceFiles = getMeetDirectory(meet.getMeetCode()).listFiles();
		for (File raceDirectory : raceFiles) {
			if (raceDirectory.isDirectory()) {
				meet.addRace(readRace(raceDirectory));
			}
		}
		return meet.getRaces();
	}
	
	public List<Race> fetchRaces(List<String> raceCodes) throws Exception {
		List<Race> result = new ArrayList<Race>();

		File raceDirectory = getRaceDirectory();
		for (String raceCode : raceCodes) {
			File f = new File(raceDirectory, raceCode);
			if (f.isDirectory()) {
				result.add(readRace(f));
			}
		}
		return result;
	}
	
	private File getRaceDirectory() {
		return new File(storageDirectory, DIRECTORY_RACES);	
	}

	private File getMeetDirectory(String meetCode) {
		File meetParent = new File(storageDirectory, DIRECTORY_MEETS);
		return new File(meetParent, String.valueOf(meetCode));	
	}

	private Meeting readMeeting(File meetDirectory) throws Exception {
		Properties props = readPropertiesFile(meetDirectory, FILENAME_MEETING);
		Meeting m = toMeeting(props);
		
		String[] sRaceCodes = fromString(props.getProperty(KEY_RACE_CODE)); 
		List<Race> races = fetchRaces(Arrays.asList(sRaceCodes));
		m.setRaces(races);
		
		return m;
	}
	
	private static String[] fromString(String string) {
		if (string == null) {
			return new String[0];
		}
		String[] strings = string.replace("[", "").replace("]", "").split(", ");
	    return strings;
	}
	
	private Meeting toMeeting(Properties props) throws ParseException {
		Meeting m = new Meeting();
		m.setVenue(props.getProperty(KEY_MEET_VENUENAME));
		m.setMeetCode(props.getProperty(KEY_MEET_CODE));
		m.setDate(dateFormat.parse(props.getProperty(KEY_MEET_TIME)));
		return m;
	}

	public Race fetchRace(String raceCode) throws Exception {
		File raceDirectory = getRaceDirectory(raceCode);
		Race race = readRace(raceDirectory);
		
		return race;
	}

	private File getRaceDirectory(String raceCode) {
		return new File(new File(storageDirectory, DIRECTORY_RACES), String.valueOf(raceCode));
	}

	private Race readRace(File raceDirectory) throws Exception {
		Race race = toRace(readPropertiesFile(raceDirectory, FILENAME_RACE));
		List<Runner> runners = fetchRunnersForRace(race);
		race.setRunners(runners);

		return race;
	}
	
	private Race toRace(Properties props) throws Exception {
		Race race = new Race();
		race.setRaceCode(props.getProperty(KEY_RACE_CODE));
		race.setMeetCode(props.getProperty(KEY_MEET_CODE));
		race.setName(props.getProperty(KEY_RACE_NAME));
		race.setVenue(props.getProperty(KEY_RACE_VENUE));
		race.setRaceNumber(Integer.parseInt(props.getProperty(KEY_RACE_NUMBER)));
		race.setPrizeMoney(doubleArrayFromString(props.getProperty(KEY_RACE_PRIZEMONEY)));
		race.setDistance(Double.parseDouble(props.getProperty(KEY_RACE_DISTANCE)));
		race.setDate(dateFormat.parse(props.getProperty(KEY_RACE_DATE)));
		race.setResult(intArrayFromString(props.getProperty(KEY_RACE_RESULT)));
		
		return race;
	}

	private double[] doubleArrayFromString(String property) {
		String[] fromString = fromString(property);
		double[] result = new double[fromString.length];
		for (int i = 0; i < fromString.length; i++) {
			result[i] = Double.parseDouble(fromString[i]);
		}
		return result;
	}

	private int[] intArrayFromString(String property) {
		String[] fromString = fromString(property);
		int[] result = new int[fromString.length];
		for (int i = 0; i < fromString.length; i++) {
			result[i] = Integer.parseInt(fromString[i]);
		}
		return result;
	}

	public List<Runner> fetchRunnersForRace(Race race)
			throws Exception {
		List<Runner> runners = new ArrayList<Runner>();
		File raceDirectory = getRaceDirectory(race.getRaceCode());
		File[] runnerFiles = raceDirectory.listFiles();
		
		for (File runnerFile : runnerFiles) {
			if (runnerFile.isDirectory()) {
				runners.add(readRunner(race, runnerFile));
			}
		}
		return runners;
	}
	
	//Make the runner have the runner result
	private Runner readRunner(Race race, File runnerDirectory) throws Exception {
		Runner runner = toRunner(readPropertiesFile(runnerDirectory, FILENAME_RUNNER));
		runner.setOdds(toOdds(readPropertiesFile(runnerDirectory, FILENAME_ODDS)));

		return runner;
	}

	private Odds toOdds(Properties props) {
		Odds odds = new Odds();
		odds.setWin(Double.parseDouble(KEY_ODDS_WIN));
		odds.setPlace(Double.parseDouble(KEY_ODDS_PLACE));
		return odds;
	}

	private Runner toRunner(Properties props) {
		Runner runner = new Runner();
		runner.setEmergency(Boolean.valueOf(props.getProperty(KEY_RUNNER_EMERGENCY)));
		runner.setTrainer(props.getProperty(KEY_TRAINER_ID));
		runner.setHorse(props.getProperty(KEY_HORSE));
		runner.setNumber(Integer.parseInt(props.getProperty(KEY_NUMBER)));
		runner.setJockey(props.getProperty(KEY_JOCKEY_ID));
		runner.setScratched(Boolean.valueOf(props.getProperty(KEY_RUNNER_SCRATCHED)));
		return runner;
	}

	private RunnerResult toRunnerResult(Properties props) throws Exception {
		RunnerResult result = new RunnerResult();
		result.setDistance(Double.parseDouble(props.getProperty(KEY_RACE_DISTANCE)));
		result.setHorse(props.getProperty(KEY_HORSE));
		result.setJockey(props.getProperty(KEY_JOCKEY_ID));
		result.setMeetCode(props.getProperty(KEY_MEET_CODE));
		result.setPosition(Integer.parseInt(props.getProperty(KEY_RACE_RESULT)));
		result.setPrizeMoney(Double.parseDouble(props.getProperty(KEY_RACE_PRIZEMONEY)));
		result.setRaceCode(props.getProperty(KEY_RACE_CODE));
		try {
			result.setRaceDate(dateFormat.parse(props.getProperty(KEY_RACE_DATE)));
		} catch (Exception ex) {}
		result.setRaceName(props.getProperty(KEY_RACE_NAME));
		result.setRaceNumber(Integer.parseInt(props.getProperty(KEY_RACE_NUMBER)));
		result.setTrainer(props.getProperty(KEY_TRAINER_ID));
		result.setVenueName(props.getProperty(KEY_RACE_VENUE));
		return result;
	}

	private RunnerResult readRunnerResult(File file) throws Exception {
		RunnerResult result = toRunnerResult(readPropertiesFile(file));		
		return result;
	}

	public void storeMeets(List<Meeting> meets) throws Exception {
		for (Meeting meet : meets) {
			storeMeet(meet);
		}
	}

	public boolean storeMeet(Meeting meet) throws Exception {
		File meetFolder = getMeetDirectory(meet.getMeetCode());
		meetFolder.mkdirs();
		
		File meetingFile = new File(meetFolder, FILENAME_MEETING);
		writePropertiesFile(meetingFile, toProperties(meet));
		//TODO return true if it is newly added = false otherwise
		return true;
	}

	private Properties toProperties(Meeting meet) {
		Properties props = new Properties();
		props.setProperty(KEY_MEET_TIME, dateFormat.format(meet.getDate()));
		props.setProperty(KEY_MEET_CODE, meet.getMeetCode());
		props.setProperty(KEY_MEET_VENUENAME, meet.getVenue());
		String[] raceCodes = new String[meet.getRaces().size()];
		for (int i = 0; i < raceCodes.length; i++) {
			raceCodes[i] = meet.getRaces().get(i).getRaceCode();
		}
		props.setProperty(KEY_RACE_CODE, Arrays.toString(raceCodes));
		
		return props;
	}

	public void storeRace(Race race) throws Exception {
		File raceFolder = getRaceDirectory(race.getRaceCode());
		raceFolder.mkdirs();
		
		File raceFile = new File(raceFolder, FILENAME_RACE);
		writePropertiesFile(raceFile, toProperties(race));
		
		for (Runner runner : race.getRunners()) {
			storeRunner(raceFolder, runner);
		}
		
	}
	
	private Properties toProperties(Race race) {
		Properties props = new Properties();
		props.setProperty(KEY_MEET_CODE, race.getMeetCode());
		props.setProperty(KEY_RACE_NAME, race.getName());
		props.setProperty(KEY_RACE_CODE, race.getRaceCode());
		props.setProperty(KEY_RACE_VENUE, race.getVenue());
		props.setProperty(KEY_RACE_DATE, dateFormat.format(race.getDate()));
		props.setProperty(KEY_RACE_NUMBER, String.valueOf(race.getRaceNumber()));
		props.setProperty(KEY_RACE_DISTANCE, String.valueOf(race.getDistance()));
		if (race.getPrizeMoney() != null) { 
			props.setProperty(KEY_RACE_PRIZEMONEY, Arrays.toString(race.getPrizeMoney()));
		}
		if (race.getResult() != null) {
			props.setProperty(KEY_RACE_RESULT, Arrays.toString(race.getResult()));
		}
		
		//Fudge on the end
		String[] runners = new String[race.getRunners().size()];
		for (int i = 0; i < race.getRunners().size(); i++) {
			runners[i] = race.getRunners().get(i).getHorse();
		}
		props.setProperty(KEY_RACE_RUNNERS, Arrays.toString(runners));
		return props;
	}

	private void storeRunner(File raceFolder, Runner runner) throws Exception {
		File runnerFolder = new File(raceFolder, String.valueOf(runner.getNumber()));
		runnerFolder.mkdir();
		
		File runnerFile = new File(runnerFolder, FILENAME_RUNNER);
		File oddsFile = new File(runnerFolder, FILENAME_ODDS);

		writePropertiesFile(runnerFile, toProperties(runner));
		writePropertiesFile(oddsFile, toProperties(runner.getOdds()));
	}


	private Properties toProperties(Odds odds) {
		Properties props = new Properties();
		props.setProperty(KEY_ODDS_WIN, String.valueOf(odds.getWin()));
		props.setProperty(KEY_ODDS_PLACE, String.valueOf(odds.getPlace()));
		return props;
	}

	private Properties toProperties(Runner runner) {
		Properties props = new Properties();
		props.setProperty(KEY_RUNNER_EMERGENCY, String.valueOf(runner.isEmergency()));
		props.setProperty(KEY_RUNNER_SCRATCHED, String.valueOf(runner.isScratched()));
		props.setProperty(KEY_HORSE, runner.getHorse());
		props.setProperty(KEY_NUMBER, String.valueOf(runner.getNumber()));
		try {
			props.setProperty(KEY_JOCKEY_ID, runner.getJockey());
		} catch (Exception ex) {}
		try {
			props.setProperty(KEY_TRAINER_ID, runner.getTrainer());
		} catch (Exception ex) {}
		return props;
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
		return readPropertiesFile(new File(directory, filename));
	}
	
	private Properties readPropertiesFile(File file) throws IOException {
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(file);
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

	public void storeHorse(Horse horse) throws Exception {
		File runnerFolder = getRunnerDirectory(horse.getId());
		runnerFolder.mkdirs();
		
		File horseFile = new File(runnerFolder, FILENAME_HORSE);
		writePropertiesFile(horseFile, toProperties(horse));
	}
	
	private Properties toProperties(Horse horse) {
		Properties props = new Properties();
		props.setProperty(KEY_HORSE_CODE, horse.getCode());
		props.setProperty(KEY_HORSE_NAME, horse.getName());
		props.setProperty(KEY_HORSE_URL, horse.getId());
		return props;
	}

	private File getRunnerDirectory(String horseCode) {
		File horseFolder = new File(storageDirectory, DIRECTORY_HORSE);
		return new File(horseFolder, horseCode);
	}

	public void storeResults(List<RunnerResult> results) throws Exception {
		for (RunnerResult result : results) {
			storeResult(result);
		}
	}

	public void storeResult(RunnerResult result) throws Exception {
		File raceFolder = new File(storageDirectory, DIRECTORY_HORSE);
		File runnerFolder = new File(raceFolder, result.getHorse());
		raceFolder.mkdirs();
		
		File runnerFile = new File(runnerFolder, result.getRaceCode() + ".props");
		writePropertiesFile(runnerFile, toProperties(result));
	}

	private Properties toProperties(RunnerResult result) {
		Properties props = new Properties();
		props.setProperty(KEY_RACE_RESULT, String.valueOf(result.getPosition()));
		props.setProperty(KEY_RACE_PRIZEMONEY, String.valueOf(result.getPrizeMoney()));
		props.setProperty(KEY_MEET_CODE, result.getMeetCode());
		props.setProperty(KEY_RACE_NAME, result.getRaceName());
		props.setProperty(KEY_MEET_VENUENAME, result.getVenueName());
		props.setProperty(KEY_RACE_CODE, result.getRaceCode());
		props.setProperty(KEY_NUMBER, String.valueOf(result.getRaceNumber()));
		try {
			props.setProperty(KEY_RACE_DATE, dateFormat.format(result.getRaceDate()));
		} catch (Exception ex) {}
		props.setProperty(KEY_RACE_DISTANCE, String.valueOf(result.getDistance()));
		try {
			props.setProperty(KEY_JOCKEY_ID, result.getJockey());
		} catch (Exception ex) {}
		try {
			props.setProperty(KEY_TRAINER_ID, result.getTrainer());
		} catch (Exception ex) {}
		return props;
	}

	public Horse fetchHorse(String horse) throws Exception {
		File horseDirectory = getRunnerDirectory(horse);
		Properties props = readPropertiesFile(horseDirectory, FILENAME_HORSE);
		Horse result = parseHorse(props);
		File[] resultFiles = horseDirectory.listFiles();
		for (File file : resultFiles) {
			if (!FILENAME_HORSE.equals(file.getName())) {
				RunnerResult runner = readRunnerResult(file);
				runner.setHorse(horse);
				result.addPastResult(runner);
			}
		}
		return result;
	}
	
	protected Horse parseHorse(Properties props ) {
		Horse result = new Horse();
		result.setCode(props.getProperty(KEY_HORSE_CODE));
		result.setName(props.getProperty(KEY_HORSE_NAME));
		result.setId(props.getProperty(KEY_HORSE_URL));
		return result;
	}

	public List<Race> fetchRacesWithoutHistories() {
		// TODO Auto-generated method stub
		return null;
	}

	public Meeting fetchMeet(String meetCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void exportRunnerHistories(RunnerHistoriesExporter exporter) throws Exception {
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

	public List<Meeting> fetchUpcomingMeets() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
