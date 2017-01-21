package com.goodpunts.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.goodpunts.GoodPuntsServices;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Race;

public class GenerateProbabilitiesServlet extends GenericServlet {

	private static final long serialVersionUID = -574531141170405668L;

	public static final String URL = "/probabilities/generate";
	public static final String KEY_RACECODE = "racecode";
	
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		String raceCode = req.getParameter(KEY_RACECODE);
		
		Model m = new Model(new ModelAttributes());
		ProbabilityBusiness probabilities = new ProbabilityBusiness(
				GoodPuntsServices.getSpringRacingDAO(),
				GoodPuntsServices.getPuntingDAO(),
				SpringRacingServices.getStatistics(), 
				SpringRacingServices.getSimulator(), 
				m);

		try {
			Race race = GoodPuntsServices.getSpringRacingDAO().fetchRace(raceCode);
			probabilities.generateProbabilitiesForRace(race);
			
//			PuntingBusiness punts = new PuntingBusiness(GoodPuntsServices.getPuntingDAO(), m);
//			punts.generateGoodPuntsForMeet(meeting);
		} catch (Exception e) {
			throw new RuntimeException("Unablt to generate punts for " + raceCode);
		}

	}
}
