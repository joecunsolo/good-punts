package com.joe.springracing.test.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joe.springracing.dao.PuntingDAO;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Punt.State;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class MockPuntingDao implements PuntingDAO {

	List<Punt> punts = new ArrayList<Punt>();

	public List<Runner> fetchProbabilitiesForRace(Race race) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Punt> fetchPuntsForMeet(Meeting meet) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void storePunts(Race race, List<Punt> punts) {
		// TODO Auto-generated method stub

	}

	public void storeProbabilities(Race race) {
		// TODO Auto-generated method stub

	}

	public List<Punt> fetchPuntsForRace(Race race) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Date fetchLastPuntEvent(Race r) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Punt> fetchPuntResults() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Punt> fetchSettledPunts() {
		List<Punt> open = new ArrayList<Punt>();
		for (Punt punt : punts) {
			if (State.SETTLED.equals(punt.getState())) {
				open.add(punt);
			}
		}
		return open;
	}

	public List<Punt> fetchOpenPunts() {
		List<Punt> open = new ArrayList<Punt>();
		for (Punt punt : punts) {
			if (State.OPEN.equals(punt.getState())) {
				open.add(punt);
			}
		}
		return open;
	}

	public void updateStakes(Punt punt) {
		punts.add(punt);
	}

	public void flush() {
		punts = new ArrayList<Punt>();
	}

}
