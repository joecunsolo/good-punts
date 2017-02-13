package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.importer.Importer;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class ImportBusiness extends AbstractSpringRacingBusiness {

	public ImportBusiness(SpringRacingDAO dao) {
		super(dao, new PrintWriter(System.out));
	}
	
	public void importUpcomingRaces(boolean histories) {
		try {
			Importer importer = new Importer();
			List<Meeting> meets = importer.importUpcomingMeets();	
			for (Meeting meet : meets) {
				getSpringRacingDAO().storeMeet(meet);
				
				importRaces(meet.getRaces(), histories);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void importRaces(List<Race> races, boolean histories) throws Exception {
		for (Race race : races) {			
			importRace(race, histories);
		}
	}
	
	public void importRace(Race race, boolean histories) throws Exception {
		getWriter().println(race.getRaceNumber() + " " + race.getName() + " " + race.getVenue());
		getWriter().flush();

		Importer importer = new Importer();
		List<Runner> runners = importer.fetchRunners(race);
		race.setRunners(runners);
		if (runners != null) {
			//race.setHistories(histories);
			boolean newRace = getSpringRacingDAO().fetchRace(race.getRaceCode()) == null;

			getSpringRacingDAO().storeRace(race);
			importRunners(runners, histories, newRace);
		}		
	}

	private void importRunners(List<Runner> runners, boolean histories, boolean newRace) throws Exception {
		Importer importer = new Importer();
		for (Runner runner : runners) {
			Horse horse = importer.fetchHorse(runner);
			horse.setHistories(!newRace && horse.hasHistories());
			getSpringRacingDAO().storeHorse(horse);

			importRunner(horse, histories, newRace);
		}
	}
		
	/**
	 * Import the runner from the {@link Importer}
	 * @param runner a horse in the race to import
	 * @param histories do we want to import the histories as well?
	 * @param newRace have we imported this race before?
	 * @throws Exception when importing the runner fails
	 */
	public void importRunner(String horseCode, boolean histories, boolean newRace) throws Exception {
		Horse horse = getSpringRacingDAO().fetchHorse(horseCode);
		if (horse == null) {
			horse = fetchHorse(horseCode);
		}
		importRunner(horse, histories, newRace);
	}
		
	private Horse fetchHorse(String horseCode) throws Exception {
		Horse result = new Horse();
		result.setCode(horseCode);
		result.setName(horseCode);
		result.setId(horseCode);
		
		return result;
	}
	
	/**
	 * Import the runner from the {@link Importer}
	 * @param runner a horse in the race to import
	 * @param histories do we want to import the histories as well?
	 * @param newRace have we imported this race before?
	 * @throws Exception when importing the runner fails
	 */
	public void importRunner(Horse horse, boolean histories, boolean newRace) throws Exception {
		Importer importer = new Importer();	
		//If this is a new race so this horse doesn't have any history
		//Or it isn't a new race but the horse doesn't have any history
		horse.setHistories(!newRace && horse.hasHistories());
		getWriter().println(horse.getName());
		getWriter().flush();
		
		//Import the horse History
		if (histories) {
			List<RunnerResult> results = importer.fetchPastResults(horse);
			getSpringRacingDAO().storeResults(results);			
			//We've just successfully imported the history of this horse
			horse.setHistories(true);
		}
		getSpringRacingDAO().storeHorse(horse);
	}

	public void importRaceResults() {
		Importer importer = new Importer();
		
		try {
			List<Race> races = getSpringRacingDAO().fetchRacesWithoutResults();
			for (Race race : races) {
				getWriter().println();
				getWriter().println(race.getVenue() + " "  + race.getRaceNumber() + " " + race.getDate());
				getWriter().flush();
				
				int[] result = importer.importRaceResults(race);
				race.setResult(result);
				
				getSpringRacingDAO().storeRace(race);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Just imports the horse histories */
	@Deprecated
	public void importExistingMeets() {
		Importer importer = new Importer();

		try {
			List<Meeting> meets = getSpringRacingDAO().fetchExistingMeets();
			
			for (Meeting meeting : meets) {
				getWriter().println();
				getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
				
				importer.importExistingMeet(meeting);
				getSpringRacingDAO().storeMeet(meeting);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Race> fetchRacesWithoutHistories() {
		return getSpringRacingDAO().fetchRacesWithoutHistories();
	}

	public List<Runner> fetchRunnerWithoutHistories() {
		try {
			return getSpringRacingDAO().fetchRunnersWithoutHistories();
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch runner histories", e);
		}
	}
	
//	public void migrateFromFileV1() {
//		List<Meeting> meets = SpringRacingServices._getSpringRacingDAOV1().fetchExistingMeets(true, true, true);
//		SpringRacingServices._getSpringRacingDAOV1().storeMeets(meets);	
//	}
	
}
