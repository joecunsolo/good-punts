package com.goodpunts.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.BettingBusiness;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.objects.Race;

/**
 * Settle the stakes with the bookie
 * 
 * @author joe.cunsolo
 *
 */
public class SettleStakesServlet extends GenericServlet {

	private static final long serialVersionUID = 303946065697290155L;
	
	public static final String URL = "/stakes/settle";

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		try {
			BettingBusiness bets = new BettingBusiness();
			bets.settleBets();	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to setlles the stakes with the bookie");
		}
	}

}
