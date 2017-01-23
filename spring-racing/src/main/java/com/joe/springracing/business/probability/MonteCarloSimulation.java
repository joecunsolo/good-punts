package com.joe.springracing.business.probability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.business.Simulator;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class MonteCarloSimulation implements Simulator {
	public int[] wins;
	public int[] places;
	public int simulations;
	
	private GatheredDistribution distribution;
	
	public void simulate(Race race, int simulations, GatheredDistribution distribution, boolean desc) {
		int horsesInRace = race.getMaxRunnerNumber();
		this.wins = new int[horsesInRace + 1];
		this.places = new int[horsesInRace + 1];
		this.simulations = simulations;
		this.setDistribution(distribution);		
		
		int[][] results = new int[simulations][horsesInRace + 1];
		for (int i = 0; i < simulations; i++) {
			results[i] = simulate(race, desc);
		}
		
		setOdds(race);
	}

	private void setOdds(Race race) {
		double checkSum = 0;
		for (int i = 0; i < race.getRunners().size(); i++) {
			Runner runner = race.getRunners().get(i);
			checkSum += wins[runner.getNumber()];
			runner.getProbability().setNumberWins(wins[runner.getNumber()]);
			double win = runner.getProbability().getNumberWins() / (double)this.simulations;
			runner.getProbability().setWin(win);
			runner.getProbability().setNumberPlaces(places[runner.getNumber()]);
			double place = runner.getProbability().getNumberPlaces() / (double)this.simulations;
			runner.getProbability().setPlace(place);			
		}
		if (checkSum > this.simulations) {
			throw new RuntimeException("Sum does not sum to the number of simulations");
		}
	}

	public int[] simulate(Race race, boolean desc) {
		List<Simulation> probabilities = new ArrayList<Simulation>();

		//calc the probability of each horse
		for (Runner runner : race.getRunners()) {
			Simulation s = new Simulation();
			s.number = runner.getNumber();
			if (runner.isScratched() || runner.isEmergency()) {
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
		int[] result = new int[race.getMaxRunnerNumber() + 1];
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
