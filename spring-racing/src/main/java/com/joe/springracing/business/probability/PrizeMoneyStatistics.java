package com.joe.springracing.business.probability;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.joe.springracing.objects.RunnerResult;

public class PrizeMoneyStatistics extends SingleWeightedStatistics {

	public static final long A_YEAR = 1000 * 60 * 60 * 24 * 365;
	
	@Override
	protected double[] careerToValues(List<RunnerResult> pastResults) {
		if (pastResults.size() == 0) {
			return new double[0];
		}
		
		Collections.sort(pastResults, new RunnerResultDateComparator());
		
		int numResults = pastResults.size() > 9 ? 9 : pastResults.size();
		double[] result = new double[numResults];
		int i = 0;
		for (RunnerResult runnerResult : pastResults) {
			if (i == numResults || 
					System.currentTimeMillis() - runnerResult.getRaceDate().getTime() > A_YEAR) {
				return result;
			}
			result[i++] = runnerResult.getPrizeMoney();
		}
		
		return result;	
	}

	public boolean isDescending() {
		return true;
	}
	
	public class RunnerResultDateComparator implements Comparator<RunnerResult> {

		public int compare(RunnerResult arg0, RunnerResult arg1) {
			Date d1 = toDate(arg0);
			Date d2 = toDate(arg1);

			if (d1 == null && d2 == null) {
				return 0;
			}
			if (d1 == null) {
				return 1;
			}
			if (d2 == null) {
				return -1;
			}
			if (d1.getTime() > d2.getTime()) {
				return -1;
			}
			return 1;
		}
		
		private Date toDate(RunnerResult arg) {
			return arg.getRaceDate();
//			try {
//				String s1 = arg.getProperty("race");
//				s1 = s1.substring(s1.indexOf("Date:") + 5, s1.indexOf("Date:") + 24);
//	
//				return RacingObject.dateFormat.parse(s1);
//			} catch (Exception ex) {
//				return null;
//			}
		}
	}
}
