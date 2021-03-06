package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Race { //extends RacingObject {

	private Properties URL;
	private List<Runner> runners;
	private int[] result;
	private double[] prizeMoney;
	
	private Date date;
	private int raceNumber;
	private String meetCode;
	private String venue;
	private String name;
	private String raceCode;
	private double distance;
	private boolean histories;
	private int lessThan3Races;
	private boolean trial;
	private Map<Integer, List<Double>> splits;
	private double racePrizeMoney;
	private int trackCondition;
	
	public Race() {
		runners = new ArrayList<Runner>();
	}

	public Properties getURL() {
		return URL;
	}

	public void setURL(Properties uRL) {
		URL = uRL;
	}

	public List<Runner> getRunners() {
		return runners;
	}

	public void setRunners(List<Runner> runners) {
		this.runners = runners;
	}
		
	@Override
	public boolean equals(Object o) {
		if (o instanceof Race) {
			return this.hashCode() == o.hashCode();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getRaceCode().hashCode();
	}

	public int getMaxRunnerNumber() {
		int max = 0;
		for (Runner runner : getRunners()) {
			if (runner.getNumber() > max) {
				max = runner.getNumber();
			}
		}
		return max;
	}

	public int[] getResult() {
		return result;
	}

	public void setResult(int[] result) {
		this.result = result;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getRaceNumber() {
		return raceNumber;
	}

	public void setRaceNumber(int raceNumber) {
		this.raceNumber = raceNumber;
	}

	public String getMeetCode() {
		return meetCode;
	}

	public void setMeetCode(String meetCode) {
		this.meetCode = meetCode;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRaceCode() {
		return raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}
	
	public Properties getProperties() {
		return null;
	}

	public double[] getPrizeMoney() {
		return prizeMoney;
	}

	public void setPrizeMoney(double[] prizeMoney) {
		this.prizeMoney = prizeMoney;
	}

	public double getPrizeMoney(int position) {
		if (prizeMoney == null) {
			return 0;
		}
		if (position < 0) {
			return 0;
		}
		if (position - 1 < prizeMoney.length) {
			return prizeMoney[position - 1];
		}
		return 0;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean hasHistories() {
		return histories;
	}

	public void setHistories(boolean histories) {
		this.histories = histories;
	}

	public void setNumberOfRunnersLessThan3Races(int lessThan3Races) {
		this.lessThan3Races = lessThan3Races;
	}
	
	public int getNumberOfRunnersLessThan3Races() {
		return lessThan3Races;
	}

	public boolean isTrial() {
		return trial;
	}

	public void setTrial(boolean trial) {
		this.trial = trial;
	}

	public void setSplits(Map<Integer, List<Double>> splits) {
		this.splits = splits;
	}	
	
	public Map<Integer, List<Double>> getSplits() {
		return splits;
	}

	public double getRacePrizeMoney() {
		return racePrizeMoney;
	}

	public void setRacePrizeMoney(double racePrizeMoney) {
		this.racePrizeMoney = racePrizeMoney;
	}

	public int getTrackCondition() {
		return trackCondition;
	}

	public void setTrackCondition(int trackCondition) {
		this.trackCondition = trackCondition;
	}

}
