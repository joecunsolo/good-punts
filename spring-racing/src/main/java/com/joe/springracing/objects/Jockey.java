package com.joe.springracing.objects;

import java.util.Properties;

import static com.joe.springracing.business.RacingKeys.*;

public class Jockey extends AnalysableRacePropertyObject {

	public Jockey(Properties props) {
		super(props);
	}
	
	public Jockey() {
		this(new Properties());
	}
	
	public String getURL () {
		return getProperty(KEY_JOCKEY_URL);
	}
	
	public boolean raced(RunnerResult result) {
		return result.getJockey().equals(this);
	}

	public String getId() {
		return getURL();
	}
}
