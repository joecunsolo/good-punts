package com.joe.springracing.objects;

public class Pick {
	private Runner runner;
	private double win; 
	private double bookieOdds;
	private String raceCode;
	
	public Runner getRunner() {
		return runner;
	}
	public void setRunner(Runner runner) {
		this.runner = runner;
	}
	public double getWin() {
		return win;
	}
	public void setWin(double win) {
		this.win = win;
	}
	public double getBookieOdds() {
		return bookieOdds;
	}
	public void setBookieOdds(double bookieOdds) {
		this.bookieOdds = bookieOdds;
	}
	public String getRaceCode() {
		return raceCode;
	}
	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}
}
