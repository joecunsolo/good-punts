package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Race { //extends RacingObject {

	private Properties URL;
	private List<Runner> runners;
	private RaceResult result;
	
	private Date date;
	private int raceNumber;
	private int meetCode;
	private String venue;
	private String name;
	private String raceCode;
	
	
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
		return getMeetCode() * 100 + getRaceNumber();
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

	public RaceResult getResult() {
		return result;
	}

	public void setResult(RaceResult result) {
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

	public int getMeetCode() {
		return meetCode;
	}

	public void setMeetCode(int meetCode) {
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

}
