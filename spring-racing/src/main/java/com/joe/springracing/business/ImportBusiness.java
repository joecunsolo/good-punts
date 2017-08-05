package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.importer.Importer;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Punt.State;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class ImportBusiness extends AbstractSpringRacingBusiness {

	public ImportBusiness() {
		super(new PrintWriter(System.out));
	}
	
	public void importUpcomingRaces(boolean histories) {
		try {
			Importer importer = new Importer();
			List<Meeting> meets = importer.importUpcomingMeets();	
			for (Meeting meet : meets) {
				SpringRacingServices.getSpringRacingDAO().storeMeet(meet);
				
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
			Race existing = SpringRacingServices.getSpringRacingDAO().fetchRace(race.getRaceCode());
			if (existing != null) {
				race.setHistories(existing.hasHistories());
			}
			importRunners(runners, histories, existing == null);			
			race.setHistories(histories || race.hasHistories());
			SpringRacingServices.getSpringRacingDAO().storeRace(race);
		}	
	}

	private void importRunners(List<Runner> runners, boolean histories, boolean newRace) throws Exception {
		Importer importer = new Importer();
		for (Runner runner : runners) {
			Horse horse = importer.fetchHorse(runner);
//			horse.setHistories(!newRace && horse.hasHistories());
//			getSpringRacingDAO().storeHorse(horse);
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
		Horse horse = SpringRacingServices.getSpringRacingDAO().fetchHorse(horseCode);
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
			SpringRacingServices.getSpringRacingDAO().storeResults(results);			
			//We've just successfully imported the history of this horse
			horse.setHistories(true);
		}
		SpringRacingServices.getSpringRacingDAO().storeHorse(horse);
	}

	public void importRaceResults() {
		try {
			List<Race> races = fetchRacesWithoutResults();
			for (Race race : races) {
				importRaceResults(race);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to import race results");
		}
	}
	
	public List<Race> fetchRacesWithoutResults() throws Exception {
		return SpringRacingServices.getSpringRacingDAO().fetchRacesWithoutResults();
	}
	
	public void importRaceResults(String raceCode) {
		try {
			Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
			importRaceResults(race);
		} catch (Exception e) {
			throw new RuntimeException("Unable to import race results for " + raceCode);
		}
	}
	
	public void importRaceResults(Race race) throws Exception {
		Importer importer = new Importer();
		PuntingBusiness punts = new PuntingBusiness();
		
		getWriter().println();
		getWriter().println(race.getVenue() + " "  + race.getRaceNumber() + " " + race.getDate());
		getWriter().flush();
		
		int[] result = importer.importRaceResults(race);
		race.setResult(result);
		SpringRacingServices.getSpringRacingDAO().storeRace(race);
		if (result != null) {
			punts.settlePunts(race);
		}
	}

	/** Just imports the horse histories */
	@Deprecated
	public void importExistingMeets() {
		Importer importer = new Importer();

		try {
			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchExistingMeets();
			
			for (Meeting meeting : meets) {
				getWriter().println();
				getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
				
				importer.importExistingMeet(meeting);
				SpringRacingServices.getSpringRacingDAO().storeMeet(meeting);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Race> fetchRacesWithoutHistories() {
		return SpringRacingServices.getSpringRacingDAO().fetchRacesWithoutHistories();
	}

	public List<Runner> fetchRunnerWithoutHistories() {
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchRunnersWithoutHistories();
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch runner histories", e);
		}
	}
	
//	public void migrateFromFileV1() {
//		List<Meeting> meets = SpringRacingServices._getSpringRacingDAOV1().fetchExistingMeets(true, true, true);
//		SpringRacingServices._getSpringRacingDAOV1().storeMeets(meets);	
//	}
	
}
