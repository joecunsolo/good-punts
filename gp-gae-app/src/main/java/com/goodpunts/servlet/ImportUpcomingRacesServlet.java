package com.goodpunts.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.joe.springracing.business.ImportBusiness;

/**
 * Using the data source defined in {@link #SpringRacingServices} import all the upcoming races
 * 
 * @author joe.cunsolo
 *
 */
public class ImportUpcomingRacesServlet extends GenericServlet {

	private static final long serialVersionUID = -5725479682605823516L;

	public static final String URL = "/import/races";
	public static final String KEY_DAYS_AGO = "daysago";
	public static final String KEY_DAYS_TO = "daysto";
	public static final int DEFAULT_NUMBER_OF_DAYS_AGO = 5;
	public static final int DEFAULT_NUMBER_OF_DAYS_TO = 5;
	
	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		int daysAgo = getInteger(req, KEY_DAYS_AGO, DEFAULT_NUMBER_OF_DAYS_AGO);
		int daysTo = getInteger(req, KEY_DAYS_TO, DEFAULT_NUMBER_OF_DAYS_TO);
		ImportBusiness importer = new ImportBusiness();
		importer.importUpcomingRaces(daysAgo, daysTo, false);
	}
	
	private int getInteger(ServletRequest req, String parameter, int _default) {
		try {
			return Integer.parseInt(req.getParameter(parameter));
		} catch (Exception ex) {
			return _default;
		}
	}

}
