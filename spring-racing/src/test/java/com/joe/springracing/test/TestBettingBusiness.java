package com.joe.springracing.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.account.BookieAccount;
import com.joe.springracing.business.BettingBusiness;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.objects.Punt;
import com.joe.springracing.objects.Punt.Confidence;
import com.joe.springracing.objects.Punt.State;
import com.joe.springracing.objects.Punt.Type;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.objects.Stake;
import com.joe.springracing.test.mock.MockBookieAccount;
import com.joe.springracing.test.mock.MockPuntingDao;

import junit.framework.TestCase;

public class TestBettingBusiness extends TestCase {

	private static double DEFAULT_BOOKIE_ODDS = 20.0;
	private static double DEFAULT_JOE_ODDS = 5.0;
	
	public void setUp() {
		SpringRacingServices.setBookieAccount(new MockBookieAccount());
		SpringRacingServices.setPuntingDAO(new MockPuntingDao());
	}
	
	private static Punt getGoodPunt() {
		return getGoodPunt(DEFAULT_BOOKIE_ODDS, DEFAULT_JOE_ODDS);
	}
	
	private static Punt getGoodPunt(double bookieOdds, double joesOdds) {
		Punt punt = new Punt();
		Runner runner = new Runner();
		runner.setHorse("punt");
		List<Runner> runners = new ArrayList<Runner>();
		runners.add(runner);		
		punt.setRunners(runners);

		Race race = new Race();
		race.setDate(new Date(System.currentTimeMillis()));
		race.setVenue("venue");
		punt.setRace(race);
		
		punt.setBookieOdds(bookieOdds);
		punt.setJoesOdds(joesOdds);
		punt.setType(Type.WIN);
		punt.setState(State.OPEN);
		return punt;
	}
	
	private static List<Punt> getGoodPuntAsList() {
		return getGoodPuntAsList(getGoodPunt());
	}

	private static List<Punt> getGoodPuntAsList(Punt p) {
		List<Punt> punts = new ArrayList<Punt>();
		punts.add(p);
		return punts;
	}
	
	/**
	 * Given there is a good Punt
	 * And there is no existing Stake for that Punt
	 * When there is a request to Stake the Punt
	 * And the Stake is matched by the bookie 
	 * Then a Stake should be placed for the Punt
	 */
	public static void testPuntIsPlaced() {
		BettingBusiness biz = new BettingBusiness();
		List<Punt> punts = biz.placeBets(getGoodPuntAsList());
		Stake stake = punts.get(0).getStakes().get(0);
		Assert.assertNotNull(stake);
	}
	
	/////////Stake Calculator///////////////
	/**
	 * Given there are two good Punts
	 * And the Punts are different
	 * And the two Punts have the same EV
	 * When the Stake amounts are calculated
	 * Then the Stake amounts should be the same
	 */
	public static void testSameEVEqualsSameStake() {
		BettingBusiness biz = new BettingBusiness();
		
		//Given there are two good Punts
		Punt goodPunt1 = getGoodPunt();
		Punt goodPunt2 = getGoodPunt();

		//And the two Punts have the same EV
		double ev1 = goodPunt1.getBookieOdds() * goodPunt1.getJoesOdds();
		double ev2 = goodPunt2.getBookieOdds() * goodPunt2.getJoesOdds();
		Assert.assertEquals(ev1, ev2, 0.001);
		
		//When the Stake amounts are calculated
		double stakeAmount1 = biz.calculateStake(goodPunt1);
		double stakeAmount2 = biz.calculateStake(goodPunt2);
		
		//Then the Stake amounts should be the same
		Assert.assertEquals(stakeAmount1, stakeAmount2, 0.001);
	}
	

	/**
	 * -- High Confidence
	 *  True if less than joe High Confidence
	 *  And less than bookie High Confidence
	 * 
	 * Given there is a good Punt
	 * And the model High Confidence level of joe  is <joeConfidence>
	 * And joes Odds of the punt equals <joeOdds>
	 * And the model High Confidence level of the market is <bookieConfidence>
	 * And the bookies Odds of the punt equals <bookieOdds>
	 * Then the punt has <highConfidence>
	 * 
	 *  |joeConfidence|joeOdds|bookieConfidence|bookieOdds|High Confidence|
	 *  |3|1|20|10|true|
	 *  |3|3|20|10|false|
	 *  |3|5|20|10|false|
	 *  |3|1|20|20|false|
	 *  |3|1|20|30|false|
	 */
	public static void testHighConfidence() {
		BettingBusiness biz = new BettingBusiness();
		
		int[][] examples = {{3,1,20,10,1},
		   {3,3,20,10,0},
		   {3,5,20,10,0},
		   {3,1,20,20,0},
		   {3,1,20,30,0}};
		
		for (int[] data : examples) {
			//Given there is a good Punt
			Punt aGoodPunt = getGoodPunt();
			//And the model High Confidence level of joe is $3
			ModelAttributes ma = biz.getModel().getAttributes();
			ma.setHighJoeConfidence(data[0]);
			//And joes Odds of the punt equal $1
			aGoodPunt.setJoesOdds(data[1]);
			//And the model High Confidence level of the market is $20
			ma.setHighBookieConfidence(data[2]);		
			// And the bookies Odds of the punt equals $10
			aGoodPunt.setBookieOdds(data[3]);
			
			biz.setModel(new Model(ma));
			//Then the punt has High Confidence 
			Assert.assertEquals(biz.hasHighConfidence(aGoodPunt) ? 1 : 0, data[4]);
		}
	}
	
