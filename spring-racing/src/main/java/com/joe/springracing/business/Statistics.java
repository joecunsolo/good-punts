package com.joe.springracing.business;

import com.joe.springracing.business.model.AnalysableObject;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.SimulatableRunner;
import com.joe.springracing.objects.Runner;

public interface Statistics {

	public SimulatableRunner evaluate(Runner runner, AnalysableObject o, Model m);

	public boolean isDescending();

}
