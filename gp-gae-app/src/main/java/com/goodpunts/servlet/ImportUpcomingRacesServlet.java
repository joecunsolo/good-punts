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

	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		ImportBusiness importer = new ImportBusiness();
		importer.importUpcomingRaces(false);
	}

}
