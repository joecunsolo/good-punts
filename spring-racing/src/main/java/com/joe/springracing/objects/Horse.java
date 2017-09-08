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
}
