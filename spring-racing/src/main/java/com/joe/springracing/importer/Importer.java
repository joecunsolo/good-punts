package com.joe.springracing.importer;

import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.MeetBusiness;
import com.joe.springracing.dao.datasource.SpringRacingDataSource;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class Importer {

	private SpringRacingDataSource datasource;
	public Importer() {
		datasource = SpringRacingServices.getSpringRacingDataSource();
	}
		
//	public List<Meeting> importUpcomingMeets() throws Exception {
//		List<Race> races = importUpcomingRaces();
//		
//		List<Meeting> meets = new MeetBusiness().organiseRacesByMeeting(races);
//		return meets;
//	}
	
	public List<Race> importUpcomingRaces() throws Exception {
		List<Race> races = datasource.fetchRaces();				
		return races;
	}

	public List<Runner> fetchRunners(Race race) throws Exception {
		List<Runner> runners = datasource.fetchRunnnersForRace(race);
		if (runners != null) {
			for (Runner runner : runners) {
				if (runner.getOdds() == null) {
					return null;
				}
			}
		}
		return runners;
	}


	public List<RunnerResult> fetchPastResults(Horse h) throws Exception {
		List<RunnerResult> results = datasource.fetchPastResultsForHorse(h.getCode());
		
		for (RunnerResult result : results) {
			//adds in some more information
			try {
				Race raceResult = datasource.fetchRaceResult(result.getRaceCode());
				try {
					int position = result.getPosition();
					result.setPrizeMoney(raceResult.getPrizeMoney(position));	
				} catch (Exception ex) {
					ex.printStackTrace();				
				}
				try {
					result.setRaceDate(raceResult.getDate());
				} catch (Exception ex) {
					ex.printStackTrace();				
				}try {
					result.setMeetCode(raceResult.getMeetCode());
				} catch (Exception ex) {
					ex.printStackTrace();				
				}try {
					result.setRaceNumber(raceResult.getRaceNumber());
				} catch (Exception ex) {
					ex.printStackTrace();				
				}try {
					result.setVenueName(raceResult.getVenue());
				} catch (Exception ex) {
					ex.printStackTrace();				
				}try {
					result.setRaceName(raceResult.getName());
					result.setHorse(h.getId());
					result.setResultsFetched(true);
				} catch (Exception ex) {
					ex.printStackTrace();
	//				result.setResultsFetched(false);
				}
			} catch (Exception ex) {
				
			}
		}
		
		return results;
	}
	
	public int[] importRaceResults(Race race) throws Exception {
		return datasource.fetchRaceResult(race.getRaceCode()).getResult();
	}

//	public SplitsAndSectionals fetchSplits(RunnerResult result) throws Exception {
//		return datasource.fetchSplits(result);
//	}

	public SpringRacingDataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(SpringRacingDataSource datasource) {
		this.datasource = datasource;
	}

//	public void importExistingMeet(Meeting meeting) throws Exception {
//		for (Race race : meeting.getRaces()) {
//			importExistingRace(race);
//		}
//	}

//	private void importExistingRace(Race race) throws Exception {
//		for (Runner runner : race.getRunners()) {
////			List<RunnerResult> results = fetchPastResults(runner);
////			runner.getHorse().setPastResults(results);
//		}
//	}

	public Horse fetchHorse(Runner runner) throws Exception {
		return this.datasource.fetchHorse(runner);
	}

}
