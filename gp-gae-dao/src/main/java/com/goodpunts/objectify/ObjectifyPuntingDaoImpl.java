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
import com.joe.springracing.objects.Stake;

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
//		Key<ObjProbability> key = 
		ObjectifyService.ofy().save().entity(oProbability).now();
		
//		if (runner.getStatistics() != null) {
//			for (AnalysableObjectStatistic stat : runner.getStatistics()) {
//				ObjStatistic objStat = new ObjStatistic(stat, oProbability.getId(), key);
//				ObjectifyService.ofy().save().entity(objStat).now();
//			}
//		}
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
		ObjPuntEvent event = fetchPuntEvent(race);
		return fetchPuntsForEvent(event, race);
	}
	
	//TODO Need to be able to query this just based on the event - not the race
	private List<Punt> fetchPuntsForEvent(ObjPuntEvent event, Race race) throws Exception {
		List<Punt> result = new ArrayList<Punt>();
		if (event != null) {
			Key<ObjRace> parent = getRaceKey(race);
			Key<ObjPuntEvent> key = Key.create(parent, ObjPuntEvent.class, event.getId());
			
			List<ObjPunt> punts = ObjectifyService.ofy()
		        .load()
		        .type(ObjPunt.class) // We want only PuntEvents
		        .ancestor(key)    // Punts for this event
		        .list();
			
			for (ObjPunt punt : punts) {
				Punt p = toPunt(punt);
				List<Runner> runners = fetchRunners(punt.getRunners());
				p.setRunners(runners);
				
//				List<Stake> stakes = fetchStakesForPunt(p);
//				p.setStakes(stakes);
//				
				result.add(p);
			}
		}
		return result;		
	}

	public List<Punt> fetchPuntResults() throws Exception {
		List<Punt> result = new ArrayList<Punt>();
		List<Meeting> meets = fetchExistingMeets();
		for (Meeting meet : meets) {
			result.addAll(fetchPuntResultsForMeet(meet));
		}
		return result;
	}
	
	private List<Punt> fetchPuntResultsForMeet(Meeting meet) throws Exception {
		List<Punt> result = new ArrayList<Punt>();
		List<Race> races = super.fetchRacesForMeet(meet);
		for (Race race : races) {
			List<Punt> punts = fetchPuntsForRace(race);
			setRaceResultsOnPunts(punts, race);
			result.addAll(punts);
		}
		return result;
	}
	
