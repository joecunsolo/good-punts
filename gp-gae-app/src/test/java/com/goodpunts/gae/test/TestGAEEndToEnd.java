package com.goodpunts.gae.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.guestbook.OfyHelper;
import com.goodpunts.gae.test.mock.MockSimulator;
import com.goodpunts.gae.test.mock.MockSpringDataSource;
import com.goodpunts.servlet.GeneratePuntsServlet;
import com.goodpunts.servlet.ImportUpcomingRacesServlet;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.Simulator;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class TestGAEEndToEnd {
	
	public static final String KEY_RACECODE = "race1";
	public static final String KEY_PASTRACECODE = "pastrace1";
	public static final String KEY_MEETCODE = "meet1";
	public static final String KEY_HORSECODE = "horse1";
	
	private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(
		      new LocalDatastoreServiceTestConfig());
	private static Closeable closeable;
	
	@BeforeClass
	public static void setUpClass() {
		helper.setUp();
		closeable = ObjectifyService.begin();
		OfyHelper.init();
	}

	@AfterClass 
	public static void tearDownClass() {
		helper.tearDown();
		closeable.close();
	}
	
	private static Race aRace() {
		Race race = new Race();
		race.setRaceCode(KEY_RACECODE);
		race.setMeetCode(KEY_MEETCODE);
		race.setDate(new Date());
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
		runner.setOdds(new Odds());
		return runner;
	}
	
	private static List<RunnerResult> somePastResults(Runner forWho) {
		List<RunnerResult> pastResults = new ArrayList<RunnerResult>();
		RunnerResult aResult = aResult(forWho);
		pastResults.add(aResult);
		return pastResults;
	}
	
	//-- Scenario The datasource has a race
	//Given the data-source has a meet in the future
	//And the meet has a race
	//And the race has a runner
	//And the runner has some past results
	public void datasourceHasARace() {
		//Given the data-source has a meet in the future
		Meeting meet = aMeet();
		MockSpringDataSource ds = new MockSpringDataSource();
		SpringRacingServices.setSpringRacingDataSource(ds);
		ds.addMeet(meet);
		//And the meet has a race
		ds.addRace(aMeet(), aRace());
		//And the race has a runner
		ds.addRunner(aRace(), aRunner());
		//And the runner has some past results
		ds.addPastResults(aRunner(), somePastResults(aRunner()));
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
	
	//--- Scenario the histories for a race are imported
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
	
	//--- Scenario probabilities are generated
	//Given the histories for a race are imported
	//And the probabilities for the race have been calculated
	//And the runner has a probability of 1
	//When the probabilities for the race are fetched
	//Then the runner should have a probability of 1
	@Test
	public void testGenerateProbabilities() throws Exception {
		//Given the histories for a race are imported
		testImportHistories();
		//And the runner has a probability of 1
		Simulator simulator = new MockSimulator(1);
		SpringRacingServices.setSimulator(simulator);
		//And the probabilities for the race have been calculated
		ProbabilityBusiness probabilities = new ProbabilityBusiness();
		probabilities.generateProbabilitiesForRace(aRace());
		//When the probabilities for the race are fetched	
		List<Runner> runners = SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(aRace());
		//Then the runner should have a probability of 1
		Assert.assertEquals(1, runners.get(0).getProbability().getWin(), 0.1);
	}
	
	//Given the histories for a race are imported
	//And the probabilities for the race have been calculated
	//When the punts are generated
	//Then the bets should be placed
	@Test
	public void testGeneratePunts() {
		
//		GeneratePuntsServlet gs = new GeneratePuntsServlet();
//		gs.service(null, null);
		
	}
}
