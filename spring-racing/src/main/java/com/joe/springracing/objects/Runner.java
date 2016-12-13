package com.joe.springracing.objects;

import java.util.List;
import java.util.Properties;

import com.joe.springracing.business.RacingKeys;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.probability.Probability;

public class Runner extends RacingObject {
	
	private Horse horse;
	private Jockey jockey;
	private Trainer trainer;
	private Odds odds;
	private Properties career;
	private Properties goodAt;

	//TODO Move these out
	private List<AnalysableObjectStatistic> statistics;
	private Probability probability = new Probability();
	
	public Runner(Properties props) {
		super(props);
		
		jockey = new Jockey();
		trainer = new Trainer();
		horse = new Horse();
	}
	
	public Runner() {
		this(new Properties());
	}
	
	public Horse getHorse() {
		return horse;
	}

	public void setHorse(Horse horse) {
		this.horse = horse;
	}

	public int getNumber() {
		return getNumber(RacingKeys.KEY_RUNNER_NUMBER);
	}
	
	public boolean isScratched() {
		return isTrue(RacingKeys.KEY_SCRATCHED);
	}
	
	public boolean isEmergency() {
		return isTrue(RacingKeys.KEY_EMERGENCY);
	}

	public Jockey getJockey() {
		return jockey;
	}

	public void setJockey(Jockey jockey) {
		this.jockey = jockey;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	public Odds getOdds() {
		return odds;
	}

	public void setOdds(Odds odds) {
		this.odds = odds;
	}

	public boolean hasOdds() {
		return odds != null && odds.getWin() > 0;
	}

	public Properties getGoodAt() {
		return goodAt;
	}

	public void setGoodAt(Properties goodAt) {
		this.goodAt = goodAt;
	}

	public Properties getCareer() {
		return career;
	}

	public void setCareer(Properties career) {
		this.career = career;
	}

	public Probability getProbability() {
		return probability;
	}

	public void setProbability(Probability probability) {
		this.probability = probability;
	}

	public List<AnalysableObjectStatistic> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<AnalysableObjectStatistic> statistics) {
		this.statistics = statistics;
	}

//	public StatisticsObject getDistance() {
//		return distance;
//	}
//
//	public void setDistance(StatisticsObject distance) {
//		this.distance = distance;
//	}

}
