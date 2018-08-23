package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.List;

public class Horse extends AnalysableRacePropertyObject {

	private List<RunnerResult> results;
	private String name;
	private String code;
	private String id;
	private boolean histories = false;
	private int numberOfRaces;
	private int spell;
	private List<Double> splits;
	private boolean hasSplits;
	private double prizeMoney;
	private double averagePrizeMoney;
	private String colour;
	private String sex;
	private int age;
	
	private boolean goodAtDistance;
	private boolean goodAtTrackCondition;
	private boolean goodAtTrack;
	private boolean goodAtClass;
	
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
	
	public Horse() {
		results = new ArrayList<RunnerResult>();
	}
	
	public void addPastResult(RunnerResult result) {
		results.add(result);
	}
	
	public List<RunnerResult> getPastResults() {
		return results;
	}

	public boolean raced(RunnerResult result) {
		return result.getHorse().equals(this.getId());
	}

	public void setPastResults(List<RunnerResult> results2) {
		this.results = results2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setHistories(boolean histories) {
		this.histories = histories;
	}
	
	public boolean hasHistories() {
		return histories;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Horse) {
			return this.hashCode() == o.hashCode();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	public int getNumberOfRaces() {
		return numberOfRaces;
	}

	public void setNumberOfRaces(int numberOfRaces) {
		this.numberOfRaces = numberOfRaces;
	}

	/** Days since last race */
	public int getSpell() {
		return spell;
	}

	public void setSpell(int spell) {
		this.spell = spell;
	}

	public void setSplits(List<Double> splits) {
		this.splits = splits;
	}
	
	public List<Double> getSplits() {
		return splits;
	}

	public boolean hasSplits() {
		return hasSplits;
	}
	
	public void setHasSplits(boolean splits) {
		this.hasSplits = splits;
	}

	public double getPrizeMoney() {
		return prizeMoney;
	}

	public void setPrizeMoney(double prizeMoney) {
		this.prizeMoney = prizeMoney;
	}

	public double getAveragePrizeMoney() {
		return averagePrizeMoney;
	}

	public void setAveragePrizeMoney(double averagePrizeMoney) {
		this.averagePrizeMoney = averagePrizeMoney;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
