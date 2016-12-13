package com.joe.springracing.dao;

import java.util.List;

import com.joe.springracing.objects.Meeting;

public interface LocalRaceDAO {

	public List<Meeting> fetchMeetings() throws Exception;

	public void storeMeets(List<Meeting> meets) throws Exception;

	public int[] fetchRaceResult(String meetCode, int raceNumber) throws Exception;

}
