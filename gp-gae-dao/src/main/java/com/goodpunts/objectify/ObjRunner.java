package com.goodpunts.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class ObjRunner {

	@Id
	private String horse;
	@Parent Key<ObjRace> race;
	private String jockey;
	private String trainer;
	private int number;
	private boolean scratched;
	private boolean emergency;
	
	public ObjRunner() {}
	
	public ObjRunner(String raceCode) {
		race = Key.create(ObjRace.class, raceCode);
	}
	
	public String getHorse() {
		return horse;
	}
	public void setHorse(String horse) {
		this.horse = horse;
	}
	public String getJockey() {
		return jockey;
	}
	public void setJockey(String jockey) {
		this.jockey = jockey;
	}
	public String getTrainer() {
		return trainer;
	}
	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean isScratched() {
		return scratched;
	}
	public void setScratched(boolean scratched) {
		this.scratched = scratched;
	}
	public boolean isEmergency() {
		return emergency;
	}
	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

}
