package com.goodpunts.gae.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.example.guestbook.OfyHelper;
import com.goodpunts.gae.test.mock.MockSimulator;
import com.goodpunts.gae.test.mock.MockSpringDataSource;
import com.goodpunts.objectify.ObjectifyGoodPuntsBookieImpl;
import com.goodpunts.servlet.ImportUpcomingRacesServlet;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.BettingBusiness;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.business.Simulator;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.objects.Stake;

public class TestGAEEndToEnd {
	
	public static final String KEY_RACECODE = "race1";
	public static final String KEY_PASTRACECODE = "pastrace1";
	public static final String KEY_MEETCODE = "meet1";
	public static final String KEY_HORSECODE = "horse1";
	
	private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(
		      new LocalDatastoreServiceTestConfig());
	private static Closeable closeable;
	private MockSpringDataSource datasource;
	
	@Before
	public void setUp() {
		helper.setUp();
		closeable = ObjectifyService.begin();
		OfyHelper.init(null);
		
		//set up mocks
		datasource = new MockSpringDataSource();
		SpringRacingServices.setSpringRacingDataSource(datasource);
		Simulator simulator = new MockSimulator(1);
		SpringRacingServices.setSimulator(simulator);
	}

	@After
	public void tearDown() {
		helper.tearDown();
		closeable.close();
	}
	
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
		Calendar c= Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 7);
		result.setRaceDate(c.getTime());
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
		RunnerResult anotherResult = aResult(forWho);
		pastResults.add(anotherResult);
		return pastResults;
	}
	
	//-- Scenario The datasource has a race
	//Given the data-source has a meet in the future
	//And the meet has a race
	//And the race has a runner
	//And the runner has some past results
	public void datasourceHasARace() {
		datasourceHasARace(aRace());
	}
	
	public void datasourceHasARace(Race aRace) {
		//Given the data-source has a meet in the future
		Meeting meet = aMeet();
		datasource.addMeet(meet);
		//And the meet has a race
		datasource.addRace(aMeet(), aRace);
		//And the race has a runner
		datasource.addRunner(aRace, aRunner());
		//And the runner has some past results
		datasource.addPastResults(aRunner(), somePastResults(aRunner()));
	}

	
	//--- Scenario a race is imported
	//Given the datasource has a race
	//And the upcoming races are imported
	//When the list of upcoming races is fetched
	//Then the race should be in the list
	@Test
	public void testImportRaces() throws Exception {
		//Given the data-source has a race
		datasourceHasARace();
		//And the upcoming races are imported
		ImportUpcomingRacesServlet is = new ImportUpcomingRacesServlet();
		is.service(null, null);
		//When the list of upcoming races is fetched
		List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchUpcomingMeets();
		//Then the race should be in the list
		Assert.assertTrue(meets.contains(aMeet()));
	}
	
	//--- Scenario fetch races without histories
	//Given a race
	//And the race has a runner
	//And the race is imported
	//When the races without histories are fetched
	//Then the race should be in the list
	@Test
	public void testFetchRacesWithoutHistories() throws Exception {
		//Given a race is imported
		testImportRaces();
		//When the races without histories are fetched
		ImportBusiness importer = new ImportBusiness();
		List<Race> races = importer.fetchRacesWithoutHistories();
		//Then the race should be in the list
		Assert.assertTrue(races.contains(aRace()));		
	}

	//--- Scenario the histories for a race are imported
	//Given a race is imported
	//And the race has a runner
	//When the race histories are imported
	//And the races without histories are fetched
	//Then the race should NOT be in the list
	@Test
	public void testImportHistories() throws Exception {
		//Given a race is imported
		testImportRaces();
		//When the race histories are imported
		ImportBusiness importer = new ImportBusiness();
		importer.importRace(aRace(), true);
		//And the races without histories are fetched
		List<Race> races = importer.fetchRacesWithoutHistories();
		//Then the race should NOT be in the list
		Assert.assertFalse(races.contains(aRace()));		
	}
	
	//Given the histories for a race are imported
	//And the races are re-imported
	//When the races without histories are fetched
	//Then the race should NOT be in the list
	@Test
	public void testRetryImportHistories() throws Exception {
		//Given the histories for a race are imported
		testImportHistories();
		ImportBusiness importer = new ImportBusiness();
		//And the race is imported
		importer.importRace(aRace(), false);
		//When the races without histories are fetched
		List<Race> races = importer.fetchRacesWithoutHistories();
		//Then the race should NOT be in the list
		Assert.assertFalse(races.contains(aRace()));
	}
	
	//Given a race is imported
	//And the race histories are imported
	//When the horse is fetched
	//Then the horse should contain the past results
	@Test
	public void testRunnerHistoriesAreImported() throws Exception {
		//MockSpringDataSource ds = (MockSpringDataSource)SpringRacingServices.getSpringRacingDataSource();
		
		//Given a race is imported
		testImportRaces();
		//And the race histories are imported
		ImportBusiness importer = new ImportBusiness();
		importer.importRace(aRace(), true);
		//When the horse is fetched
		Horse o = SpringRacingServices.getSpringRacingDAO().fetchHorse(aRunner().getHorse());
		//Then the horse should contain the past results
		Assert.assertTrue(o.getPastResults().contains(aResult(aRunner())));
	}
	
	//Given a race is imported
	//And the race histories are imported
	//When the horse is fetched
	//Then the horse stats should be calculated
	//And the race stats should be calculated
	@Test
	public void testRunnerStatsAreCalculated() throws Exception {
		//MockSpringDataSource ds = (MockSpringDataSource)SpringRacingServices.getSpringRacingDataSource();
		
		//Given a race is imported
		testImportRaces();
		//And the race histories are imported
		ImportBusiness importer = new ImportBusiness();
		importer.importRace(aRace(), true);
		//When the horse is fetched
		Horse o = SpringRacingServices.getSpringRacingDAO().fetchHorse(aRunner().getHorse());
		//Then the horse stats should be calculated
		Assert.assertTrue(o.getNumberOfRaces() > 0);
		Assert.assertTrue(o.getSpell() > 0);
		//And the race stats should be calculated
		Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(aRace().getRaceCode());
		Assert.assertTrue(race.getNumberOfRunnersLessThan3Races() == 1);
	}
	
	//--- Scenario probabilities are generated
	//Given the histories for a race are imported
	//And the probabilities for the race have been calculated
	//And the runner has a probability of 1
	//When the probabilities for the race are fetched
	//Then the runner should have a probability of 1
	//And the mean should be 1
	//And the Standard Deviation should be 1
	@Test
	public void testGenerateProbabilities() throws Exception {
		//Given the histories for a race are imported
		testImportHistories();
		//And the runner has a probability of 1
		Simulator simulator = new MockSimulator(1);
		SpringRacingServices.setSimulator(simulator);
		//And the probabilities for the race have been calculated
		ProbabilityBusiness probabilities = new ProbabilityBusiness();
		probabilities.generate(aRace());
		//When the probabilities for the race are fetched	
		List<Runner> runners = SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(aRace());
		//Then the runner should have a probability of 1
		Assert.assertEquals(1, runners.get(0).getProbability().getWin(), 0.1);
		//And the mean should be 1
		//Assert.assertEquals(1, runners.get(0).getProbability().getMean(), 0.1);
		//And the Standard Deviation should be 1
		//Assert.assertEquals(1, runners.get(0).getProbability().getStandardDeviation(), 0.1);
	}

	//Given the probabilities for the race have been calculated
	//And the punts are generated
	//When the punts for the race are fetched
	//Then the confidence of the punts should be available
	@Test
	public void testConfidence() throws Exception {
		//Given the probabilities for the race have been calculated
		testGenerateProbabilities();
		Race race = aRace();
		race.setRunners(SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(aRace()));
		//And the punts are generated
		PuntingBusiness pb = new PuntingBusiness();
		pb.generate(race);
		//When the probabilities for the race are fetched
		List<Punt> punts = SpringRacingServices.getPuntingDAO().fetchPuntsForRace(race);
		//Then the confidence of the punts should be available
		for (Punt punt : punts) {
			Assert.assertNotNull(punt.getConfidence());
		}
	}
	
	//Given the probabilities for the race have been calculated
	//When the punts are generated
	//Then the bets should be placed
	@Test
	public void testGeneratePunts() throws Exception {
		PuntingBusiness punt = new PuntingBusiness();
		List<Punt> punts = generatePunts();
		
		List<Punt> open = punt.fetchPuntsForMeet(aMeet());
		Assert.assertTrue(open.contains(punts.get(0)));
	}
	
	private List<Punt> generatePunts() throws Exception {
		testGenerateProbabilities();
		
		PuntingBusiness punt = new PuntingBusiness();
		return punt.generate();
	}
	
	//Given a race is in the past 
	//And the datasource has the race
	//And the race histories are imported
	//And the probabilities for the race have been calculated
	//When the punts are generated
	//Then there should be no punts on the race
	@Test
	public void testDontGeneratePuntsOnOldRaces() throws Exception {
		//Given a race is in the past
		String myOldRaceCode = "OLDRACECODE";
		Race aRaceInThePast = aRace();
		aRaceInThePast.setRaceCode(myOldRaceCode);
		aRaceInThePast.setDate(new Date(System.currentTimeMillis() - 1000));
		//And the datasource has the race
		datasourceHasARace(aRaceInThePast);
		//And the race histories are imported
		ImportBusiness importer = new ImportBusiness();
		importer.importRace(aRaceInThePast, true);
		//And the probabilities for the race have been calculated
		ProbabilityBusiness probabilities = new ProbabilityBusiness();
		probabilities.generate(aRaceInThePast);
		//When the punts are generated
		List<Punt> punts = generatePunts();
		//Then there should be no punts on the race
		for (Punt punt : punts) {
			if (myOldRaceCode.equals(punt.getRaceCode())) {
				Assert.fail("Punt generated for " + myOldRaceCode);
			}
		}
	}
	
	//Given a race has results
	//And the datasource has the race
	//And the datasource has the race result
	//And the race histories are imported
	//And the probabilities for the race have been calculated
	//When the punts are generated
	//Then there should be no punts on the race
	@Test
	public void testDontGeneratePuntsOnRacesWithResults() throws Exception {
		//Given a race is in the past
		String myResultsRaceCode = "RESULTSRACECODE";
		Race aRaceInThePast = aRace();
		aRaceInThePast.setRaceCode(myResultsRaceCode);
		aRaceInThePast.setResult(new int[]{1});
		//And the datasource has the race
		datasourceHasARace(aRaceInThePast);
		//And the datasource has the race result
		datasource.addRaceResult(aRaceInThePast);
		//And the race histories are imported
		ImportBusiness importer = new ImportBusiness();
		importer.importRace(aRaceInThePast, true);
		//And the probabilities for the race have been calculated
		ProbabilityBusiness probabilities = new ProbabilityBusiness();
		probabilities.generate(aRaceInThePast);
		//When the punts are generated
		List<Punt> punts = generatePunts();
		//Then there should be no punts on the race
		for (Punt punt : punts) {
			if (myResultsRaceCode.equals(punt.getRaceCode())) {
				Assert.fail("Punt generated for " + myResultsRaceCode);
			}
		}
	}
	
	//Given a punt has been generated
	//And a bets has been placed for the punt
	//When the open stakes are fetched
	//Then the punt should have a stake
	//And the account should be debited
	@Test
	public void testPlaceBets() throws Exception {
		//Given a punt has been generated
		List<Punt> punts = generatePunts();

		//And a bets has been placed for the punt
		BettingBusiness bet = new BettingBusiness();		
		bet.placeBets(punts);
		
		//When the stakes for the meet are fetched
		PuntingBusiness pbiz = new PuntingBusiness();
		Punt punt = pbiz.fetchPuntsForMeet(aMeet()).get(0);		
		List<Stake> stakes = bet.getExistingOpenStakesForPunt(punt);
		
		//Then the punt should have a stake
		Assert.assertTrue(stakes.size() > 0);
		Assert.assertTrue(bet.fetchAccountAmount(ObjectifyGoodPuntsBookieImpl.ACCOUNT) == 9900);
		
		//return punt;
	}
	
	
	//Place multiple bets on the same punt
	
	
	//-- Settle Stakes
	//Given a punt has a stake
	//And the punt is successful
	//When the results are fetched
	//And the stakes are settled
	//Then the stakes should be settled 
	//And the account should be credited
	//And the account amount should equal the sum of the stakes
	@Test
	public void testSettleStakes() throws Exception {
		BettingBusiness bbiz = new BettingBusiness();
		
		//Given a punt has a stake of 100 @ $10
		testPlaceBets();
		MockSpringDataSource ds = (MockSpringDataSource)SpringRacingServices.getSpringRacingDataSource();
		double original = bbiz.fetchAccountAmount(ObjectifyGoodPuntsBookieImpl.ACCOUNT);
		//And the punt is successful
		Race aRace = aRace();
		aRace.setResult(new int[]{1});
		ds.addRaceResult(aRace);
		//When the results are fetched
		ImportBusiness importer = new ImportBusiness();
		importer.importRaceResults();
		//And the stakes are settled
		bbiz.settleBets();
		//Then the stakes should be settled 
		List<Stake> settled = SpringRacingServices.getPuntingDAO().fetchSettledStakes();
		double sumOfStakes = 0;
		for (Stake stake : settled) {
			Assert.assertTrue(stake.isSettled());
			sumOfStakes += stake.getReturn();
		}
		//And the account should be credited $1000
		double finalA = bbiz.fetchAccountAmount(ObjectifyGoodPuntsBookieImpl.ACCOUNT);
		Assert.assertEquals(1000, finalA - original, 0.01);
		//And the account amount should equal the sum of the stakes
		Assert.assertEquals(1000, sumOfStakes, 0.01);		
	}
	
	
	//Alarm when a bookie settles wrong
	
	
}
