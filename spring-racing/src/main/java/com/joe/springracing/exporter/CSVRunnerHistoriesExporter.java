package com.joe.springracing.exporter;

import java.io.PrintWriter;

import com.joe.springracing.objects.RunnerResult;

/** Exports the histories of the runners as a CSV */
public class CSVRunnerHistoriesExporter extends CSVExporter<RunnerResult> {

	public CSVRunnerHistoriesExporter(PrintWriter writer) {
		super(writer);
	}
	
	public String[] getHeader() {
		return new String[]{"Horse",
				"Distance",
				"Jockey",
				"Position",
				"Racecode",
				"Result",
				"RaceNumber",
				"Trainer",
				"Venue",
				"Date",
				"PrizeMoney",
				"Weight"};
	}
	
	public String[] toRecord(RunnerResult runnerResult) {
		return new String[]{runnerResult.getHorse(),
				String.valueOf(runnerResult.getDistance()),
				runnerResult.getJockey(),
				String.valueOf(runnerResult.getPosition()),
				runnerResult.getRaceCode(),
//				runnerResult.getRaceName() + "," + 
				String.valueOf(runnerResult.getRaceNumber()),
				runnerResult.getResultType(),
				runnerResult.getTrainer(),
				runnerResult.getVenueName(),
				runnerResult.getRaceDate() != null ? 
						sdf.format(runnerResult.getRaceDate()) : 
						"null",
				String.valueOf(runnerResult.getPrizeMoney()),
				String.valueOf(runnerResult.getWeight())};
	}

}
