package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.joe.springracing.business.RacingKeys;

public class RaceResult extends RacingObject {

	private List<RunnerResult> runners;
	private double[] prizeMoney;

	public RaceResult(Properties props) {
		super(props);
		runners = new ArrayList<RunnerResult>();
	}

	public List<RunnerResult> getRunners() {
		return runners;
	}

	public void setRunners(List<RunnerResult> runners) {
		this.runners = runners;
	}
	
	public String getMeetCode() {
		return getProperty(RacingKeys.KEY_MEETCODE);
	}

	public double[] getPrizeMoney() {
		return prizeMoney;
	}

	public void setPrizeMoney(double[] prizeMoney) {
		this.prizeMoney = prizeMoney;
	}

	public double getPrizeMoney(int result) {
		if (result > prizeMoney.length) {
			return 0.0;
		}
		return prizeMoney[result - 1];
	}

	public void addRunner(RunnerResult result) {
		runners.add(result);
	}

}
