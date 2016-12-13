package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.joe.springracing.business.RacingKeys.*;

public class Horse extends AnalysableRacePropertyObject {

	private List<RunnerResult> results;
	
	public Horse(Properties props) {
		super(props);
		results = new ArrayList<RunnerResult>();
	}

	public Horse() {
		this(new Properties());
	}
	
	public String getName() {
		return getProperty(KEY_FULLNAME);
	}
	
	public String getCode() {
		return getProperty(KEY_CODE);
	}

	public void addPastResult(RunnerResult result) {
		results.add(result);
	}
	
	public List<RunnerResult> getPastResults() {
		return results;
	}

	public boolean raced(RunnerResult result) {
		return result.getHorse().equals(this);
	}

	public String getId() {
		return getURL();
	}

	public String getURL () {
		return getProperty(KEY_HORSE_URL);
	}

	public void setPastResults(List<RunnerResult> results2) {
		this.results = results2;
	}
	
}
