package com.goodpunts.objectify;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.joe.springracing.objects.Meeting;

/**
 * The set of good punts for a meeting
 * @author joe.cunsolo
 *
 */
@Entity
public class ObjPuntEvent {

	@Parent
	Key<ObjMeet> meet;
	@Id
	Long id;
	@Index
	Date date;
	
	public ObjPuntEvent() {}
	
	public ObjPuntEvent(Meeting m) {
		this(m.getMeetCode());
	}
	
	public ObjPuntEvent(String meetCode) {
//		this.meetCode = meetCode;
		meet = Key.create(ObjMeet.class, meetCode);	
		date = new Date();
	}
	
	public Long getId() {
		return id;
	}
	
//	public String getMeetCode() {
//		return meetCode;
//	}
}
