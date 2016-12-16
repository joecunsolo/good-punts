package com.joe.springracing.test;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.GeneratePunts;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.business.ProbabilityBusiness;
import com.joe.springracing.business.RacingKeys;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;
import com.joe.springracing.business.probability.FormStatistics;
import com.joe.springracing.business.probability.PrizeMoneyStatistics;
import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.objects.Runner;

import org.junit.Assert;

import junit.framework.TestCase;

public class TestProbabilityBusiness extends TestCase {

	public static void testCalculateOddsForRaceByForm() {
		int[] donkeyArray = new int[]{100};
		int[] pharLapArray = new int[]{1};
		
		Race race = raceHistoriesToMeeting(new int[][]{donkeyArray, pharLapArray});
		Meeting meeting = new Meeting();
		meeting.addRace(race);

		SpringRacingServices.setStatistsics(new FormStatistics());
		Model m = new Model(new ModelAttributes());
		new ProbabilityBusiness(m).calculateOddsForMeet(meeting);
		
		GeneratePunts.printStatistics(meeting);
		
		Runner donkeyRunner = race.getRunners().get(0);
		Runner pharLapRunner = race.getRunners().get(1);
		Assert.assertTrue(pharLapRunner.getProbability().getWin() > donkeyRunner.getProbability().getWin());
	}
	
	public static void testCalculateOddsForRaceByPrizeMoney() {
		int[] donkeyArray = new int[]{10, 5, 10};
		int[] pharLapArray = new int[]{1000, 500, 1000};
		
		Race race = raceHistoriesToMeeting(new int[][]{donkeyArray, pharLapArray});
		Meeting meeting = new Meeting();
		meeting.addRace(race);
		
		SpringRacingServices.setStatistsics(new PrizeMoneyStatistics());
		Model m = new Model(new ModelAttributes());
		new ProbabilityBusiness(m).calculateOddsForMeet(meeting);
		
		GeneratePunts.printStatistics(meeting);
		
		Runner donkeyRunner = race.getRunners().get(0);
		Runner pharLapRunner = race.getRunners().get(1);
		Assert.assertTrue(pharLapRunner.getProbability().getWin() > donkeyRunner.getProbability().getWin());
	}

	
//	public static void testLongAndShortHistoryOdds() {
//		int[][] array = new int[][]{{5,5,9,7,9,4,6,1,14,9},
//		{2,3,2,10,3,14,2,1,7,9,8,8,4,1,4,1,6,3,9,7,4,1,5,5,4,3,9,9,6,8},
//		{2,2,2,5,1,9,3,7,8,5,1,4,1,5},
//		{9,14,3,7,8,9,7,8,5,2,7,8,1,4,10},
//		{7,1,3,4,8,2,2,8,5,5,7,3,2,2,8,3,5,1,1,8,10,7},
//		{12,1,1,11,8,3,2,1,4,3,6,5,8,9,1},
//		{3,7,4,3,4,5,3,1,10},
//		{3,1,4,7,4,3,6,9,5},
//		{10,3,7,3,6,1,8},
//		{5,4,3,6,9,6,2,1,5},
//		{1,10,3,2,2,7,6}};
//		
//		Race race = raceHistoriesToMeeting(array);
//		Meeting meeting = new Meeting();
//		meeting.addRace(race);
//		
//		SpringRacingServices.setStatistsics(new FormStatistics());
//		Model m = new Model(new ModelAttributes());
//		new ProbabilityBusiness(m).calculateOddsForMeet(meeting);
//		
//		GeneratePunts.printStatistics(meeting);
//		
//		Runner donkeyRunner = race.getRunners().get(0);
//		Runner pharLapRunner = race.getRunners().get(1);
//		Assert.assertTrue(pharLapRunner.getProbability().getWin() > donkeyRunner.getProbability().getWin());
//	}
	
	public static void testOneRace() {
		int[][] array = new int[][]{{2},
		{3, 5}};
		
		Race race = raceHistoriesToMeeting(array);
		Meeting meeting = new Meeting();
		meeting.addRace(race);
		
		Model m = new Model(new ModelAttributes());
		new ProbabilityBusiness(m).calculateOddsForMeet(meeting);
		
		GeneratePunts.printStatistics(meeting);
		
		Runner donkeyRunner = race.getRunners().get(0);
		Runner pharLapRunner = race.getRunners().get(1);
		Assert.assertTrue(pharLapRunner.getProbability().getWin() > donkeyRunner.getProbability().getWin());
	}
	
	public static Race raceHistoriesToMeeting(int[][] positions) {
		List<Runner> runners = new ArrayList<Runner>();
		for (int i = 0; i < positions.length; i++) {
			Horse donkey = new Horse();
			donkey.setProperty(RacingKeys.KEY_FULLNAME, "Donkey");
			donkey.setProperty(RacingKeys.KEY_HORSE_URL, "Donkey");
			donkey.setProperty(RacingKeys.KEY_NUMBER, String.valueOf(i));
			
			addPositions(positions[i], donkey);
			Runner donkeyRunner = new Runner();
			donkeyRunner.setHorse(donkey);
			donkeyRunner.setProperty(RacingKeys.KEY_RUNNER_NUMBER, String.valueOf(i));
			runners.add(donkeyRunner);
		}
		
		Race race = new Race();
		race.setRunners(runners);
		
		return race;
	}
	
	public static void addPositions(int[] positions, Horse horse) {
		for (int i = 0; i < positions.length; i++) {
			RunnerResult raceResult = new RunnerResult();
			raceResult.setProperty(RacingKeys.KEY_RESULT_RESULT, String.valueOf(positions[i]));
			raceResult.setPrizeMoney(positions[i]);
			raceResult.setHorse(horse);
			
			horse.addPastResult(raceResult);
		}
	}
}
