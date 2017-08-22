package com.joe.springracing.exporter;

import java.io.PrintWriter;

import com.joe.springracing.objects.Runner;

/** Exports the histories of the runners as a CSV */
public class CSVRunnerExporter extends CSVExporter<Runner> {

	public CSVRunnerExporter(PrintWriter writer) {
		super(writer);
	}
	
	public String[] getHeader() {
		return new String[]{"Horse",
				"Jockey",
				"Number",
				"Win",
				"Place",
				"Mean",
				"SD",
				"Win%",
				"Place%",
				"Result",
				"Trainer",
				"Weight",
				"Eligible"};
	}
	
	public String[] toRecord(Runner runner) {
		return new String[]{
				runner.getHorse(),
				runner.getJockey(),
				String.valueOf(runner.getNumber()),
				String.valueOf(runner.getOdds().getWin()),
				String.valueOf(runner.getOdds().getPlace()),
				String.valueOf(runner.getProbability().getMean()),
				String.valueOf(runner.getProbability().getStandardDeviation()),
				String.valueOf(runner.getProbability().getWin()),
				String.valueOf(runner.getProbability().getPlace()),
				String.valueOf(runner.getResult()),
				runner.getTrainer(),
				String.valueOf(runner.getWeight()),
				String.valueOf(runner.isEligible())
		};
	}

}
