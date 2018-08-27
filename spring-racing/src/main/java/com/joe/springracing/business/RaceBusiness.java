package com.joe.springracing.business;

import java.util.ArrayList;
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

	/**
	 * Not perfect but assumes that splits come in after results...
	 * So if you want where splits = true does an AND results = true/false
	 * If you want where splits = false does an OR results = true/false
	 * @param results the positional results
	 * @param splits the 200m sectional splits
	 * @param from date
	 * @param to date
	 * @param fetchRunners get the runner details as well?
	 * @return
	 */
	public List<Race> fetchRaces(boolean results, boolean splits, Date from, Date to, boolean fetchRunners) {
		List<Race> result = null;
		List<Race> spr = null;
		try {
			//Does an AND
			if (splits) {
				//Get the races with splits
				spr = SpringRacingServices.getSpringRacingDAO().fetchRacesWithSplits(from, to);
				//AND filter out the races with/without results...
				result = new ArrayList<Race>();
				for (Race r : spr) {
					//use an XOR for results && hasResults..
					//results = true && hasResults, OR
					//results = false = !hasResults
					if (results ^ r.getResult() != null) {
						result.add(r);
					}
				}
			//Does an OR
			} else {
				spr = SpringRacingServices.getSpringRacingDAO().fetchRacesWithoutSplits(from, to);	
				if (results) {
					result = SpringRacingServices.getSpringRacingDAO().fetchRacesWithResults(from, to);
				} else {
					result = SpringRacingServices.getSpringRacingDAO().fetchRacesWithoutResults(from, to);
				}
				//Add both sets together races without splits and races with/without results
				result.addAll(spr);
			}
			
			
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
