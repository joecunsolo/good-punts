package com.joe.springracing;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.importer.Importer;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.utils.io.html.HTMLReaderIO;


/**
 * Get all the horses and then import their histories from racing.com
 * @author joecunsolo
 *
 */
public class ImportData {
	
	public static final String HORSES_URL = "http://good-punts.appspot.com/api/horses/";
	
	public static void main(String[] args) throws Exception {
		List<Horse> listHorse = fetchListOfHorses();
		Importer importer = new Importer();

		for (Horse horse : listHorse) {
			List<RunnerResult> results = importer.fetchPastResults(horse);
			SpringRacingServices.getSpringRacingDAO().storeResults(results);	
		}
		
		
	}
	
	private static List<Horse> fetchListOfHorses() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		String url = buildHorsesURL(false, false);
		String html = HTMLReaderIO.getHTML(url);
		List<Horse> listHorse = objectMapper.readValue(html, new TypeReference<List<Horse>>(){});
		System.out.println(listHorse.size());
		
		url = buildHorsesURL(true, false);
		html = HTMLReaderIO.getHTML(url);
		listHorse.addAll((List<Horse>) objectMapper.readValue(html, new TypeReference<List<Horse>>(){}));
		System.out.println(listHorse.size());
		
		url = buildHorsesURL(false, true);
		html = HTMLReaderIO.getHTML(url);
		listHorse.addAll((List<Horse>) objectMapper.readValue(html, new TypeReference<List<Horse>>(){}));
		System.out.println(listHorse.size());
		
		url = buildHorsesURL(true, true);
		html = HTMLReaderIO.getHTML(url);
		listHorse.addAll((List<Horse>) objectMapper.readValue(html, new TypeReference<List<Horse>>(){}));
		System.out.println(listHorse.size());
		
		return listHorse;
	}

	public static String buildHorsesURL(boolean splits, boolean results) {
		return HORSES_URL + "?splits=" + splits + "&results=" + results;
	}
}
