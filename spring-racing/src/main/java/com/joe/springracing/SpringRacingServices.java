package com.joe.springracing;

import java.net.Authenticator;
import java.net.Proxy;

import com.joe.springracing.business.Simulator;
import com.joe.springracing.business.Statistics;
import com.joe.springracing.business.probability.MonteCarloSimulation;
import com.joe.springracing.business.probability.WeightedPrizeMoneyStatistics;
import com.joe.springracing.dao.LocalRaceDAOImpl;
import com.joe.springracing.dao.PuntingDAO;
import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.dao.datasource.RacingDotComDataSource;
import com.joe.springracing.dao.datasource.SpringRacingDataSource;

public class SpringRacingServices {

	public static final String OFFLINE_DIRECTORY = "C:\\racing2\\";
	public static final boolean USE_PROXY = false;
	
	public static Simulator getSimulator() {
		return new MonteCarloSimulation();
	}

	private static Statistics statistics = new WeightedPrizeMoneyStatistics();
	public static Statistics getStatistics() {
		return statistics;
	}
	
	public static void setStatistsics(Statistics statistics1) {
		statistics = statistics1;
	}
	
	private static SpringRacingDAO springRacingDao = new LocalRaceDAOImpl(OFFLINE_DIRECTORY);
	public static SpringRacingDAO getSpringRacingDAO() {
		return springRacingDao;
	}
	
	public static void setSpringRacingDAO(SpringRacingDAO dao) {
		springRacingDao = dao;
	}
	
	//TODO Move these to properties - they are not services
	public static Proxy getHTTPProxy() {
		return null;
	}
	
	public static boolean useHTTPProxy() {
		return USE_PROXY;
	}
	
	public static Authenticator getHTTPAuthenticator() {
		return null;
	}

	private static PuntingDAO puntingDao = null;
	public static PuntingDAO getPuntingDAO() {
		return puntingDao;
	}

	public static void setPuntingDAO(PuntingDAO dao) {
		puntingDao = dao;
	}
	
	private static SpringRacingDataSource datasource = new RacingDotComDataSource();
	public static SpringRacingDataSource getSpringRacingDataSource() {
		return datasource;
	}
	
	public static void setSpringRacingDataSource(SpringRacingDataSource srdsource) {
		datasource = srdsource;
	}
}
