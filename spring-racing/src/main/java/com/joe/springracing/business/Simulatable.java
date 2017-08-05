package com.joe.springracing.business;

import java.util.List;

import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.probability.Probability;

/** 
 * Represents an object that can be run through a Simulator against other Simulatable  objects
 */
public interface Simulatable {

	/** The statistics that will be used in the Simulation */
	public List<AnalysableObjectStatistic> getStatistics();
	/** The result of the Simulation */
	public void setProbability(Probability probability);
	/** The unique id of the Object */
	public int getNumber();
	/** Is this Simulatable eligible to compete or just informational */
	public boolean isEligible();
}
