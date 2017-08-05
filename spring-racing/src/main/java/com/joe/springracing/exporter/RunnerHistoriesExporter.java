package com.joe.springracing.exporter;

import com.joe.springracing.objects.RunnerResult;

public interface RunnerHistoriesExporter {

	public void export(RunnerResult runnerResult);

}
