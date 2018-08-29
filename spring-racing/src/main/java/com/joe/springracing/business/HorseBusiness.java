package com.joe.springracing.business;

import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Horse;

public class HorseBusiness {

	public Horse fetchHorse(String horse) {
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchHorse(horse);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch horse: " + horse, e);
		}
	}

	public List<Horse> fetchHorses(boolean histories, boolean splits) {
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchHorses(histories, splits);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch horses", e);
		}
	}
}
