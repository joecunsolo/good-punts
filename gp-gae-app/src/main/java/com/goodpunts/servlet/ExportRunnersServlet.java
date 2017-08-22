package com.goodpunts.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.exporter.CSVExporter;
import com.joe.springracing.exporter.CSVRunnerExporter;
import com.joe.springracing.exporter.CSVRunnerHistoriesExporter;
import com.joe.springracing.objects.Runner;

public class ExportRunnersServlet extends GenericServlet {

	private static final long serialVersionUID = -574531141170405668L;

	public static final String URL = "/runners/export";
	public static final String FILENAME = "runners_export.csv";
	
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		((HttpServletResponse)res).setHeader("Content-Type", "text/csv");
		((HttpServletResponse)res).setHeader("Content-Disposition", "attachment; filename=\"" + FILENAME + "\"");
		PrintWriter writer = ((HttpServletResponse)res).getWriter();
		
		try {
			CSVExporter<Runner> exporter = new CSVRunnerExporter(writer);
			SpringRacingServices.getSpringRacingDAO().exportRunners(exporter);
		} catch (Exception e) {
			throw new RuntimeException("Unable to export histories", e);
		} finally {
			writer.flush();
			writer.close();	
		}
	}
}
