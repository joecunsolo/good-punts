package com.goodpunts.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.stats.SingleVariateStatistic;

@Entity
public class ObjStatistic {

	@Id
	private Long id;
	@Parent
	private Key<ObjProbability> parent;
	private double mean;
	private double standardDeviation;
	private double weight;
	
	public ObjStatistic() {}
	
	public ObjStatistic(AnalysableObjectStatistic stat, Key<ObjProbability> parent) {
		this.parent = parent;
		this.weight = stat.getWeight();
		if (stat instanceof SingleVariateStatistic) {
			SingleVariateStatistic svs = (SingleVariateStatistic)stat;
			this.setMean(svs.getMean());
			this.setStandardDeviation(svs.getStandardDeviation());
		}
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

}
