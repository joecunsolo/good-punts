package com.joe.springracing.business.probability;

import java.util.List;

import com.joe.springracing.objects.RunnerResult;

public class FormStatistics extends SingleWeightedStatistics {
	
	protected double[] careerToValues(List<RunnerResult> pastResults) {
		if (pastResults.size() == 0) {
			return new double[0];
		}
		
		double[] result = new double[pastResults.size()];
		int i = 0;
		for (RunnerResult race : pastResults) {
			result[i++] = race.getResult();
		}
		
		return result;	
	}

	public boolean isDescending() {
		return false;
	}

}
