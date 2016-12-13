package com.joe.springracing.objects;

import static com.joe.springracing.business.RacingKeys.KEY_TRAINER_URL;

import java.util.Properties;

public class Trainer extends AnalysableRacePropertyObject {

	public Trainer(Properties props) {
		super(props);
	}
	
	public Trainer() {
		this(new Properties());
	}
	
	public String getURL () {
		return getProperty(KEY_TRAINER_URL);
	}

	public boolean raced(RunnerResult result) {
		return result.getTrainer().equals(this);
	}

	public String getId() {
		return getURL();
	}

}
