package com.goodpunts.objectify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.stats.SingleVariateStatistic;
import com.joe.springracing.business.probability.Probability;
import com.joe.springracing.dao.PuntingDAO;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class ObjectifyPuntingDaoImpl extends ObjectifyBaseDaoImpl implements PuntingDAO {

	public void storeProbabilities(Race race) {
		Key<ObjRace> raceKey = super.getRaceKey(race);
		for (Runner runner : race.getRunners()) {
			storeProbabilities(raceKey, race.getRaceCode(), runner);
		}
	}

	private void storeProbabilities(Key<ObjRace> race, String raceCode, Runner runner) {	
		Key<ObjRunner> runnerKey = super.getRunnerKey(race, runner.getHorse());

		ObjProbability oProbability = toObjProbability(runnerKey, raceCode, runner);
		Key<ObjProbability> key = ObjectifyService.ofy().save().entity(oProbability).now();
		
		if (runner.getStatistics() != null) {
			for (AnalysableObjectStatistic stat : runner.getStatistics()) {
				ObjStatistic objStat = new ObjStatistic(stat, oProbability.getId(), key);
				ObjectifyService.ofy().save().entity(objStat).now();
			}
		}
	}

	private ObjProbability toObjProbability(Key<ObjRunner> runnerKey, String raceCode, Runner runner) {
		return new ObjProbability(runner.getProbability(), raceCode, runner.getHorse(), runnerKey);
	}

	public void storePunts(Race race, List<Punt> punts) {
		Key<ObjPuntEvent> event = storePuntEvent(race);
		for (Punt punt : punts) {
			storePunt(event, punt);
		}
	}

	private Key<ObjPuntEvent> storePuntEvent(Race race) {
		ObjPuntEvent event = new ObjPuntEvent(race);
		return ObjectifyService.ofy().save().entity(event).now();
	}

	private void storePunt(Key<ObjPuntEvent> event, Punt punt) {
		System.out.println(punt.getRace().getVenue() + " " +
				punt.getRace().getRaceNumber() + " " +
				punt.getRunners().get(0).getHorse());
		ObjPunt oPunt = toObjPunt(event, punt);
		ObjectifyService.ofy().save().entity(oPunt).now();
	}
	
	public List<Punt> fetchPuntsForMeet(Meeting meet) throws Exception {
		List<Punt> result = new ArrayList<Punt>();
		List<Race> races = super.fetchRacesForMeet(meet);
		for (Race race : races) {
			result.addAll(fetchPuntsForRace(race));
		}
		return result;
	}
	
	public List<Punt> fetchPuntsForRace(Race race) throws Exception {
		List<Punt> result = new ArrayList<Punt>();
		ObjPuntEvent event = fetchPuntEvent(race);
		if (event != null) {
			Key<ObjRace> parent = getRaceKey(race);
			Key<ObjPuntEvent> key = Key.create(parent, ObjPuntEvent.class, event.getId());
			
			List<ObjPunt> punts = ObjectifyService.ofy()
		        .load()
		        .type(ObjPunt.class) // We want only PuntEvents
		        .ancestor(key)    // Punts for this event
		        .list();
			
			for (ObjPunt punt : punts) {
				Punt p = toPunt(punt, race);
				List<Runner> runners = fetchRunners(punt.getRunners(), race);
				p.setRunners(runners);
				result.add(p);
			}
		}
		return result;
	}
	
	public List<Punt> fetchPuntResults() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ObjPuntEvent fetchPuntEvent(Race race) {
		return ObjectifyService.ofy()
		          .load()
		          .type(ObjPuntEvent.class) // We want only PuntEvents
		          .ancestor(getRaceKey(race))    // Punts for the meet
		          .order("-date")
		          .first()
		          .now();
	}
	
	public Date fetchLastPuntEvent(Race r) {
		ObjPuntEvent event = fetchPuntEvent(r);
		return event.getDate();
	}

//	private List<Punt> fetchPuntsForEvent(ObjPuntEvent pe) throws Exception {
//		Key<ObjPuntEvent> parent = Key.create(ObjPuntEvent.class, pe.getMeetCode());
//		List<ObjPunt> punts = ObjectifyService.ofy()
//		          .load()
//		          .type(ObjPunt.class) // We want only Punt
//		          .ancestor(parent)    // Punts for the race
//		          .list();
//		List<Punt> result = new ArrayList<Punt>();
//		for (ObjPunt oPunt : punts) {
//			Punt p = toPunt(oPunt, null);
//			List<Runner> runners = fetchRunners(oPunt.getRunners(), null);
//			p.setRunners(runners);
//			result.add(p);
//		}
//		return result;
//	}
	
	private List<Runner> fetchRunners(List<Key<ObjRunner>> runners, Race race) throws Exception {
		List<Runner> result = new ArrayList<Runner>();
		for (Key<ObjRunner> runner : runners) {
			result.add(fetchRunner(runner, race));
		}
		return result;
	}

	private Runner fetchRunner(Key<ObjRunner> keyRunner, Race race) throws Exception {
		ObjRunner objRunner = ObjectifyService.ofy()
			.load()
			.key(keyRunner)		
			.now();
		
		return toRunner(objRunner);
	}

	
	private ObjPunt toObjPunt(Key<ObjPuntEvent> event, Punt punt) {
		List<String> runnerIds = new ArrayList<String>();
		for (Runner r : punt.getRunners()) {
			runnerIds.add(r.getHorse());
		}
		ObjPunt result = new ObjPunt(event, punt.getRace().getRaceCode(), runnerIds);
		result.setBookieOdds(punt.getBookieOdds());
		result.setJoesOdds(punt.getJoesOdds());
		result.setType(punt.getType());
		return result;
	}

	private Punt toPunt(ObjPunt oPunt, Race race) {
		Punt p = new Punt(race, oPunt.getType(), oPunt.getJoesOdds(), oPunt.getBookieOdds());
		return p;
	}

//	private List<Race> fetchRaceIdsForMeet(Meeting meet) {
//		List<ObjRace> races = ObjectifyService.ofy()
//		          .load()
//		          .type(ObjRace.class) // We want only Races
//		          .filter("meetCode", meet.getMeetCode())    // Races in the meet
//		          .list();
//		List<Race> result = new ArrayList<Race>();
//		for (ObjRace race : races) {
//			result.add(toRace(race));
//		}
//		return result;
//	}

	public List<Runner> fetchProbabilitiesForRace(Race race) throws Exception {
		List<Runner> result = super.fetchRunnersForRace(race);
		Key<ObjRace> raceKey = super.getRaceKey(race);
		for (Runner runner : result) {
			ObjProbability objProb = fetchProbabilityForRunner(raceKey, runner);
			if (objProb != null) {
				runner.setProbability(toProbability(objProb));
				///Where to ?
				Key<ObjRunner> parent = super.getRunnerKey(raceKey, runner.getHorse());
				Key<ObjProbability> key = Key.create(parent, ObjProbability.class, objProb.getId());
				/////
				List<ObjStatistic> objStats = fetchStatisticsForProbability(key);
				runner.setStatistics(toStatistics(objStats));
			}
		}
		return result;
	}

	private List<AnalysableObjectStatistic> toStatistics(List<ObjStatistic> objStats) {
		List<AnalysableObjectStatistic> result = new ArrayList<AnalysableObjectStatistic>();
		for (ObjStatistic objStat : objStats) {
			result.add(toStatistic(objStat));
		}
		return result;
	}

	private AnalysableObjectStatistic toStatistic(ObjStatistic objStat) {
		SingleVariateStatistic stat = new SingleVariateStatistic();
		stat.setMean(objStat.getMean());
		stat.setStandardDeviation(objStat.getStandardDeviation());
		stat.setWeight(objStat.getWeight());
		return stat;
	}

	private List<ObjStatistic> fetchStatisticsForProbability(Key<ObjProbability> key) {
		List<ObjStatistic> stats = ObjectifyService.ofy()
		          .load()
		          .type(ObjStatistic.class) // We want only Races
		          .ancestor(key)    // Races in the meet
		          .list();
		return stats;
	}

	private Probability toProbability(ObjProbability objProb) {
		Probability probability = new Probability();
		probability.setPlace(objProb.getPlace());
		probability.setWin(objProb.getWin());
		return probability;
	}

	private ObjProbability fetchProbabilityForRunner(Key<ObjRace> race, Runner runner) {
		Key<ObjRunner> runnerKey = super.getRunnerKey(race, runner.getHorse());
		List<ObjProbability> probs = ObjectifyService.ofy()
	          .load()
	          .type(ObjProbability.class) // We want only Probabilities
	          .ancestor(runnerKey)    // Probabilities for this runner
	          .list();
	          //.first()
	          //.now();
		
		if (probs.size() > 0) {
			return probs.get(0);
		}
		return null;
	}

}
