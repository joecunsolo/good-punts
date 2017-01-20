package com.goodpunts.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.goodpunts.GoodPuntsServices;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Meeting;

public class GeneratePuntsQueueServlet extends GenericServlet {

	private static final long serialVersionUID = -574531141170405668L;

	public static final String URL = "/punts/queue";

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
		List<Meeting> upcoming = probabilities.fetchUpcomingMeets();
		
		for (Meeting meet : upcoming) {
			Queue queue = QueueFactory.getDefaultQueue();
			queue.
				add(TaskOptions.Builder.
						withUrl(GeneratePuntsServlet.URL).
						param(GeneratePuntsServlet.KEY_MEETCODE, meet.getMeetCode()));	
		}
	}
}
