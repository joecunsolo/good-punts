package com.joe.springracing.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.services.PuntingService;
import com.joe.springracing.services.PuntingServiceImpl;
import com.joe.springracing.test.mock.MockRacingDao;

import junit.framework.TestCase;

public class TestPuntingBusiness extends TestCase {

	public static final String KEY_RACECODE = "race1";
	public static final String KEY_PASTRACECODE = "pastrace1";
	public static final String KEY_MEETCODE = "meet1";
	public static final String KEY_HORSECODE = "horse1";

	private static Race aRace() {
		Race race = new Race();
		race.setRaceCode(KEY_RACECODE);
		race.setMeetCode(KEY_MEETCODE);
		race.setDate(new Date(System.currentTimeMillis() + 60000));
		return race;
	}
	
	private static Meeting aMeet() {
		Meeting meet = new Meeting();
		meet.setMeetCode(KEY_MEETCODE);
		meet.setDate(new Date());
		return meet;
	}
	
	private static RunnerResult aResult(Runner forWho) {
		RunnerResult result = new RunnerResult();
		result.setDistance(1000);
		result.setRaceCode(KEY_PASTRACECODE);
		result.setHorse(forWho.getHorse());
		result.setJockey(forWho.getJockey());
		return result;
	}

	private static Runner aRunner() {
		Runner runner = new Runner();
		runner.setHorse(KEY_HORSECODE);
		Odds o = new Odds();
		o.setWin(10);
		runner.setOdds(o);
		runner.setNumber(1);
		return runner;
	}
	
	private static List<RunnerResult> somePastResults(Runner forWho) {
		List<RunnerResult> pastResults = new ArrayList<RunnerResult>();
		RunnerResult aResult = aResult(forWho);
		pastResults.add(aResult);
		return pastResults;
	}
	
	
	/**
	 * Given a Punt is generated
	 * When the punt is stored
	 * Then status of the punt should be open
	 */
	
	/**
	 * Given a Punt is generated
	 * When the punt is stored
	 * Then status of the punt should be open
	 */
	
	/**
	 * Given a Race
	 * And a runner in the race
	 * And the runner is a good punt
	 * And the horse has been in only race
	 * When we get all the good Punts for the Race
	 * Then the punt should not be returned
	 * @throws Exception 
	 */
	public static void testNoOneRaceHorses() throws Exception {
		PuntingService s = new PuntingServiceImpl();
		//Given a Race
		Race aRace = aRace();
		//And a runner in the race
		Runner aRunner = aRunner();
		//And the runner is a good punt
		aRunner.getProbability().setWin(25);
		List<Runner> runners = new ArrayList<Runner>();
		runners.add(aRunner);
		aRace.setRunners(runners);
		//And the horse has been in only race
		Horse aHorse = new Horse();
		aHorse.setCode(KEY_HORSECODE);
		aHorse.setNumberOfRaces(1);
		MockRacingDao dao = new MockRacingDao();
		dao.storeHorse(aHorse);
		SpringRacingServices.setSpringRacingDAO(dao);
		//When we get all the good Punts for the Race
		List<Punt> p = s.generate(aRace);
		//Then the punt should not be returned
		Assert.assertTrue("No one run horses", p.size() == 0);
	}
	
	/**
	 * Given a Race
	 * And a runner in the race
	 * And the runner is a good punt
	 * And the horse has been in only race
	 * And there is no minimum number of horses
	 * When we get all the good Punts for the Race
	 * Then the punt should not be returned
	 * @throws Exception 
	 */
	public static void testMoreThanOneRaceHorses() throws Exception {
		//Given a Race
		Race aRace = aRace();
		//And a runner in the race
		Runner aRunner = aRunner();
		//And the runner is a good punt
		aRunner.getProbability().setWin(25);
		List<Runner> runners = new ArrayList<Runner>();
		runners.add(aRunner);
		aRace.setRunners(runners);
		//And the horse has been in only race
		Horse aHorse = new Horse();
		aHorse.setCode(KEY_HORSECODE);
		aHorse.setNumberOfRaces(2);
		MockRacingDao dao = new MockRacingDao();
		dao.storeHorse(aHorse);
		SpringRacingServices.setSpringRacingDAO(dao);
		//And there is no minimum number of horses
		ModelAttributes ma = new ModelAttributes();
		ma.setMinimumThreeRaceHorses(10);
		Model m = new Model(ma);
		PuntingService s = new PuntingServiceImpl(m);
		//When we get all the good Punts for the Race
		List<Punt> p = s.generate(aRace);
		//Then the punt should not be returned
		Assert.assertTrue("No one run horses", p.size() == 1);
	}
}
