package com.goodpunts.objectify;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ObjRunnerResult {

	@Id Long Id;
	private String jockey;
	private String trainer;
	@Parent
	private Key<ObjHorse> horse;
	private String raceCode;
	private Date raceDate;
	private List<Double> splits;
	private double raceTime;
//	private int raceEntryCode;
//	private int raceEntryNumber;
	private double prizeMoney;
	private String resultType;
	private int position;
	private String venueName;
	private double distance;

	public ObjRunnerResult() {}
	
	public ObjRunnerResult(String horseCode) {
		horse = Key.create(ObjHorse.class, horseCode);
	}
	
	public String getJockey() {
		return jockey;
	}
	public void setJockey(String jockey) {
		this.jockey = jockey;
	}
	public String getTrainer() {
		return trainer;
	}
	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}
	public String getRaceCode() {
		return raceCode;
	}
	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}
	public Date getRaceDate() {
		return raceDate;
	}
	public void setRaceDate(Date raceDate) {
		this.raceDate = raceDate;
	}
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
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public double getPrizeMoney() {
		return prizeMoney;
	}
	public void setPrizeMoney(double prizeMoney) {
		this.prizeMoney = prizeMoney;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
