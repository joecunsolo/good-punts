package com.goodpunts.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.BettingBusiness;
import com.joe.springracing.business.MeetBusiness;
import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Punt.Type;

@RestController
@RequestMapping("/punts/good")
public class PuntController {

	@RequestMapping(method = RequestMethod.GET, headers="Accept=application/json")
    public List<Punt> meets(@RequestParam(value="meet") String meet) {
		MeetBusiness meetBusiness = new MeetBusiness();
		Meeting meeting = meetBusiness.fetchMeet(meet);
		
		PuntingBusiness business = new PuntingBusiness();
		return business.fetchPuntsForMeet(meeting);
    }
	
	
	@RequestMapping (value = "/place", method = RequestMethod.GET, headers="Accept=application/json")
	public void place(@RequestParam(value="race") String raceCode, 
			@RequestParam(value="horses") String[] horseCodes,
			@RequestParam(value="type") String type,
			@RequestParam(value="amount") double amount) throws Exception {
		
		Punt punt = SpringRacingServices.getPuntingService().punt(Type.valueOf(type), raceCode, horseCodes);
		
		//And then place bets on them
		try {
			BettingBusiness bet = new BettingBusiness();
			bet.placeBet(punt, amount);
		} catch (Exception ex) {
			throw new RuntimeException("Unable to place bets", ex);
		}
	}
}
