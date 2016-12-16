package com.joe.springracing.dao.datasource;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Jockey;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RaceResult;
import com.joe.springracing.objects.RacingObject;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.objects.SplitsAndSectionals;
import com.joe.springracing.objects.Trainer;
import com.joe.springracing.utils.io.html.HTMLReaderIO;
import com.joe.springracing.utils.io.json.JsonReaderIO;

public class RacingDotComDataSource extends JsonReaderIO {

	public static final int RACE_RESULTS_PER_PAGE = 5;
	public static final int NUM_PAGES = 1;
	public static final int HORSE_RESULTS_PER_PAGE = 30;

	public static final String DELIMITTER_COMMA = ",";
	public static final String DELIMITTER_CLOSE_PARENTHESES = ")";
	
	public static final String KEY_RUNNERS = "runners";
	public static final String KEY_HORSE = "horse";
	public static final String KEY_TRAINER = "trainer";
	public static final String KEY_JOCKEY = "jockey";
	public static final String KEY_ODDS = "odds";
	public static final String KEY_URL = "Url";
	public static final String KEY_CAREER = "career";
	public static final String KEY_GOODATFILTERS = "goodAtFilters";
	public static final String KEY_RACE_RESULTS = "resultCollection";
	public static final String KEY_RACE = "race";
	public static final String KEY_RESULT_NUMBER = "raceEntryNumber";
	public static final String KEY_RESULT_POSITION = "position";
	public static final String KEY_RESULT_POSITION_FINISH = "finish";
	public static final String KEY_RESULT_PRIZEMONEY_DETAILS = "PrizeMoneyDetails";
	public static final String KEY_RESULT_RACE = "Race";
	public static final String KEY_RESULT_HORSE = "Horse";
	public static final String KEY_RESULT_TRAINER = "Trainer";
	public static final String KEY_RESULT_JOCKEY = "Jockey";
	public static final String KEY_SECTIONAL_HORSES = "Horses";
	public static final String KEY_SECTIONAL_SPLITS = "SplitTimes";
	public static final String KEY_SECTIONAL_SPLITS_TIME = "time";
	public static final String KEY_SECTIONAL_RACETIME = "racetime";
	public static final String KEY_MEETCODE = "meetcode";
	public static final String KEY_DATA_URL = "dataurl";
	public static final String KEY_TIME = "time";
	
	public static final String PREFIX_RACE_RESULTS_URL = "https://api.racing.com/v1/en-au/race/results/";
	public static final String PREFIX_RACE_DAY_URL = "https://api.racing.com/api/meet/RacesByDay/";
	public static final String PREFIX_RUNNER_URL = "https://api.racing.com/v1/en-au/race/entries/";
	public static final String PREFIX_SPLIT_SECTION_URL = "https://api.racing.com/v1/en-au/race/splitsandsectionals/";
	public static final String PREFIX_RACE_URL = "https://api.racing.com/api/race/details/";
	public static final String PREFIX_FORM_URL = "https://api.racing.com/api/form/horse/";

	public static final String KEY_WRAP_SECTIONAL_TIMES_CALLBACK = "sectionaltimes_callback(";
	
	private static SimpleDateFormat raceTimeFormat = new SimpleDateFormat("mm:ss.SS");
	
	public List<Race> fetchRaces() throws Exception {
		List<Race> races = new ArrayList<Race>();
		for (int i = 0; i < 10; i += RACE_RESULTS_PER_PAGE) {
			String urlToRead = getRaceDayURL(i, RACE_RESULTS_PER_PAGE);
			String html = HTMLReaderIO.getHTML(urlToRead);			
			parseRaces(html, races);
		}
		return races;
	}
	
	private List<Race> parseRaces(String html, List<Race> result) throws IOException {
		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonArray array = jsonReader.readArray();
		
		
		for (int i = 0; i < array.size(); i++) {
			Race race = new Race();
			
			JsonObject jObject = array.getJsonObject(i);
			Properties props = parseProperties(jObject);
//			race.setProperties(props);

			//Dont worry about
			if ((!result.contains(race)) && //races we already have
					race.getDate().getTime() > System.currentTimeMillis() - (1000 * 60 * 60 * 24) && //races in the past
					props.getProperty(KEY_TIME) != null) { //races not scheduled
				JsonObject jUrl = jObject.getJsonObject(KEY_URL);
				race.setURL(parseProperties(jUrl));
				
				result.add(race);
			}
		}
		return result;
	}

	public List<Runner> fetchRunnnersForRace(int meetCode, int race) throws Exception {
		String urlToRead = getRunnerURL(meetCode, race);
		String html = HTMLReaderIO.getHTML(urlToRead);			
		return parseRunners(html);
	}
	
