package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RaceResult {

	private List<RunnerResult> runners;
	private double[] prizeMoney;
	private int meetCode;

	public RaceResult(Properties props) {
		runners = new ArrayList<RunnerResult>();
	}

	public List<RunnerResult> getRunners() {
		return runners;
	}

	public void setRunners(List<RunnerResult> runners) {
		this.runners = runners;
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

	public int getMeetCode() {
		return meetCode;
	}

	public void setMeetCode(int meetCode) {
		this.meetCode = meetCode;
	}

	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
