package com.joe.springracing.business.model;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class StandardDeviationCalculator {

	private Model model;
	public StandardDeviationCalculator(Model model) {
		setModel(model);
	}
	public double calculate() {
		return model.getMeanCalculator().calculate() 
				/ model.getAttributes().getDefaultDeviationDivider();
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public double calculate(double[] values) {
		double sd = calculate();
		if (values.length > 1) {
			StandardDeviation sdFunc = new StandardDeviation();
			sd = sdFunc.evaluate(values);	
		}
		
		if (sd == 0) {
			return model.getMeanCalculator().calculate(values) 
					/ model.getAttributes().getDefaultDeviationDivider();
		}
		return sd;
	}

}
