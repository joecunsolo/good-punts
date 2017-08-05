package com.joe.springracing.objects;

import java.util.List;

public class SplitsAndSectionals {

	private List<Double> splits;
	private double raceTime;
	public List<Double> getSplits() {
		return splits;
	}
	public void setSplits(List<Double> splits) {
		this.splits = splits;
	}
	public double getRaceTime() {
		return raceTime;
	}
	public void setRaceTime(double raceTime) {
		this.raceTime = raceTime;
	}
}
