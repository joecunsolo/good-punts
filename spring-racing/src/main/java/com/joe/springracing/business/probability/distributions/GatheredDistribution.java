package com.joe.springracing.business.probability.distributions;

import java.util.List;

import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.stats.SingleVariateStatistic;

/**
 * Is able to perform distribution functions across a range of statistics
 * @author joe
 *
 */
public class GatheredDistribution {

	private List<AnalysableObjectStatistic> statistics;
	private Model model;
	
	public GatheredDistribution(Model model) {
		this.setModel(model);
	}
	
	public double inverseCumulativeProbability(double random) {
		double probability = 0;
		for (AnalysableObjectStatistic stat : statistics) {
			if (stat instanceof SingleVariateStatistic) {
				SingleVariateStatistic ss = (SingleVariateStatistic)stat;
				RealDistributionFacade d = this.getModel().getSingleVariateDistribution();
				d.setMean(ss.getMean());
				d.setStandardDeviation(ss.getStandardDeviation());
				probability += d.inverseCumulativeProbability(random) * stat.getWeight();
			}
		}
		return probability;
	}

	public List<AnalysableObjectStatistic> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<AnalysableObjectStatistic> statistics) {
		this.statistics = statistics;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}


}
