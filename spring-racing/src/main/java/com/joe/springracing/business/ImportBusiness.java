package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.importer.Importer;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class ImportBusiness extends AbstractSpringRacingBusiness {

	//The number of milliseconds in a day
	public static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

	public ImportBusiness() {
		super(new PrintWriter(System.out));
	}
	
	public void importUpcomingRaces(boolean histories) {
		try {
			Importer importer = new Importer();
			List<Race> races = importer.importUpcomingRaces();	
			List<Meeting> meets = organiseRacesByMeeting(races);

			for (Meeting meet : meets) {
				SpringRacingServices.getSpringRacingDAO().storeMeet(meet);
			}
			importRaces(races, histories);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Meeting> organiseRacesByMeeting(List<Race> races) {
		List<Meeting> meets = new ArrayList<Meeting>();
		for (Race race : races) {
			Meeting meeting = getMeeting(meets, race);
			if (meeting == null) {
				meeting = new Meeting();
				meets.add(meeting);
			} 
			meeting.addRace(race);
		}
		return meets;
	}
	
	private Meeting getMeeting (List<Meeting> meets, Race race) {
		Meeting meeting = getMeeting(meets, race.getMeetCode());
		if (meeting == null) {
			meeting = new Meeting();
			meeting.setMeetCode(race.getMeetCode());
			meeting.setDate(race.getDate());
			meeting.setVenue(race.getVenue());
			meets.add(meeting);			
		}
		meeting.addRace(race);
		return meeting;
	}

	private Meeting getMeeting(List<Meeting> meets, String meetCode) {
		for (Meeting meet : meets) {
			if (meet.getMeetCode().equals(meetCode)) {
				return meet;
			}
		}
		return null;
	}
	
	private void importRaces(List<Race> races, boolean histories) throws Exception {
		for (Race race : races) {			
			importRace(race, histories);
		}
	}
	
	/**
	 * Imports the race and calculates any meta-data
	 * @param race
	 * @param histories
	 * @throws Exception
	 */
	public void importRace(Race race, boolean histories) throws Exception {
		getWriter().println("Race: " + race.getRaceNumber() + " " + race.getName() + " " + race.getVenue());
		getWriter().flush();

		Importer importer = new Importer();
		List<Runner> runners = importer.fetchRunners(race);
		race.setRunners(runners);
		if (runners != null) {
			Race existing = SpringRacingServices.getSpringRacingDAO().fetchRace(race.getRaceCode());
			if (existing != null) {
				race.setHistories(existing.hasHistories());
			}
			//import the runners
			int lessThan3Races = importRunners(runners, histories, existing == null);			
			race.setHistories(histories || race.hasHistories());
			//calculate the meta-data
			if (histories) {
				race.setNumberOfRunnersLessThan3Races(lessThan3Races);
			}
			SpringRacingServices.getSpringRacingDAO().storeRace(race);
		}	
	}

	/**
	 * Import these runners and returns the number of runners less than 3 races
	 * @param runners the list of runners to import
	 * @param histories do we want to import the histories as well?
	 * @param newRace have we imported this race before?
	 * @return the number of runners with less than 3 races
	 * @throws Exception when one of the imports fails
	 */
	private int importRunners(List<Runner> runners, boolean histories, boolean newRace) throws Exception {
		Importer importer = new Importer();
		int runnersLessThan3 = 0;
		for (Runner runner : runners) {
			Horse horse = null;
			//Get the horse we have with it's meta-data
			if (!newRace) {
				horse = SpringRacingServices.getSpringRacingDAO().fetchHorse(runner.getHorse());
			} 
			//Don't have the horse? Let's import it.
			if (horse == null) {
				horse = importer.fetchHorse(runner);
			}

			//import the runner
			int races = importRunner(horse, histories, newRace);
			if (races < 3 && histories) {
				runnersLessThan3++;
			}
		}
		return runnersLessThan3;
	}
		
	/**
	 * Import the runner from the {@link Importer}
	 * @param runner a horse in the race to import
	 * @param histories do we want to import the histories as well?
	 * @param newRace have we imported this race before?
	 * @throws Exception when importing the runner fails
	 */
//	public void importRunner(String horseCode, boolean histories, boolean newRace) throws Exception {
//		Horse horse = SpringRacingServices.getSpringRacingDAO().fetchHorse(horseCode);
//		if (horse == null) {
//			horse = fetchHorse(horseCode);
//		}
//		importRunner(null, horse, histories, newRace);
//	}
		
//	private Horse fetchHorse(String horseCode) throws Exception {
//		Horse result = new Horse();
//		result.setCode(horseCode);
//		result.setName(horseCode);
//		result.setId(horseCode);
//		
//		return result;
//	}
	
	/**
	 * Import the runner from the {@link Importer}
	 * Also adds any meta-data for the runner
	 * @param runner a horse in the race to import
	 * @param histories do we want to import the histories as well?
	 * @param newRace have we imported this race before?
	 * @return the number of races this horse has run
	 * @throws Exception when importing the runner fails
	 */
	public int importRunner(Horse horse, boolean histories, boolean newRace) throws Exception {
		Importer importer = new Importer();	
		//If this is a new race so this horse doesn't have any history
		//Or it isn't a new race but the horse doesn't have any history
		horse.setHistories(!newRace && horse.hasHistories());
		getWriter().println(horse.getName());
		getWriter().flush();
		
		//Import the horse History
		int races = 0;
		if (histories) {
			List<RunnerResult> results = importer.fetchPastResults(horse);
			SpringRacingServices.getSpringRacingDAO().storeResults(results);			
			//We've just successfully imported the history of this horse
			horse.setHistories(true);
			//add some meta-data about the horse			
			horse.setNumberOfRaces(results.size());
			System.out.println("Races: " + horse.getNumberOfRaces());
			horse.setSpell(calculateSpell(results));
			System.out.println("Spell: " + horse.getSpell());
			races = results.size();
		}
		SpringRacingServices.getSpringRacingDAO().storeHorse(horse);
		return races;
	}

	//How long since this horse last raced in days
	public int calculateSpell(List<RunnerResult> results) {
		Collections.sort(results, new Comparator<RunnerResult>() {
			public int compare(RunnerResult o1, RunnerResult o2) {
				if (o1.getRaceDate() == null && o2.getRaceDate() == null) {
					return 0;
				}
				if (o1.getRaceDate() == null) {
					return 1;
				}
				if (o2.getRaceDate() == null) {
					return -1;
				}
				return o2.getRaceDate().compareTo(o1.getRaceDate());
			}});
		if (results.size() > 0 &&
				results.get(0).getRaceDate() != null) {
			long timeSinceLastRace = System.currentTimeMillis() - results.get(0).getRaceDate().getTime();
			return (int)(timeSinceLastRace / MILLIS_IN_A_DAY);			
		}
		return 0;
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
			throw new RuntimeException("Unable to import race results for " + raceCode, e);
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
//	@Deprecated
//	public void importExistingMeets() {
//		Importer importer = new Importer();
//
//		try {
//			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchExistingMeets();
//			
//			for (Meeting meeting : meets) {
//				getWriter().println();
//				getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
//				
//				importer.importExistingMeet(meeting);
//				SpringRacingServices.getSpringRacingDAO().storeMeet(meeting);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

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
