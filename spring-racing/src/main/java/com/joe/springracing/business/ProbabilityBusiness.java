package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Pick;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class ProbabilityBusiness extends AbstractSpringRacingBusiness {
	
	private Model model;
	
	public ProbabilityBusiness(Model m) {
		this(new PrintWriter(System.out), m);
	}

	public ProbabilityBusiness(PrintWriter pw, Model m) {
		super(pw);
		model = m;
	}

	public ProbabilityBusiness(PrintWriter pw) {
		this(pw, new Model(new ModelAttributes()));
	}
	
	public ProbabilityBusiness() {
		this(new Model(new ModelAttributes()));
	}
	
	@Deprecated
	public List<Race> fetchRacesForMeet(Meeting meeting) {
		getWriter().println();
		getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
		
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch races for meet " + meeting.getMeetCode(), e);
		}		
	}
	
	public void generate(Meeting meeting) {
		try {
			getWriter().println();
			getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
			
			List<Race> races = SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
			generate(races);			
		} catch (Exception ex) {
			throw new RuntimeException("Unable to generate probabilities for: " + meeting.getMeetCode(), ex);
		}
	}
	
	public void generate(List<Race> races) throws Exception {		
		for (Race race : races) {
			generate(race);
		}
	}

	public void generate(Race race) throws Exception {
		//load the runners
		List<Runner> runners = SpringRacingServices.getSpringRacingDAO().fetchRunnersForRace(race);
		race.setRunners(runners);
		
		//Generate the probabilities
		SpringRacingServices.getProbabilityService().generate(race);
		
		//Store the results
		SpringRacingServices.getPuntingDAO().storeProbabilities(race);
		SpringRacingServices.getSpringRacingDAO().storeRace(race);
		
		//Store the picks
		List<Pick> picks = generatePicks(race);
		SpringRacingServices.getPuntingDAO().storePicks(race, picks);
	}

	public List<Pick> generatePicks(Race race) {
		List<Pick> picks = new ArrayList<Pick>();
		for (Runner runner : race.getRunners()) {
			if (runner.getProbability().getWin() < model.getAttributes().getPicksWinPercentage()) {
				Pick pick = new Pick();
				pick.setRunner(runner);
				pick.setRaceCode(race.getRaceCode());
				pick.setBookieOdds(runner.getOdds().getWin());
				pick.setWin(runner.getProbability().getWin());
				picks.add(pick);
			}
		}
		return picks;
	}

	public List<Runner> fetchProbabilitiesForRace(Race race) {
		try {
			return SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(race);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch probabilities for race", e);
		}
	}

	public Model getModel() {
		return model;
	}
	
	public void setModel(Model m) {
		this.model = m;
	}
}
