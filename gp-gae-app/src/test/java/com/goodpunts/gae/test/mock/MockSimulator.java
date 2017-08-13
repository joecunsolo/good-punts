package com.goodpunts.gae.test.mock;

import java.util.List;

import com.joe.springracing.business.Simulatable;
import com.joe.springracing.business.Simulator;
import com.joe.springracing.business.probability.Probability;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;

public class MockSimulator implements Simulator {

	private int probability;
	public MockSimulator(int p) {
		probability = p;
	}
	
	public Simulator clone() {
		return new MockSimulator(probability);
	}
	
	public void simulate(List<? extends Simulatable> race, int i,
			GatheredDistribution distribution, boolean desc) {
		Probability probs = new Probability();
		probs.setWin(probability);
		probs.setPlace(probability);
		probs.setNumberPlaces(probability);
		probs.setNumberWins(probability);
		
		for (Simulatable s : race) {
			s.setProbability(probs);
		}
	}

}
