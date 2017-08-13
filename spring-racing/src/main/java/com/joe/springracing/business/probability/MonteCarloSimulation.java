package com.joe.springracing.business.probability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.business.Simulatable;
import com.joe.springracing.business.Simulator;
import com.joe.springracing.business.model.stats.SingleVariateStatistic;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;

public class MonteCarloSimulation implements Simulator {
	public int[] wins;
	public int[] places;
	public int simulations;
	
	public Simulator clone() {
		return new MonteCarloSimulation();
	}
	
	private GatheredDistribution distribution;
	
	public void simulate(List<? extends Simulatable> race, int simulations, GatheredDistribution distribution, boolean desc) {
		int horsesInRace = getMaxRunnerNumber(race);
		if (horsesInRace < 1) {
			return;
		}
		this.wins = new int[horsesInRace + 1];
		this.places = new int[horsesInRace + 1];
		this.simulations = simulations;
		this.setDistribution(distribution);		
		
		int[][] results = new int[simulations][horsesInRace + 1];
		for (int i = 0; i < simulations; i++) {
			results[i] = simulate(race, horsesInRace, desc);
			for (int j = 0; j < results[i].length; j++) {
				System.out.print(results[i][j] + ",");
			}
			System.out.println();
		}
		
		setOdds(race);
	}

	private int getMaxRunnerNumber(List<? extends Simulatable> race) {
		int max = Integer.MIN_VALUE;
		for (Simulatable s : race) {
			if (s.getNumber() > max) {
				max = s.getNumber();
			}
		}
		return max;
	}

	/** Determines the Odds based on the simulation results */
	public void setOdds(List<? extends Simulatable> race) {
		double checkSum = 0;
		for (Simulatable runner : race) {
			//Calculate the probabilities
			Probability probability = new Probability();
			int number = runner.getNumber();
			int rwins = wins[number];
			checkSum += rwins;
			probability.setNumberWins(wins[runner.getNumber()]);
			double win = probability.getNumberWins() / (double)this.simulations;
			probability.setWin(win);
			probability.setNumberPlaces(places[runner.getNumber()]);
			double place = probability.getNumberPlaces() / (double)this.simulations;
			probability.setPlace(place);
			
			//TODO This is a hack - FIXME...
			probability.setMean(((SingleVariateStatistic)runner.getStatistics().get(0)).getMean());
			probability.setStandardDeviation(((SingleVariateStatistic)runner.getStatistics().get(0)).getStandardDeviation());
			
			runner.setProbability(probability);
		}
		if (checkSum > this.simulations) {
			throw new RuntimeException("Sum does not sum to the number of simulations");
		}
	}

	public int[] simulate(List<? extends Simulatable> race, int horsesInRace, boolean desc) {
		List<Simulation> probabilities = new ArrayList<Simulation>();

		//calc the probability of each horse
		for (Simulatable runner : race) {
			Simulation s = new Simulation();
			s.number = runner.getNumber();
			if (!runner.isEligible()) {
				s.probability = Double.MIN_VALUE;
			}
			else {
				double random = Math.random();
				try {
					GatheredDistribution d = getDistribution();
					d.setStatistics(runner.getStatistics());
					
					s.probability = d.inverseCumulativeProbability(random);
					probabilities.add(s);
				} catch (Exception ex) {
					//System.out.println(random + " " + runner.getHorse().getMean() + " " + runner.getHorse().getStandardDeviation());
					throw new RuntimeException(ex);
				}
			} 
		}
		
		//order the horses
		Collections.sort(probabilities, new SimulationComparator(desc));
		int[] result = new int[horsesInRace + 1];
		for (int p = 0; p < probabilities.size(); p++) {
			result[probabilities.get(p).number] = p+1;
			//add wins and places
			if (p == 0) {
				wins[probabilities.get(p).number]++;
			}
			if (p < 3) {
				places[probabilities.get(p).number]++;
			}
		}
		return result;
	}
	
	public GatheredDistribution getDistribution() {
		return distribution;
	}

	public void setDistribution(GatheredDistribution distribution2) {
		this.distribution = distribution2;
	}

	class Simulation {
		int number;
		double probability;
	}
	
	class SimulationComparator implements Comparator<Simulation> {

		private boolean descending = true;
		public SimulationComparator(boolean desc) {
			this.descending = desc;
		}
		public int compare(Simulation o1, Simulation o2) {
			double win = o1.probability - o2.probability;
			if (win > 0 ^ descending) {
				return 1;
			} else {
				return -1;
			} 
		}
	}
}
