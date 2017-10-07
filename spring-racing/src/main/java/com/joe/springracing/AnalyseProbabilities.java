package com.joe.springracing;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.model.AnalysableObjectStatistic;
import com.joe.springracing.business.model.stats.SingleVariateStatistic;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class AnalyseProbabilities {

	public static void main(String[] args) {
		
		try {
			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchExistingMeets();
		
//			Model m = new Model(new ModelAttributes());
			for (Meeting meeting : meets) {
				if (meeting.getDate().getTime() > System.currentTimeMillis() - 96 * 60 * 60 * 1000) {
					System.out.println();
					System.out.println(meeting.getDate() + " "  + meeting.getVenue());
					
					List<Race> races = SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
					new ProbabilityBusiness().generate(races);
					
					printAndSortMeet(meeting);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printAndSortMeet(Meeting meeting) {
		AnalyseProbabilities ap = new AnalyseProbabilities();
//		for (Race race : meeting.getRaces()) {
//			System.out.println();
//			System.out.println(race.getRaceNumber() + " " + race.getName());
//
//			List<Runner> runners = race.getRunners();
//			Collections.sort(runners, ap.new RunnerProbabilitySort());
//			
////			printRunners(runners);
//		}
	}
	
	private static void printRunners(List<List<AnalysableObjectStatistic>> runners) {
		for (List<AnalysableObjectStatistic> stats : runners) {
			if (stats.get(0) instanceof SingleVariateStatistic) {
				SingleVariateStatistic svs = (SingleVariateStatistic)stats.get(0);
//				System.out.println(runner.getNumber() + " " + 
//						runner.getHorse() + " " + 
//						runner.getProbability().getWin() + " " + 
//						svs.getMean() + " " + 
//						svs.getStandardDeviation() + " " + 
//						runner.getOdds().getWin());
			}
		}
	}

	class RunnerProbabilitySort implements Comparator<Runner> {

		public int compare(Runner o1, Runner o2) {
			return o1.getProbability().getWin() > o2.getProbability().getWin() ? -1 : 1;
		}
		
	}
}
