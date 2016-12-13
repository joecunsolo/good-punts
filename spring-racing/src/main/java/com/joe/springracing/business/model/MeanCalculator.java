package com.joe.springracing.business.model;

public class MeanCalculator {

	private Model model;
	public MeanCalculator(Model model) {
		setModel(model);
	}
	public double calculate() {
		return model.getRace().getRunners().size();
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public double calculate(double[] values) {
		if (values.length == 0) {
			return calculate();
		}
		double result = 0;
		for (int i = 0 ; i < values.length; i++) {
			result += values[i];
		}
		return result / values.length;
	}

}
