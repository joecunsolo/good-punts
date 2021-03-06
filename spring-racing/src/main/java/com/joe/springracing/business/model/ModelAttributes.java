package com.joe.springracing.business.model;

import com.joe.springracing.objects.Punt;

public class ModelAttributes {

	public static final int DEFAULT_SIMULATIONS = 100;
	public static final double BOOKIE_HIGH = 50;
	public static final double DEFAULT_DEVIATION_DIVIDER = 2;
	public static final double DEFAULT_MEAN_DIVIDER = 1;
	public static final double DEFAULT_JOCKEY_INFLUENCE = 0.0;
	public static final double DEFAULT_TRAINER_INFLUENCE = 0.0;
	public static final double DEFAULT_HORSE_INFLUENCE = 1.0;
	public static final Punt.Type[] DEFAULT_GOOD_PUNT_TYPES = new Punt.Type[]{Punt.Type.WIN};
	public static final int DEFAULT_NUMBER_OF_PUNTS = 10;
	public static final double DEFAULT_HIGH_JOE_CONFIDENCE = 3;
	public static final double DEFAULT_HIGH_BOOKIE_CONFIDENCE = 30;
	public static final double DEFAULT_MAXIMUM_PERCENTAGE_STAKE = 1;
	public static final double DEFAULT_MINIMUM_PERCENTAGE_STAKE = 0.5;
	public static final double DEFAULT_MINIMUM_DOLLAR_STAKE = 1;
	public static final double DEFAULT_MAXIUM_DOLLAR_STAKE = 1000;
	public static final int DEFAULT_LONG_SPELL = 100;
	public static final int DEFAULT_MINIUM_HORSES = 3;
	public static final int DEFAULT_PICKS_PERCENTAGE = 30;

	private int simulations = DEFAULT_SIMULATIONS;
	private double bookieHigh = BOOKIE_HIGH;
	private double defaultDeviationDivider = DEFAULT_DEVIATION_DIVIDER;
	private double defaultMeanDivider = DEFAULT_MEAN_DIVIDER;
	private double trainerInfluence = DEFAULT_TRAINER_INFLUENCE;
	private double jockeyInfluence = DEFAULT_JOCKEY_INFLUENCE;
	private double horseInfluence = DEFAULT_HORSE_INFLUENCE;
	private Punt.Type[] goodPuntTypes = DEFAULT_GOOD_PUNT_TYPES;
	private int numberOfPunts = DEFAULT_NUMBER_OF_PUNTS;
	private double highJoeConfidence = DEFAULT_HIGH_JOE_CONFIDENCE;
	private double highBookieConfidence = DEFAULT_HIGH_BOOKIE_CONFIDENCE;
	private double maximumPercentageStake = DEFAULT_MAXIMUM_PERCENTAGE_STAKE;
	private double minimumPercentageStake = DEFAULT_MINIMUM_PERCENTAGE_STAKE;
	private double minimumDollarStake = DEFAULT_MINIMUM_DOLLAR_STAKE;
	private double maximumDollarStake = DEFAULT_MAXIUM_DOLLAR_STAKE;
	private int longSpell = DEFAULT_LONG_SPELL;
	private int minimumThreeRaceHorses = DEFAULT_MINIUM_HORSES;
	private double picksWinPercentage = DEFAULT_PICKS_PERCENTAGE;
	
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

	public double getHighJoeConfidence() {
		return highJoeConfidence;
	}

	public void setHighJoeConfidence(double highJoeConfidence) {
		this.highJoeConfidence = highJoeConfidence;
	}

	public double getHighBookieConfidence() {
		return highBookieConfidence;
	}

	public void setHighBookieConfidence(double highBookieConfidence) {
		this.highBookieConfidence = highBookieConfidence;
	}

	public double getMinimumPercentageStake() {
		return minimumPercentageStake;
	}

	public void setMinimumPercentageStake(double minimumStake) {
		this.minimumPercentageStake = minimumStake;
	}

	public double getMaximumPercentageStake() {
		return maximumPercentageStake;
	}

	public void setMaximumPercentageStake(double maximumStake) {
		this.maximumPercentageStake = maximumStake;
	}

	public double getMaximumDollarStake() {
		return maximumDollarStake;
	}

	public void setMaximumDollarStake(double maximumDollarStake) {
		this.maximumDollarStake = maximumDollarStake;
	}

	public double getMinimumDollarStake() {
		return minimumDollarStake;
	}

	public void setMinimumDollarStake(double minimumDollarStake) {
		this.minimumDollarStake = minimumDollarStake;
	}

	public int getLongSpell() {
		return longSpell;
	}

	public void setLongSpell(int longSpell) {
		this.longSpell = longSpell;
	}

	public int getMinimumThreeRaceHorses() {
		return minimumThreeRaceHorses;
	}

	public void setMinimumThreeRaceHorses(int minimum) {
		minimumThreeRaceHorses = minimum;
	}

	public double getPicksWinPercentage() {
		return picksWinPercentage;
	}
	
	public void setPicksWinPercentage(double picksWinPercentage) {
		this.picksWinPercentage  = picksWinPercentage;
	}
}