	/**
	 * -- The maximum stake is X% of the fund
	 * 
	 * Given there is a good Punt
	 * And the Account has $100
	 * And the maximum percentage Stake equals 1%
	 * And there is no minimum stake
	 * And the Punt has High confidence
	 * When the Stake amount is calculated
	 * Then the Stake amount should equal $1
	 */
	public static void testMaximumStake() {
		BettingBusiness biz = new BettingBusiness();
		BookieAccount account = new MockBookieAccount();
		
		//Given there is a good Punt
		Punt aGoodPunt = getGoodPunt();
		//And the Account has $100
		((MockBookieAccount)account).setAmount(100);
		//And the Maximum allowable Stake equals 1%
		ModelAttributes ma = biz.getModel().getAttributes();
		ma.setMaximumPercentageStake(1);
		//And there is no minimum stake
		ma.setMinimumPercentageStake(0);
		//And the Punt has High confidence
		aGoodPunt.setConfidence(Confidence.HIGH);
		// When the Stake amount is calculated
		double stakeAmount1 = biz.calculateStake(aGoodPunt);
		//Then the Stake amount should equal $1 
		Assert.assertEquals(1, stakeAmount1, 0.001);
	}
	
	/**
	 * -- The maximum dollar stake overrules the maximum % stake
	 */

	
	/**
	 * -- The minimum stake is $X
	 * 
	 * Given there is a good Punt
	 * And the Account has $100
	 * And the maximum percentage Stake equals 1%
	 * And the minimum dollar Stake equals $10
	 * And the Punt does NOT have High confidence
	 * When the Stake amount is calculated
	 * Then the Stake amount should equal $10 
	 */
	public static void testMinimumDollarStake() {
		BettingBusiness biz = new BettingBusiness();
		BookieAccount account = new MockBookieAccount();
		
		//Given there is a good Punt
		Punt aGoodPunt = getGoodPunt();
		//And the Account has $100
		((MockBookieAccount)account).setAmount(100);
		//And the Maximum allowable Stake equals 1%
		ModelAttributes ma = biz.getModel().getAttributes();
		ma.setMaximumPercentageStake(1);
		//And the minimum dollar Stake equals $10
		ma.setMinimumDollarStake(10);
		//And the Punt does NOT have High confidence
		aGoodPunt.setConfidence(Confidence.LOW);
		// When the Stake amount is calculated
		double stakeAmount1 = biz.calculateStake(aGoodPunt);
		//Then the Stake amount should equal $10
		Assert.assertEquals(10, stakeAmount1, 0.001);
	}
	

	/**
	 * -- The minimum stake is X% of the fund
	 * 
	 * Given there is a good Punt
	 * And the Account has $100
	 * And the minimum percentage Stake equals 1%
	 * And there is no minimum dollar stake
	 * And the Punt does NOT have High confidence
	 * When the Stake amount is calculated
	 * Then the Stake amount should equal $1 
	 */
	public static void testMinimumPercentageStake() {
		BettingBusiness biz = new BettingBusiness();
		BookieAccount account = new MockBookieAccount();
		
		//Given there is a good Punt
		Punt aGoodPunt = getGoodPunt();
		//And the Account has $100
		((MockBookieAccount)account).setAmount(100);
		//And the Maximum allowable Stake equals 1%
		ModelAttributes ma = biz.getModel().getAttributes();
		ma.setMinimumPercentageStake(1);
		//And there is no minimum stake
		ma.setMinimumDollarStake(0);
		//And the Punt does NOT have High confidence
		aGoodPunt.setConfidence(Confidence.LOW);
		// When the Stake amount is calculated
		double stakeAmount1 = biz.calculateStake(aGoodPunt);
		//Then the Stake amount should equal $1 
		Assert.assertEquals(1, stakeAmount1, 0.001);
	}
	
