package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;
import com.joe.springracing.dao.PuntingDAO;
import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class ProbabilityBusiness extends AbstractSpringRacingBusiness {
		
	private Model model;
	private PuntingDAO puntingDAO;
	private Statistics statistics;
	private Simulator simulator;
	
	public ProbabilityBusiness(SpringRacingDAO dao, PuntingDAO puntingDAO, Statistics stats, Simulator sim, Model model) {
		super(dao, new PrintWriter(System.out, true));
		this.setPuntingDAO(puntingDAO);
		this.setModel(model);
		this.setStatistics(stats);
		this.setSimulator(sim);
	}
	
	public List<Meeting> fetchUpcomingMeets() {
		SpringRacingDAO springRacingDao = super.getSpringRacingDAO();
		try {
			List<Meeting> meets = springRacingDao.fetchExistingMeets();
			List<Meeting> upcoming = new ArrayList<Meeting>();
			
			for (Meeting meeting : meets) {
				try {
					//if (meeting.getDate().getTime() > System.currentTimeMillis() - 96 * 60 * 60 * 1000) {
						upcoming.add(meeting);
					//}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return upcoming;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Race> fetchRacesForMeet(Meeting meeting) {
		SpringRacingDAO springRacingDao = super.getSpringRacingDAO();
		getWriter().println();
		getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
		
		try {
			return springRacingDao.fetchRacesForMeet(meeting);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch races for meet " + meeting.getMeetCode(), e);
		}		
	}
	
	public void generateProbabilitiesForMeet(Meeting meeting) {
		try {
			SpringRacingDAO springRacingDao = super.getSpringRacingDAO();
			getWriter().println();
			getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
			
			List<Race> races = springRacingDao.fetchRacesForMeet(meeting);
			generateProbabilitiesForRaces(races);			
		} catch (Exception ex) {
			throw new RuntimeException("Unable to generate probabilities for: " + meeting.getMeetCode(), ex);
		}
	}
	
	public void generateProbabilitiesForRaces(List<Race> races) throws Exception {		
		for (Race race : races) {
			generateProbabilitiesForRace(race);
		}
	}

	public void generateProbabilitiesForRace(Race race) throws Exception {
		//load the runners
		List<Runner> runners = this.getSpringRacingDAO().fetchRunnersForRace(race);
		race.setRunners(runners);
		
		this.getModel().setRace(race);
		Statistics statistics = getStatistics();
		for (Runner runner : race.getRunners()) {
			if (!(runner.isScratched() || runner.isEmergency())) {
				Horse o = getSpringRacingDAO().fetchHorse(runner.getHorse());
				List<AnalysableObjectStatistic> stats = statistics.evaluate(runner, o, this.getModel());
				runner.setStatistics(stats);
			}
		}
		GatheredDistribution gd = new GatheredDistribution(model);
		getSimulator().simulate(race, model.getAttributes().getSimulations(), gd, statistics.isDescending());
		
		getPuntingDAO().storeProbabilities(race);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Simulator getSimulator() {
		return simulator;
	}

	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public PuntingDAO getPuntingDAO() {
		return puntingDAO;
	}

	public void setPuntingDAO(PuntingDAO puntingDAO) {
		this.puntingDAO = puntingDAO;
	}

}
