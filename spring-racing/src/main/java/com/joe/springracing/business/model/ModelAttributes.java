package com.joe.springracing.business.model;

import com.joe.springracing.objects.Punt;

public class ModelAttributes {

	public static final int DEFAULT_SIMULATIONS = 100;
	public static final double BOOKIE_HIGH = 20;
	public static final double DEFAULT_DEVIATION_DIVIDER = 2;
	public static final double DEFAULT_MEAN_DIVIDER = 1;
	public static final double DEFAULT_JOCKEY_INFLUENCE = 0.0;
	public static final double DEFAULT_TRAINER_INFLUENCE = 0.0;
	public static final double DEFAULT_HORSE_INFLUENCE = 1.0;
	public static final Punt.Type[] DEFAULT_GOOD_PUNT_TYPES = new Punt.Type[]{Punt.Type.WIN};
	public static final int DEFAULT_NUMBER_OF_PUNTS = 10;

	private int simulations = DEFAULT_SIMULATIONS;
	private double bookieHigh = BOOKIE_HIGH;
	private double defaultDeviationDivider = DEFAULT_DEVIATION_DIVIDER;
	private double defaultMeanDivider = DEFAULT_MEAN_DIVIDER;
	private double trainerInfluence = DEFAULT_TRAINER_INFLUENCE;
	private double jockeyInfluence = DEFAULT_JOCKEY_INFLUENCE;
	private double horseInfluence = DEFAULT_HORSE_INFLUENCE;
	private Punt.Type[] goodPuntTypes = DEFAULT_GOOD_PUNT_TYPES;
	private int numberOfPunts = DEFAULT_NUMBER_OF_PUNTS;
	
	public ModelAttributes() {
	}

	public int getSimulations() {
		return simulations;
	}

	public void setSimulations(int simulations) {
		this.simulations = simulations;
	}

	public double getBookieHigh() {
		return bookieHigh;
	}

	public void setBookieHigh(double bookieHigh) {
		this.bookieHigh = bookieHigh;
	}

	public double getDefaultDeviationDivider() {
		return defaultDeviationDivider;
	}

	public void setDefaultDeviationDivider(double defaultDeviationDivider) {
		this.defaultDeviationDivider = defaultDeviationDivider;
	}

	public double getDefaultMeanDivider() {
		return defaultMeanDivider;
	}

	public void setDefaultMeanDivider(double defaultMeanDivider) {
		this.defaultMeanDivider = defaultMeanDivider;
	}

	public double getTrainerInfluence() {
		return trainerInfluence;
	}

	public void setTrainerInfluence(double trainerInfluence) {
		this.trainerInfluence = trainerInfluence;
	}

	public double getJockeyInfluence() {
		return jockeyInfluence;
	}

	public void setJockeyInfluence(double jockeyInfluence) {
		this.jockeyInfluence = jockeyInfluence;
	}

	public double getHorseInfluence() {
		return horseInfluence;
	}

	public void setHorseInfluence(double horseInfluence) {
		this.horseInfluence = horseInfluence;
	}

	public Punt.Type[] getGoodPuntTypes() {
		return goodPuntTypes;
	}

	public void setGoodPuntTypes(Punt.Type[] goodPuntTypes) {
		this.goodPuntTypes = goodPuntTypes;
	}

	public int getNumberOfPunts() {
		return numberOfPunts;
	}

	public void setNumberOfPunts(int numberOfPunts) {
		this.numberOfPunts = numberOfPunts;
	}
	
}
