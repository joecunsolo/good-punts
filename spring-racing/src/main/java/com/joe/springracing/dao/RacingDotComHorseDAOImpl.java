package com.joe.springracing.dao;

import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.joe.springracing.objects.Horse;
import com.joe.springracing.objects.RunnerResult;
import com.joe.springracing.objects.Runner;
import com.joe.springracing.utils.io.html.HTMLReaderIO;
import com.joe.springracing.utils.io.json.JsonReaderIO;

//		https://api.racing.com/api/Horse/all/career/all_victorian/all/all/all/1/prize/1000/100/

public class RacingDotComHorseDAOImpl  {

	
	public static final String KEY_JOCKEY = "Jockey";
	public static final String KEY_TRAINER = "Trainer";
	public static final String KEY_HORSE= "Horse";
	public static final String KEY_RACE = "Race";
	
	
	

}
