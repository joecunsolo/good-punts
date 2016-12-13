package com.joe.springracing.test;

import com.joe.springracing.business.PuntingBusiness;
import com.joe.springracing.business.model.Model;
import com.joe.springracing.business.model.ModelAttributes;

import org.junit.Assert;

import junit.framework.TestCase;

public class TestPuntingBusiness extends TestCase {

	public static void testProbabilityToOdds() {
		double probability = 0.5;
		
		Model m = new Model(new ModelAttributes());
		PuntingBusiness business = new PuntingBusiness(m);
		double odds = business.probabilityToOdds(probability);
		
		Assert.assertEquals(2.0, odds, 0.01);
		
		probability = 0.1;
		odds = business.probabilityToOdds(probability);
		
		Assert.assertEquals(10.0, odds, 0.01);
		
		probability = 0.9;
		odds = business.probabilityToOdds(probability);
		
		Assert.assertEquals(1.11, odds, 0.01);
	}
}