	/**
	 * -- The minimum $ stake overrules the minimum % stake
	 * -- when the % is less than the dollar amount
	 * 
	 * Given there is a good Punt
	 * And the punt does not have High Confidence
	 * And the Account has $100
	 * And the minimum dollar Stake equals $10
	 * And the minimum percentage Stake equals 1%
	 * When the Stake amount is calculated
	 * Then the Stake amount should equal $10
	 */
	public static void testDollarOverrulesPercentage() {
		BettingBusiness biz = new BettingBusiness();
		BookieAccount account = new MockBookieAccount();
		
		//Given there is a good Punt
		Punt aGoodPunt = getGoodPunt();
		//And the Punt does NOT have High confidence
		aGoodPunt.setConfidence(Confidence.LOW);
		//And the Account has $100
		((MockBookieAccount)account).setAmount(100);
		//And the minimum dollar Stake equals $10
		ModelAttributes ma = biz.getModel().getAttributes();
		ma.setMinimumDollarStake(10);
		//And the minimum percentage Stake equals 1%
		ma.setMinimumPercentageStake(1);
		// When the Stake amount is calculated
		double stakeAmount1 = biz.calculateStake(aGoodPunt);
		//Then the Stake amount should equal $1 
		Assert.assertEquals(10, stakeAmount1, 0.001);
	}
	
	/**
	 * -- The minimum % stake overrules the minimum $ stake
	 * -- when the % is greater than the dollar amount
	 * 
	 * Given there is a good Punt
	 * And the punt does not have High Confidence
	 * And the Account has $100
	 * And the minimum dollar Stake equals $1
	 * And the minimum percentage Stake equals 10%
	 * When the Stake amount is calculated
	 * Then the Stake amount should equal $10
	 */
	public static void testPercentageOverrulesDollar() {
		BettingBusiness biz = new BettingBusiness();
		BookieAccount account = new MockBookieAccount();
		
		//Given there is a good Punt
		Punt aGoodPunt = getGoodPunt();
		//And the Punt does NOT have High confidence
		aGoodPunt.setConfidence(Confidence.LOW);
		//And the Account has $100
		((MockBookieAccount)account).setAmount(100);
		//And the minimum dollar Stake equals $1
		ModelAttributes ma = biz.getModel().getAttributes();
		ma.setMinimumDollarStake(1);
		//And the minimum percentage Stake equals 10%
		ma.setMinimumPercentageStake(10);
		// When the Stake amount is calculated
		double stakeAmount1 = biz.calculateStake(aGoodPunt);
		//Then the Stake amount should equal $1 
		Assert.assertEquals(10, stakeAmount1, 0.001);
	}

	
	/**
	 * -- The stake amount equals the calculated stake amount minus what is already staked
	 * 
	 * Given there is a good Punt
	 * And there is an existing Stake for that Punt of <existing>
	 * And the Account has $100
	 * And the maximum percentage Stake equals 5%
	 * And the minimum percentage Stake equals 1%
	 * And the minimum dollar Stake equals $1
	 * When there is the same Punt with <confidence> 
	 * And the stake amount is calculated
	 * Then the Stake amount should equal <stake>
	 * 
	 * Examples:
	 * |existing|confidence|stake|
	 * |1|High|4|
	 * |4.5|High|0|
	 * |5|High|0|
	 * |1|Low|0|
	 * |0.5|Low|0|
	 */
	public static void testLowPuntIsUpdatedWhenHigh() {
		BettingBusiness biz = new BettingBusiness();
		BookieAccount account = new MockBookieAccount();
		
		double[][] examples = new double[][] {
				{1,1,4},
				{4.5,1,0},
				{5,1,0},
				{1,0,0},
				{0.5,0,0}
		};
		
		for (double[] data : examples) {
			//set up
			((MockPuntingDao)SpringRacingServices.getPuntingDAO()).flush();
			//Given there is a good Punt
			Punt aGoodPunt = getGoodPunt();
			//And there is an existing Stake for that Punt of $1
			biz.placeBets(getGoodPuntAsList(aGoodPunt), data[0]);
	
			//And the Account has $100
			((MockBookieAccount)account).setAmount(100);
			ModelAttributes ma = biz.getModel().getAttributes();
			//And the maximum percentage Stake equals 5% 
			ma.setMaximumPercentageStake(5);
			//And the minimum percentage Stake equals 1%
			ma.setMinimumPercentageStake(1);
			// And the minimum dollar Stake equals $1
			ma.setMinimumDollarStake(1);
			
			//When there is the same Punt with high confidence
			aGoodPunt.setConfidence(data[1] == 1.0 ? Confidence.HIGH: Confidence.LOW);
			//And the stake amount is calculated
			double stakeAmount1 = biz.calculateStake(aGoodPunt);
			
			//Then the Stake amount should equal $4
			Assert.assertEquals(data[2], stakeAmount1, 0.001);	
		}
	}
	
	public static void aStakeIsSomeUsefulIncrement() {
		
	}
}
