package com.goodpunts.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.joe.springracing.business.ImportBusiness;

/**
 * Using the data source defined in {@link #SpringRacingServices} import all the race results
 * 
 * @author joe.cunsolo
 *
 */
public class ImportRaceResultsServlet extends GenericServlet {

	private static final long serialVersionUID = -5725479682605823516L;

	public static final String URL = "/race-results/import";

	public static final String KEY_RACECODE = "racecode";

	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		String raceCode = req.getParameter(KEY_RACECODE);

		ImportBusiness importer = new ImportBusiness();
		importer.importRaceResults(raceCode);
	}

}
