package com.goodpunts.objectify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.account.BookieAccount;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Stake;

/** This is a simple mock bookie - 
 * Who pays out what we say the odds are*/
public class ObjectifyGoodPuntsBookieImpl implements BookieAccount {

	public static final String ACCOUNT = "OBJECTIFY";
	
	/** Just matches what you have placed */
	public Stake placeBet(Punt punt, double amount) {
		try {
			Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(punt.getRaceCode());
			if (race.getResult() != null) {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Stake stake = new Stake();
		stake.setAmount(amount);
		stake.setOdds(punt.getBookieOdds());
		stake.setDate(new Date());
		stake.setAccount(ACCOUNT);
		stake.setTxnNo(String.valueOf(Math.random()));
		stake.setVenue(punt.getVenue());
		stake.setBookieOdds(punt.getBookieOdds());
		stake.setConfidence(punt.getConfidence());
		stake.setJoesOdds(punt.getJoesOdds());
		stake.setRaceCode(punt.getRaceCode());
		stake.setRaceNumber(punt.getRaceNumber());
		stake.setResult(punt.getResult());
		stake.setRunners(punt.getRunners());
		stake.setState(punt.getState());
		stake.setType(punt.getType());
		
		deductAccount(stake);
		return stake;
	}

	private void deductAccount(Stake stake) {
		double accountAmount = fetchAccountAmount();
		accountAmount -= stake.getAmount();
		setAmount(accountAmount);
		
		stake.setBalance(accountAmount);
	}
	
	private void creditAccount(Stake stake) {
		double accountAmount = fetchAccountAmount();
		accountAmount += stake.getReturn();
		setAmount(accountAmount);
		
		stake.setBalance(accountAmount);
	}
		
	public double fetchAccountAmount() {
		ObjAccount account = ObjectifyService.ofy()
		          .load()
		          .type(ObjAccount.class) // We want to get our account
		          //.list();
		          .first()
		          .now();
			
		//Good Bookie sets the amount to $10,000 when the account has less than $100
		if (account == null || account.getAmount() < 1000) {
			account = setAmount(10000);
		}
		return account.getAmount();
	}

	private ObjAccount setAmount(double amount) {
		ObjAccount account = new ObjAccount();
		account.setAmount(amount);
		ObjectifyService.ofy().save().entity(account).now();
		return account;
	}

	public List<Stake> getAllBets(Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Stake> getOpenBets() throws Exception {
		List<Stake> stakes = new ArrayList<Stake>();
		List<Race> races = SpringRacingServices.getSpringRacingDAO().fetchRacesWithoutResults();
		for (Race race : races) {
			stakes.addAll(getStakesForRace(race));
		}
		return stakes;
	}
	
	private List<Stake> getStakesForRace(Race race) throws Exception {
		return SpringRacingServices.getPuntingDAO().fetchStakesForRace(race);
	}
	
	/** 
	 * Gets the list of open stakes.
	 * If the race has results, the stake is settled and returned
	 */
	public List<Stake> getSettledBets(Date from) throws Exception {
		List<Stake> stakes = SpringRacingServices.getPuntingDAO().fetchOpenStakes();
		List<Stake> result = new ArrayList<Stake>();
		for (Stake stake : stakes) {
			Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(stake.getRaceCode());
			//Only settle races that have been run
			if (race.getResult() != null) {
				result.addAll(settleRace(race));
			}
		}
		return result;
	}

	private List<Stake> settleRace(Race race) throws Exception {
		List<Stake> result = getStakesForRace(race);
		for (Stake stake : result) {
			settleStake(race.getResult(), stake);
		}
		return result;
	}

	/** If the Punt wins then pay out what was staked */
	private void settleStake(int[] positions, Stake stake) {
		stake.setResult(Punt.Result.LOSS);
		if (Punt.Type.WIN.equals(stake.getType()) &&
				stake.getRunners().get(0).getNumber() == positions[0]) {
			stake.setResult(Punt.Result.WIN);
		} else 
		if	(Punt.Type.PLACE.equals(stake.getType()) &&
				(stake.getRunners().get(0).getNumber() == positions[0] ||
				stake.getRunners().get(0).getNumber() == positions[1] ||
				stake.getRunners().get(0).getNumber() == positions[2])) {
			stake.setResult(Punt.Result.WIN);
		}
		if (Punt.Result.WIN.equals(stake.getResult())) {
			stake.setReturn(stake.getAmount() * stake.getOdds());
		}
		creditAccount(stake);		
	}
	
}
