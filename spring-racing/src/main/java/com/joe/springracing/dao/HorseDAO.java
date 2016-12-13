package com.joe.springracing.dao;

import java.util.List;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.Runner;

public interface HorseDAO {

	public List<Horse> fetchHorses(List<String> horses);

	public void enrichHorses(List<Runner> runners) throws Exception;
		
}
