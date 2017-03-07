package com.joe.springracing.business.probability;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.business.Statistics;
import com.joe.springracing.business.model.AnalysableObject;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.AnalysableObjectRaceResultFilter;
import com.joe.springracing.business.model.stats.SingleVariateStatistic;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.objects.Runner;

public abstract class SingleWeightedStatistics implements Statistics {
	
	private Model model;
	private Runner runner;
	
	public List<AnalysableObjectStatistic> evaluate(Runner r, AnalysableObject o, Model m) {
		this.setModel(m);
		this.setRunner(r);
				
		List<AnalysableObjectStatistic> stats = new ArrayList<AnalysableObjectStatistic>();
//		List<RunnerResult> pastResults = getRunner().getHorse().getPastResults();
		
		stats.add(evaluate(o, ((Horse)o).getPastResults(), getInfluence()));
//		stats.add(evaluate(runner.getJockey(), pastResults, m.getAttributes().getJockeyInfluence()));
//		stats.add(evaluate(runner.getTrainer(), pastResults, m.getAttributes().getTrainerInfluence()));
//		runner.getDistance().evaluate(pastResults);
		
		return stats;
	}

	private AnalysableObjectStatistic evaluate(AnalysableObject sObject, List<RunnerResult> pastResults, double influence) {
		AnalysableObjectRaceResultFilter filter = new AnalysableObjectRaceResultFilter(sObject);
		List<RunnerResult> myResults = filter.filter(pastResults);

		return toSingleVariateStatistic(myResults, influence);
	}
	
	private SingleVariateStatistic toSingleVariateStatistic(List<RunnerResult> pastResults, double influence) {
		SingleVariateStatistic stat = new SingleVariateStatistic(careerToValues(pastResults));
		stat.setWeight(influence);
		
		double[] values = stat.getValues();
		double mean = model.getMeanCalculator().calculate(values);
		stat.setMean(mean);
		double standardDeviation = model.getStandardDeviationCalculator().calculate(values);
		stat.setStandardDeviation(standardDeviation);
		
		return stat;
	}
	
	protected abstract Double[] careerToValues(List<RunnerResult> pastResults);
	
	protected double getInfluence() {
		return this.getModel().getAttributes().getHorseInfluence();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}
	
	
}
