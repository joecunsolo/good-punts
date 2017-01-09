package com.goodpunts.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.business.probability.Probability;

@Entity
public class ObjProbability {

	@Id
	private Long id;
	@Parent 
	private Key<ObjRunner> runner;
	private double win;
	private double place;
	private double mean;
	private double standardDeviation;
	private double weight;

	public ObjProbability() {}
	
	public ObjProbability(Probability p, Key<ObjRunner> runnerKey) {
		runner = runnerKey;
		this.setPlace(p.getPlace());
		this.setWin(p.getWin());
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
	
	public Long getId() {
		return id;
	}
}
