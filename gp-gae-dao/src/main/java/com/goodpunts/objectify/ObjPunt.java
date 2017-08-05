package com.goodpunts.objectify;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import com.joe.springracing.objects.Punt.Confidence;
import com.joe.springracing.objects.Punt.State;
import com.joe.springracing.objects.Punt.Type;

@Entity
public class ObjPunt {
	
	@Id Long id;
	@Parent
	private Key<ObjPuntEvent> event;
	@Index
	private String raceCode;
	private Date date;
	private Type type;
	private double joesOdds;
	private double bookieOdds;
	private List<Key<ObjRunner>> runners;
	private Confidence confidence;
	@Index
	private State state;
	private int raceNumber;
	
	public ObjPunt() {
		runners = new ArrayList<Key<ObjRunner>>();		
	}
	
	public ObjPunt(Key<ObjPuntEvent> event, String raceCode, List<String> runnerIds) {
		this();
		this.event = event;
		this.setRaceCode(raceCode);
		
		Key<ObjRace> race = Key.create(ObjRace.class, raceCode);
		for (String id : runnerIds) {
			runners.add(Key.create(race, ObjRunner.class, id));
		}
	}
		
	public List<Key<ObjRunner>> getRunners() {
		return runners;
	}
	public void setRunners(List<Key<ObjRunner>> runners) {
		this.runners = runners;
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

	public String getRaceCode() {
		return raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}

	public Confidence getConfidence() {
		return confidence;
	}

	public void setConfidence(Confidence confidence) {
		this.confidence = confidence;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getRaceNumber() {
		return raceNumber;
	}

	public void setRaceNumber(int raceNumber) {
		this.raceNumber = raceNumber;
	}
}
