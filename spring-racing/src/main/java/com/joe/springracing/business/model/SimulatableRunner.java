package com.joe.springracing.business.model;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.business.Simulatable;
import com.joe.springracing.business.probability.Probability;
import com.joe.springracing.objects.Runner;

/** 
 * Basic implementation of simulatable
 * Allows for simulating a runner
 * 
 * @author joecunsolo
 *
 */
public class SimulatableRunner implements Simulatable {
	
	/** The statistcs that will be used by the sim */
	private List<AnalysableObjectStatistic> stats;
	/** The runner that will be used to identify the sim object */
	private Runner runner;

	public SimulatableRunner(Runner runner, List<AnalysableObjectStatistic> stats) {
		setRunner(runner);
		setStatistics(stats);
	}
	
	public SimulatableRunner() {
		setRunner(new Runner());
		setStatistics(new ArrayList<AnalysableObjectStatistic>());
	}

	public List<AnalysableObjectStatistic> getStatistics() {
		return stats;
	}

	public void setStatistics(List<AnalysableObjectStatistic> stats) {
		this.stats = stats;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	public void setProbability(Probability probability) {
		runner.setProbability(probability);
	}

	public int getNumber() {
		return runner.getNumber();
	}

	public boolean isEligible() {
		return runner.isEligible();
	}
}
