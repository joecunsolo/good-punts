package com.joe.springracing.dao.datasource;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.utils.io.json.JsonReaderIO;

/**
 * This is the parser... takes some json string and converts it 
 * to the spring racing object
 * 
 * @author joecunsolo
 *
 */
public class RacingDotComParser extends JsonReaderIO {
	
	public static final String DELIMITTER_COMMA = ",";
	public static final String DELIMITTER_CLOSE_PARENTHESES = ")";
	public static final String DELIMITTER_COLON = ":";
	
	public static final String TRUE = "true";
	
	public static final String KEY_RUNNERS = "runners";
	public static final String KEY_HORSE = "horse";
	public static final String KEY_TRAINER = "trainer";
	public static final String KEY_JOCKEY = "jockey";
	public static final String KEY_ODDS = "odds";
	public static final String KEY_URL = "Url";
	public static final String KEY_CAREER = "career";
	public static final String KEY_GOODATFILTERS = "goodAtFilters";
	public static final String KEY_GOODAT_TRACK = "isgoodattrack";
	public static final String KEY_GOODAT_CLASS = "isgoodatclass";
	public static final String KEY_GOODAT_CONDITION = "isgoodattrackcondition";
	public static final String KEY_GOODAT_DISTANCE = "isgoodatdistance";
	public static final String KEY_RACE_RESULTS = "resultCollection";
	public static final String KEY_RACE = "race";
	public static final String KEY_RACE_NUMBER = "number";
	public static final String KEY_RACE_CODE = "code";
	public static final String KEY_RACE_NAME = "name";
	public static final String KEY_RACE_TIME = "time";
	public static final String KEY_RACE_DATE = "date";
	public static final String KEY_RACE_VENUE = "venuename";
	public static final String KEY_RACE_RESULT = "result";
	public static final String KEY_RACE_DISTANCE = "distance";
	public static final String KEY_RACE_PRIZEMONEY = "prizemoneydetails";
	public static final String KEY_RACE_ISTRIAL = "istrial";
	public static final String KEY_RACE_TRACKCONDITION = "trackconditionratingsource";
	
	public static final String KEY_RESULT_NUMBER = "raceentrynumber";
	public static final String KEY_RESULT_POSITION = "position";
	public static final String KEY_RESULT_POSITION_FINISH = "finish";
	//there are a bunch of these - not sure if we need to know all the numbers
	/**
    SCR - Scratched - 109
    LSCR - Late Scratched
    FF - Failed to Finish
    LR - Lost Rider
    F - Fell
    NP - Not Placed
    DQ - Disqualified
    LP - Left at Post
    BD - Brought Down
    RO - Ran Off */
	public static final int VALUE_RESULT_SCRACTHED = 100;
	public static final int VALUE_SECTIONAL_DISTANCE = 200;
	public static final String VALUE_SECTIONALTIMES_CALLBACK = "sectionaltimes_callback(";
	
	public static final String KEY_RESULT_PRIZEMONEY_DETAILS = "PrizeMoneyDetails";
	public static final String KEY_RESULT_RACE = "Race";
	public static final String KEY_RESULT_HORSE = "Horse";
	public static final String KEY_RESULT_TRAINER = "Trainer";
	public static final String KEY_RESULT_JOCKEY = "Jockey";
	public static final String KEY_RESULT_WEIGHT = "carriedweight";
	public static final String KEY_RESULT_RACE_ENTRY_NUMBER = "raceentrynumber";
	public static final String KEY_RESULT_BARNUMBER = "barnumber";
	public static final String KEY_RESULT_RATING = "horserating";
	public static final String KEY_RESULT_PRIZEMONEY = "prizemoney";
	public static final String KEY_RESULT_TRACKCONDITION = "trackconditionratingsource";
	
