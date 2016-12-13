package com.joe.springracing;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

import com.joe.springracing.business.Simulator;
import com.joe.springracing.business.Statistics;
import com.joe.springracing.business.probability.MonteCarloSimulation;
import com.joe.springracing.business.probability.PrizeMoneyStatistics;
import com.joe.springracing.dao.HorseDAO;
import com.joe.springracing.dao.LocalRaceDAOImpl;
import com.joe.springracing.dao.RaceDAO;
import com.joe.springracing.dao.RacingDotComHorseDAOImpl;
import com.joe.springracing.dao.RacingDotComRaceDAOImpl;
import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.dao.SpringRacingFileDAOV1;

public class SpringRacingServices {

	public static final String OFFLINE_DIRECTORY = "C:\\racing\\";
	public static final boolean USE_PROXY = true;
	
//	public static final HorseDAO getHorseDAO() {
//		return new RacingDotComHorseDAOImpl();
//	}
//	
//	public static final RaceDAO getRaceDAO() {
//		return new RacingDotComRaceDAOImpl();
//	}

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
		return new LocalRaceDAOImpl(OFFLINE_DIRECTORY);
	}
	
	//TODO Move these to properties - they are not services
	public static Proxy getHTTPProxy() {
		return null;
	}
	
	public static boolean useHTTPProxy() {
		return USE_PROXY;
	}
	
	public static Authenticator getHTTPAuthenticator() {
		return new Authenticator() {
	        public PasswordAuthentication getPasswordAuthentication() {
	            return null;
	        }
	    };
	}

	public static SpringRacingFileDAOV1 _getSpringRacingDAOV1() {
		return new SpringRacingFileDAOV1(OFFLINE_DIRECTORY);
	}
	
}
