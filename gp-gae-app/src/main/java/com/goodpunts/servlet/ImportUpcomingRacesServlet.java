package com.goodpunts.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.goodpunts.GoodPuntsServices;
import com.joe.springracing.business.ImportBusiness;

public class ImportUpcomingRacesServlet extends GenericServlet {

	private static final long serialVersionUID = -5725479682605823516L;

	@Override
	public void service(ServletRequest arg0, ServletResponse resp)
			throws ServletException, IOException {
		ImportBusiness importer = new ImportBusiness(GoodPuntsServices.getSpringRacingDAO());
		importer.importUpcomingRaces(true);
	}

}
