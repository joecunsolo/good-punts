package com.joe.springracing.business;

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
		super(dao);
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
		Importer importer = new Importer();
		for (Race race : races) {			
			System.out.println(race.getRaceNumber() + " " + race.getName() + " " + race.getVenue());
			List<Runner> runners = importer.fetchRunners(race);
			race.setRunners(runners);
			getSpringRacingDAO().storeRace(race);
			if (runners != null) {
				importRunners(runners, histories);
			}
		}
	}

	private void importRunners(List<Runner> runners, boolean histories) throws Exception {
		Importer importer = new Importer();
		for (Runner runner : runners) {
			Horse horse = importer.fetchHorse(runner);
			System.out.println(runner.getNumber() + " " + horse.getName());
			getSpringRacingDAO().storeHorse(horse);
			
			if (histories) {
				List<RunnerResult> results = importer.fetchPastResults(horse);
				getSpringRacingDAO().storeResults(results);			
			}
		}
	}

	public void importRaceResults() {
		try {
			List<Meeting> meets = getSpringRacingDAO().fetchExistingMeets();
			for (Meeting meeting : meets) {
				System.out.println();
				System.out.println(meeting.getDate() + " "  + meeting.getVenue());
				
				importRaceResults(meeting);
				getSpringRacingDAO().storeMeet(meeting);
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
				System.out.println();
				System.out.println(meeting.getDate() + " "  + meeting.getVenue());
				
				importer.importExistingMeet(meeting);
				getSpringRacingDAO().storeMeet(meeting);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importRaceResults(Meeting meet) throws Exception {
		Importer importer = new Importer();
		for (Race race : meet.getRaces()) {
			int[] result = importer.importRaceResults(race);
			race.setResult(result);
		}
	}
	
//	public void migrateFromFileV1() {
//		List<Meeting> meets = SpringRacingServices._getSpringRacingDAOV1().fetchExistingMeets(true, true, true);
//		SpringRacingServices._getSpringRacingDAOV1().storeMeets(meets);	
//	}
	
}
