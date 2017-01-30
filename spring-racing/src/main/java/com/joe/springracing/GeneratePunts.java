package com.joe.springracing;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.business.model.stats.SingleVariateStatistic;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class GeneratePunts {

	public static final boolean PRINT_STATISTICS = false;
	
	public static void main(String[] args) {
		try {
			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchExistingMeets();
		
			Model m = new Model(new ModelAttributes());
			for (Meeting meeting : meets) {
				if (meeting.getDate().getTime() > System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
					System.out.println();
					System.out.println(meeting.getDate() + " "  + meeting.getVenue());
					
					List<Race> races = SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
					meeting.setRaces(races);
					new ProbabilityBusiness(SpringRacingServices.getSpringRacingDAO(),
							SpringRacingServices.getPuntingDao(), 
							SpringRacingServices.getStatistics(),
							SpringRacingServices.getSimulator(),
							m).generateProbabilitiesForRaces(races);
					List<Punt> goodPunts = new PuntingBusiness(
							SpringRacingServices.getPuntingDao(),
							m).generateGoodPuntsForMeet(meeting);
					printGoodPunts(goodPunts);
					
					if (PRINT_STATISTICS) {
						printStatistics(meeting);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printStatistics(Meeting meeting) {
		GeneratePunts gp = new GeneratePunts();
		for (Race race : meeting.getRaces()) {
			System.out.println();
			System.out.println(race.getRaceNumber() + " " + race.getName());
			List<Runner> runners = race.getRunners();
			Collections.sort(runners, gp.new RunnersByProbability());
			for (Runner runner : race.getRunners()) {
				System.out.print(runner.getNumber() + " " + runner.getHorse() + " " + runner.getProbability().getWin() + " " + runner.getProbability().getPlace() + " ");
//				for (RunnerResult result : runner.getHorse().getPastResults()) {
//					System.out.print(result.getPosition() + ",");
//				}
				SingleVariateStatistic stat = (SingleVariateStatistic)runner.getStatistics().get(0);
				System.out.println(" " + stat.getMean());
			}
		}
	}

	public class RunnersByProbability implements Comparator<Runner> {

		public int compare(Runner arg0, Runner arg1) {
			if (arg0.getProbability().getPlace() > arg1.getProbability().getPlace()) {
				return -1;
			}
			return 1;
		}
		
	}

	public static void printGoodPunts(List<Punt> goodPunts) {
		for (Punt punt : goodPunts) {
			System.out.print(punt.getRace().getRaceNumber() + " " + 
					punt.getType() + " " + 
					punt.getJoesOdds() + " " +
					punt.getBookieOdds());
			printHorses(punt.getRunners());
			System.out.println();
		}
	}
	
	public static void printHorses(List<Runner> horses) {
		System.out.print("[");
		for (Runner horse : horses) {
			System.out.print(horse.getNumber() + "{" + horse.getHorse() + "},");
		}
		System.out.print("]");
	}
}
