package com.joe.springracing;

import com.joe.springracing.dao.SpringRacingDAO;

public class AbstractSpringRacingBusiness {

	private SpringRacingDAO springRacingDAO;

	public AbstractSpringRacingBusiness(SpringRacingDAO dao) {
		setSpringRacingDAO(dao);
	}
	
	public SpringRacingDAO getSpringRacingDAO() {
		return springRacingDAO;
	}

	public void setSpringRacingDAO(SpringRacingDAO springRacingDAO) {
		this.springRacingDAO = springRacingDAO;
	}
}
