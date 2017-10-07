package com.joe.springracing.business;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.joe.springracing.AbstractSpringRacingBusiness;
import com.joe.springracing.SpringRacingServices;
import com.joe.springracing.objects.Meeting;
import com.joe.springracing.objects.Race;

public class MeetBusiness extends AbstractSpringRacingBusiness {

	
	public MeetBusiness() {
		this(new PrintWriter(System.out));
	}

	public MeetBusiness(PrintWriter pw) {
		super(pw);
	}
	
	public List<Meeting> fetchUpcomingMeets() {
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchUpcomingMeets();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public List<Race> fetchRacesForMeet(Meeting meeting) {
		getWriter().println();
		getWriter().println(meeting.getDate() + " "  + meeting.getVenue());
		
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meeting);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch races for meet " + meeting.getMeetCode(), e);
		}		
	}

	public Meeting fetchMeet(String meetCode) {
		try {
			return SpringRacingServices.getSpringRacingDAO().fetchMeet(meetCode);
		} catch (Exception e) {
			throw new RuntimeException("Unable to fetch meet " + meetCode, e);
		}	
	}

}
