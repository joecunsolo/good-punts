package com.joe.springracing.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/** This is a handy racing.com object 
 * not to be stored*/
public class RaceResult {

	private List<RunnerResult> runners;
	private String meetCode;
	private double[] prizeMoney;

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

	public String getMeetCode() {
		return meetCode;
	}

	public void setMeetCode(String meetCode) {
		this.meetCode = meetCode;
	}

	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
