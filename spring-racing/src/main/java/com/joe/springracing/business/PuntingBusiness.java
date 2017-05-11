package com.joe.springracing.business;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;

public class PuntingBusiness {

	public List<Punt> fetchSettledPunts() {
		return SpringRacingServices.getPuntingDAO().fetchSettledPunts();
	}
	
	public List<Punt> generate(Race race) throws Exception {
		List<Punt> punts = SpringRacingServices.getPuntingService().generate(race);
		SpringRacingServices.getPuntingDAO().storePunts(race, punts);
		return punts;
	}
	
	public List<Punt> generate() {
		List<Punt> result = new ArrayList<Punt>();
		try {
			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchUpcomingMeets();
			for (Meeting meeting : meets) {
				try {
					List<Race> races = SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
					for (Race r : races) {
						result.addAll(generate(r));
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
}
