package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/** Convert the horse histories export into something else useful */
public class HorseHistories {

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static final String DELIMITER = ",";
	private static final String KEY_HORSE = "Horse";
	private static final String KEY_PRIZEMONEY = "PrizeMoney";
	private static final String KEY_DATE = "Date";
	
	public static void main(String[] args) {
		String input = "C:\\Users\\ft\\Downloads\\histories_export.csv";
		String output = "C:\\Users\\ft\\Downloads\\date_and_money.csv";
		HorseHistories hh = new HorseHistories();
		try {
			List<Properties> importFile = hh.importFile(input);
			List<Properties> exportFile = hh.organiseByRaceDateDifferenceAndPrizeMoney(importFile);
			hh.exportFile(exportFile, output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void exportFile(List<Properties> exportFile, String filename) throws IOException {
		File f = new File(filename);
		PrintWriter pw = new PrintWriter(new FileWriter(f));
		
		String[] header = null;
		for (Properties p : exportFile) {
			if (header == null) {
				header = readHeader(p);
				print(header, pw);
			}
			print(header, p, pw);
		}
		pw.flush();
		pw.close();
	}

	private void print(String[] header, Properties p, PrintWriter pw) {
		for (String key : header) {
			Object a = p.get(key);
			pw.print(a);
			pw.print(DELIMITER);
		}
		pw.println();
		pw.flush();
	}

	private void print(String[] record, PrintWriter pw) {
		for (String a : record) {
			pw.print(a);
			pw.print(DELIMITER);
		}
		pw.println();
		pw.flush();
	}

	/** Convert the properties keys into a String [] */
	private String[] readHeader(Properties p) {
		String[] result = new String[p.size()];
		return p.keySet().toArray(result);
	}

	/** Read a CSV into a list of props */
	public List<Properties> importFile(String filename) throws IOException {
		File f = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f));

		String[] header = readHeader(br);
		List<Properties> result = new ArrayList<Properties>();
		String line = br.readLine();
		while (line != null) {
			String[] value = line.split(DELIMITER);
			result.add(toProperties(header, value));
			line = br.readLine();
		}
		return result;
		
	}
	
	private Properties toProperties(String[] header, String[] value) {
		Properties result = new Properties();
		int i = 0;
		for (String key : header) {
			result.put(key, value[i++]);
		}
		return result;
	}

	private String[] readHeader(BufferedReader br) throws IOException {
		String line = br.readLine();
		if (line != null) {
			return line.split(DELIMITER);
		}
		return null;
	}

	/** 
	 * Every race a horse has raced in compared to every other race it has raced in..
	 * Compared by date and prize money difference
	 * 
	 * @return something that can be easily printed out
	 */
	public List<Properties> organiseByRaceDateDifferenceAndPrizeMoney(List<Properties> importFile) {
		//organise the horses
		Map<String, List<DatePrizeMoney>> horses = readRaceDateAndPrizeMoney(importFile);
		//This is the output
		List<Properties> result = new ArrayList<Properties>();
		for (String horse : horses.keySet()) {
			//all the races for the horse
			List<DatePrizeMoney> horseMoney = horses.get(horse);
			//compared to all the other races
			List<Properties> horseProps = sortHorseByRaceDateAndPrizeMoney(horse, horseMoney);
			result.addAll(horseProps);
		}
		return result;
	}
	
	/** calculate the values for one horse */
	private List<Properties> sortHorseByRaceDateAndPrizeMoney(String horse, List<DatePrizeMoney> horseMoney) {
		List<Properties> result = new ArrayList<Properties>();
		//one horse compared to all the others
		for (int i = 0; i < horseMoney.size() - 1; i++) {
			DatePrizeMoney a = horseMoney.get(i);
			//the other horses
			for (int j = i+1; j < horseMoney.size() - 1; j++) {
				try {
					DatePrizeMoney b = horseMoney.get(j);
					//compare money
					double variance = variance(a.prizeMoney, b.prizeMoney);				
					//compare dates
					int days = (int) variance(a.date, b.date);
					Properties props = toDateAndPrizeMoney(horse, variance, days);
					result.add(props);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/** Use the data to create a record */
	private Properties toDateAndPrizeMoney(String horse, double variance,
			int days) {
		Properties props = new Properties();
		props.put(KEY_HORSE, horse);
		props.put(KEY_DATE, days);
		props.put(KEY_PRIZEMONEY, variance);
		return props;
	}

	/** The number of days between the two */
	private long variance(Date date, Date date2) {
		if (date.getTime() > date2.getTime()) {
			return days(date.getTime() - date2.getTime());
		} else
		if (date.getTime() < date2.getTime()) {
			return days(date2.getTime() - date.getTime());
		}
		return 0;
	}

	/** Number of days in these milliseconds */
	private long days(long l) {
		return l / 1000 / 60 / 60 / 24;
	}

	/** The percentage difference between the two */
	private double variance (double a, double b) {
		if (a != 0 && b != 0) {
			if (a == b) {
				return 0;
			} else
			if (a > b) {
				return b / a;
			} else {
				return a / b;
			}
		}
		throw new RuntimeException("DIV/0");
	}

	/** Converts the raw data into useful objects */
	private Map<String, List<DatePrizeMoney>> readRaceDateAndPrizeMoney(List<Properties> importFile) {
		Map<String, List<DatePrizeMoney>> horses = new HashMap<String, List<DatePrizeMoney>>();
		for (Properties props : importFile) {
			try {
				//the horse
				String horse = props.getProperty(KEY_HORSE);
				//what we know about it
				List<DatePrizeMoney> horseMoney = horses.get(horse);
				if (horseMoney == null) {
					horseMoney = new ArrayList<DatePrizeMoney>();
				}
				//what this line is telling us
				String aDate = props.getProperty(KEY_DATE);
				String aMoney = props.getProperty(KEY_PRIZEMONEY);
				horseMoney.add(new DatePrizeMoney(aDate, aMoney));
				//update the horse
				horses.put(horse, horseMoney);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return horses;
	}
	
	private class DatePrizeMoney {
		public Date date;
		public double prizeMoney;
				
		public DatePrizeMoney(String aDate, String aMoney) throws ParseException {
			date = sdf.parse(aDate);
			prizeMoney = Double.parseDouble(aMoney);
		}
	}
}
