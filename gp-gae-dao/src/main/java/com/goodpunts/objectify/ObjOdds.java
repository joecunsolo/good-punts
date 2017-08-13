package com.goodpunts.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.objects.Runner;

@Entity
public class ObjOdds {

	@Id Long id;
	@Parent Key<ObjRunner> runner;
	private String horse;
	private double win;
	private double place;
	
	public ObjOdds() {}
	
	public ObjOdds(Key<ObjRunner> runnerKey, Runner r) {
		runner = runnerKey;
		this.setPlace(r.getOdds().getPlace());
		this.setWin(r.getOdds().getWin());
		this.setHorse(r.getHorse());
	}
	
	public double getWin() {
		return win;
	}
	public void setWin(double win) {
		this.win = win;
	}
	public double getPlace() {
		return place;
	}
	public void setPlace(double place) {
		this.place = place;
	}

	public String getHorse() {
		return horse;
	}

	public void setHorse(String horse) {
		this.horse = horse;
	}
}
