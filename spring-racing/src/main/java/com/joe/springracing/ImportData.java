package com.joe.springracing;

import com.joe.springracing.business.ImportBusiness;


public class ImportData {

	public static final int IMPORT_FUTURE_MEETS = 0;
	public static final int IMPORT_RACE_RESULTS = 1;
	public static final int IMPORT_EXISTING_MEETS = 2;
	
	public static final int IMPORT_FLAG = IMPORT_FUTURE_MEETS;
	
	public static void main(String[] args) {	
		ImportBusiness importer = new ImportBusiness();
		if (IMPORT_FLAG == IMPORT_FUTURE_MEETS) {
			importer.importUpcomingRaces(true);
		} else
		if (IMPORT_FLAG == IMPORT_RACE_RESULTS) {
			importer.importRaceResults();
		} else 
		if (IMPORT_FLAG == IMPORT_EXISTING_MEETS) {
//			importer.importExistingMeets();
		}
	}
}
