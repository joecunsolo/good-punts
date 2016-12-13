package com.joe.springracing.objects;

import java.util.List;
import java.util.Properties;

import com.joe.springracing.business.RacingKeys;

public class RunnerResult extends RacingObject {
	
	private Properties raceProperties;

	private Jockey jockey;
	private Trainer trainer;
	private Horse horse;
	private Race race;
	private List<Double> splits;
	private double raceTime;
	
	public RunnerResult() {
		this(new Properties());
	}
		
	public RunnerResult(Properties props) {
		super(props);
		jockey = new Jockey();
		trainer = new Trainer();
		horse = new Horse();
		race = new Race();		
	}

	public Properties getRaceProperties() {
		return raceProperties;
	}

	public void setRaceProperties(Properties raceProperties) {
		this.raceProperties = raceProperties;
	}

	public String getRaceEntryCode() {
		return getProperty(RacingKeys.KEY_RESULT_RACE_CODE);
	}

	public Horse getHorse() {
		return horse;
	}

	public void setHorse(Horse horse) {
		this.horse = horse;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	public Jockey getJockey() {
		return jockey;
	}

	public void setJockey(Jockey jockey) {
		this.jockey = jockey;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
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

	public int getNumber() {
		int number = getNumber(RacingKeys.KEY_NUMBER);
		if (number == 0) {
			return getNumber(RacingKeys.KEY_RESULT_RACEENTRYNUMBER);
		}
		return number;
	}

//	public RacingObject getPosition() {
//		return position;
//	}
//
//	public void setPosition(RacingObject position) {
//		this.position = position;
//	}

	public int getResult() {
//		if (position == null) {
//			return getNumber(RacingKeys.KEY_RESULT_RESULT);
//		}
		int finish = getNumber(RacingKeys.KEY_RESULT_RESULT);
		if (finish == 0) {
			return getNumber(RacingKeys.KEY_FINISH);
		}
		return finish;
	}

	public double getPrizeMoney() {
		return getDouble(RacingKeys.KEY_RESULT_PRIZEMONEY);
	}

	public void setPrizeMoney(double prizeMoney) {
		this.setProperty(RacingKeys.KEY_RESULT_PRIZEMONEY, String.valueOf(prizeMoney));
	}

	public boolean isScratched() {
		// TODO Auto-generated method stub
		String resultType = getProperty(RacingKeys.KEY_RESULT_TYPE);
		return "Scratched".equals(resultType);
	}

}
