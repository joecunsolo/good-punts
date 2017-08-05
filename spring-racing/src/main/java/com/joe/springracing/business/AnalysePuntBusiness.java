package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.dao.SpringRacingDAO;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;

public class AnalysePuntBusiness extends AbstractSpringRacingBusiness {
	
	public AnalysePuntBusiness(SpringRacingDAO dao) {
		super(new PrintWriter(System.out));
	}
	
	public double getReturnOnGoodPunts(List<Punt> goodPunts) throws Exception {
		if (goodPunts.size() == 0) {
			return 0;
		}
		if (goodPunts.get(0).getDate().getTime() > System.currentTimeMillis() - 12 * 60 * 60 * 1000) {
			throw new Exception("Future Punt");
		}
		double returnOnPunts = 0;
		String raceCode = goodPunts.get(0).getRaceCode();
		
		for (Punt punt : goodPunts) {
//			int raceNumber = punt.getRace().getRaceNumber();
			int[] result = new int[0];
			result = getResultsForRace(raceCode);
			//System.out.println("Unable to get results for " + meetCode + " " + raceNumber);
			
			
			double returnOnPunt = getReturn(result, punt);
			if (returnOnPunt > 0) {
				getWriter().println(raceCode + " " + 
						punt.getRunners().get(0).getNumber() + ":" + 
						punt.getRunners().get(0).getHorse() + " " + 
						returnOnPunt + " " +
						punt.getJoesOdds());
			}
			returnOnPunts += returnOnPunt;
		}
		return returnOnPunts;
	}

	public int[] getResultsForRace(String raceCode) throws Exception {
		Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
		return race.getResult();
//		RaceResult raceResult = race.getResult();
//		int[] result = new int[raceResult.getRunners().size()];
//		for (RunnerResult runner : raceResult.getRunners()) {
//			int finish = runner.getResult();
//			if (!runner.isScratched() && finish <= 3) {
//				int number = runner.getNumber(); 
//				result[finish - 1] = number;
//			}
//		}
//		return result;
	}

	private double getReturn(int[] result, Punt punt) {
		switch (punt.getType()) {
			case WIN : {
				return winAndPlace(result, punt);
			}
			case PLACE : {
				return place(result, punt);
			}
			default: {
				break;
			}
		}
		return 0;
	}

	private double place(int[] result, Punt punt) {
		for (int i = 0; i < result.length; i++) {
			int number = punt.getRunners().get(0).getNumber();
			if (result[i] == number) {
				return punt.getBookieOdds();
			}
		}
		return 0;
	}

	//TODO Need to know what the place odds are in order to do this
	private double winAndPlace(int[] result, Punt punt) {
		//place
		//double winAndPlace = place(result, punt);
		//win
		if (result[0] == punt.getRunners().get(0).getNumber()) {
			return punt.getBookieOdds();
		}
		return 0;//winAndPlace;
	}


}
