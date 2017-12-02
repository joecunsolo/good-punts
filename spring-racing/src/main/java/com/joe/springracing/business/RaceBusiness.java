package com.joe.springracing.business;

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
}