	private List<Runner> parseRunners(String html) {
		List<Runner> result = new ArrayList<Runner>();
		if (html == null) {
			return result;
		}
		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonObject object = jsonReader.readObject();
		JsonValue value = object.get(KEY_RUNNERS);
		if (!(value instanceof JsonArray)) {
			return null;
		}
		JsonArray array = (JsonArray)value;
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject jObject = array.getJsonObject(i);
			Properties props = parseProperties(jObject);
			
			Runner runner = new Runner(props);
			runner.setHorse(parseHorse(jObject.get(KEY_HORSE)));
			runner.setTrainer(parseTrainer(jObject.get(KEY_TRAINER)));
			runner.setJockey(parseJockey(jObject.get(KEY_JOCKEY)));
			JsonValue odds = jObject.get(KEY_ODDS);
			if (odds != null & odds instanceof JsonObject) {
				runner.setOdds(parseOdds((JsonObject)odds));
			}
			
			runner.setCareer(parseProperties(jObject.get(KEY_CAREER)));
			runner.setGoodAt(parseProperties(jObject.get(KEY_GOODATFILTERS)));

			result.add(runner);
		}
		return result;
	}
	
	public List<Horse> fetchHorses(List<String> horses) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<RunnerResult> fetchPastResultsForHorse(Horse horse) throws Exception {
		String urlToRead = getHorseURL(horse);
		String html = HTMLReaderIO.getHTML(urlToRead);	
		List<RunnerResult> results = new ArrayList<RunnerResult>();

		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonArray array = jsonReader.readArray();

		for (int i = 0; i < array.size(); i++) {
			RunnerResult result = new RunnerResult();
			
			JsonObject jObject = array.getJsonObject(i);
			
			Properties props = parseProperties(jObject);
			result.setProperties(props);

			try {
				result.setJockey(parseJockey(jObject.get(KEY_RESULT_JOCKEY)));
				result.setTrainer(parseTrainer(jObject.get(KEY_RESULT_TRAINER)));
				result.setHorse(parseHorse(jObject.get(KEY_RESULT_HORSE)));
				result.setRace(parseRace(jObject.get(KEY_RESULT_RACE)));
			} catch (NullPointerException nex) {
				throw new RuntimeException(nex);
			}
			results.add(result);
		}
		return results;
	}
	
//	private RunnerResult getRunner(RaceResult race, int number) {
//		for (RunnerResult runner : race.getRunners()) {
//			if (runner.getNumber() == number) {
//				return runner;
//			}
//		}
//		return null;
//	}
	
//	public SplitsAndSectionals fetchSplits(RunnerResult result) throws Exception {
//		String urlToRead = getSplitAndSectionalURL(result.getRace().getMeetCode(), result.getRace().getRaceNumber());
//		String html = HTMLReaderIO.getHTML(urlToRead);			
//
//		Properties props = parseRaceSplitsOverview(html);
//		String dataURL = props.getProperty(KEY_DATA_URL);
//		if (dataURL.length() <= 2) {
//			return null;
//		}
//		urlToRead = PREFIX_PROTOCOL_HTTP + dataURL;
//		html = HTMLReaderIO.getHTML(urlToRead);
//		if (html == null) {
//			return null;
//		}
//		
//		try {
//			return parseSplitsAndSectionals(html, result);
//		} catch (Exception ex) {
//			System.out.println(urlToRead);
//			throw new RuntimeException(ex);
//		}
//	}

//	private Properties parseRaceSplitsOverview(String html) {
//		JsonReader jsonReader = Json.createReader(new StringReader(html));
//		return parseProperties(jsonReader.readObject());
//	}

//	private SplitsAndSectionals parseSplitsAndSectionals(String html, RunnerResult result) throws ParseException {
//		html = html.substring(KEY_WRAP_SECTIONAL_TIMES_CALLBACK.length());
//		html = html.substring(0, html.length() - DELIMITTER_CLOSE_PARENTHESES.length());
//
//		JsonReader jsonReader = Json.createReader(new StringReader(html));
//		JsonArray array = jsonReader.readObject().getJsonArray(KEY_SECTIONAL_HORSES);
//
//		for (int i = 0; i < array.size(); i++) {
//			JsonObject jObject = array.getJsonObject(i);
//			Horse horse = parseHorse(jObject);
//			
//			if (horse.getName().equals(result.getHorse().getName())) {
//				SplitsAndSectionals splits = new SplitsAndSectionals();
//				String time = horse.getProperty(KEY_SECTIONAL_RACETIME);
//				splits.setRaceTime(parseRaceTime(time));
//				
//				splits.setSplits(parseHorseSplits(jObject));
//				return splits;
//			}
//		}
//		return null;
//	}
	
	public double parseRaceTime(String time) throws ParseException {
		if (time == null) {
			return 0;
		}
		time = time.trim();
		if (time.length() < 6) {
			time = "00:" + time;
		}
		Date timeAsDate = raceTimeFormat.parse(time);
		return timeAsDate.getTime() / (1000.00);
	}

