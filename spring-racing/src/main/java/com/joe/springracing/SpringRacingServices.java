package com.joe.springracing;

import java.net.Authenticator;
import java.net.Proxy;

import com.joe.springracing.account.BookieAccount;
import com.joe.springracing.business.Simulator;
import com.joe.springracing.business.Statistics;
import com.joe.springracing.business.probability.MonteCarloSimulation;
import com.joe.springracing.business.probability.WeightedPrizeMoneyStatistics;
import com.joe.springracing.dao.LocalRaceDAOImpl;
import com.joe.springracing.dao.PuntingDAO;
import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.dao.datasource.RacingDotComDataSource;
import com.joe.springracing.dao.datasource.SpringRacingDataSource;
import com.joe.springracing.services.ProbabilityService;
import com.joe.springracing.services.ProbabilityServiceImpl;
import com.joe.springracing.services.PuntingService;
import com.joe.springracing.services.PuntingServiceImpl;

public class SpringRacingServices {

	public static final String OFFLINE_DIRECTORY = "C:\\racing2\\";
	public static final String RACINGDOTCOM_URL = "http://127.0.0.1:9000/";//"https://api.racing.com/";
	public static final boolean USE_PROXY = false;
	
	private static Simulator simulator = new MonteCarloSimulation();
	public static Simulator getSimulator() {
		return simulator;
	}
	
	public static void setSimulator(Simulator s) {
		simulator = s;
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
	
	public static BookieAccount account = null;
	public static BookieAccount getBookieAccount() {
		return account;
	}
	
	public static void setBookieAccount(BookieAccount srAccount) {
		account = srAccount;
	}

	private static ProbabilityService probabilityService = new ProbabilityServiceImpl();
	public static ProbabilityService getProbabilityService() {
		return probabilityService;
	}

	private static PuntingService puntingService = new PuntingServiceImpl();
	public static PuntingService getPuntingService() {
		return puntingService;
	}
	
	private static String racingDotComURL = RACINGDOTCOM_URL;
	public static String getRacingDotComURL() {
		return racingDotComURL;
	}

	public static void setRacingDotComURL(String racingDotComURL) {
		SpringRacingServices.racingDotComURL = racingDotComURL;
	}
}
