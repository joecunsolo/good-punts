package com.joe.springracing.business.probability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.joe.springracing.objects.RunnerResult;

public class PrizeMoneyStatistics extends SingleWeightedStatistics {

	public static final long A_YEAR = 31536000000l;
	
	@Override
	protected Double[] careerToValues(List<RunnerResult> pastResults) {
		if (pastResults.size() == 0) {
			return new Double[0];
		}
		
		Collections.sort(pastResults, new RunnerResultDateComparator());
		
		int numResults = pastResults.size() > 9 ? 9 : pastResults.size();
		List<Double> list = new ArrayList<Double>();
		for (RunnerResult runnerResult : pastResults) {
			if (list.size() == numResults) {
				Double[] result = new Double[list.size()];
				return list.toArray(result);
			}
			
			Double result = getResult(runnerResult);
			if (runnerResult.getRaceDate() != null &&
					! runnerResult.isTrial() &&
					result > 0 &&
					System.currentTimeMillis() - runnerResult.getRaceDate().getTime() < A_YEAR) {
				list.add(getResult(runnerResult));
			}
		}

		Double[] result = new Double[list.size()];
		return list.toArray(result);
	}
	
	protected Double getResult(RunnerResult runnerResult) {
		return runnerResult.getPrizeMoney();
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
			return d1.compareTo(d2);
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
