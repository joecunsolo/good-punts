package com.goodpunts.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.objects.Race;

/**
 * For a horse with a given horse code imports the horse histories
 * ... This has a max timeout of 10 mins.
 * 
 * @author joe.cunsolo
 *
 */
public class ImportRunnerHistoriesServlet extends GenericServlet {

	private static final long serialVersionUID = 303946065697290155L;
	
	public static final String URL = "/import/histories";
	public static final String KEY_HORSECODE = "horsecode";
	public static final String KEY_RACECODE = "racecode";

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		String raceCode = req.getParameter(KEY_RACECODE);
		try {
			ImportBusiness importer = new ImportBusiness();
			Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
			importer.importRace(race, true);	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to get histories for race " + raceCode);
		}
	}

}
