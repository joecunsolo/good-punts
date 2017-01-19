package com.goodpunts.objectify;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.objects.Punt.Type;

@Entity
public class ObjPunt {
	
	@Id Long id;
	@Parent
	private Key<ObjPuntEvent> event;
	@Index
	private String raceCode;
	private Type type;
	private double joesOdds;
	private double bookieOdds;
	private List<Key<ObjRunner>> runners;
	
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

}
