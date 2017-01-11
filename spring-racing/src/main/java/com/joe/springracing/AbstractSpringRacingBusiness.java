package com.joe.springracing;

import java.io.PrintWriter;

import com.joe.springracing.dao.SpringRacingDAO;

public class AbstractSpringRacingBusiness {

	private SpringRacingDAO springRacingDAO;
	private PrintWriter writer;

	public AbstractSpringRacingBusiness(SpringRacingDAO dao, PrintWriter pw) {
		setSpringRacingDAO(dao);
		setWriter(pw);
	}
	
	public SpringRacingDAO getSpringRacingDAO() {
		return springRacingDAO;
	}

	public void setSpringRacingDAO(SpringRacingDAO springRacingDAO) {
		this.springRacingDAO = springRacingDAO;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}
}
