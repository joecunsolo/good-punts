package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting { //extends RacingObject {

	private List<Race> races;
	private int meetCode;
	private String venue;
	private Date date;
	
	public Meeting() {
		races = new ArrayList<Race>();
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

	
}
