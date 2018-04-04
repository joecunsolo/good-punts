package com.goodpunts.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.objects.Pick;

@Entity
public class ObjPick extends Pick {
	
	@Id Long id;
	@Parent
	private Key<ObjRace> raceKey;

	public Key<ObjRace> getRaceKey() {
		return raceKey;
	}

	public void setRaceKey(Key<ObjRace> raceKey) {
		this.raceKey = raceKey;
	}
}
