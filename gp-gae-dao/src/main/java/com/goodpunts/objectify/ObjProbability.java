package com.goodpunts.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.business.probability.Probability;

@Entity
public class ObjProbability {

	@Id
	private String id;
	@Parent 
	private Key<ObjRunner> runner;
	private double win;
	private double place;
	private int numberWins;
	private int numberPlaces;
	private double mean;
	private double standardDeviation;
	private double weight;

	public ObjProbability() {}
	
	public ObjProbability(Probability p, String race, String horse, Key<ObjRunner> runnerKey) {
		runner = runnerKey;
		this.setId(race+horse);
		this.setPlace(p.getPlace());
		this.setWin(p.getWin());
		this.setNumberWins(p.getNumberWins());
		this.setNumberPlaces(p.getNumberPlaces());
		this.setMean(p.getMean());
		this.setStandardDeviation(p.getStandardDeviation());
	}
		
	public double getWin() {
		return win;
	}
	public void setWin(double win) {
		this.win = win;
	}
	public double getPlace() {
		return place;
	}
	public void setPlace(double place) {
		this.place = place;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getNumberPlaces() {
		return numberPlaces;
	}

	public void setNumberPlaces(int numberPlaces) {
		this.numberPlaces = numberPlaces;
	}

	public int getNumberWins() {
		return numberWins;
	}

	public void setNumberWins(int numberWins) {
		this.numberWins = numberWins;
	}
}
