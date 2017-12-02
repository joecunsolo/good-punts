package com.goodpunts.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joe.springracing.business.MeetBusiness;
import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;

@RestController
@RequestMapping("/punts")
public class PuntController {

	@RequestMapping(method = RequestMethod.GET, headers="Accept=application/json")
    public List<Punt> meets(@RequestParam(value="meet") String meet) {
		MeetBusiness meetBusiness = new MeetBusiness();
		Meeting meeting = meetBusiness.fetchMeet(meet);
		
		PuntingBusiness business = new PuntingBusiness();
		return business.fetchPuntsForMeet(meeting);
    }
}
