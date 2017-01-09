package com.joe.springracing.dao.datasource;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Runner;

public class RacingDotComRunner extends Runner {

	private Horse horseObject;

	public Horse getHorseObject() {
		return horseObject;
	}

	public void setHorseObject(Horse horseObject) {
		this.horseObject = horseObject;
	}
}
