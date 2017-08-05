package com.goodpunts.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.objects.Race;

/**
 * Using the data source defined in {@link #SpringRacingServices} import all the race results
 * 
 * @author joe.cunsolo
 *
 */
public class ImportRaceResultsQueueServlet extends GenericServlet {

	private static final long serialVersionUID = -5725479682605823516L;

	public static final String URL = "/queue/results";

	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		ImportBusiness importer = new ImportBusiness();
		try {
			List<Race> races = importer.fetchRacesWithoutResults();
			for (Race race : races) {
				Queue queue = QueueFactory.getDefaultQueue();
				queue.
					add(TaskOptions.Builder.
							withUrl(ImportRaceResultsServlet.URL).
							param(ImportRaceResultsServlet.KEY_RACECODE, race.getRaceCode())
							.retryOptions(RetryOptions.Builder.withTaskRetryLimit(0)));
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to queue race results");
		}
		
	}

}
