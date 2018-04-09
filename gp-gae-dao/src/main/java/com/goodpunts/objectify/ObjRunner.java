package com.goodpunts.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class ObjRunner {

	@Id
	private String horse;
	@Parent Key<ObjRace> race;
	private String jockey;
	private String trainer;
	private int number;
	private boolean scratched;
	private boolean emergency;
	@Index
	private boolean histories;
	private String raceCode;
	private double weight;
	private int barrier;
	private boolean goodAtDistance;
	private boolean goodAtTrackCondition;
	private boolean goodAtTrack;
	private boolean goodAtClass;
	
	public ObjRunner() {}
	
	public ObjRunner(String raceCode) {
		race = Key.create(ObjRace.class, raceCode);
		this.setRaceCode(raceCode);
	}
	
	public String getHorse() {
		return horse;
	}
	public void setHorse(String horse) {
		this.horse = horse;
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
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean isScratched() {
		return scratched;
	}
	public void setScratched(boolean scratched) {
		this.scratched = scratched;
	}
	public boolean isEmergency() {
		return emergency;
	}
	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	public boolean isHistories() {
		return histories;
	}

	public void setHistories(boolean histories) {
		this.histories = histories;
	}

	public String getRaceCode() {
		return raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
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

	public boolean isGoodAtDistance() {
		return goodAtDistance;
	}

	public void setGoodAtDistance(boolean goodAtDistance) {
		this.goodAtDistance = goodAtDistance;
	}

	public boolean isGoodAtTrackCondition() {
		return goodAtTrackCondition;
	}

	public void setGoodAtTrackCondition(boolean goodAtTrackCondition) {
		this.goodAtTrackCondition = goodAtTrackCondition;
	}

	public boolean isGoodAtTrack() {
		return goodAtTrack;
	}

	public void setGoodAtTrack(boolean goodAtTrack) {
		this.goodAtTrack = goodAtTrack;
	}

	public boolean isGoodAtClass() {
		return goodAtClass;
	}

	public void setGoodAtClass(boolean goodAtClass) {
		this.goodAtClass = goodAtClass;
	}

}
