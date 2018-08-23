package com.goodpunts.objectify;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.joe.springracing.objects.Horse;

@Entity
public class ObjHorse extends Horse {

	@Id
	private String id2;
	@Index
	private boolean histories;
	@Index
	private boolean hasSplits = false;
	
	public ObjHorse() {}
	
	public String getId() {
		return id2;
	}
	public void setId(String id) {
		this.id2 = id;
	}

	public void setHistories(boolean histories) {
		this.histories = histories;
	}
	
	public boolean hasHistories() {
		return histories;
	}

	public void setHasSplits(boolean splits) {
		this.hasSplits = splits;
	}
	
	public boolean hasSplits() {
		return hasSplits;
	}

}
