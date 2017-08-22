package com.joe.springracing.exporter;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public abstract class CSVExporter<T> implements Exporter<T> {

	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private PrintWriter writer;
	
	public CSVExporter(PrintWriter writer) {
		this.writer = writer;
		printHeader();
	}
	
	public void printHeader() {
		String[] header = getHeader();
		print(header);
	}

	/** Writer a runner history to CSV */
	public void export(T object) {
		printRecord(object);
	}
	
	public void printRecord(T object) {
		String[] record = toRecord(object);
		print(record);
	}

	private void print(String[] record) {
		for (String s : record) {
			writer.print(s + ",");
		}
		writer.println();
		writer.flush();
	}

	public void close() {
		writer.close();
	}
	
	protected abstract String[] getHeader();
	
	protected abstract String[] toRecord(T object);
	
}
