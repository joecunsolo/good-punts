package com.joe.springracing.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;
import com.joe.springracing.objects.Runner;

public class MeetBusiness {

	List<Meeting> meets = new ArrayList<Meeting>();
	
	public List<Meeting> organiseRacesByMeeting(List<Race> races) {
		//List<Meeting> meets = new ArrayList<Meeting>();
		for (Race race : races) {
//			if (race.getRunners() != null &&
//					race.getRunners().size() > 0 &&
//					oddsSet(race)) {
				Meeting meeting = getMeeting(race);
				if (meeting == null) {
					meeting = new Meeting(race);
					meets.add(meeting);
				} 
				meeting.addRace(race);
//			}
		}
		return meets;
	}
	
	public List<Meeting> getMeetings() {
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

	private Meeting getMeeting(List<Meeting> meets, String meetCode) {
		for (Meeting meet : meets) {
			if (meet.getMeetCode().equals(meetCode)) {
				return meet;
			}
		}
		return null;
	}
	
	public Meeting getMeeting (Race race) {
		Meeting meeting = getMeeting(meets, race.getMeetCode());
		if (meeting == null) {
			meeting = new Meeting();
			meeting.setMeetCode(race.getMeetCode());
			meeting.setDate(race.getDate());
			meeting.setVenue(race.getVenue());
			meets.add(meeting);			
		}
		meeting.addRace(race);
		return meeting;
	}
	
	public void sortMeetingsByDate(List<Meeting> meets) {
		Collections.sort(meets, new _ByDateComprator());
	}
	
	private class _ByDateComprator implements Comparator<Meeting> {
		public int compare(Meeting o1, Meeting o2) {
			return o1.getDate().compareTo(o2.getDate());		
		}
	}
	
	public void sortRacesByNumber(List<Race> races) {
		Collections.sort(races, new _ByRaceNumberComparator());
	}

	private class _ByRaceNumberComparator implements Comparator<Race> {
		public int compare(Race o1, Race o2) {
			return o1.getRaceNumber() > o2.getRaceNumber() ? 1 : -1;
		}
	}

}
