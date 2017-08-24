package com.joe.springracing.exporter;

import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class RaceRunner {

	public Runner runner;
	public Race race;
	
	public RaceRunner(Race race2, Runner runner2) {
		this.race = race2;
		this.runner = runner2;
	}

}

