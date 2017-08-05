package com.joe.springracing.business.probability;

/**
 * What is the probability of this Runner achieving something in the event?
 * Also maintains the summary data used in determining that probability.
 * @author joecunsolo
 *
 */
public class Probability {

	private double win;
	private double place;
	private int numberWins;
	private int numberPlaces;
	private double mean;
	private double standardDeviation;
	
	public double getWin() {
		return win;
	}
	public void setWin(double win) {
		this.win = win;
	}
	public double getPlace() {
		return place;
	}
	public void setPlace(double place) {
		this.place = place;
	}
	public int getNumberWins() {
		return numberWins;
	}
	public void setNumberWins(int numberWins) {
		this.numberWins = numberWins;
	}
	public int getNumberPlaces() {
		return numberPlaces;
	}
	public void setNumberPlaces(int numberPlaces) {
		this.numberPlaces = numberPlaces;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getStandardDeviation() {
		return standardDeviation;
	}
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
}
