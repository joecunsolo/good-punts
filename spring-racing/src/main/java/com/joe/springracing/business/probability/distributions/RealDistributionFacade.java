package com.joe.springracing.business.probability.distributions;

public abstract class RealDistributionFacade {

	private double mean;
	private double standardDeviation;
	
	public abstract double inverseCumulativeProbability(double d);

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}
}
