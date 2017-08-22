package com.goodpunts.objectify;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.exporter.Exporter;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class ObjectifySpringRacingDaoImpl extends ObjectifyBaseDaoImpl implements SpringRacingDAO {

	public boolean storeMeet(Meeting meet) throws Exception {
		ObjMeet oMeet = toObjMeet(meet);
		ObjectifyService.ofy().save().entity(oMeet).now();
		return false;
	}

	public List<Meeting> fetchMeetsWithoutResults() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void storeMeets(List<Meeting> meets) throws Exception {
		for (Meeting meet : meets) {
			storeMeet(meet);
		}
	}

	public void storeRace(Race race) throws Exception {
		ObjRace oRace = toObjRace(race);
		ObjectifyService.ofy().save().entity(oRace).now();
		if (race.getRunners() != null) {
			for (Runner runner : race.getRunners()) {
				storeRunner(runner, race);
			}
		}
	}

	private void storeRunner(Runner runner, Race race) {
		ObjRunner oRunner = toObjRunner(runner, race);
		ObjectifyService.ofy().save().entity(oRunner).now();
		if (runner.getOdds() != null &&
				runner.getOdds().getWin() > 0) {
			storeOdds(getRunnerKey(race, runner), runner);
		}
	}

	private void storeOdds(Key<ObjRunner> runnerKey, Runner runner) {
		ObjOdds odds = toObjOdds(runnerKey, runner);
		ObjectifyService.ofy().save().entity(odds).now();
	}

	private ObjOdds toObjOdds(Key<ObjRunner> runnerKey, Runner runner) {
		return new ObjOdds(runnerKey, runner);
	}

	public void storeHorse(Horse horse) throws Exception {
		ObjHorse objHorse = toObjHorse(horse);
		ObjectifyService.ofy().save().entity(objHorse).now();
	}

	public void storeResults(List<RunnerResult> results) throws Exception {
		try {
			for (RunnerResult runner : results) {
				storeRunnerResult(runner);
			}
		} catch (Exception ex) {}
	}

	private void storeRunnerResult(RunnerResult runner) {
		ObjRunnerResult objResult = toObjRunnerResult(runner);
		ObjectifyService.ofy().save().entity(objResult).now();
	}


	public Horse fetchHorse(String horse) throws Exception {
		if (horse == null) {
			return null;
		}
		ObjHorse objHorse = ObjectifyService.ofy()
		          .load()
		          .type(ObjHorse.class) // We want only Races
		          .ancestor(getHorseKey(horse))    // for this race code
		          .first()
		          .now();

		try {
			Horse result = toHorse(objHorse);
			result.setPastResults(fetchPastResults(horse));
			return result;
		} catch (Exception ex) {
			System.out.println(horse);
			return null;
		}
	}

	private List<RunnerResult> fetchPastResults(String horse) {
		List<ObjRunnerResult> objResults = ObjectifyService.ofy()
		          .load()
		          .type(ObjRunnerResult.class) // We want only RunnerResults
		          .ancestor(getHorseKey(horse))    // for this horse
		          .list();
		List<RunnerResult> result = new ArrayList<RunnerResult>();
		for (ObjRunnerResult objResult : objResults) {
			result.add(toRunnerResult(objResult, horse));
		}
		return result;
	}

	private RunnerResult toRunnerResult(ObjRunnerResult objResult, String horse) {
		RunnerResult result = new RunnerResult();
		result.setDistance(objResult.getDistance());
		result.setHorse(horse);
		result.setJockey(objResult.getJockey());
//		result.setMeetCode(objResult.getM);
		result.setPosition(objResult.getPosition());
		result.setPrizeMoney(objResult.getPrizeMoney());
		result.setRaceCode(objResult.getRaceCode());
		result.setRaceDate(objResult.getRaceDate());
//		result.setRaceName(objResult.getR);
//		result.setRaceNumber(objResult.getR);
		result.setRaceTime(objResult.getRaceTime());
		result.setResultType(objResult.getResultType());
		result.setTrainer(objResult.getTrainer());
		result.setVenueName(objResult.getVenueName());
		result.setWeight(objResult.getWeight());
		return result;
	}

	public List<Race> fetchRacesWithoutHistories() {
		List<ObjRace> races = ObjectifyService.ofy()
		          .load()
		          .type(ObjRace.class) // We want only Races
		          .filter("histories", false)
		          .list();
	
		List<Race> result = new ArrayList<Race>();
		for (ObjRace oRace : races) {
			Race r = toRace(oRace);
			result.add(r);
		}
		return result;
	}
	
	public List<Runner> fetchRunnersWithoutHistories() throws Exception {
		List<ObjRunner> runners = ObjectifyService.ofy()
		          .load()
		          .type(ObjRunner.class) // We want only Runners
		          .filter("histories", false) //without histories
		          .list();
	
		List<Runner> result = new ArrayList<Runner>();
		for (ObjRunner oRunner : runners) {
			Runner r = toRunner(oRunner);
			result.add(r);
		}
		return result;
	}

	
	public List<Race> fetchRacesWithoutResults() throws Exception {
		List<ObjRace> races = ObjectifyService.ofy()
		          .load()
		          .type(ObjRace.class) // We want only Races
		          .filter("results", false) //without results
		          .list();
	
		List<Race> result = new ArrayList<Race>();
		for (ObjRace oRace : races) {
			Race r = toRace(oRace);
			result.add(r);
		}
		return result;
	}

	public List<Race> fetchRacesWithResults() throws Exception {
		List<ObjRace> races = ObjectifyService.ofy()
		          .load()
		          .type(ObjRace.class) // We want only Races
		          .filter("results", true) //with results
		          .list();
	
		List<Race> result = new ArrayList<Race>();
		for (ObjRace oRace : races) {
			Race r = toRace(oRace);
			result.add(r);
		}
		return result;
	}
	
	public void exportRunnerHistories(Exporter<RunnerResult> exporter) throws Exception {
		List<ObjRunner> runners = ObjectifyService.ofy()
		          .load()
		          .type(ObjRunner.class) // We want only Runners
		          .list();
		for (ObjRunner oRunner : runners) {
			exportRunnerHistories(exporter, oRunner);
		}
	}
	
	private void exportRunnerHistories(Exporter<RunnerResult> exporter, ObjRunner runner) {
		List<ObjRunnerResult> objResults = ObjectifyService.ofy()
		          .load()
		          .type(ObjRunnerResult.class) // We want only RunnerResults
		          .ancestor(getHorseKey(runner.getHorse()))    // for this horse
		          .list();
		for (ObjRunnerResult objResult : objResults) {
			exporter.export(toRunnerResult(objResult, runner.getHorse()));
		}
	}

	public List<Meeting> fetchUpcomingMeets() throws Exception {
		//TODO this should probably all be done by Objectify
		try {
			List<Meeting> meets = fetchExistingMeets();
			List<Meeting> upcoming = new ArrayList<Meeting>();
			
			for (Meeting meeting : meets) {
				try {
					if (meeting.getDate().getTime() > System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
						upcoming.add(meeting);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return upcoming;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void exportRunners(Exporter<Runner> exporter) throws Exception {
		List<Race> races = fetchRacesWithResults();
		for (Race race : races) {
			exportRunnersForRace(exporter, race);
		}
	}

	private void exportRunnersForRace(Exporter<Runner> exporter, Race race) throws Exception {
		List<Runner> runners = super.fetchRunnersForRace(race);
		for (Runner runner : runners) {
			exporter.export(runner);
		}
	}
}
