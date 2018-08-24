package com.joe.springracing.business;

import java.util.Date;
import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class RaceBusiness {

	public Race fetchRace(String raceCode) {
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch race " + raceCode, e);
		}	
	}

	public List<Race> fetchRaces(boolean results, boolean splits, Date from, Date to, boolean fetchRunners) {
		List<Race> result = null;
		try {
			if (results) {
				result = SpringRacingServices.getSpringRacingDAO().fetchRacesWithResults(from, to);
			} else {
				result = SpringRacingServices.getSpringRacingDAO().fetchRacesWithoutResults(from, to);				
			}
			List<Race> spr = null;
			if (splits) {
				spr = SpringRacingServices.getSpringRacingDAO().fetchRacesWithSplits(from, to);
			} else {
				spr = SpringRacingServices.getSpringRacingDAO().fetchRacesWithoutSplits(from, to);				
			}
			result.addAll(spr);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch races", e);
		}
		
		if (fetchRunners) {
			for (Race race : result) {
				try {
					List<Runner> runners = SpringRacingServices.getSpringRacingDAO().fetchRunnersForRace(race);
					race.setRunners(runners);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
