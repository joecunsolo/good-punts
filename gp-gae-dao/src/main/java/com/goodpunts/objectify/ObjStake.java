package com.goodpunts.objectify;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.joe.springracing.objects.Stake;

@Entity
public class ObjStake extends Stake {
	
	@Id
	public long theId;
	@Index
	private boolean settled;
	@Index
	private String raceCode;
	
	public ObjStake(int id) {
		this.theId = id;
	}
	
	public ObjStake() {
	}

	public long getId() {
		return theId;
	}

	public void setId(long id) {
		this.theId = id;
	}

	public boolean isSettled() {
		return settled;
	}

	public void setSettled(boolean settled) {
		this.settled = settled;
	}
	
	public String getRaceCode() {
		return raceCode;
	}
	
	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}
}
