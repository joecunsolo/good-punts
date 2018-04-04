package com.joe.springracing.business;

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

}
