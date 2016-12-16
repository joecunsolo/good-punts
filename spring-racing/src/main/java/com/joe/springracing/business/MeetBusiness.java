package com.joe.springracing.business;

import java.util.ArrayList;
import java.util.List;

import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class MeetBusiness {

	List<Meeting> meets = new ArrayList<Meeting>();
	
	public List<Meeting> organiseRacesByMeeting(List<Race> races) {
		//List<Meeting> meets = new ArrayList<Meeting>();
		for (Race race : races) {
			if (race.getRunners() != null &&
					race.getRunners().size() > 0 &&
					oddsSet(race)) {
				Meeting meeting = getMeeting(race.getMeetCode());
				if (meeting == null) {
					meeting = new Meeting(race);
					meets.add(meeting);
				} 
				meeting.addRace(race);
			}
		}
		return meets;
	}
	
	private boolean oddsSet(Race race) {
		for (Runner runner : race.getRunners()) {
			if (runner.hasOdds()) {
				return true;
			}
		}
		return false;
	}

	public Meeting getMeeting(List<Meeting> meets, int meetCode) {
		for (Meeting meet : meets) {
			if (meet.getMeetCode() == meetCode) {
				return meet;
			}
		}
		return null;
	}
	
	public Meeting getMeeting (int meetCode) {
		for (Meeting meet : meets) {
			if (meet.getMeetCode() == meetCode) {
				return meet;
			}
		}
		Meeting meeting = getMeeting(meets, meetCode);
		meets.add(meeting);
		return meeting;
	}

}
