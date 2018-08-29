package com.goodpunts.api;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joe.springracing.business.HorseBusiness;
import com.joe.springracing.objects.Horse;

@RestController
@RequestMapping("/horses")
public class HorseController {

	@RequestMapping (value = "/{horse}", method = RequestMethod.GET, headers="Accept=application/json")
	public Horse fetchHorse(@PathVariable String horse) {
		HorseBusiness business = new HorseBusiness();
		return business.fetchHorse(horse);
	}
	
	@RequestMapping (value = "/", method = RequestMethod.GET, headers="Accept=application/json")
	public List<Horse> fetchHorses(@RequestParam(value="splits", defaultValue="true") boolean splits,
			@RequestParam(value="histories", defaultValue="true") boolean histories) {
		HorseBusiness business = new HorseBusiness();
		return business.fetchHorses(histories, splits);
	}
}
