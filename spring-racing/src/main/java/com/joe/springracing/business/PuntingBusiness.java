package com.joe.springracing.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Stake;
import com.joe.springracing.objects.Punt.State;

public class PuntingBusiness {
	
	public List<Punt> generate(Race race) throws Exception {
		race.setRunners(SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(race));
		List<Punt> punts = SpringRacingServices.getPuntingService().generate(race);
		SpringRacingServices.getPuntingDAO().storePunts(race, punts);
		return punts;
	}
	
	/**
	 * Generates a list of Punts for the upcoming meets
	 * A race is only included if it is in the future (date > now), and
	 * The race does not have results
	 * @return the list of punts
	 */
	public List<Punt> generate() {
		List<Punt> result = new ArrayList<Punt>();
		try {
			//Upcoming meets
			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchUpcomingMeets();
			for (Meeting meeting : meets) {
				try {
					//races for the meet
					List<Race> races = SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
					for (Race r : races) {
						//A race is only included if it is in the future (date > now)
						if (r.getDate().compareTo(new Date()) > 0 &&
								//The race does not have results
								r.getResult() == null) {
							result.addAll(generate(r));
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("Unable to generate punts for " + meeting.getMeetCode(), e);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Unable to generate any punts", ex);
		}
		return result;
	}

	public List<Stake> fetchOpenStakes() {
		return SpringRacingServices.getPuntingDAO().fetchOpenStakes();
	}

	public List<Stake> fetchSettledStakes() {
		return SpringRacingServices.getPuntingDAO().fetchSettledStakes();
	}

	public void settlePunts(Race race) throws Exception {
		List<Punt> punts = SpringRacingServices.getPuntingDAO().fetchPuntsForRace(race);
		for (Punt punt : punts) {
			punt.setState(State.FINISHED);
		}
		SpringRacingServices.getPuntingDAO().storePunts(race, punts);
	}

	public List<Punt> fetchPuntsForMeet(Meeting meet) {
		try {
			return SpringRacingServices.getPuntingDAO().fetchPuntsForMeet(meet);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch punts for meet", e);
		}
	}

	public List<Punt> fetchSurePunts() {
		// TODO Auto-generated method stub
		return null;
	}

	public void generateSurePunts() {
		//SurePuntService service = SpringRacingServices.getSurePuntingService();
	}

}