	public static final String KEY_SECTIONAL_HORSES = "Horses";
	public static final String KEY_SECTIONAL_SPLITS = "SplitTimes";
	public static final String KEY_SECTIONAL_TIMES = "SectionalTimes";
	public static final String KEY_SECTIONAL_SADDLE = "SaddleNumber";
	public static final String KEY_SECTIONAL_SPLITS_DISTANCE = "distance";
	public static final String KEY_SECTIONAL_SPLITS_TIME = "time";
	public static final String KEY_SECTIONAL_RACETIME = "racetime";
	public static final String KEY_MEETCODE = "meetcode";
	public static final String KEY_DATA_URL = "dataurl";
	public static final String KEY_TIME = "time";
	
	public static final String KEY_RUNNER_EMEGENCY = "emergencyentry";
	public static final String KEY_RUNNER_SCRATCHED = "scratched";
	public static final String KEY_RUNNER_NUMBER = "number";
	public static final String KEY_RUNNER_BARRIER = "barriernumber";
	public static final String KEY_RUNNER_JOCKEY = "urlsegment";
	public static final String KEY_RUNNER_TRAINER = "urlsegment";
	public static final String KEY_RUNNER_WEIGHT = "weight";

	public static final String KEY_HORSE_CODE = "code";
	public static final String KEY_HORSE_URL = "urlsegment";
	public static final String KEY_HORSE_NAME = "fullname";
	
	public static final String KEY_ODDS_WIN = "returnwin";
	public static final String KEY_ODDS_PLACE = "returnplace";
	public static final String KEY_HORSE_TOTALPRIZEMONEY = "totalprizemoney";
	public static final String KEY_HORSE_AVERAGEPRIZEMONEY = "averageprize";
	public static final String KEY_HORSE_COLOUR = "colour";
	public static final String KEY_HORSE_SEX = "sex";
	public static final String KEY_HORSE_AGE = "age";

//	private static SimpleDateFormat raceTimeFormat = new SimpleDateFormat("mm:ss.SS");
	private SimpleDateFormat raceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private DecimalFormat df = new DecimalFormat("#,000");

	public List<Race> parseRaces(String html) throws Exception { 
		List<Race> result = new ArrayList<Race>();
		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonArray array = jsonReader.readArray();
		for (int i = 0; i < array.size(); i++) {
			JsonObject jObject = array.getJsonObject(i);
			Properties props = parseProperties(jObject);
			Race race = toRace(props);

			//Dont worry about
			if ((!result.contains(race)) && //races we already have
					race.getDate() != null && //races not scheduled
					race.getDate().getTime() > System.currentTimeMillis() - (1000 * 60 * 60 * 24) && //races in the past
					props.getProperty(KEY_TIME) != null) { //races not scheduled
				JsonObject jUrl = jObject.getJsonObject(KEY_URL);
				race.setURL(parseProperties(jUrl));
				
				result.add(race);
			}
		}
		return result;
	}
	
