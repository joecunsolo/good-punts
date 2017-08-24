package com.joe.springracing.exporter;

import java.io.PrintWriter;

/** Exports the histories of the runners as a CSV */
public class CSVRunnerExporter extends CSVExporter<RaceRunner> {

	public CSVRunnerExporter(PrintWriter writer) {
		super(writer);
	}
	
	public String[] getHeader() {
		return new String[]{
				"Race",
				"Date",
				"Venue",
				"Horse",
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
	
	public String[] toRecord(RaceRunner runner) {
		if (runner.race.getResult() != null) {
			for (int i = 0; i < runner.race.getResult().length; i++) {
				if (runner.runner.getNumber() == runner.race.getResult()[i]) {
					runner.runner.setResult(i+1);
				}
			}
		}
		return new String[]{
				runner.race.getRaceCode(),
				String.valueOf(runner.race.getDate()),
				runner.race.getVenue(),
				String.valueOf(runner.race.getDistance()),
				runner.runner.getHorse(),
				runner.runner.getJockey(),
				String.valueOf(runner.runner.getNumber()),
				String.valueOf(runner.runner.getOdds().getWin()),
				String.valueOf(runner.runner.getOdds().getPlace()),
				String.valueOf(runner.runner.getProbability().getMean()),
				String.valueOf(runner.runner.getProbability().getStandardDeviation()),
				String.valueOf(runner.runner.getProbability().getWin()),
				String.valueOf(runner.runner.getProbability().getPlace()),
				String.valueOf(runner.runner.getResult()),
				runner.runner.getTrainer(),
				String.valueOf(runner.runner.getWeight()),
				String.valueOf(runner.runner.isEligible())
		};
	}

}
