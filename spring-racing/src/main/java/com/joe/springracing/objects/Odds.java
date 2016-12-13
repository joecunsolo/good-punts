package com.joe.springracing.objects;

import static com.joe.springracing.business.RacingKeys.*;

import java.util.Properties;

public class Odds extends RacingObject {

	public Odds(Properties props) {
		super(props);
	}
	
	public double getWin() {
		return getDouble(KEY_ODDS_WIN);
	}
	
	public double getPlace() {
		return getDouble(KEY_ODDS_PLACE);		
	}
}
