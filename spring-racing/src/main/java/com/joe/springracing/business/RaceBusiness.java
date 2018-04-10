package com.joe.springracing.business;

import java.util.Date;
import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Race;

public class RaceBusiness {

	public Race fetchRace(String raceCode) {
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch race " + raceCode, e);
		}	
	}

	public List<Race> fetchRaces(boolean results, Date from, Date to) {
		try {
			if (results) {
				return SpringRacingServices.getSpringRacingDAO().fetchRacesWithResults(from, to);
			} else {
				return SpringRacingServices.getSpringRacingDAO().fetchRacesWithoutResults(from, to);				
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch races", e);
		}
	}
}