//	private List<Double> parseHorseSplits(JsonObject jObject) {
//		List<Double> splits = new ArrayList<Double>();
//		
//		JsonArray array = jObject.getJsonArray(KEY_SECTIONAL_SPLITS);
//		for (int i = 0; i < array.size(); i++) {
//			JsonObject jSplit = array.getJsonObject(i);
//			Properties props = parseProperties(jSplit);
//	
//			String time = props.getProperty(KEY_SECTIONAL_SPLITS_TIME);
//			if (time.length() > 0) {
//				splits.add(new Double(time));
//			} else {
//				splits.add(new Double(-1));
//			}
//		}
//		return splits;
//	}
	
	public RaceResult fetchRaceResult(String racecode) throws Exception {
		String urlToRead = getRaceURL(racecode);
		String html = HTMLReaderIO.getHTML(urlToRead);			

		RaceResult result = parseRaceDetails(html);
		setPrizeMoney(result);
		return result;
//			
//			runner.getRace().setResult(result);
//			setSplitsAndSectionals(result.getMeetCode(), race.getRaceNumber(), runner);
//		return result;
	}

	private void setPrizeMoney(RaceResult result) {
		double[] prizeMoney = result.getPrizeMoney();
		for (RunnerResult runner : result.getRunners()) {
			if (runner.getResult() < prizeMoney.length) {
				runner.setPrizeMoney(prizeMoney[runner.getResult() - 1]);
			}
		}
	}

	private RaceResult parseRaceDetails(String html) {
		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonObject jObject = jsonReader.readObject();
		Properties props = parseProperties(jObject);
		
		RaceResult result = new RaceResult(props);
		result.setPrizeMoney(readDoubleArray(jObject.getJsonArray(KEY_RESULT_PRIZEMONEY_DETAILS)));
		
		return result;
	}
	
	public Meeting fetchMeet(String meetCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RaceResult fetchRaceResult(Race race) throws Exception {
//		if (race.getMeetCode() == null) {
//			return fetchRaceResult(race.getCode());
//		}
		String urlToRead = getRaceResultURL(race.getMeetCode(), race.getRaceNumber());
		String html = HTMLReaderIO.getHTML(urlToRead);			
		return parseRaceResults(html, race);
	}	

	private RaceResult parseRaceResults(String html, Race race) {
		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonObject jObject = jsonReader.readObject();
		
		RaceResult result = new RaceResult(parseProperties(jObject.getJsonObject(KEY_RACE)));
		JsonArray array = jObject.getJsonArray(KEY_RACE_RESULTS);
				
		for (int i = 0; i < array.size(); i++) {
			JsonObject jRunner = array.getJsonObject(i);
			Properties props = parseProperties(jRunner);
			
			JsonObject jPosition = jRunner.getJsonObject(KEY_RESULT_POSITION);
			props.putAll(parseProperties(jPosition));

			RunnerResult runner = new RunnerResult(props);
			result.getRunners().add(runner);
		}
		
		return result;
	}


	private String getRaceResultURL(int meetCode, int race) {
		return PREFIX_RACE_RESULTS_URL + meetCode + "/" + race;
	}

	private String getRaceDayURL(int page, int resultsPerPage) {
		return PREFIX_RACE_DAY_URL + page + "/" + resultsPerPage;
	}
	
	private String getRunnerURL(int meeting, int race) {
		return PREFIX_RUNNER_URL + meeting + "/" + race;
	}

	private String getRaceURL(String raceCode) {
		return PREFIX_RACE_URL + raceCode;
	}

//	private String getSplitAndSectionalURL(String meetCode, int race) {
//		return PREFIX_SPLIT_SECTION_URL + meetCode + "/" + race;
//	}
	
	private String getHorseURL(Horse horse) {
		return PREFIX_FORM_URL + horse.getCode() + "/" + NUM_PAGES + "/" + HORSE_RESULTS_PER_PAGE;
	}
	
	protected Trainer parseTrainer(JsonValue jsonObject) {
		return new Trainer(parseProperties(jsonObject));
	}

	protected Jockey parseJockey(JsonValue jsonObject) {
		return new Jockey(parseProperties(jsonObject));
	}

	protected Odds parseOdds(JsonObject jsonObject) {
		if (jsonObject != null) {
			return new Odds(parseProperties(jsonObject.get(KEY_FIXED_ODDS)));			
		}
		return null;
	}

	protected Horse parseHorse(JsonValue jsonObject) {
		return new Horse(parseProperties(jsonObject));
	}
	
	protected Race parseRace(JsonValue jsonValue) {
		return new Race();//parseProperties(jsonValue));
	}

}
