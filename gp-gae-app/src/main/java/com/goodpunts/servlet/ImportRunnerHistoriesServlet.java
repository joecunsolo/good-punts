package com.goodpunts.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.goodpunts.GoodPuntsServices;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.objects.Race;

/**
 * For a race with a given race code imports the horse histories
 * ... This has a max timeout of 10 mins.
 * 
 * @author joe.cunsolo
 *
 */
public class ImportRunnerHistoriesServlet extends GenericServlet {

	private static final long serialVersionUID = 303946065697290155L;
	
	public static final String URL = "/import/histories";
	public static final String KEY_RACECODE = "racecode";

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		String raceCode = req.getParameter(KEY_RACECODE);
		try {
			Race race = GoodPuntsServices.getSpringRacingDAO().fetchRace(raceCode);
			ImportBusiness importer = new ImportBusiness(GoodPuntsServices.getSpringRacingDAO());
			importer.importRace(race, true);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get histories for race " + raceCode, e);
		}
	}

}
