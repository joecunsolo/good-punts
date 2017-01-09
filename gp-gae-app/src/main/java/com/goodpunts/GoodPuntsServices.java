package com.goodpunts;

import com.goodpunts.objectify.ObjectifyPuntingDaoImpl;
import com.goodpunts.objectify.ObjectifySpringRacingDaoImpl;
import com.joe.springracing.business.Simulator;
import com.joe.springracing.business.Statistics;
import com.joe.springracing.business.probability.MonteCarloSimulation;
import com.joe.springracing.business.probability.PrizeMoneyStatistics;
import com.joe.springracing.dao.PuntingDAO;
import com.joe.springracing.dao.SpringRacingDAO;

public class GoodPuntsServices {

	public static Simulator getSimulator() {
		return new MonteCarloSimulation();
	}

	private static Statistics statistics = new PrizeMoneyStatistics();
	public static Statistics getStatistics() {
		return statistics;
	}
	
	public static void setStatistsics(Statistics statistics1) {
		statistics = statistics1;
	}
	
	public static SpringRacingDAO getSpringRacingDAO() {
		return new ObjectifySpringRacingDaoImpl();
	}

	public static PuntingDAO getPuntingDAO() {
		return new ObjectifyPuntingDaoImpl();
	}
		
}
