package com.goodpunts.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.goodpunts.GoodPuntsServices;
import com.joe.springracing.business.ImportBusiness;

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

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		String horseCode = req.getParameter(KEY_HORSECODE);
		try {
			ImportBusiness importer = new ImportBusiness(GoodPuntsServices.getSpringRacingDAO());
			importer.importRunner(horseCode, true, true);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get histories for horse " + horseCode, e);
		}
	}

}
