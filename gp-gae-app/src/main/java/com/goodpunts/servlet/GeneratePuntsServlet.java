package com.goodpunts.servlet;

import java.io.IOException;
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

public class GeneratePuntsServlet extends GenericServlet {

	private static final long serialVersionUID = -574531141170405668L;

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		Model m = new Model(new ModelAttributes());
		ProbabilityBusiness probabilities = new ProbabilityBusiness(
				GoodPuntsServices.getSpringRacingDAO(),
				GoodPuntsServices.getPuntingDAO(),
				SpringRacingServices.getStatistics(), 
				SpringRacingServices.getSimulator(), 
				m);
		List<Meeting> upcoming = probabilities.generateProbabilitiesForUpcomingMeets();
		
		PuntingBusiness punts = new PuntingBusiness(GoodPuntsServices.getPuntingDAO(), m);
		punts.generateGoodPuntsForMeets(upcoming);
	}
}
