package com.goodpunts.objectify;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

@Entity
public class ObjRace  {

	private int[] result;
	@Index
	private boolean results;
	private double[] prizeMoney;
	private Date date;
	private int raceNumber;
	@Index
	private String meetCode;
	private String venue;
	private String name;
	@Id
	private String raceCode;
	private double distance;
	@Index
	private boolean histories;
	
	public ObjRace() {}
	
	@OnSave 
	private void updateResults() {
        this.setResults(result != null && result.length > 0);
    }
	
	public int[] getResult() {
		return result;
	}
	public void setResult(int[] result) {
		this.result = result;
	}
	public double[] getPrizeMoney() {
		return prizeMoney;
	}
	public void setPrizeMoney(double[] prizeMoney) {
		this.prizeMoney = prizeMoney;
	}
	public int getRaceNumber() {
		return raceNumber;
	}
	public void setRaceNumber(int raceNumber) {
		this.raceNumber = raceNumber;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setHistories(boolean hasHistories) {
		this.histories = hasHistories;
	}
	
	public boolean hasHistories() {
		return histories;
	}

	public boolean hasResults() {
		return results;
	}

	public void setResults(boolean results) {
		this.results = results;
	}

}
