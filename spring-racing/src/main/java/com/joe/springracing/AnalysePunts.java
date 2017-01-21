package com.joe.springracing;

import java.util.List;

import com.joe.springracing.business.AnalysePuntBusiness;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;

public class AnalysePunts {

	public static void main(String[] args) {
		try {
			List<Meeting> meets = SpringRacingServices.getSpringRacingDAO().fetchExistingMeets();
			Model m = new Model(new ModelAttributes());
			double returnOnPunts = 0;
			int totalPunts = 0;
			for (Meeting meeting : meets) {
				System.out.println();
				System.out.println(meeting.getDate() + " "  + meeting.getVenue() + "(" + meeting.getMeetCode() + ")");
				
				new ProbabilityBusiness(SpringRacingServices.getSpringRacingDAO(),
						SpringRacingServices.getPuntingDao(), 
						SpringRacingServices.getStatistics(),
						SpringRacingServices.getSimulator(),
						m).generateProbabilitiesForRaces(meeting.getRaces());
				PuntingBusiness pb = new PuntingBusiness(
						SpringRacingServices.getPuntingDao(),
						m);
				List<Punt> goodPunts = pb.getGoodPuntsForMeet(meeting);
				//goodPunts = new PuntingBusiness(m).addWinAndPlacePunts(goodPunts);
				goodPunts = pb.getPlacePunts(goodPunts);
				
				
				try {
					double returnOnPunt = new AnalysePuntBusiness(
							SpringRacingServices.getSpringRacingDAO()).getReturnOnGoodPunts(goodPunts);
					returnOnPunts += returnOnPunt;
					totalPunts += goodPunts.size();
					System.out.println(returnOnPunt + "/" + goodPunts.size());
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
			System.out.println("----------------------");
			System.out.println("Total return: " + returnOnPunts);
			System.out.println("Punts: " + totalPunts);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
