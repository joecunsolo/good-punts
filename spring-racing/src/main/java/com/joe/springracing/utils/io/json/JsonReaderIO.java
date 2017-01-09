package com.joe.springracing.utils.io.json;

import java.util.Properties;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class JsonReaderIO {
	
	public static final String KEY_FIXED_ODDS = "fixed";
	public static final String PREFIX_PROTOCOL_HTTP = "http:";

	protected Properties parseProperties(JsonValue jsonValue) {
		if (jsonValue != null) {
			if (jsonValue instanceof JsonObject) {
				JsonObject jObject = (JsonObject)jsonValue;
				Properties props = new Properties();
				Set<String> keys = jObject.keySet();
				
				for (String key : keys) {
					String value = jObject.get(key).toString();
					value = value.replaceAll("\"", "");
					props.put(key.toLowerCase(), value);
				}
				return props;
			}
		}
		return null;
	}
	
	protected double[] readDoubleArray(JsonArray jsonArray) {
		double[] result = new double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			String value = jsonArray.get(i).toString(); 
			result[i] = Double.parseDouble(value);
		}
		return result;
	}

//	private double[] readDoubleArray(String property) {
//		double[] result = new double[property.];
//		return null;
//	}


}
