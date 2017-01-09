package com.joe.springracing.business;

import java.util.List;

import com.joe.springracing.business.model.AnalysableObject;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.objects.Runner;

public interface Statistics {

	public List<AnalysableObjectStatistic> evaluate(Runner runner, AnalysableObject o, Model m);

	public boolean isDescending();

}
