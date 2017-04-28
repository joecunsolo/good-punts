package com.goodpunts.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.joe.springracing.business.BettingBusiness;
import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.objects.Punt;

public class GeneratePuntsServlet extends GenericServlet {

	private static final long serialVersionUID = -574531141170405668L;

	public static final String URL = "/punts/generate";
	
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		
		PuntingBusiness punt = new PuntingBusiness();
		List<Punt> punts = punt.generate();
		
		try {
			BettingBusiness bet = new BettingBusiness();
			bet.placeBets(punts);
		} catch (Exception ex) {
			//TODO something
		}
	}	
}
