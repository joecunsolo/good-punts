package com.joe.springracing.exporter;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import com.joe.springracing.objects.RunnerResult;

/** Exports the histories of the runners as a CSV */
public class CSVRunnerHistoriesExporter implements RunnerHistoriesExporter {

	private PrintWriter writer;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
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
				"PrizeMoney," +
				"Weight";
	}

	/** Writer a runner history to CSV */
	public void export(RunnerResult runnerResult) {
		writer.println(toRecord(runnerResult));
		writer.flush();
	}
	
	public void close() {
		writer.close();
	}
	
	private String toRecord(RunnerResult runnerResult) {
		String result = runnerResult.getHorse() + "," +
				runnerResult.getDistance() + "," +
				runnerResult.getJockey() + "," +
				runnerResult.getPosition() + "," +
				runnerResult.getRaceCode() + "," +
//				runnerResult.getRaceName() + "," + 
				runnerResult.getRaceNumber() + "," +
				runnerResult.getResultType() + "," +
				runnerResult.getTrainer() + "," +
				runnerResult.getVenueName() + ",";
		if (runnerResult.getRaceDate() != null) { 	
			result += sdf.format(runnerResult.getRaceDate()) + ",";
		} else {
			result += "null,";
		}
		result += runnerResult.getPrizeMoney() + ",";
		result += runnerResult.getWeight();
		return result;
	}

}
