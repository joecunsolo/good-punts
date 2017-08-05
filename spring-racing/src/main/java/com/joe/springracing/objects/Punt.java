package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Punt {

	private String raceCode;
	/** When the race will be run */
	private Date date;
	private Type type;
	private double joesOdds;
	private double bookieOdds;
	private List<Runner> runners;
//	private List<Stake> stakes;
	private Confidence confidence;
	/** The state of the punt */
	private State state;
	private long id;
	private int raceNumber;
	/** Where the race will be run */
	private String venue;
	/** The final result - win or loss*/
	private Result result;
	
	public enum Result {
		WIN,
		LOSS
	}
	
	public enum State {
		LIVE,
		OPEN,
		FINISHED
	}

	public enum Confidence {
		HIGH,
		LOW
	}
	
	public enum Type {
		WIN,
		PLACE,
		TRIFECTA
	}
	
	public Punt() {
		this.runners = new ArrayList<Runner>();
//		this.stakes = new ArrayList<Stake>();
	}
	
	public Punt(String raceCode, Date d, Type t, double odds, double bookieOdds, Confidence conf, State state) {
		this();
		this.setRaceCode(raceCode);
		this.setDate(d);
		this.setType(t);
		this.setJoesOdds(odds);
		this.setBookieOdds(bookieOdds);
		this.setConfidence(conf);
		this.setState(state);
	}	

//	public Race getRace() {
//		return race;
//	}
//
//	public void setRace(Race race) {
//		this.race = race;
//	}
//
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public double getJoesOdds() {
		return joesOdds;
	}

	public void setJoesOdds(double joesOdds) {
		this.joesOdds = joesOdds;
	}

	public double getBookieOdds() {
		return bookieOdds;
	}

	public void setBookieOdds(double bookieOdds) {
		this.bookieOdds = bookieOdds;
	}

	public List<Runner> getRunners() {
		return runners;
	}

	public void setRunners(List<Runner> runners) {
		this.runners = runners;
	}
	
	public double variance() {
		return bookieOdds / joesOdds;
	}

//	public List<Stake> getStakes() {
//		return stakes;
//	}
//
//	public void setStakes(List<Stake> stakes) {
//		this.stakes = stakes;
//	}

	public Confidence getConfidence() {
		return confidence;
	}

	public void setConfidence(Confidence confidence) {
		this.confidence = confidence;
	}

//	public void addStake(Stake stake) {
//		stakes.add(stake);
//	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getRaceCode() {
		return raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Same race code
	 * Same punt type
	 * Same runners
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Punt) {
			Punt p2 = (Punt)o;
			//same race code
			if (p2.getRaceCode().equals(this.getRaceCode())) {
				//same punt type
				if (p2.getType().equals(this.getType())) {
					for (Runner r2 : p2.getRunners()) {
						if (!this.getRunners().contains(r2)) {
							return false;
						}
					}
					//same runners
					return true;
				}
			}
		}
		return false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRaceNumber() {
		return raceNumber;
	}

	public void setRaceNumber(int raceNumber) {
		this.raceNumber = raceNumber;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
