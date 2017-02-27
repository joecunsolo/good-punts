package com.joe.springracing.test;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.objects.Race;
import com.joe.springracing.test.mock.MockRacingDao;
import com.joe.springracing.test.mock.MockSpringDataSource;

import junit.framework.TestCase;

public class TestImport extends TestCase {
	
	/**
	 * Given a Runner does not exist in Good Punts
	 * When there is a request to import the horses history
	 * Then the horse should be added to the database
	 * And the horse's history imported
	 * @throws Exception 
	 */
	public static void testImportNullHorse() throws Exception {
		String horseCode = "not-in-db";
		SpringRacingServices.setSpringRacingDataSource(new MockSpringDataSource());
		ImportBusiness importer = new ImportBusiness(new MockRacingDao());
		importer.importRunner(horseCode, true, true);	
	}
	

	/**
	 * Given a Races histories have been imported
	 * And the race histories are imported successfully
	 * When the histories are imported
	 * Then the horses histories should NOT be imported
	 */

	/**
	 * Given a Race has been imported
	 * And the race histories have not been imported
	 * And a horses histories were imported
	 * When the histories are imported
	 * Then the successful horses histories should NOT be imported
	 * @throws Exception 
	 */
	public static void testRetryFailedRace() throws Exception {
		String horseNotImported = "not-imported";
		String horseImported = "imported";
		
		//Given a Race has been imported
		Race race = new Race();
		race.setName("failed import");
		race.setVenue("mixed result");
		//And the race histories have not been imported
		race.setHistories(false);
		MockRacingDao dao = new MockRacingDao();
		dao.setRacesWithoutHistories(race);
		//And a horses histories were imported
		
		ImportBusiness importer = new ImportBusiness(dao);
		importer.importRace(race, true);
	}
	
	/**
	 * Given a Races histories have not been imported
	 * And a horses histories were imported previously
	 * When the histories are imported
	 * Then the horses histories should be imported
	 */

	
	//	
//	/**
//	 * Given a race is in the datasource
//	 * When I fetch the list of races
//	 * Then the race should be in the list
//	 * @throws Exception 
//	 */
//	public static void testImportRace() throws Exception {
//		Race race = new Race();
//		race.setRaceCode("test");
//		MockSpringDataSource mock = new MockSpringDataSource();
//		mock.addRace(race);
//		SpringRacingServices.setSpringRacingDataSource(mock);
//		
//		Importer imp = new Importer();
//		Assert.assertTrue(imp.importUpcomingRaces().size() == 1);
//
//		SpringRacingDAO dao = new MockRacingDao();
//		SpringRacingServices.setSpringRacingDAO(dao);
//		ImportBusiness business = new ImportBusiness(dao);
//		business.importUpcomingRaces(false);
//	}
//
//	/**
//	 * Given a race is in the datasource
//	 * And the race is NOT in the database
//	 * When I fetch the list of races without histories
//	 * Then the race should be in the list
//	 * @throws Exception 
//	 */
//	public static void testASynchImportNewData() throws Exception {
//		testImportRace();
//		
//		SpringRacingDAO dao = SpringRacingServices.getSpringRacingDAO();
//		
//		ImportBusiness business = new ImportBusiness(dao);
//		List<Race> races = business.fetchRacesWithoutHistories();
//		Assert.assertTrue(races.size() == 1);
//	}
//
//	/**
//	 * Given a race is in the datasource
//	 * And the race is in the database
//	 * And the race histories have NOT been loaded
//	 * When I fetch the list of races without histories
//	 * Then the race should be in the list
//	 * @throws Exception 
//	 */
//	public static void testASynchImportExistingData() throws Exception {		
//		testImportRace();
//		testImportRace();
//		
//		SpringRacingDAO dao = SpringRacingServices.getSpringRacingDAO();
//		
//		ImportBusiness business = new ImportBusiness(dao);
//		List<Race> races = business.fetchRacesWithoutHistories();
//		Assert.assertTrue(races.size() == 1);
//	}
//
//	/**
//	 * Given a race is in the datasource
//	 * And the race is in the database
//	 * And the race histories have  been loaded
//	 * When I fetch the list of races without histories
//	 * Then the race should NOT be in the list
//	 * @throws Exception 
//	 */
//	public static void testASynchImportExistingDataNoHistory() throws Exception {
//		testImportRace();
//		
//		SpringRacingDAO dao = SpringRacingServices.getSpringRacingDAO();
//		
//		ImportBusiness business = new ImportBusiness(dao);
//		List<Race> races = business.fetchRacesWithoutHistories();
//		business.importRace(races.get(0), true);
//		
//		List<Race> races2 = business.fetchRacesWithoutHistories();
//		Assert.assertTrue(races2.size() == 0);
//	}
//
}
