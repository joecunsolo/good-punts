package com.goodpunts.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.goodpunts.GoodPuntsServices;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.Queue;
import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.objects.Race;

/**
 * Gets all the races where the horse histories have not been loaded and adds them to a push queue
 * 
 * @author joe.cunsolo
 *
 */
public class ImportHistoryQueueServlet extends GenericServlet {

	private static final long serialVersionUID = 8566930885326993392L;
	
	public static final String URL = "/import/queue";
	
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		ImportBusiness importer = new ImportBusiness(GoodPuntsServices.getSpringRacingDAO());
		List<Race> races = importer.fetchRacesWithoutHistories();
		
		for (Race race : races) {
			Queue queue = QueueFactory.getDefaultQueue();
			queue.
				add(TaskOptions.Builder.
						withUrl(ImportRunnerHistoriesServlet.URL).
						param(ImportRunnerHistoriesServlet.KEY_RACECODE, race.getRaceCode()));	
		}
	}

}
