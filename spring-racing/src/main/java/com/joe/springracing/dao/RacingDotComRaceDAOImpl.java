package com.joe.springracing.dao;

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
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RaceResult;
import com.joe.springracing.objects.RacingObject;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.utils.io.html.HTMLReaderIO;
import com.joe.springracing.utils.io.json.JsonReaderIO;

//https://api.racing.com/api/meet/RacesByDay/1/4/

public class RacingDotComRaceDAOImpl {


	


	
//	public void enrichPastResults(RunnerResult runner) throws Exception {
//		Race race = runner.getRace();
//		String urlToRead = getRaceURL(race.getCode());
//		String html = HTMLReaderIO.getHTML(urlToRead);			
//
//		try {
//			RaceResult result = parseRaceDetails(html);
//			runner.setPrizeMoney(result.getPrizeMoney(runner.getResult()));
//			
//			runner.getRace().setResult(result);
//			setSplitsAndSectionals(result.getMeetCode(), race.getRaceNumber(), runner);
//		} catch (Exception ex) {
//			///Uugh now we are ugly!!
//		}
//	}
//
//	private RaceResult parseRaceDetails(String html) {
//		JsonReader jsonReader = Json.createReader(new StringReader(html));
//		JsonObject jObject = jsonReader.readObject();
//		Properties props = parseProperties(jObject);
//		
//		RaceResult result = new RaceResult(props);
//		result.setPrizeMoney(readDoubleArray(jObject.getJsonArray(KEY_RESULT_PRIZEMONEY_DETAILS)));
//		
//		return result;
//	}
//
//	public void enrichPastResults(String meetCode, int raceNumber,	RunnerResult result) throws Exception {
//		setSplitsAndSectionals(meetCode, raceNumber, result);
//	}
//	


}
