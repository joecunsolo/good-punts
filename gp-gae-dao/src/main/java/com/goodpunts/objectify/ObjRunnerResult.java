package com.goodpunts.objectify;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ObjRunnerResult {

	@Id String id;
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
	private double weight;
	private int barrier;
	private double rating;
	private boolean trial;
	private double racePrizeMoney;
	private int trackCondition;
	private double margin;
	private double oddsStart;
	private boolean firstUp;
	private boolean secondUp;
	private boolean thirdUp;
	private boolean fourthUp;
	private int spell;

	public ObjRunnerResult() {}
	
	public ObjRunnerResult(String horseCode, String raceCode) {
		horse = Key.create(ObjHorse.class, horseCode);
		id = horseCode + raceCode;
		setRaceCode(raceCode);
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getBarrier() {
		return barrier;
	}
	
	public void setBarrier(int barrier) {
		this.barrier = barrier;
	}

	public double getHorseRating() {
		return rating;
	}
	
	public void setHorseRating(double rating) {
		this.rating = rating;
	}

	public boolean isTrial() {
		return trial;
	}
	
	public void setTrial(boolean trial) {
		this.trial = trial;
	}

	public void setRacePrizeMoney(double racePrizeMoney) {
		this.racePrizeMoney = racePrizeMoney;
	}
	
	public double getRacePrizeMoney() {
		return racePrizeMoney;
	}

	public int getTrackCondition() {
		return trackCondition;
	}

	public void setTrackCondition(int trackCondition) {
		this.trackCondition = trackCondition;
	}

	public double getMargin() {
		return margin;
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}

	public void setOddsStart(double oddsStart) {	
		this.oddsStart = oddsStart;
	}
	
	public double getOddsStart() {
		return oddsStart;
	}

	public void setFirstUp(boolean firstUp) {
		this.firstUp = firstUp;
	}

	public void setSecondUp(boolean secondUp) {
		this.secondUp = secondUp;
	}

	public void setThirdUp(boolean thirdUp) {
		this.thirdUp = thirdUp;		
	}
	
	public boolean getFirstUp() {
		return firstUp;
	}

	public boolean getSecondUp() {
		return secondUp;
	}

	public boolean getThirdUp() {
		return thirdUp;
	}

	public void setSpell(int spell) {
		this.spell = spell;
	}
	
	public int getSpell() {
		return spell;
	}

	public boolean isFourthUp() {
		return fourthUp;
	}

	public void setFourthUp(boolean fourthUp) {
		this.fourthUp = fourthUp;
	}

}
