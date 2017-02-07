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
			race.setHistories(histories);
			getSpringRacingDAO().storeRace(race);

			importRunners(runners, histories);
		}		
	}

	private void importRunners(List<Runner> runners, boolean histories) throws Exception {
		Importer importer = new Importer();
		for (Runner runner : runners) {
			Horse horse = importer.fetchHorse(runner);
			getWriter().println(runner.getNumber() + " " + horse.getName());
			getWriter().flush();

			getSpringRacingDAO().storeHorse(horse);
			
			if (histories) {
				List<RunnerResult> results = importer.fetchPastResults(horse);
				getSpringRacingDAO().storeResults(results);			
			}
		}
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
	
//	public void migrateFromFileV1() {
//		List<Meeting> meets = SpringRacingServices._getSpringRacingDAOV1().fetchExistingMeets(true, true, true);
//		SpringRacingServices._getSpringRacingDAOV1().storeMeets(meets);	
//	}
	
}
