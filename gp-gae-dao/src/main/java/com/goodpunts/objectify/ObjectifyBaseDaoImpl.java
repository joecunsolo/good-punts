package com.goodpunts.objectify;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class ObjectifyBaseDaoImpl {
	public List<Runner> fetchRunnersForRace(Race race) throws Exception {
		Key<ObjRace> raceKey = getRaceKey(race);
		List<ObjRunner> runners = ObjectifyService.ofy()
		          .load()
		          .type(ObjRunner.class) // We want only Races
		          .ancestor(raceKey)    // Races in the meet
		          .list();
		List<Runner> result = new ArrayList<Runner>();
		for (ObjRunner oRunner : runners) {
			Runner r = toRunner(oRunner);
			Odds o = fetchOdds(raceKey, oRunner.getHorse());
			r.setOdds(o);
			result.add(r);
		}
		return result;
	}

	public Odds fetchOdds(Key<ObjRace> race, String horse) {
		List<ObjOdds> odds = ObjectifyService.ofy()
		          .load()
		          .type(ObjOdds.class) // We want only Odds
		          .ancestor(getRunnerKey(race, horse))    // for this horse
		          .list();
		
		if (odds.size() > 0) {
			return toOdds(odds.get(0));
		}
		return new Odds();
	}

	protected Race toRace(ObjRace r) {
		Race race = new Race();
		race.setDate(r.getDate());
		race.setDistance(r.getDistance());
		race.setMeetCode(r.getMeetCode());
		race.setName(r.getName());
		race.setPrizeMoney(r.getPrizeMoney());
		race.setRaceCode(r.getRaceCode());
		race.setRaceNumber(r.getRaceNumber());
		race.setResult(r.getResult());
		race.setVenue(r.getVenue());
		return race;
	}
	
	protected Odds toOdds(ObjOdds objOdds) {
		Odds odds = new Odds();
		odds.setWin(objOdds.getWin());
		odds.setPlace(objOdds.getPlace());
		return odds;
	}

	protected Runner toRunner(ObjRunner oRunner) {
		Runner r = new Runner();
		r.setEmergency(oRunner.isEmergency());
		r.setHorse(oRunner.getHorse());
		r.setJockey(oRunner.getJockey());
		r.setNumber(oRunner.getNumber());
		r.setScratched(oRunner.isScratched());
		r.setTrainer(oRunner.getTrainer());
		return r;
	}
	
	protected ObjRunner toObjRunner(Runner runner, Race r) {
		ObjRunner o = new ObjRunner(r.getRaceCode());
		o.setEmergency(runner.isEmergency());
		o.setHorse(runner.getHorse());
		o.setJockey(runner.getJockey());
		o.setNumber(runner.getNumber());
		o.setScratched(runner.isScratched());
		o.setTrainer(runner.getTrainer());
		return o;
	}

	protected ObjMeet toObjMeet(Meeting meet) {
		ObjMeet result = new ObjMeet();
		result.setMeetCode(meet.getMeetCode());
		result.setDate(meet.getDate());
		result.setVenue(meet.getVenue());
		return result;
	}
	
	protected ObjRace toObjRace(Race race) {
		ObjRace result = new ObjRace(race.getMeetCode());
		result.setDate(race.getDate());
		result.setDistance(race.getDistance());
		result.setName(race.getName());
		result.setRaceCode(race.getRaceCode());
		result.setPrizeMoney(race.getPrizeMoney());
		result.setRaceNumber(race.getRaceNumber());
		result.setResult(race.getResult());
		result.setVenue(race.getVenue());
		return result;
	}
	
	protected ObjHorse toObjHorse(Horse horse) {
		ObjHorse result = new ObjHorse();
		result.setCode(horse.getCode());
		result.setName(horse.getName());
		result.setId(horse.getId());
		return result;
	}
	
	protected ObjRunnerResult toObjRunnerResult(RunnerResult runner) {
		ObjRunnerResult result = new ObjRunnerResult(runner.getHorse());
		result.setDistance(runner.getDistance());
		result.setJockey(runner.getJockey());
		result.setPosition(runner.getPosition());
		result.setPrizeMoney(runner.getPrizeMoney());
		result.setRaceCode(runner.getRaceCode());
		result.setRaceDate(runner.getRaceDate());
		result.setRaceTime(runner.getRaceTime());
		result.setResultType(runner.getResultType());
		result.setSplits(runner.getSplits());
		result.setTrainer(runner.getTrainer());
		result.setVenueName(runner.getVenueName());
		return result;
	}

	protected Horse toHorse(ObjHorse objHorse) {
		Horse horse = new Horse();
		horse.setCode(objHorse.getCode());
		horse.setId(objHorse.getId());
		horse.setName(objHorse.getName());
		return horse;
	}
	protected Key<ObjMeet> getMeetKey(Meeting meet) {
	    return Key.create(ObjMeet.class, meet.getMeetCode());
	}

	protected Key<ObjRace> getRaceKey(Race race) {
	    return getRaceKey(race.getRaceCode());
	}
	
	protected Key<ObjRace> getRaceKey(String raceCode) {
	    return Key.create(ObjRace.class, raceCode);
	}
	
	protected Key<ObjHorse> getHorseKey(String horse) {
	    return Key.create(ObjHorse.class, horse);
	}

	protected Key<ObjRunner> getRunnerKey(Key<ObjRace> race, String horse) {
	    return Key.create(race, ObjRunner.class, horse);		
	}

	protected Key<ObjRunner> getRunnerKey(Race race, Runner runner) {
		Key<ObjRace> raceKey = getRaceKey(race);
		return getRunnerKey(raceKey, runner.getHorse());
	}

}