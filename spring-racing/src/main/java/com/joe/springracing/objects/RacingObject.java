package com.joe.springracing.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.joe.springracing.business.RacingKeys.*;

public class RacingObject {
	private Properties properties = new Properties();
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public static final SimpleDateFormat oldDateFormat = new SimpleDateFormat("\"yyyy-MM-dd'T'HH:mm:ss\"");
	
	
	public RacingObject() {}

	public RacingObject(Properties props) {
		setProperties(props);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public String getProperty(String key) {
		if (properties == null) {
			return null;
		}
		String value = getProperties().getProperty(key);
		if (value == null || VALUE_NULL.equals(value)) {
			return null;
		}
		return value;
	}

	public int getNumber(String key) {
		String number = getProperty(key);
		if (number == null) {
			return 0;
		}
		return Integer.parseInt(number);
	}
	
	public double getDouble(String key) {
		String number = getProperty(key);
		if (number == null) {
			return 0;
		}
		return Double.parseDouble(number);
	}
	
	public boolean isTrue(String key) {
		String value = getProperty(key);
		if (value == null) {
			return false;
		}
		return Boolean.parseBoolean(value);
	}
	
	public Date getDate(String key) {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			try {
				return oldDateFormat.parse(value);
			} catch (ParseException e2) {
				throw new RuntimeException("Unable to parse date for " + key, e2);
			}
		}
	}
	
	public void setProperty(String key, String value) {
		if (value != null) {
			this.properties.setProperty(key, value);
		}
	}

}
