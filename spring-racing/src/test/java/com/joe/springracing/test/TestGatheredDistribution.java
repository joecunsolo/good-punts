package com.joe.springracing.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.business.model.stats.SingleVariateStatistic;
import com.joe.springracing.business.probability.distributions.GatheredDistribution;

public class TestGatheredDistribution extends TestCase {


	public static void testInverseCumulativeProbability() {
		Model m = new Model(new ModelAttributes());
		
		List<List<AnalysableObjectStatistic>> stats = getStatistics();
		for (List<AnalysableObjectStatistic> statistics : stats) {
			GatheredDistribution d = new GatheredDistribution(m);
			d.setStatistics(statistics);
			testInverseCumulativeProbability(d);
		}
	}
	
	private static void testInverseCumulativeProbability(GatheredDistribution d) {
		double[] randoms = {0.267632758, 0.819538072, 0.982277565, 0.425494063, 0.198610679, 0.831385992, 0.751072621, 0.654838666, 0.069997002, 0.757918114};
		for (int i = 0; i < randoms.length; i++) {
			System.out.print(d.inverseCumulativeProbability(randoms[i]) + "\t");
		}
		System.out.println();
	}
	
	
//	#	Name	Mean	Wmean	SD	Weight	%	Odds
//	1	black-heart-bart 	268113.3	193041.576	181715.2	59	0	2.2
//	2	palentino 	61652.6	44389.872	133810.6	59	21.8	5.5
//	3	he-or-she 	51920	41016.8	49754.4	58	7.7	15
//	4	sir-isaac-newton-gb 	85500	73530	85500	57	29.5	17
//	5	real-love 	40016.2	37215.066	55792.8	56	9.3	21
//	6	tosen-stardom-jpn 	71887.5	71887.5	79072.2	55	31.7	2.9
//	7	harlem-gb 	7	7	7	55	0	34
	private static List<List<AnalysableObjectStatistic>> getStatistics() {
		List<List<AnalysableObjectStatistic>> result = new ArrayList<List<AnalysableObjectStatistic>>();
		
		double[] means = {193041.576,
				7,
				41016.8,
				44389.872,
				37215.066,
				73530,
				71887.5};
		double[] sd = {181715.2,
				7,
				49754.4,
				133810.6,
				55792.8,
				85500,
				79072.2};
		for (int i = 0; i < means.length; i++) {
			SingleVariateStatistic bhb = new SingleVariateStatistic();
			bhb.setMean(means[i]);
			bhb.setStandardDeviation(sd[i]);
			bhb.setWeight(1.0);
			List<AnalysableObjectStatistic> stats = new ArrayList<AnalysableObjectStatistic>();
			stats.add(bhb);
			result.add(stats);
		}
		return result;
	}
	
}