//	private List<ObjPuntEvent> fetchAllPunts() throws Exception {
//		return ObjectifyService.ofy()
//		          .load()
//		          .type(ObjPuntEvent.class) // We want only PuntEvents
//		          .list();
//	}
	
	private void setRaceResultsOnPunts(List<Punt> punts, Race race) {
		for (Punt p : punts) {
			setRaceResultsOnPunts(p, race);
		}
	}

	private void setRaceResultsOnPunts(Punt p, Race race) {
		for (Runner r : p.getRunners()) {
			setRaceResultOnRunner(r, race);
		}
	}

	private void setRaceResultOnRunner(Runner r, Race race) {
		int[] results = race.getResult();
		for (int i = 0; i < results.length; i++) {
			if (results[i] == r.getNumber()) {
				r.setResult(i + 1);
				return;
			}
		}
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
	
	private List<Runner> fetchRunners(List<Key<ObjRunner>> runners) throws Exception {
		List<Runner> result = new ArrayList<Runner>();
		for (Key<ObjRunner> runner : runners) {
			result.add(fetchRunner(runner));
		}
		return result;
	}

	private Runner fetchRunner(Key<ObjRunner> keyRunner) throws Exception {
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
		ObjPunt result = new ObjPunt(event, punt.getRaceCode(), runnerIds);
		result.setBookieOdds(punt.getBookieOdds());
		result.setJoesOdds(punt.getJoesOdds());
		result.setType(punt.getType());
		result.setConfidence(punt.getConfidence());
		result.setDate(punt.getDate());
		result.setState(punt.getState());
		result.setRaceNumber(punt.getRaceNumber());
		return result;
	}

	private Punt toPunt(ObjPunt oPunt) {
		Punt p = new Punt(oPunt.getRaceCode(), oPunt.getDate(), oPunt.getType(), oPunt.getJoesOdds(), oPunt.getBookieOdds(), oPunt.getConfidence(), oPunt.getState());
		p.setId(oPunt.id);
		p.setRaceNumber(oPunt.getRaceNumber());
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
//				///Where to ?
//				Key<ObjRunner> parent = super.getRunnerKey(raceKey, runner.getHorse());
//				Key<ObjProbability> key = Key.create(parent, ObjProbability.class, objProb.getId());
//				/////
//				List<ObjStatistic> objStats = fetchStatisticsForProbability(key);
//				runner.setStatistics(toStatistics(objStats));
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
		probability.setMean(objProb.getMean());
		probability.setStandardDeviation(objProb.getStandardDeviation());
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

	public List<Punt> fetchSettledPunts() {
		List<ObjPunt> punts = ObjectifyService.ofy()
		        .load()
		        .type(ObjPunt.class) // We want only Punts
		        .filter("state", Punt.State.FINISHED) //FINISHED Punts
		        .list();
		
		List<Punt> result = new ArrayList<Punt>();
		for (ObjPunt punt : punts) {
			Punt p = toPunt(punt);
			try {
				List<Runner> runners = fetchRunners(punt.getRunners());
				p.setRunners(runners);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
//			List<Stake> stakes = fetchStakesForPunt(p);
//			if (stakes != null) {
//				p.setStakes(stakes);
//			}
			result.add(p);
		}

		return result;
	}

	public List<Punt> fetchOpenPunts() {
		List<ObjPunt> punts = ObjectifyService.ofy()
		        .load()
		        .type(ObjPunt.class) // We want only Punts
		        .filter("state", Punt.State.OPEN) //Open Punts
		        .list();
		
		List<Punt> result = new ArrayList<Punt>();
		for (ObjPunt punt : punts) {
			Punt p = toPunt(punt);
			try {
				List<Runner> runners = fetchRunners(punt.getRunners());
				p.setRunners(runners);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
//			List<Stake> stakes = fetchStakesForPunt(p);
//			if (stakes != null) {
//				p.setStakes(stakes);
//			}
			result.add(p);
		}

		return result;
	}

	/**
	 * Get the existing stakes for the Punt
	 * If there are none add them all
	 * 
	 * Otherwise get the new Stakes and add them 
	 */
//	public void updateStakes(Punt punt) {
//		//Get the existing stakes
//		List<Stake> oldStakes = fetchStakesForPunt(punt);
//		//no stakes yet - so we can add all of the Punt's stakes
//		if (oldStakes == null) {
//			List<ObjStake> newStakes = toObjStakes(punt, punt.getStakes());
//			storeStakes(newStakes);
//		//Otherwise get the new Stakes 
//		} else {
//			for (Stake stake : punt.getStakes()) {
//				if (!oldStakes.contains(stake)) {
//					//and add them
//					ObjStake oStake = toObjStake(stake);
//					storeStake(oStake);
//				}
//			}
//		}
//	}
	

//	private void storeStakes(List<ObjStake> stakes) {
//		for (ObjStake oStake : stakes) {
//			storeStake(oStake);
//		}
//	}

//	private void storeStake(ObjStake oStake) {
//	}

//	private List<ObjStake> toObjStakes(Punt p, List<Stake> stakes) {
//		List<ObjStake> result = new ArrayList<ObjStake>();
//		for (Stake stake : stakes) {
//			result.add(toObjStake(stake));
//		}
//		return result;
//	}

	private ObjStake toObjStake(Stake stake) {
		ObjStake result = new ObjStake(stake.hashCode());
		
		result.setAccount(stake.getAccount());
		result.setAmount(stake.getAmount());
		result.setBookieOdds(stake.getBookieOdds());
		result.setConfidence(stake.getConfidence());
		result.setDate(stake.getDate());
		result.setJoesOdds(stake.getJoesOdds());
		result.setOdds(stake.getOdds());
		result.setRaceCode(stake.getRaceCode());
		result.setRaceNumber(stake.getRaceNumber());
		result.setReturn(stake.getReturn());
		result.setResult(stake.getResult());
		result.setRunners(stake.getRunners());
		result.setSettled(stake.isSettled());
		result.setState(stake.getState());
		result.setType(stake.getType());
		result.setTxnNo(stake.getTxnNo());
		result.setVenue(stake.getVenue());
		result.setBalance(stake.getBalance());
		return result;
	}

//	public List<Stake> fetchStakesForPunt(Punt punt) {
//		List<ObjStake> objStakes =  ObjectifyService.ofy()
//		        .load()
//		        .type(ObjStake.class) // We want only Stakes
//		        .ancestor(key)	//For this punt
//		        .list();
//		
//		return toStakeList(objStakes);
//	}

//	private List<Stake> toStakeList(List<ObjStake> objStakes) {
//		List<Stake> result = new ArrayList<Stake>();
//		for (ObjStake oStake : objStakes) {
//			Stake stake = new Stake();
//			stake.setAccount(oStake.getAccount());
//			stake.setAmount(oStake.getAmount());
//			stake.setDate(oStake.getDate());
//			stake.setOdds(oStake.getOdds());
//			stake.setReturn(oStake.getRetrn());
//			stake.setTxnNo(oStake.getTxnNo());
//			stake.setSettled(oStake.isSettled());
//			result.add(stake);
//		}
//		return result;
//	}

//	private ObjStakeList toObjStakeList(long id, List<ObjStake> stakes) {
//		return 
//	}

	public Date getLastBookieUpdateTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateStake(Stake stake) {
		storeStake(stake);
	}

	public List<Stake> fetchSettledStakes() {
		return downgrade(ObjectifyService.ofy()
		        .load()
		        .type(ObjStake.class) // We want only Punts
		        .filter("settled", true) //Open Punts
		        .list());
	}

	public void storeStake(Stake stake) {
		ObjStake oStake = toObjStake(stake);
		ObjectifyService.ofy().save().entity(oStake).now();
	}

	public List<Stake> fetchOpenStakes() {
		return downgrade(ObjectifyService.ofy()
		        .load()
		        .type(ObjStake.class) // We want only Punts
		        .filter("settled", false) //Open Punts
		        .list());
	}

	public List<Stake> fetchStakesForRace(Race race) {
		return downgrade(ObjectifyService.ofy()
		        .load()
		        .type(ObjStake.class) // We want only Punts
		        .filter("settled", false) //Open Punts
		        .filter("raceCode", race.getRaceCode())
		        .list());
	}
	
	private List<Stake> downgrade(List<ObjStake> obj) {
		List<Stake> result = new ArrayList<Stake>();
		for (ObjStake s : obj) {
			result.add(s);
		}
		return result;
	}

}
