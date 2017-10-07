package com.joe.springracing.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Punt.State;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.Stake;
import com.joe.springracing.objects.Punt.Confidence;
import com.joe.springracing.objects.Punt.Type;

public class PuntingServiceImpl implements PuntingService {

	private Model model;
	private boolean puntOnWin;
	private boolean puntOnPlace;
	private boolean puntOnTrifecta;
	private boolean puntOnFlexi;

	public PuntingServiceImpl(Model model) {
		this.setModel(model);
		updatePuntTypes(model.getAttributes().getGoodPuntTypes());
	}
	
	public PuntingServiceImpl() {
		this(new Model(new ModelAttributes()));
	}

	public List<Punt> generate(Race race) throws Exception {
		//race.setRunners(SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(race));
		List<Punt> puntsForRace = getAllGoodPuntsForRace(race);
		
		List<Punt> flexiBets = filterFlexiBets(puntsForRace);
		Collections.sort(puntsForRace, new PuntComparator(true));
		int punts = puntsForRace.size() - flexiBets.size();
		puntsForRace = puntsForRace.subList(0, punts);
		
		if (model.getAttributes().getNumberOfPunts() < punts) {
			puntsForRace = puntsForRace.subList(0,  model.getAttributes().getNumberOfPunts());
		}
		if (isPuntOnFlexi()) {
			puntsForRace.addAll(flexiBets);
		}
		
		return puntsForRace;
	}
	
//	public List<Punt> generate(Meeting meet) throws Exception {
//		List<Punt> punts = new ArrayList<Punt>();
//		for (Race race : meet.getRaces()) {
//			punts.addAll(generate(race));
//		}
//		return punts;
//	}
	
