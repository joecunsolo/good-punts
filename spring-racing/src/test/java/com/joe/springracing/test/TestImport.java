//package com.joe.springracing.test;
//
//import java.util.List;
//
//import com.joe.springracing.SpringRacingServices;
//import com.joe.springracing.business.ImportBusiness;
//import com.joe.springracing.dao.SpringRacingDAO;
//import com.joe.springracing.importer.Importer;
//import com.joe.springracing.objects.Race;
//import com.joe.springracing.test.mock.MockRacingDao;
//import com.joe.springracing.test.mock.MockSpringDataSource;
//
//import org.junit.Assert;
//
//import junit.framework.TestCase;
//
//public class TestImport extends TestCase {
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
//}
