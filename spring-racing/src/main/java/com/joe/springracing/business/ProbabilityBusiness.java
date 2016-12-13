package com.joe.springracing.business;

import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class ProbabilityBusiness {
		
	private Model model;
	
	public ProbabilityBusiness(Model model) {
		this.setModel(model);
	}
	
	public void calculateOddsForMeet(Meeting meeting) {		
		for (Race race : meeting.getRaces()) {
			calculateOddsForRace(race);
		}
	}

	public void calculateOddsForRace(Race race) {
		this.getModel().setRace(race);
		Statistics statistics = SpringRacingServices.getStatistics();
		for (Runner runner : race.getRunners()) {
			List<AnalysableObjectStatistic> stats = statistics.evaluate(runner, this.getModel());
			runner.setStatistics(stats);
		}
		GatheredDistribution gd = new GatheredDistribution(model);
		SpringRacingServices.getSimulator().simulate(race, model.getAttributes().getSimulations(), gd, statistics.isDescending());
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