	private void updatePuntTypes(Type[] goodPuntTypes) {
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

	private List<Punt> filterFlexiBets(List<Punt> puntsForMeet) {
		List<Punt> flexiBets = new ArrayList<Punt>();
		for (Punt p : puntsForMeet) {
			if (Type.TRIFECTA.equals(p.getType())) {
				flexiBets.add(p);
			}
		}
		return flexiBets;
	}
	
//	private List<Punt> getPlacePunts(List<Punt> wins) {
//		List<Punt> result = new ArrayList<Punt>();
//		for (Punt punt : wins) {
//			if (Punt.Type.WIN.equals(punt.getType()) &&
//					!containsPlacePunt(punt.getRunners().get(0), wins)) {
//				Punt placePunt = getPlacePunt(punt);
//				if (placePunt != null) {
//					result.add(placePunt);
//				}
//			}
//		} 
//		return result;
//	}
//	
//	private Punt getPlacePunt(Race race, Punt punt) {
//		for (Runner runner : race.getRunners()) {
//			if (runner.getNumber() == punt.getRunners().get(0).getNumber()) {
//				double placeProbability = runner.getProbability().getPlace();
//				double joePlaceOdds = probabilityToOdds(placeProbability);
//				
//				Confidence c = calcConfidence(joePlaceOdds, punt.getBookieOdds());
//				Punt p = new Punt(race, Type.PLACE, joePlaceOdds, runner.getOdds().getPlace(), c);
//				p.getRunners().add(runner);
//				
//				return p;
//			}
//		}
//		return null;
//	}
//
//	private boolean containsPlacePunt(Runner runner, List<Punt> wins) {
//		for (Punt punt : wins) {
//			if (Punt.Type.PLACE.equals(punt.getType()) &&
//					punt.getRunners().get(0).getNumber() == runner.getNumber()) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	private List<Punt> getAllGoodPuntsForRace(Race r) {
		List<Punt> punts = new ArrayList<Punt>();
		//Not enough horses with experience to bet on this
		if (r.getNumberOfRunnersLessThan3Races() > model.getAttributes().getMinimumThreeRaceHorses()) {
			return punts;
		}
		
		double bookieHigh =  model.getAttributes().getBookieHigh();
		
		Collections.sort(r.getRunners(), new ProbailityComparator());
		for (Runner runner : r.getRunners()) {
			try {
				Horse horse = SpringRacingServices.getSpringRacingDAO().fetchHorse(runner.getHorse());
				double winProbability = runner.getProbability().getWin();
				double placeProbability = runner.getProbability().getPlace();
				
				double joeWinOdds = probabilityToOdds(winProbability);
				double joePlaceOdds = probabilityToOdds(placeProbability);
				
				if (isPuntOnWin() &&
						runner.getOdds().getWin() < bookieHigh &&
						runner.getOdds().getWin() - joeWinOdds * 2.5 > 0 &&
						!runner.isEmergency() &&
						horse.getNumberOfRaces() > 1 &&
						horse.getSpell() < model.getAttributes().getLongSpell() &&
						joeWinOdds < 5) {
					Confidence c = calcConfidence(joeWinOdds, runner.getOdds().getWin());
					Punt p = new Punt(r.getRaceCode(), r.getDate(), Type.WIN, joeWinOdds, runner.getOdds().getWin(), c, State.OPEN);
					p.setRaceNumber(r.getRaceNumber());
					p.setVenue(r.getVenue());
					p.getRunners().add(runner);
					punts.add(p);
				}
				if (isPuntOnPlace() &&
						runner.getOdds().getPlace() < bookieHigh &&
						runner.getOdds().getPlace() - joePlaceOdds * 2.5 > 0 &&
						!runner.isEmergency() &&
						horse.getNumberOfRaces() > 1 &&
						horse.getSpell() < model.getAttributes().getLongSpell() &&
						joePlaceOdds < 3) {
					Confidence c = calcConfidence(joePlaceOdds, runner.getOdds().getPlace());
					Punt p = new Punt(r.getRaceCode(), r.getDate(), Type.PLACE, joePlaceOdds, runner.getOdds().getPlace(), c, State.OPEN);
					p.setRaceNumber(r.getRaceNumber());
					p.setVenue(r.getVenue());
					p.getRunners().add(runner);
					punts.add(p);
				}
			} catch (Exception e) {
				e.printStackTrace();
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
	

	/**
	 * Sets the Confidence {@link #setConfidence(Punt)} of the punt if it is not already set
	 * and uses the value of any existing punts to determine the stake
	 * 
	 * The maximum stake is the lower of the max dollar or max percentage stake
	 * The minimum stake is the max of the min dollar or min percentage stake		
	 * 
	 * The stake amount equals the calculated stake amount minus what is already staked
	 * but not less than the minimum dollar stake
	 * 
	 * @param goodPunt a good Punt that is worth betting on
	 * @return the amount that should be staked on this punt
	 */
	public double calculateStake(Punt goodPunt, List<Stake> existingPunt, double accountAmount) {
		ModelAttributes ma = this.getModel().getAttributes();
		double existingStake = calculateExistingStake(existingPunt);
		
		double stake = 0;		
		if (Confidence.HIGH.equals(goodPunt.getConfidence())) {
			stake = ma.getMaximumPercentageStake() * accountAmount / 100;
			//The maximum stake is the lower of the dollar or percentage stake
			stake = stake > ma.getMaximumDollarStake() ?  
					ma.getMaximumDollarStake() : stake;
		} else {
			stake = ma.getMinimumPercentageStake() * accountAmount / 100;
			//The maximum stake is the max of the dollar or percentage stake
			stake = stake > ma.getMinimumDollarStake() ?  
					stake : ma.getMinimumDollarStake();
		}

		// The stake amount equals the calculated stake amount minus what is already staked
		if (stake > existingStake) {
			stake = stake - existingStake;
			//can't ever bet less than the minimum dollar stake
			if (stake < ma.getMinimumDollarStake()) {
				stake = 0;
			}
		} else {
			//already staked too much
			stake = 0;
		}
		//TODO A stake should be in some worthwhile increment
		return stake;
	}
	
	/** Loops through the existing Stakes for the Punt to determine how much is already staked */
	public double calculateExistingStake(List<Stake> existingPunt) {
		if (existingPunt == null) {
			return 0;
		}
		double amount = 0;
		for (Stake stake : existingPunt) {
			amount += stake.getAmount();
		}
		return amount;
	}


	/** uses {@link #hasHighConfidence(Punt)} to set the confidence of the Punt */
	public Confidence calcConfidence(double joesOdds, double bookieOdds) {
		if (hasHighConfidence(joesOdds, bookieOdds)) {
			return Confidence.HIGH;
		} else {
			return Confidence.LOW;
		}
	}
	
	/** 
	 * Determines if the Punt is a High Confidence punt
	 * @param aGoodPunt
	 * @return true if {@link Punt#getJoesOdds()} < {@link ModelAttributes#getHighJoeConfidence()}
	 * and {@link Punt#getBookieOdds()} < {@link ModelAttributes#getHighBookieConfidence()}
	 * false Otherwise
	 */
	public boolean hasHighConfidence(double joesOdds, double bookieOdds) {
		return joesOdds < this.getModel().getAttributes().getHighJoeConfidence() &&
				bookieOdds < this.getModel().getAttributes().getHighBookieConfidence();
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
	
	private Punt getTrifecta(Race r, int number) {
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
		
		Confidence c = calcConfidence(probability, bookieOdds);
		Punt p = new Punt(r.getRaceCode(), r.getDate(), Type.TRIFECTA, probability, bookieOdds, c, State.OPEN);
		p.setRaceNumber(r.getRaceNumber());
		p.setVenue(r.getVenue());
		p.setRunners(puntRunner);
		return p;
	}

	private double probabilityToOdds(double probability) {
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
