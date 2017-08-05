package com.joe.springracing.dao.datasource;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.utils.io.html.HTMLReaderIO;

/**
 * This reads from the url..
 * It's pretty dumb takes some url gets the html and then feeds it to the parser.
 * 
 * @author joecunsolo
 *
 */
public class RacingDotComReader  {

	private RacingDotComParser parser;
	
	public RacingDotComReader() {
		this.setParser(new RacingDotComParser());
	}
	
	public List<Race> readRaces(String url) throws Exception {
		String html = HTMLReaderIO.getHTML(url);
		try {
			return parser.parseRaces(html);
		} catch (javax.json.stream.JsonParsingException jpe) {
			return null;
		}
	}
	
	public List<Runner> readRunners(String url) throws Exception {
		String html = HTMLReaderIO.getHTML(url);	
		try {
			return parser.parseRunners(html);
		} catch (javax.json.stream.JsonParsingException jpe) {
			return null;
		}
	}
		
	public Race readRace(String urlToRead) throws Exception {		
		String html = HTMLReaderIO.getHTML(urlToRead);			
		try {
			return parser.parseRaceDetails(html);
		} catch (javax.json.stream.JsonParsingException jpe) {
			return null;
		}
	}

	public List<RunnerResult> readResults(String url) throws Exception {
		String html = HTMLReaderIO.getHTML(url);
		try {
			return parser.parseResults(html);
		} catch (javax.json.stream.JsonParsingException jpe) {
			return null;
		}
	}
	
	public int[] readRaceResult(String urlToRead) throws Exception {
		String html = HTMLReaderIO.getHTML(urlToRead);			
		try {
			return parser.parseRaceResults(html);
		} catch (javax.json.stream.JsonParsingException jpe) {
			return null;
		}
	}

	public RacingDotComParser getParser() {
		return parser;
	}

	public void setParser(RacingDotComParser parser) {
		this.parser = parser;
	}	
	
}
