package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Meeting { //extends RacingObject {

	private List<Race> races;
	private int meetCode;
	private String venue;
	private Date date;
	
	public Meeting() {
		races = new ArrayList<Race>();
	}

	public Meeting(Race race) {
		// TODO Auto-generated constructor stub
	}

	public int getMeetCode() {
//		return getProperty(KEY_MEETCODE);
		return meetCode;
	}

	public String getVenue() {
//		return getProperty(KEY_VENUENAME);		
		return venue;
	}
	
	public Date getDate() {
//		return getDate(KEY_DATE);
		return date;
	}

	public void addRace(Race race) {
		races.add(race);
	}

	public List<Race> getRaces() {
		return races;
	}

	public void setRaces(List<Race> races2) {
		this.races = races2;
	}

	public void setMeetCode(int meetCode) {
		this.meetCode = meetCode;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
