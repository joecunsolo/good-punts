package com.joe.springracing.exporter;

import java.io.PrintWriter;

import com.joe.springracing.objects.RunnerResult;

public class CSVRunnerHistoriesExporter implements RunnerHistoriesExporter {

	private PrintWriter writer;
	public CSVRunnerHistoriesExporter(PrintWriter writer) {
		this.writer = writer;
		writer.println(getHeader());
	}
	
	private String getHeader() {
		return "Horse," +
				"Distance," +
				"Jockey," +
				"Position," +
				"Racecode," +
				"Result," +
				"RaceNumber," +
				"Trainer," +
				"Venue," +
				"Date," +
				"PrizeMoney";
	}

	public void export(RunnerResult runnerResult) {
		writer.println(toRecord(runnerResult));
		writer.flush();
	}
	
	public void close() {
		writer.close();
	}
	
	private String toRecord(RunnerResult runnerResult) {
		return runnerResult.getHorse() + "," +
				runnerResult.getDistance() + "," +
				runnerResult.getJockey() + "," +
				runnerResult.getPosition() + "," +
				runnerResult.getRaceCode() + "," +
//				runnerResult.getRaceName() + "," + 
				runnerResult.getRaceNumber() + "," +
				runnerResult.getResultType() + "," +
				runnerResult.getTrainer() + "," +
				runnerResult.getVenueName() + "," +
				runnerResult.getRaceDate() + "," +
				runnerResult.getPrizeMoney();
	}

}
