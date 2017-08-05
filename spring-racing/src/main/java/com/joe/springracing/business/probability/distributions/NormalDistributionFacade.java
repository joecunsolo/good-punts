package com.joe.springracing.business.probability.distributions;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

public class NormalDistributionFacade extends RealDistributionFacade {

	@Override
	public double inverseCumulativeProbability(double random) {
		RealDistribution d = new NormalDistribution(getMean(), getStandardDeviation());
		return d.inverseCumulativeProbability(random);
	}

}
