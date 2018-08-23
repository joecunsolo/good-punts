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
	public Race fetchRace(String raceCode) throws Exception {
		ObjRace race = ObjectifyService.ofy()
		          .load()
		          .key(getRaceKey(raceCode))
		          .now();

		return toRace(race);
	}

	public Meeting fetchMeet(String meetCode) throws Exception {
		ObjMeet meet = ObjectifyService.ofy()
		          .load()
		          .key(getMeetKey(meetCode))
		          .now();

		return toMeeting(meet);
	}
	
	public List<Meeting> fetchExistingMeets() throws Exception {
		List<ObjMeet> meetings = ObjectifyService.ofy()
		          .load()
		          .type(ObjMeet.class) // We want only Meetings
		          .list();
		List<Meeting> result = new ArrayList<Meeting>();
		for (ObjMeet oMeet : meetings) {
			Meeting m = toMeeting(oMeet);
			result.add(m);
		}
		return result;
	}

	public List<Race> fetchRacesForMeet(Meeting meet) throws Exception {
		List<ObjRace> races = ObjectifyService.ofy()
		          .load()
		          .type(ObjRace.class) // We want only Races
		          .filter("meetCode", meet.getMeetCode())
//		          .ancestor(getMeetKey(meet))    // Races in the meet
		          .list();
		List<Race> result = new ArrayList<Race>();
		for (ObjRace oRace : races) {
			Race r = toRace(oRace);
			result.add(r);
		}
		return result;
	}
	
	protected Meeting toMeeting(ObjMeet oMeet) {
		Meeting m = new Meeting();
		m.setDate(oMeet.getDate());
		m.setMeetCode(oMeet.getMeetCode());
		m.setVenue(oMeet.getVenue());
		return m;
	}


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
		if (r == null) {
			return null;
		}
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
		race.setHistories(r.hasHistories());
		race.setNumberOfRunnersLessThan3Races(r.getLessThan3Races());
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
//		r.setHistories(oRunner.isHistories());
		r.setRaceCode(oRunner.getRaceCode());
		r.setWeight(oRunner.getWeight());
		r.setBarrier(oRunner.getBarrier());
	
		return r;
	}
	
	protected ObjRunner toObjRunner(Runner runner, Race r) {
		ObjRunner o = new ObjRunner(r.getRaceCode());
//		o.setHistories(runner.hasHistories());
		o.setEmergency(runner.isEmergency());
		o.setHorse(runner.getHorse());
		o.setJockey(runner.getJockey());
		o.setNumber(runner.getNumber());
		o.setScratched(runner.isScratched());
		o.setTrainer(runner.getTrainer());
		o.setWeight(runner.getWeight());
		o.setBarrier(runner.getBarrier());
		
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
		ObjRace result = new ObjRace();
		result.setMeetCode(race.getMeetCode());
		result.setDate(race.getDate());
		result.setDistance(race.getDistance());
		result.setName(race.getName());
		result.setRaceCode(race.getRaceCode());
		result.setPrizeMoney(race.getPrizeMoney());
		result.setRaceNumber(race.getRaceNumber());
		result.setResult(race.getResult());
		result.setResults(race.getResult() != null);
		result.setVenue(race.getVenue());
		result.setHistories(race.hasHistories());
		result.setLessThan3Races(race.getNumberOfRunnersLessThan3Races());
		return result;
	}
	
	protected ObjHorse toObjHorse(Horse horse) {
		ObjHorse result = new ObjHorse(); 
		result.setCode(horse.getCode());
		result.setName(horse.getName());
		result.setId(horse.getId());
		result.setHistories(horse.hasHistories());
		result.setNumberOfRaces(horse.getNumberOfRaces());
		result.setSpell(horse.getSpell());
		result.setSplits(horse.getSplits());
		result.setHasSplits(horse.hasSplits());
		result.setAge(horse.getAge());
		result.setAveragePrizeMoney(horse.getAveragePrizeMoney());
		result.setPrizeMoney(horse.getPrizeMoney());
		result.setColour(horse.getColour());
		result.setSex(horse.getSex());
		
		result.setGoodAtDistance(horse.isGoodAtDistance());
		result.setGoodAtClass(horse.isGoodAtClass());
		result.setGoodAtTrack(horse.isGoodAtTrack());
		result.setGoodAtTrackCondition(horse.isGoodAtTrackCondition());

		return result;
	}
	
	protected ObjRunnerResult toObjRunnerResult(RunnerResult runner) {
		ObjRunnerResult result = new ObjRunnerResult(runner.getHorse(), runner.getRaceCode());
		result.setDistance(runner.getDistance());
		result.setJockey(runner.getJockey());
		result.setPosition(runner.getPosition());
		result.setPrizeMoney(runner.getPrizeMoney());
//		result.setRaceCode(runner.getRaceCode());
		result.setRaceDate(runner.getRaceDate());
		result.setRaceTime(runner.getRaceTime());
		result.setResultType(runner.getResultType());
		result.setSplits(runner.getSplits());
		result.setTrainer(runner.getTrainer());
		result.setVenueName(runner.getVenueName());
		result.setWeight(runner.getWeight());
		result.setBarrier(runner.getBarrier());
		result.setTrial(runner.isTrial());
		result.setHorseRating(runner.getRating());
		result.setRacePrizeMoney(runner.getRacePrizeMoney());
		result.setTrackCondition(runner.getTrackCondition());
		return result;
	}

	protected Horse toHorse(ObjHorse objHorse) {
		Horse horse = new Horse();
		horse.setCode(objHorse.getCode());
		horse.setId(objHorse.getId());
		horse.setName(objHorse.getName());
		horse.setHistories(objHorse.hasHistories());
		horse.setNumberOfRaces(objHorse.getNumberOfRaces());
		horse.setSpell(objHorse.getSpell());
		horse.setSplits(objHorse.getSplits());
		horse.setHasSplits(objHorse.hasSplits());
		horse.setAge(objHorse.getAge());
		horse.setColour(objHorse.getColour());
		horse.setAveragePrizeMoney(objHorse.getAveragePrizeMoney());
		horse.setPrizeMoney(objHorse.getPrizeMoney());
		horse.setSex(objHorse.getSex());
		
		horse.setGoodAtClass(objHorse.isGoodAtClass());
		horse.setGoodAtDistance(objHorse.isGoodAtDistance());
		horse.setGoodAtTrack(objHorse.isGoodAtTrack());
		horse.setGoodAtTrackCondition(objHorse.isGoodAtTrackCondition());
		return horse;
	}
	protected Key<ObjMeet> getMeetKey(Meeting meet) {
	    return getMeetKey(meet.getMeetCode());
	}
	protected Key<ObjMeet> getMeetKey(String meetCode) {
	    return Key.create(ObjMeet.class, meetCode);
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
