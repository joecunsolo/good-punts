package com.joe.springracing.test;

import junit.framework.TestCase;

import org.junit.Assert;

import com.joe.springracing.business.probability.WeightedPrizeMoneyStatistics;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.RunnerResult;

public class TestWeightedPrizeMoneyStatistics extends TestCase {

	public static void testZeroResult() {
		WeightedPrizeMoneyStatistics stat = new WeightedPrizeMoneyStatistics();
		RunnerResult runnerResult = new RunnerResult();
		runnerResult.setPrizeMoney(1000);
		runnerResult.setWeight(0);
		double result = stat.getResult(runnerResult);
		Assert.assertEquals(1000, result, 0.1);
	}
	
	public static void testZeroInfluence() {
		WeightedPrizeMoneyStatistics stat = new WeightedPrizeMoneyStatistics();
		Runner runner = new Runner();
		runner.setWeight(0);
		stat.setRunner(runner);
		double influence = stat.getInfluence();
		Assert.assertEquals(1, influence, 0.1);
	}
	
}
