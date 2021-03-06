package com.goodpunts.api;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.RaceBusiness;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

@RestController
@RequestMapping("/races")
public class RaceController {


	@RequestMapping (value = "/", method = RequestMethod.GET, headers="Accept=application/json")
	public List<Race> races(
			@RequestParam(value="results", defaultValue="false") boolean results, 
			@RequestParam(value="splits", defaultValue="false") boolean splits, 
			@RequestParam(value="from", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
			@RequestParam(value="to", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
		
		RaceBusiness business = new RaceBusiness();
		return business.fetchRaces(results, splits, from, to, true);
	}
	
//	@RequestMapping (value = "/", method = RequestMethod.GET, headers="Accept=application/json")
//	public List<Race> racesFromResults(
//			@RequestParam(value="results", defaultValue="false") boolean results, 
//			@RequestParam(value="from", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
//			@RequestParam(value="to", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
//		
//		RaceBusiness business = new RaceBusiness();
//		return business.fetchRacesWithResult(results, from, to, true);
//	}
//	
//	
//	@RequestMapping (value = "/", method = RequestMethod.GET, headers="Accept=application/json")
//	public List<Race> racesFromSplits(
//			@RequestParam(value="splits", defaultValue="false") boolean splits, 
//			@RequestParam(value="from", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
//			@RequestParam(value="to", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
//		
//		RaceBusiness business = new RaceBusiness();
//		return business.fetchRacesWithSplits(splits, from, to, true);
//	}
	
	@RequestMapping (value = "/{race}", method = RequestMethod.GET, headers="Accept=application/json")
	public Race race(@PathVariable String race) {
		RaceBusiness business = new RaceBusiness();
		return business.fetchRace(race);
	}
	
	@RequestMapping (value = "/{race}/probabilities", method = RequestMethod.GET, headers="Accept=application/json")
	public List<Runner> probabilities(@PathVariable String race) {
		RaceBusiness meetBusiness = new RaceBusiness();
		Race oRace = meetBusiness.fetchRace(race);
		
		ProbabilityBusiness business = new ProbabilityBusiness();
		return business.fetchProbabilitiesForRace(oRace);
	}

	@RequestMapping (value = "/{race}/probabilities/generate", method = RequestMethod.GET, headers="Accept=application/json")
	public void generateProbabilities(@PathVariable String race) throws Exception {
		RaceBusiness meetBusiness = new RaceBusiness();
		Race oRace = meetBusiness.fetchRace(race);
		
		ProbabilityBusiness business = new ProbabilityBusiness();
		business.generate(oRace);
	}
	
	@RequestMapping (value = "/{race}/import", method = RequestMethod.GET, headers="Accept=application/json")
	public void importRace(@PathVariable String race, @RequestParam(value="histories", defaultValue="false") boolean histories) throws Exception {
		RaceBusiness meetBusiness = new RaceBusiness();
		Race oRace = meetBusiness.fetchRace(race);
		
		ImportBusiness importer = new ImportBusiness();
		importer.importRace(oRace, histories);
	}
}
