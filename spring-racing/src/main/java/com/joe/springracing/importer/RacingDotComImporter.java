package com.joe.springracing.importer;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.business.MeetBusiness;
import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.dao.datasource.RacingDotComDataSource;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RaceResult;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.objects.SplitsAndSectionals;

public class RacingDotComImporter {

	private RacingDotComDataSource datasource;
	public RacingDotComImporter() {
		datasource = new RacingDotComDataSource();
	}
		
	public void importUpcomingMeets(boolean histories, SpringRacingDAO dao) throws Exception {
		importUpcomingRaces(histories, dao);

//		List<Meeting> meets = new MeetBusiness().organiseRacesByMeeting(races);
//		return meets;
	}
	
	public List<Race> importUpcomingRaces(boolean histories, SpringRacingDAO dao) throws Exception {
		MeetBusiness meetBusiness = new MeetBusiness();
		List<Race> races = datasource.fetchRaces();
		List<Race> upcoming = new ArrayList<Race>();
		for (Race race : races) {
			if (race.getTime().getTime() > System.currentTimeMillis() ) {
				Meeting meet = meetBusiness.getMeeting(race.getMeetCode());
				dao.storeMeet(meet);
				dao.storeRace(race);
				
				System.out.println(race.getRaceNumber() + " " + race.getName() + " " + race.getVenue());
				List<Runner> runners = fetchRunners(race, histories);
				if (runners != null) {
					race.setRunners(runners);
					upcoming.add(race);
				}
				dao.storeRunners(runners);
			}
		}			
		return upcoming;
	}
	
	public List<Runner> fetchRunners(Race race, boolean histories) throws Exception {
		List<Runner> runners = datasource.fetchRunnnersForRace(race.getMeetCode(), race.getRaceNumber());
		
		for (Runner runner : runners) {
			if (runner.getOdds() == null) {
				return null;
			}
			System.out.println(runner.getNumber() + " " + runner.getHorse().getName());

			if (histories) {
				Horse horse = runner.getHorse();
				List<RunnerResult> results = fetchPastResults(horse);
				horse.setPastResults(results);
			}
		}
		
		return runners;
	}

	public List<RunnerResult> fetchPastResults(Horse horse) throws Exception {
		List<RunnerResult> results = datasource.fetchPastResultsForHorse(horse);
		
		for (RunnerResult result : results) {
			RaceResult raceResult = datasource.fetchRaceResult(result.getRace());
			result.getRace().setMeetCode(raceResult.getMeetCode());
			result.getRace().setResult(raceResult);
			
			int position = result.getResult();
			result.setPrizeMoney(raceResult.getPrizeMoney(position));				
			
//			SplitsAndSectionals splits = fetchSplits(result);
//			if (splits != null) {
//				result.setSplits(splits.getSplits());
//				result.setRaceTime(splits.getRaceTime());
//			}
		}
		
		return results;
	}
	
	public RaceResult importRaceResults(Race race) throws Exception {
		return datasource.fetchRaceResult(race);
	}

	public SplitsAndSectionals fetchSplits(RunnerResult result) throws Exception {
		return datasource.fetchSplits(result);
	}

	public RacingDotComDataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(RacingDotComDataSource datasource) {
		this.datasource = datasource;
	}

	public void importExistingMeet(Meeting meeting) throws Exception {
		for (Race race : meeting.getRaces()) {
			importExistingRace(race);
		}
	}

	public void importExistingRace(Race race) throws Exception {
		for (Runner runner : race.getRunners()) {
			Horse horse = runner.getHorse();
			horse.setPastResults(fetchPastResults(horse));
		}
	}

}
