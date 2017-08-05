package com.joe.springracing.business.model;

import com.joe.springracing.objects.RunnerResult;

/**
 * A spring racing object that can be independently statistically analysed
 * @author joe
 *
 */
public interface AnalysableObject {
	
	public boolean raced(RunnerResult result);
	public String getId();
}
