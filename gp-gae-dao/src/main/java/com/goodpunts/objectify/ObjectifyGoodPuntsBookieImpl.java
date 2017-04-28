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

public class ObjectifyGoodPuntsBookieImpl implements BookieAccount {

	/** Just matches what you have placed */
	public Stake placeBet(Punt punt, double amount) {
		Stake stake = new Stake();
		stake.setAmount(amount);
		stake.setOdds(punt.getBookieOdds());
		return stake;
	}

	public double fetchAccountAmount() {
		ObjAccount account = ObjectifyService.ofy()
		          .load()
		          .type(ObjAccount.class) // We want to get our account
		          //.list();
		          .first()
		          .now();
			
		//Good Bookie sets the amount to $10,000 when the account has less than $100
		double amount = account.getAmount();
		if (amount < 1000) {
			amount = 10000;
			setAmount(amount);
		}
		return amount;
	}

	private void setAmount(double amount) {
		ObjAccount account = new ObjAccount();
		account.setAmount(amount);
		ObjectifyService.ofy().save().entity(account).now();
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
		List<Stake> stakes = new ArrayList<Stake>();
		List<Punt> punts = SpringRacingServices.getPuntingDAO().fetchPuntsForRace(race);
		for (Punt punt : punts) {
			stakes.addAll(punt.getStakes());
		}
		return stakes;
	}
	
	public List<Stake> getSettledBets(Date from) {
		return null;
	}
	
}
