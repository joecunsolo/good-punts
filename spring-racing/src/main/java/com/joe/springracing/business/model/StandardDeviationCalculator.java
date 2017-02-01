package com.joe.springracing.business.model;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class StandardDeviationCalculator {

	private Model model;
	public StandardDeviationCalculator(Model model) {
		setModel(model);
	}
	public double calculate() {
		return model.getMeanCalculator().calculate();
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public double calculate(double[] values) {
		if (values.length > 1) {
			StandardDeviation sdFunc = new StandardDeviation();
			return sdFunc.evaluate(values);	
		}
		
		double sd = model.getMeanCalculator().calculate(values);
		if (sd == 0) {
			return calculate();
		}
		return sd;
	}
}
