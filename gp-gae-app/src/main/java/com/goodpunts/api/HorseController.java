package com.goodpunts.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.joe.springracing.business.HorseBusiness;
import com.joe.springracing.objects.Horse;

@RestController
@RequestMapping("/horses")
public class HorseController {

	@RequestMapping (value = "/{horse}", method = RequestMethod.GET, headers="Accept=application/json")
	public Horse meet(@PathVariable String horse) {
		HorseBusiness business = new HorseBusiness();
		return business.fetchHorse(horse);
	}
}
