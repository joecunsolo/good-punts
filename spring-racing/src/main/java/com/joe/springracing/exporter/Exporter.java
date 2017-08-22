package com.joe.springracing.exporter;

/** Exports data to some format */
public interface Exporter<T> {
	
	/** Export this record */
	public void export(T object);
	
}
