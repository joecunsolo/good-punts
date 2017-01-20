package com.joe.springracing.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.business.model.Model;
import com.joe.springracing.dao.PuntingDAO;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Punt.Type;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class PuntingBusiness {

	private Model model;
	private PuntingDAO dao;
	
	private boolean puntOnWin;
	private boolean puntOnPlace;
	private boolean puntOnTrifecta;
	private boolean puntOnFlexi;
	
	public PuntingBusiness(PuntingDAO puntingDAO, Model model) {
		this.setModel(model);
		this.setDao(puntingDAO);
		updatePuntTypes(model.getAttributes().getGoodPuntTypes());
	}
	
	public void updatePuntTypes(Type[] goodPuntTypes) {
		for (Type t : goodPuntTypes) {
			if (Type.WIN.equals(t)) {
				this.setPuntOnWin(true);
			}
			if (Type.PLACE.equals(t)) {
				this.setPuntOnPlace(true);
			}
			if (Type.TRIFECTA.equals(t)) {
				this.setPuntOnTrifecta(true);
				this.setPuntOnFlexi(true);
			}
		}
	}


	public void generateGoodPuntsForMeet(Meeting upcoming) {
		try {
//			for (Meeting meet : upcoming) {
				List<Punt> punts = getGoodPuntsForMeet(upcoming);
				this.getDao().storePunts(upcoming, punts);
//			}
		} catch (Exception ex) {
			throw new RuntimeException("Unable to generate punts",  ex);
		}
	}

	public List<Punt> getGoodPuntsForMeet(Meeting meeting) throws Exception {
		List<Punt> puntsForMeet = new ArrayList<Punt>();
		for (Race r : meeting.getRaces()) {
			r.setRunners(this.getDao().fetchProbabilitiesForRace(r));
			List<Punt> puntsForRace = getGoodPuntsForRace(r);
			puntsForMeet.addAll(puntsForRace);
		}
		
		List<Punt> flexiBets = filterFlexiBets(puntsForMeet);
		Collections.sort(puntsForMeet, new PuntComparator(true));
		int punts = puntsForMeet.size() - flexiBets.size();
		puntsForMeet = puntsForMeet.subList(0, punts);
		
		if (model.getAttributes().getNumberOfPunts() < punts) {
			puntsForMeet = puntsForMeet.subList(0,  model.getAttributes().getNumberOfPunts());
		}
		if (isPuntOnFlexi()) {
			puntsForMeet.addAll(flexiBets);
		}
		
		return puntsForMeet;
	}

	private List<Punt> filterFlexiBets(List<Punt> puntsForMeet) {
		List<Punt> flexiBets = new ArrayList<Punt>();
		for (Punt p : puntsForMeet) {
			if (Type.TRIFECTA.equals(p.getType())) {
				flexiBets.add(p);
			}
		}
		return flexiBets;
	}

	public List<Punt> addWinAndPlacePunts(List<Punt> wins) {
		List<Punt> result = new ArrayList<Punt>();
		for (Punt punt : wins) {
			result.add(punt);
			if (Punt.Type.WIN.equals(punt.getType()) &&
					!containsPlacePunt(punt.getRunners().get(0), wins)) {
				Punt placePunt = getPlacePunt(punt);
				if (placePunt != null) {
					result.add(placePunt);
				}
			}
		} 
		return result;
	}
	
	public List<Punt> getPlacePunts(List<Punt> wins) {
		List<Punt> result = new ArrayList<Punt>();
		for (Punt punt : wins) {
			if (Punt.Type.WIN.equals(punt.getType()) &&
					!containsPlacePunt(punt.getRunners().get(0), wins)) {
				Punt placePunt = getPlacePunt(punt);
				if (placePunt != null) {
					result.add(placePunt);
				}
			}
		} 
		return result;
	}
	
	public Punt getPlacePunt(Punt punt) {
		Race race = punt.getRace();
		for (Runner runner : race.getRunners()) {
			if (runner.getNumber() == punt.getRunners().get(0).getNumber()) {
				double placeProbability = runner.getProbability().getPlace();
				double joePlaceOdds = probabilityToOdds(placeProbability);
				
				Punt p = new Punt(race, Type.PLACE, joePlaceOdds, runner.getOdds().getPlace());
				p.getRunners().add(runner);
				
				return p;
			}
		}
		return null;
	}

	public boolean containsPlacePunt(Runner runner, List<Punt> wins) {
		for (Punt punt : wins) {
			if (Punt.Type.PLACE.equals(punt.getType()) &&
					punt.getRunners().get(0).getNumber() == runner.getNumber()) {
				return true;
			}
		}
		return false;
	}

	public List<Punt> getGoodPuntsForRace(Race r) {
		List<Punt> punts = new ArrayList<Punt>();
		double bookieHigh =  model.getAttributes().getBookieHigh();
		
		Collections.sort(r.getRunners(), new ProbailityComparator());
		for (Runner runner : r.getRunners()) {
			double winProbability = runner.getProbability().getWin();
			double placeProbability = runner.getProbability().getPlace();
			
			double joeWinOdds = probabilityToOdds(winProbability);
			double joePlaceOdds = probabilityToOdds(placeProbability);
			
			if (isPuntOnWin() &&
					runner.getOdds().getWin() < bookieHigh &&
					runner.getOdds().getWin() - joeWinOdds * 2.5 > 0 &&
					!runner.isEmergency()) {
				Punt p = new Punt(r, Type.WIN, joeWinOdds, runner.getOdds().getWin());
				p.getRunners().add(runner);
				punts.add(p);
			}
			if (isPuntOnPlace() &&
					runner.getOdds().getPlace() < bookieHigh &&
					runner.getOdds().getPlace() - joePlaceOdds * 2.5 > 0 &&
					!runner.isEmergency()) {
				Punt p = new Punt(r, Type.PLACE, joePlaceOdds, runner.getOdds().getPlace());
				p.getRunners().add(runner);
				punts.add(p);
			}
		}
		
		if (isPuntOnTrifecta()) {
			Punt p = getBestTrifecta(r);
			if (p != null) {
				punts.add(p);
			}
		}
		
		return punts;
	}

	private Punt getBestTrifecta(Race r) {
		Punt firstThree = getTrifecta(r, 3);
		Punt firstFour = getTrifecta(r, 4);
		Punt firstFive = getTrifecta(r, 5);

		Punt bestPunt = firstThree;
		if (firstFive != null && firstFour != null && 
				firstFive.getJoesOdds() - firstFour.getJoesOdds() > 1 - firstFive.getJoesOdds()) {
			bestPunt = firstFive;
		}
		else if (firstFour != null && firstThree != null &&
				firstFour.getJoesOdds() - firstThree.getJoesOdds() > 1 - firstFour.getJoesOdds()) {
			bestPunt = firstFour;
		}
		if (bestPunt != null) {
			bestPunt.setJoesOdds(bestPunt.getJoesOdds());
		}
		return bestPunt;
	}
	
	public Punt getTrifecta(Race r, int number) {
		List<Runner> runners = r.getRunners();
		Collections.sort(runners, new ProbailityComparator());

		if (runners.size() < number) {
			return null;
		}
		
		double bookieOdds = 0;
		double probability = 0;
		List<Runner> puntRunner = new ArrayList<Runner>();
		for (int i = 0; i < number; i++) {
			Runner runner = runners.get(i);
			if (i == 1) {
				bookieOdds = runner.getOdds().getWin();				
			}
			if (i < 3) {
				bookieOdds *= runner.getOdds().getPlace();
			}
			probability += runner.getProbability().getWin();
			puntRunner.add(runner);
		}
		
		if (number == 3) {
			bookieOdds /= 6;
			//probability /= 6;
		} else if (number == 4) {
			bookieOdds /= 24;
			//probability /= 24;
		} else if (number == 5) {
			bookieOdds /= 60;
			//probability /= 60;
		}
		
		Punt p = new Punt(r, Type.TRIFECTA, probability, bookieOdds);
		p.setRunners(puntRunner);
		return p;
	}

	public double probabilityToOdds(double probability) {
		double joesWinOdds = probability / (1 - probability);
		joesWinOdds = 1 /probability;

		return joesWinOdds;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public boolean isPuntOnWin() {
		return puntOnWin;
	}

	public void setPuntOnWin(boolean puntOnWin) {
		this.puntOnWin = puntOnWin;
	}

	public boolean isPuntOnPlace() {
		return puntOnPlace;
	}

	public void setPuntOnPlace(boolean puntOnPlace) {
		this.puntOnPlace = puntOnPlace;
	}
	
	public boolean isPuntOnQuinella() {
		return true;
	}
	
	public boolean isPuntOnTrifecta() {
		return puntOnTrifecta;
	}

	public void setPuntOnTrifecta(boolean puntOnTrifecta) {
		this.puntOnTrifecta = puntOnTrifecta;
	}

	public boolean isPuntOnFlexi() {
		return puntOnFlexi;
	}

	public void setPuntOnFlexi(boolean puntOnFlexi) {
		this.puntOnFlexi = puntOnFlexi;
	}

	public PuntingDAO getDao() {
		return dao;
	}

	public void setDao(PuntingDAO dao) {
		this.dao = dao;
	}

	public class ProbailityComparator implements Comparator<Runner> {

		public int compare(Runner r1, Runner r2) {
			if (r1.getProbability().getWin() > r2.getProbability().getWin()) {
				return -1;
			}
			return 1;
		}
		
	}
	
	public class PuntComparator implements Comparator<Punt> {

		boolean flexiFlag;
		public PuntComparator(boolean flexiFlag) {
			this.flexiFlag = flexiFlag;
		}
		
		public int compare(Punt p0, Punt p1) {
			if (flexiFlag && Type.TRIFECTA.equals(p0.getType()) &&
					!Type.TRIFECTA.equals(p1.getType())) {
				return 1;
			}  
			if (flexiFlag && Type.TRIFECTA.equals(p1.getType()) &&
					!Type.TRIFECTA.equals(p0.getType())) {
				return -1;
			}
			if (p1.variance() - p0.variance() > 0) {
				return 1;
			}
			return -1;
		}
	}
}
