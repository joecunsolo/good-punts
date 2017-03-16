package com.joe.springracing;

import java.io.PrintWriter;

public class AbstractSpringRacingBusiness {

	private PrintWriter writer;

	public AbstractSpringRacingBusiness(PrintWriter pw) {
		setWriter(pw);
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}
}
