package com.goodpunts.objectify;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ObjHorse {

	private String name;
	private String code;
	@Id
	private String id;
	@Index
	private boolean histories;
	
	public ObjHorse() {}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void setHistories(boolean histories) {
		this.histories = histories;
	}
	
	public boolean hasHistories() {
		return histories;
	}

}
