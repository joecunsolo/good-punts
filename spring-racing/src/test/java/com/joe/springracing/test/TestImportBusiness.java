package com.joe.springracing.test;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.business.ImportBusiness;
import com.joe.springracing.objects.RunnerResult;

import org.junit.Assert;
import junit.framework.TestCase;

public class TestImportBusiness extends TestCase {
	
	/** 
	 * Scenario: Spell is last race
	 * Given a horse has a race result with a date of today -7
	 * Given a horse has a race result with a date of today -10
	 * When the spell is calculated
	 * Then the spell is 7
	 */
	public static void testCalculateSpell() {
		ImportBusiness biz = new ImportBusiness();
		List<RunnerResult> results = new ArrayList<RunnerResult>();
		results.add(aResultWithADate(7));
		results.add(aResultWithADate(10));
		int spell = biz.calculateSpell(results);		
		Assert.assertEquals(7, spell);
	}

	/** 
	 * Scenario: Spell is last race not null
	 * Given a horse has a race result with a date of today -7
	 * Given a horse has a race result with a date of today -10
	 * And the horse has a race result without a date
	 * When the spell is calculated
	 * Then the spell is 7
	 */
	public static void testCalculateSpellNotNll() {
		ImportBusiness biz = new ImportBusiness();
		List<RunnerResult> results = new ArrayList<RunnerResult>();
		results.add(aResultWithADate(7));
		results.add(aResultWithADate(10));
		results.add(aResultWithoutADate());
		int spell = biz.calculateSpell(results);		
		Assert.assertEquals(7, spell);
	}
	
	/** 
	 * Scenario: Ignore a null date
	 * Given a horse has a race result with a date of today -7
	 * And the horse has a race result without a date
	 * When the spell is calculated
	 * Then the spell is 7
	 */
	public static void testCalculateSpellWithNll() {
		ImportBusiness biz = new ImportBusiness();
		List<RunnerResult> results = new ArrayList<RunnerResult>();
		results.add(aResultWithADate(7));
		results.add(aResultWithoutADate());
		int spell = biz.calculateSpell(results);		
		Assert.assertEquals(7, spell);
	}
	
	/** 
	 * Scenario: Ignore multiple null dates
	 * Given a horse has a race result with a date of today -7
	 * And the horse has a race result without a date
	 * And the horse has another race result without a date
	 * When the spell is calculated
	 * Then the spell is 7
	 */
	public static void testCalculateSpellWithMultipleNull() {
		ImportBusiness biz = new ImportBusiness();
		List<RunnerResult> results = new ArrayList<RunnerResult>();
		results.add(aResultWithADate(7));
		results.add(aResultWithoutADate());
		results.add(aResultWithoutADate());
		int spell = biz.calculateSpell(results);
		Assert.assertEquals(7, spell);
	}


	private static RunnerResult aResultWithoutADate() {
		return aResult(null);
	}

	private static RunnerResult aResultWithADate(int days) {
		long millis = days * ImportBusiness.MILLIS_IN_A_DAY;
		Date raceDate = new Date(System.currentTimeMillis() - millis);
		
		return aResult(raceDate);
	}

	private static RunnerResult aResult(Date raceDate) {
		RunnerResult result = new RunnerResult();
		result.setRaceDate(raceDate);
		return result;
	}

}