	private Race toRace(Properties props) {
		Race race = new Race();
		race.setMeetCode(props.getProperty(KEY_MEETCODE));
		race.setRaceNumber(Integer.parseInt(props.getProperty(KEY_RACE_NUMBER)));
		race.setRaceCode(props.getProperty(KEY_RACE_CODE));
		race.setName(props.getProperty(KEY_RACE_NAME));
		race.setVenue(props.getProperty(KEY_RACE_VENUE));
		race.setDistance(Double.parseDouble(props.getProperty(KEY_RACE_DISTANCE)));
		race.setTrial(Boolean.TRUE.toString().equals(props.getProperty(KEY_RACE_ISTRIAL)));
		try {
			race.setDate(raceDateFormat.parse(props.getProperty(KEY_RACE_TIME)));
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return race;
	}
	
	//returns the 200m sectionals for all the horses in the race - indexed by saddle number
	public Map<Integer, List<Double>> parseSectionalTimes(String html) throws Exception {
		Map<Integer, List<Double>> result = new HashMap<Integer, List<Double>>();
		if (html == null) {
			return result;
		}
		//remove the sectionaltimes_callback wrapper
		html = html.substring(VALUE_SECTIONALTIMES_CALLBACK.length(), html.length() -1);
		
		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonObject object = jsonReader.readObject();
		JsonValue value = object.get(KEY_SECTIONAL_HORSES);
		if (!(value instanceof JsonArray)) {
			return null;
		}
		JsonArray horses = (JsonArray)value;
		for (int i = 0; i < horses.size(); i++) {
			JsonObject horseObject = horses.getJsonObject(i);
			int number = horseObject.getInt(KEY_SECTIONAL_SADDLE);
			JsonValue sectionalsValue = horseObject.get(KEY_SECTIONAL_TIMES);
			//get this horses sectionals
			List<Double> sectionals = parseSectionalTimes(sectionalsValue);
			result.put(new Integer(number), sectionals);
		}
		
		return result;
	}
	
	//takes a sectionals JSON object and turns it into a List of Double
	private List<Double> parseSectionalTimes(JsonValue sectionalsValue) throws ParseException {
		if (!(sectionalsValue instanceof JsonArray)) {
			return null;
		}
		JsonArray sectionals = (JsonArray)sectionalsValue;
		
		Double[] rawResult = new Double[sectionals.size() - 1]; //ignore the FINISH
		for (int i = 0; i < sectionals.size(); i++) {
			try {
				JsonObject jObject = sectionals.getJsonObject(i);
				Properties props = parseProperties(jObject);
				String distance = props.getProperty(KEY_SECTIONAL_SPLITS_DISTANCE);
				
				int index = distanceToIndex(distance);
				if (index != -1 && index < rawResult.length) {
					double time = parseSplitTime(props.getProperty(KEY_SECTIONAL_SPLITS_TIME));
					rawResult[index] = time;	
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return Arrays.asList(rawResult);
	}
	
	//mm:ss.SS
	private Double parseSplitTime(String sTime) {
		int minutes = 0;
		if (sTime.length() > 5) {
			int minuteIndex = sTime.indexOf(DELIMITTER_COLON);
			String sMinutes = sTime.substring(0, minuteIndex);
			minutes = Integer.parseInt(sMinutes);
			
			sTime = sTime.substring(minuteIndex+1);
		}
		
		try {
			double time = Double.parseDouble(sTime);
			time += minutes*60;
			return time;
		} catch (Exception ex) {
			return new Double(0);
		}
	}
	
	public int distanceToIndex(String distance) {
		try {
			distance = distance.substring(0, distance.length() - 1);
			int index = Integer.parseInt(distance);
			return (index / VALUE_SECTIONAL_DISTANCE) - 1;
		} catch (Exception ex) {
			return -1;
		}
	}
	
	public String parseSplitsAndSectionals(String html) {
		if (html == null) {
			return null;
		}
		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonObject object = jsonReader.readObject();
		Properties props = parseProperties(object);
		return props.getProperty(KEY_DATA_URL);
	}

	
	public List<Runner> parseRunners(String html) {
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
		
			//need a map to the horse
			Horse horse = parseHorse(jObject.get(KEY_HORSE)); 
		
			RacingDotComRunner runner = new RacingDotComRunner();
			String weight = props.getProperty(KEY_RUNNER_WEIGHT);
			try {
				weight = weight.substring(0, weight.length() - 2);
				runner.setWeight(Double.parseDouble(weight));
			} catch (Exception ex) {
				//do nothing - it's ok if it doesn't have a weight
			}
			runner.setEmergency(Boolean.valueOf(props.getProperty(KEY_RUNNER_EMEGENCY)));
			runner.setScratched(Boolean.valueOf(props.getProperty(KEY_RUNNER_SCRATCHED)));
			runner.setNumber(Integer.parseInt(props.getProperty(KEY_RUNNER_NUMBER)));
			runner.setHorse(horse.getId());
			runner.setTrainer(parseTrainer(jObject.get(KEY_TRAINER)));
			runner.setJockey(parseJockey(jObject.get(KEY_JOCKEY)));
			runner.setBarrier(Integer.parseInt(props.getProperty(KEY_RUNNER_BARRIER)));
//			runner.setRaceCode(raceCode);
			
			JsonValue odds = jObject.get(KEY_ODDS);
			if (odds != null & odds instanceof JsonObject) {
				runner.setOdds(parseOdds((JsonObject)odds));
			}
			JsonValue goodat = jObject.get(KEY_GOODATFILTERS);
			if (goodat != null & goodat instanceof JsonObject) {
				Properties goodAtProps = parseProperties(goodat);
				horse.setGoodAtClass(TRUE.equals(goodAtProps.getProperty(KEY_GOODAT_CLASS)));
				horse.setGoodAtDistance(TRUE.equals(goodAtProps.getProperty(KEY_GOODAT_DISTANCE)));
				horse.setGoodAtTrack(TRUE.equals(goodAtProps.getProperty(KEY_GOODAT_TRACK)));
				horse.setGoodAtTrackCondition(TRUE.equals(goodAtProps.getProperty(KEY_GOODAT_CONDITION)));
			}
			
			runner.setHorseObject(horse);	
			result.add(runner);
		}
		return result;
	}

	public List<RunnerResult> parseResults(String html) {
		if (html == null) {
			return new ArrayList<RunnerResult>();
		}
		
		List<RunnerResult> results = new ArrayList<RunnerResult>();

		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonArray array = null; 
//		JsonObject object = null;
		JsonStructure struct = jsonReader.read();
		try {
			array = (JsonArray)struct;
		} catch (Exception ex) {
//			object = (JsonObject)struct;
		}
		for (int i = 0; i < array.size() && i < 15; i++) {
			RunnerResult result = new RunnerResult();
			JsonObject jObject = array.getJsonObject(i);
			
			Properties props = parseProperties(jObject);
			result.setPosition(Integer.parseInt(props.getProperty(KEY_RACE_RESULT)));

			try {
				result.setJockey(parseJockey(jObject.get(KEY_RESULT_JOCKEY)));
				result.setTrainer(parseTrainer(jObject.get(KEY_RESULT_TRAINER)));
				Race race = parseRace(jObject.get(KEY_RESULT_RACE));
				result.setRaceCode(race.getRaceCode());
				result.setDistance(race.getDistance());
				result.setWeight(Double.parseDouble(props.getProperty(KEY_RESULT_WEIGHT)));
				result.setTrial(race.isTrial());
				try {
					result.setNumber(Integer.parseInt(props.getProperty(KEY_RESULT_RACE_ENTRY_NUMBER)));
				} catch (Exception ex) {}
				try {
					result.setBarrier(Integer.parseInt(props.getProperty(KEY_RESULT_BARNUMBER)));
				} catch (Exception ex) {}
				try {
					result.setHorseRating(Double.parseDouble(props.getProperty(KEY_RESULT_RATING)));
				} catch (Exception ex) {}
				try {
					result.setRacePrizeMoney(Double.parseDouble(props.getProperty(KEY_RESULT_PRIZEMONEY)));
				} catch (Exception ex) {}
				try {
					result.setTrackCondition(Integer.parseInt(props.getProperty(KEY_RESULT_TRACKCONDITION)));
				} catch (Exception ex) {}
			} catch (NullPointerException nex) {
				throw new RuntimeException(nex);
			}
			results.add(result);
		}
		return results;
	}

//	private double parseRaceTime(String time) throws ParseException {
//		if (time == null) {
//			return 0;
//		}
//		time = time.trim();
//		if (time.length() < 6) {
//			time = "00:" + time;
//		}
//		Date timeAsDate = raceTimeFormat.parse(time);
//		return timeAsDate.getTime() / (1000.00);
//	}
	
	public Race parseRaceDetails(String html) {
		if (html != null) {
			JsonReader jsonReader = Json.createReader(new StringReader(html));
			JsonObject jObject = jsonReader.readObject();
		    Race result = toRace(parseProperties(jObject));
		
			result.setPrizeMoney(readDoubleArray(jObject.getJsonArray(KEY_RESULT_PRIZEMONEY_DETAILS)));
			return result;
		}
		return null;
	}

	public int[] parseRaceResults(String html) {
		JsonReader jsonReader = Json.createReader(new StringReader(html));
		JsonObject jObject = jsonReader.readObject();
		
		JsonArray array = jObject.getJsonArray(KEY_RACE_RESULTS);
		int[] results = new int[array.size()];
		int scratchings = 0;
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject jRunner = array.getJsonObject(i);
			Properties props = parseProperties(jRunner);
			
			JsonObject jPosition = jRunner.getJsonObject(KEY_RESULT_POSITION);
			props.putAll(parseProperties(jPosition));
			
			int finish = -1;
			int number = 0;
			try {
				number = Integer.parseInt(props.getProperty(KEY_RESULT_NUMBER));
				finish = Integer.parseInt(props.getProperty(KEY_RESULT_POSITION_FINISH));
			} catch (Exception ex) {}
			
			//Racing.com's method of handling exceptions
			if (finish <= results.length && finish > 0) {
				results[finish-1] = number;
			} else {
				scratchings++;
			}
		}
		
		return Arrays.copyOf(results, results.length - scratchings);
	}


	private String parseTrainer(JsonValue jsonObject) {
		try {
			Properties props = parseProperties(jsonObject);
			return props.getProperty(KEY_RUNNER_TRAINER);
		} catch (Exception ex) {
			return null;
		}
	}

	private String parseJockey(JsonValue jsonObject) {
		try {
			Properties props = parseProperties(jsonObject);
			return props.getProperty(KEY_RUNNER_JOCKEY);
		} catch (Exception ex) {
			return null;
		}
	}

	/** tries to set the odds with the fixed odds then the parimutel odds if not available */
	private Odds parseOdds(JsonObject jsonObject) {
		if (jsonObject != null) {
			
			Odds odds = new Odds();
			try {
				setOdds(odds, jsonObject, KEY_FIXED_ODDS);
			} catch (Exception ex) {
				try {
					setOdds(odds, jsonObject, KEY_PARIMUTEL_ODDS);
				} catch (Exception ex2) {}
			}
			return odds;
		}
		return null;
	}
	
	private void setOdds(Odds odds, JsonObject jsonObject, String key) {
		Properties props = parseProperties(jsonObject.get(key));
		odds.setWin(Double.parseDouble(props.getProperty(KEY_ODDS_WIN)));
		odds.setPlace(Double.parseDouble(props.getProperty(KEY_ODDS_PLACE)));
	}

	private Horse parseHorse(JsonValue jsonObject) {
		Properties props = parseProperties(jsonObject);
		Horse result = new Horse();
		result.setCode(props.getProperty(KEY_HORSE_CODE));
		result.setName(props.getProperty(KEY_HORSE_NAME));
		result.setId(props.getProperty(KEY_HORSE_URL));
		try {
			result.setPrizeMoney(df.parse(props.getProperty(KEY_HORSE_TOTALPRIZEMONEY).substring(1)).doubleValue()); //totalprizemoney
		} catch (Exception ex) {}
		try {
			result.setAveragePrizeMoney(df.parse(props.getProperty(KEY_HORSE_AVERAGEPRIZEMONEY).substring(1)).doubleValue()); //averageprize
		} catch (Exception ex) {}
		result.setColour(props.getProperty(KEY_HORSE_COLOUR)); //colour
		result.setSex(props.getProperty(KEY_HORSE_SEX)); //sex
		try {
			result.setAge(Integer.parseInt(props.getProperty(KEY_HORSE_AGE))); //age
		} catch (Exception ex) {}
		
		return result;
	}
	
	private Race parseRace(JsonValue jsonValue) {
		Properties props = parseProperties(jsonValue);
		Race race = new Race();
		race.setName(props.getProperty(KEY_RACE_NAME));
		race.setRaceCode(props.getProperty(KEY_RACE_CODE));
		race.setDistance(Double.parseDouble(props.getProperty(KEY_RACE_DISTANCE)));
		race.setRaceNumber(Integer.parseInt(props.getProperty(KEY_RACE_NUMBER)));
		try {
			race.setDate(raceDateFormat.parse(props.getProperty(KEY_RACE_DATE)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return race;
	}
}
