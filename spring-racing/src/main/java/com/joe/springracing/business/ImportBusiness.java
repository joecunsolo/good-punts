package com.joe.springracing.business;

import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.importer.RacingDotComImporter;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RaceResult;

public class ImportBusiness {

	public void importUpcomingRaces(boolean histories) {
		try {
			RacingDotComImporter importer = new RacingDotComImporter();
			importer.importUpcomingMeets(histories, SpringRacingServices.getSpringRacingDAO());			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void importRaceResults() {
		try {
			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchMeetsWithoutResults(true, true, false);
			for (Meeting meeting : meets) {
				System.out.println();
				System.out.println(meeting.getDate() + " "  + meeting.getVenue());
				
				importRaceResults(meeting);
				SpringRacingServices.getSpringRacingDAO().storeMeet(meeting);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Just imports the horse histories */
	public void importExistingMeets() {
		RacingDotComImporter importer = new RacingDotComImporter();

		try {
			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchExistingMeets(true, true, false);
			
			for (Meeting meeting : meets) {
				System.out.println();
				System.out.println(meeting.getDate() + " "  + meeting.getVenue());
				
				importer.importExistingMeet(meeting);
				SpringRacingServices.getSpringRacingDAO().storeMeet(meeting);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importRaceResults(Meeting meet) throws Exception {
		RacingDotComImporter importer = new RacingDotComImporter();
		for (Race race : meet.getRaces()) {
			RaceResult result = importer.importRaceResults(race);
			race.setResult(result);
		}
	}
	
	public void migrateFromFileV1() {
		List<Meeting> meets = SpringRacingServices._getSpringRacingDAOV1().fetchExistingMeets(true, true, true);
		SpringRacingServices._getSpringRacingDAOV1().storeMeets(meets);	
	}
	
}
