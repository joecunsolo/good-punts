package com.joe.springracing.objects;

import com.joe.springracing.business.probability.Probability;

public class Runner { //implements Simulatable {
	
	private String horse;
	private String jockey;
	private String trainer;
	private Odds odds;
	private int result;
//	private Properties career;
//	private Properties goodAt;
	private String raceCode;
	private double weight;	
	
	//TODO Move these out
//	private List<AnalysableObjectStatistic> statistics;
	private Probability probability = new Probability();
	
	private int number;
	private boolean scratched;
	private boolean emergency;
	
	public Runner() {		
	}
		
	public String getHorse() {
		return horse;
	}

	public void setHorse(String horse) {
		this.horse = horse;
	}

	public String getJockey() {
		return jockey;
	}

	public void setJockey(String jockey) {
		this.jockey = jockey;
	}

	public String getTrainer() {
		return trainer;
	}

	public void setTrainer(String trainer) {
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

	public Probability getProbability() {
		return probability;
	}

	public void setProbability(Probability probability) {
		this.probability = probability;
	}
//
//	public List<AnalysableObjectStatistic> getStatistics() {
//		return statistics;
//	}
//
//	public void setStatistics(List<AnalysableObjectStatistic> statistics) {
//		this.statistics = statistics;
//	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isScratched() {
		return scratched;
	}

	public void setScratched(boolean scratched) {
		this.scratched = scratched;
	}

	public boolean isEmergency() {
		return emergency;
	}

	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRaceCode() {
		return raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isEligible() {
		return !isScratched() && !isEmergency();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Runner) {
			return this.hashCode() == o.hashCode();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getHorse().hashCode() + this.getRaceCode().hashCode();
	}
}
