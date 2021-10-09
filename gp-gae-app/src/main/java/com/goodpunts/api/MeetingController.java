package com.goodpunts.api;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.business.MeetBusiness;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.RaceBusiness;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
	
	@RequestMapping(method = RequestMethod.GET, headers="Accept=application/json")
    public List<Meeting> meets(@RequestParam(value="period", defaultValue="upcoming") String period) {
		MeetBusiness business = new MeetBusiness();
		return business.fetchUpcomingMeets();
    }
	
	@RequestMapping (value = "/{meet}", method = RequestMethod.GET, headers="Accept=application/json")
	public Meeting meet(@PathVariable String meet) {
		MeetBusiness business = new MeetBusiness();
		return business.fetchMeet(meet);
	}
	
	@RequestMapping (value = "/{meet}/races", method = RequestMethod.GET, headers="Accept=application/json")
	public List<Race> racesForMeet(@PathVariable String meet) {
		MeetBusiness business = new MeetBusiness();
		Meeting meeting = business.fetchMeet(meet);
		return business.fetchRacesForMeet(meeting);
	}
	
	@RequestMapping (value = "/{meet}/probabilities/generate", method = RequestMethod.GET, headers="Accept=application/json")
	public void generateProbabilities(@PathVariable String meet) throws Exception {
		MeetBusiness meetBusiness = new MeetBusiness();
		Meeting oMeet = meetBusiness.fetchMeet(meet);
		List<Race> races = meetBusiness.fetchRacesForMeet(oMeet);

		ProbabilityBusiness business = new ProbabilityBusiness();
		business.generate(races);
	}
	
	@RequestMapping (value = "/{meet}/import", method = RequestMethod.GET, headers="Accept=application/json")
	public void importRace(@PathVariable String meet, @RequestParam(value="histories", defaultValue="false") boolean histories) throws Exception {
		MeetBusiness meetBusiness = new MeetBusiness();
		Meeting oMeet = meetBusiness.fetchMeet(meet);
		
		List<Race> races = meetBusiness.fetchRacesForMeet(oMeet);
		
		for (Race race : races) {
			ImportBusiness importer = new ImportBusiness();
			importer.importRace(race, histories);
		}
		
	}
}
