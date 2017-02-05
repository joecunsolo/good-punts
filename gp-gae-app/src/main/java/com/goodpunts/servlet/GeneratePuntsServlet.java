package com.goodpunts.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.goodpunts.GoodPuntsServices;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;

public class GeneratePuntsServlet extends GenericServlet {

	private static final long serialVersionUID = -574531141170405668L;

	public static final String URL = "/punts/generate";
	
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		
		Model m = new Model(new ModelAttributes());
		ProbabilityBusiness probabilities = new ProbabilityBusiness(
				GoodPuntsServices.getSpringRacingDAO(),
				GoodPuntsServices.getPuntingDAO(),
				SpringRacingServices.getStatistics(), 
				SpringRacingServices.getSimulator(), 
				m);

		List<Meeting> meets = probabilities.fetchUpcomingMeets();
		for (Meeting meeting : meets) {
			try {
				List<Race> races = GoodPuntsServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
//				meeting.setRaces(races);
				PuntingBusiness punts = new PuntingBusiness(GoodPuntsServices.getPuntingDAO(), m);
				for (Race r : races) {
				//	Date lastPuntEvent = punts.fetchLastPuntEvent(r);
				//	if (lastPuntEvent == null ||
				//			r.getDate().getTime() - System.currentTimeMillis() < 60 * 60 * 1000 ||
				//			System.currentTimeMillis() - lastPuntEvent.getTime() > 60 * 60 * 1000) {
						punts.generateAndStoreGoodPuntsForRace(r);
				//	}
				}
			} catch (Exception e) {
				throw new RuntimeException("Unable to generate punts for " + meeting.getMeetCode(), e);
			}
		}
	}	
}
