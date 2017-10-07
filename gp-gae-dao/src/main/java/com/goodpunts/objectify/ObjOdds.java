package com.goodpunts.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.objects.Odds;
import com.joe.springracing.objects.Runner;

@Entity
public class ObjOdds extends Odds {

	@Id Long id;
	@Parent Key<ObjRunner> runner;
	private String horse;
	
	public ObjOdds() {}
	
	public ObjOdds(Key<ObjRunner> runnerKey, Runner r) {
		runner = runnerKey;
		this.setHorse(r.getHorse());
	}
	
	public String getHorse() {
		return horse;
	}

	public void setHorse(String horse) {
		this.horse = horse;
	}
}
