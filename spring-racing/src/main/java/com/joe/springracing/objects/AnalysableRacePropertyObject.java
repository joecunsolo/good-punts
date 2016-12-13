package com.joe.springracing.objects;

import java.util.Properties;

import com.joe.springracing.business.model.AnalysableObject;

public abstract class AnalysableRacePropertyObject extends RacingObject implements AnalysableObject {

	protected AnalysableRacePropertyObject(Properties props) {
		super(props);
	}
	
	@Override
	public boolean equals(Object o) {
		if (getId() == null) {
			return false;
		}
		if (o instanceof AnalysableObject) {
			AnalysableObject so = ((AnalysableObject)o);
			if (so.getId() == null) {
				return false;
			}
			return so.getId().equals(getId());
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

}
