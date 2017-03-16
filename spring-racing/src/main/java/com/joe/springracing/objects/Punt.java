package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.List;

public class Punt {

	private Race race;
	private Type type;
	private double joesOdds;
	private double bookieOdds;
	private List<Runner> runners;
	private List<Stake> stakes;
	
	public enum Type {
		WIN,
		PLACE,
		TRIFECTA
	}
	
	public Punt() {
		this.runners = new ArrayList<Runner>();
	}
	
	public Punt(Race r, Type t, double odds, double bookieOdds) {
		this();
		this.setRace(r);
		this.setType(t);
		this.setJoesOdds(odds);
		this.setBookieOdds(bookieOdds);
	}	

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public double getJoesOdds() {
		return joesOdds;
	}

	public void setJoesOdds(double joesOdds) {
		this.joesOdds = joesOdds;
	}

	public double getBookieOdds() {
		return bookieOdds;
	}

	public void setBookieOdds(double bookieOdds) {
		this.bookieOdds = bookieOdds;
	}

	public List<Runner> getRunners() {
		return runners;
	}

	public void setRunners(List<Runner> runners) {
		this.runners = runners;
	}
	
	public double variance() {
		return bookieOdds / joesOdds;
	}

	public List<Stake> getStakes() {
		return stakes;
	}

	public void setStakes(List<Stake> stakes) {
		this.stakes = stakes;
	}
}
