package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class ProbabilityBusiness extends AbstractSpringRacingBusiness {
	
	private Model model;
	
	public ProbabilityBusiness() {
		this(new Model(new ModelAttributes()));
	}
	
	public ProbabilityBusiness(Model model) {
		this(model, new PrintWriter(System.out));
	}

	public ProbabilityBusiness(Model model, PrintWriter pw) {
		super(pw);
		this.setModel(model);
	}

	public List<Meeting> fetchUpcomingMeets() {
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchUpcomingMeets();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Race> fetchRacesForMeet(Meeting meeting) {
		getWriter().println();
		getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
		
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch races for meet " + meeting.getMeetCode(), e);
		}		
	}
	
	public void generateProbabilitiesForMeet(Meeting meeting) {
		try {
			getWriter().println();
			getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
			
			List<Race> races = SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
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
		List<Runner> runners = SpringRacingServices.getSpringRacingDAO().fetchRunnersForRace(race);
		race.setRunners(runners);
		
		//Generate the probabilities
		model.setRace(race);
		Statistics statistics = SpringRacingServices.getStatistics();
		for (Runner runner : race.getRunners()) {
			if (!(runner.isScratched() || runner.isEmergency())) {
				Horse o = SpringRacingServices.getSpringRacingDAO().fetchHorse(runner.getHorse());
				List<AnalysableObjectStatistic> stats = statistics.evaluate(runner, o, model);
				runner.setStatistics(stats);
			}
		}
		GatheredDistribution gd = new GatheredDistribution(model);
		SpringRacingServices.getSimulator().simulate(race.getRunners(), model.getAttributes().getSimulations(), gd, statistics.isDescending());
		
		//Store the results
		SpringRacingServices.getPuntingDAO().storeProbabilities(race);
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	public Model getModel() {
		return model;
	}


}
