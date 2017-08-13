package com.goodpunts.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.exporter.CSVRunnerHistoriesExporter;

public class ExportRunnerHistoriesServlet extends GenericServlet {

	private static final long serialVersionUID = -574531141170405668L;

	public static final String URL = "/histories/export";
	public static final String FILENAME = "histories_export.csv";
	
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		((HttpServletResponse)res).setHeader("Content-Type", "text/csv");
		((HttpServletResponse)res).setHeader("Content-Disposition", "attachment; filename=\"" + FILENAME + "\"");
		PrintWriter writer = ((HttpServletResponse)res).getWriter();
		
		try {
			CSVRunnerHistoriesExporter exporter = new CSVRunnerHistoriesExporter(writer);
			SpringRacingServices.getSpringRacingDAO().exportRunnerHistories(exporter);
		} catch (Exception e) {
			throw new RuntimeException("Unable to export histories", e);
		} finally {
			writer.flush();
			writer.close();	
		}
	}
}
