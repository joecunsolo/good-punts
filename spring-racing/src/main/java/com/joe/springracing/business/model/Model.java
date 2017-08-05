package com.joe.springracing.business.model;

import com.joe.springracing.business.probability.distributions.NormalDistributionFacade;
import com.joe.springracing.business.probability.distributions.RealDistributionFacade;
import com.joe.springracing.objects.Race;

public class Model {

	private ModelAttributes attributes;
	private Race race;
	
	private MeanCalculator defaultMeanCalculator;
	private StandardDeviationCalculator defaultStandardDeviationCalculator;
	private RealDistributionFacade singleVariateDistribution;

	public Model(ModelAttributes attributes) {
		setAttributes(attributes);
		defaultMeanCalculator = new MeanCalculator(this);
		defaultStandardDeviationCalculator = new StandardDeviationCalculator(this);
		singleVariateDistribution = new NormalDistributionFacade();
	}
	
	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}
	
	public double standardDeviation(double[] values) {
		return this.getStandardDeviationCalculator().calculate(values);
	}	
	
	public double mean(double[] values) {
		return this.getMeanCalculator().calculate(values);
	}
	
	public ModelAttributes getAttributes() {
		return attributes;
	}

	public void setAttributes(ModelAttributes attributes) {
		this.attributes = attributes;
	}
	
	public MeanCalculator getMeanCalculator() {
		return defaultMeanCalculator;
	}

	public void setMeanCalculator(MeanCalculator defaultMeanCalculator) {
		this.defaultMeanCalculator = defaultMeanCalculator;
	}

	public StandardDeviationCalculator getStandardDeviationCalculator() {
		return defaultStandardDeviationCalculator;
	}

	public void setStandardDeviationCalculator(
			StandardDeviationCalculator defaultStandardDeviationCalculator) {
		this.defaultStandardDeviationCalculator = defaultStandardDeviationCalculator;
	}

	public RealDistributionFacade getSingleVariateDistribution() {
		return singleVariateDistribution;
	}

	public void setSngleVariateDistribution(RealDistributionFacade singleVariateDistribution) {
		this.singleVariateDistribution = singleVariateDistribution;
	}
}
