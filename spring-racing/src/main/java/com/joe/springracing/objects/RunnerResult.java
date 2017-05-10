package com.joe.springracing.objects;

import java.util.Date;
import java.util.List;

public class RunnerResult {
	
//	private Properties raceProperties;

	private String jockey;
	private String trainer;
	private String horse;
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
	private String meetCode;
	private double distance;
	private int raceNumber;
	private String raceName;
	//TODO Splits and Sectionals
	private boolean resultsFetched;
	private double weight;
	
	public RunnerResult() {
//		horse = new Horse();
	}

//	public Properties getRaceProperties() {
//		return raceProperties;
//	}
//
//	public void setRaceProperties(Properties raceProperties) {
//		this.raceProperties = raceProperties;
//	}

//	public String getRaceEntryCode() {
//		return getProperty(RacingKeys.KEY_RESULT_RACE_CODE);
//	}

	public String getHorse() {
		return horse;
	}

	public void setHorse(String horse) {
		this.horse = horse;
	}

	public String getTrainer() {
		return trainer;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

	public String getJockey() {
		return jockey;
	}

	public void setJockey(String jockey) {
		this.jockey = jockey;
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

//	public int getNumber() {
//		int number = getNumber(RacingKeys.KEY_NUMBER);
//		if (number == 0) {
//			return getNumber(RacingKeys.KEY_RESULT_RACEENTRYNUMBER);
//		}
//		return number;
//	}

//	public RacingObject getPosition() {
//		return position;
//	}
//
//	public void setPosition(RacingObject position) {
//		this.position = position;
//	}

//	public int getResult() {
////		if (position == null) {
////			return getNumber(RacingKeys.KEY_RESULT_RESULT);
////		}
//		int finish = getNumber(RacingKeys.KEY_RESULT_RESULT);
//		if (finish == 0) {
//			return getNumber(RacingKeys.KEY_FINISH);
//		}
//		return finish;
//	}

//	public double getPrizeMoney() {
//		return getDouble(RacingKeys.KEY_RESULT_PRIZEMONEY);
//	}
//
//	public void setPrizeMoney(double prizeMoney) {
//		this.setProperty(RacingKeys.KEY_RESULT_PRIZEMONEY, String.valueOf(prizeMoney));
//	}

	public boolean isScratched() {
		String resultType = getResultType();
		return "Scratched".equals(resultType);
	}

//	public int getRaceEntryCode() {
//		return raceEntryCode;
//	}
//
//	public void setRaceEntryCode(int raceEntryCode) {
//		this.raceEntryCode = raceEntryCode;
//	}
//
//	public int getRaceEntryNumber() {
//		return raceEntryNumber;
//	}
//
//	public void setRaceEntryNumber(int raceEntryNumber) {
//		this.raceEntryNumber = raceEntryNumber;
//	}

	public double getPrizeMoney() {
		return prizeMoney;
	}

	public void setPrizeMoney(double prizeMoney) {
		this.prizeMoney = prizeMoney;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
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
	
	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getMeetCode() {
		return meetCode;
	}

	public void setMeetCode(String meetCode) {
		this.meetCode = meetCode;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getRaceNumber() {
		return raceNumber;
	}

	public void setRaceNumber(int raceNumber) {
		this.raceNumber = raceNumber;
	}

	public String getRaceName() {
		return raceName;
	}

	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}

	public boolean isResultsFetched() {
		return resultsFetched;
	}

	public void setResultsFetched(boolean resultsFetched) {
		this.resultsFetched = resultsFetched;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RunnerResult) {
			return this.hashCode() == o.hashCode();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getRaceCode().hashCode() + this.getHorse().hashCode();
	}

}
