package com.joe.springracing.utils.io.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;

import com.joe.springracing.SpringRacingServices;

public class HTMLReaderIO {
	
	public static String getHTML(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		
		HttpURLConnection conn;
		if (SpringRacingServices.useHTTPProxy()) {
			conn = (HttpURLConnection) url.openConnection(SpringRacingServices.getHTTPProxy());			
		    Authenticator.setDefault(SpringRacingServices.getHTTPAuthenticator());
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		
		conn.setRequestMethod("GET");
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
			   result.append(line);
			}
			rd.close();
			return result.toString();
		} catch (IOException ioe) {
			return null;
		}
	}
}
