package com.goodpunts.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.objects.Punt;

@RestController
@RequestMapping("/punts/sure")
public class SurePuntController {

	@RequestMapping(method = RequestMethod.GET, headers="Accept=application/json")
    public List<Punt> surePunts() {
		PuntingBusiness business = new PuntingBusiness();
		return business.fetchSurePunts();
    }
	
	@RequestMapping (value = "/generate", method = RequestMethod.GET, headers="Accept=application/json")
	public void generate() {
		PuntingBusiness business = new PuntingBusiness();
		business.generateSurePunts();
	}
}
