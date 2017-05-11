package com.joe.springracing.test;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.business.probability.MonteCarloSimulation;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;
import com.joe.springracing.objects.Runner;

import junit.framework.TestCase;

public class TestMonteCarloSimulation extends TestCase {

	public static void testSimulate() {
		MonteCarloSimulation sim = new MonteCarloSimulation();
		Model model = new Model(new ModelAttributes());
		GatheredDistribution gd = new GatheredDistribution(model);

		Runner runner = new Runner();
		List<Runner> race = new ArrayList<Runner>();
		race.add(runner);
		
		sim.simulate(race, 100, gd, false);
	}
}
