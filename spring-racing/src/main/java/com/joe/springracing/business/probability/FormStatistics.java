package com.joe.springracing.business.probability;

import java.util.List;

import com.joe.springracing.objects.RunnerResult;

public class FormStatistics extends SingleWeightedStatistics {
	
	protected Double[] careerToValues(List<RunnerResult> pastResults) {
		if (pastResults.size() == 0) {
			return new Double[0];
		}
		
		Double[] result = new Double[pastResults.size()];
		int i = 0;
		for (RunnerResult race : pastResults) {
			result[i++] = Double.valueOf(race.getPosition());
		}
		
		return result;	
	}

	public boolean isDescending() {
		return false;
	}

}
