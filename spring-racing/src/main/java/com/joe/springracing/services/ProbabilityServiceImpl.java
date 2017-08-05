package com.joe.springracing.services;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.Simulatable;
import com.joe.springracing.business.Statistics;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.business.model.SimulatableRunner;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class ProbabilityServiceImpl implements ProbabilityService {
	
	private Model model;
	public ProbabilityServiceImpl() {
		model = new Model(new ModelAttributes());
	}
	
	//Generate the probabilities
	public void generate(Race race) throws Exception {
		model.setRace(race);
		Statistics statistics = SpringRacingServices.getStatistics();
		List<Simulatable> simulatables = new ArrayList<Simulatable>();
		for (Runner runner : race.getRunners()) {
			if (!(runner.isScratched() || runner.isEmergency())) {
				Horse o = SpringRacingServices.getSpringRacingDAO().fetchHorse(runner.getHorse());
				SimulatableRunner stats = statistics.evaluate(runner, o, model);
				simulatables.add(stats);
			}
		}
		GatheredDistribution gd = new GatheredDistribution(model);
		SpringRacingServices.getSimulator().simulate(simulatables, model.getAttributes().getSimulations(), gd, statistics.isDescending());
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public Model getModel() {
		return model;
	}
}
