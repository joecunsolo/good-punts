package com.joe.springracing.business.model.stats;

import com.joe.springracing.business.model.AnalysableObjectStatistic;

public class SingleVariateStatistic implements AnalysableObjectStatistic {

	private double[] values;
	private double mean;
	private double standardDeviation;
	private double weight;
	
	public SingleVariateStatistic() {}
		
	public SingleVariateStatistic(double[] myResults) {
		this.setValues(myResults);
	}

	public SingleVariateStatistic(Double[] myResults) {
		this.setValues(myResults);
	}
	
	private void setValues(Double[] myResults) {
		int i = 0;
		values = new double[myResults.length];
		for (Double a : myResults) {
			values[i++] = a.doubleValue();
		}
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
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
