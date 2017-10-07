package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class ProbabilityBusiness extends AbstractSpringRacingBusiness {
		
	public ProbabilityBusiness() {
		this(new PrintWriter(System.out));
	}

	public ProbabilityBusiness(PrintWriter pw) {
		super(pw);
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
		
	}

	public List<Runner> fetchProbabilitiesForRace(Race race) {
		try {
			return SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(race);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch probabilities for race", e);
		}
	}

}
