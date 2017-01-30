package com.goodpunts.objectify;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.objects.Race;

/**
 * The set of good punts for a Race
 * @author joe.cunsolo
 *
 */
@Entity
public class ObjPuntEvent {

	@Parent
	Key<ObjRace> race;
	@Id
	Long id;
	@Index
	Date date;
	
	public ObjPuntEvent() {}
	
	public ObjPuntEvent(Race race) {
		this(race.getRaceCode());
	}
	
	public ObjPuntEvent(String raceCode) {
		race = Key.create(ObjRace.class, raceCode);	
		date = new Date();
	}
	
	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}
}
