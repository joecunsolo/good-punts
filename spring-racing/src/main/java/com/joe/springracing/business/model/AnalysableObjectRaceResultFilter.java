package com.joe.springracing.business.model;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.objects.RunnerResult;

public class AnalysableObjectRaceResultFilter  {
	private AnalysableObject object;
	
	public AnalysableObjectRaceResultFilter(AnalysableObject object) {
		this.setObject(object);
	}
	
	public List<RunnerResult> filter(List<RunnerResult> pastRaces) {
		List<RunnerResult> pastJockeyResults = new ArrayList<RunnerResult>();
		for (RunnerResult result : pastRaces) {
			if (getObject().raced(result)) {
				pastJockeyResults.add(result);
			}
		}
		return pastJockeyResults;
	}
		
//	public void analyse(List<RaceResult> myResults, ModelAttributes model) {
//		double[] values = careerToValues(myResults);
//		double mean = model.mean(values);
//		double sd = model.standardDeviation(values);
//		
//		this.setMean(mean);
//		this.setStandardDeviation(sd);
//	}


	public AnalysableObject getObject() {
		return object;
	}
	public void setObject(AnalysableObject object) {
		this.object = object;
	}

}
