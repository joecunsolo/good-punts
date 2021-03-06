package com.goodpunts.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.BettingBusiness;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;

public class GenerateProbabilitiesServlet extends GenericServlet {

	private static final long serialVersionUID = -574531141170405668L;

	public static final String URL = "/probabilities/generate";
	public static final String KEY_RACECODE = "racecode";
	
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		String raceCode = req.getParameter(KEY_RACECODE);
		
		ProbabilityBusiness probabilities = new ProbabilityBusiness();
		try {
			Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
			System.out.println(race.getVenue() + " " + race.getRaceNumber() + " " + race.getName());
			probabilities.generate(race);
			
			//Generate the punts as well because we have them now..
			PuntingBusiness pbix = new PuntingBusiness();
			List<Punt> punts = pbix.generate(race);
			
			//And then place bets on them
			try {
				BettingBusiness bet = new BettingBusiness();
				bet.placeBets(punts);
			} catch (Exception ex) {
				throw new RuntimeException("Unable to place bets", ex);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to generate punts for " + raceCode, e);
		}

	}
}
