package com.n42.analytics.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;

public class RickshawResponseBuilder {
	
	public static void main(String[] args) {
	}
	
	public static String build(Map<Long, Double> input) {
		if(MapUtils.isEmpty(input)) {
			return "[]";
		}
		
		StringBuilder rickshawFormat = new StringBuilder();
		rickshawFormat.append("[");
		
		for(Entry<Long, Double> entry : input.entrySet()) {
			rickshawFormat.append(getRickshawDataPoints(entry));
		}
		
		if (rickshawFormat.length() > 1) {
			rickshawFormat.deleteCharAt(rickshawFormat.length() - 1);
		}
		rickshawFormat.append("]");
		return rickshawFormat.toString();
		
	}
	
	private static String getRickshawDataPoints(Entry<Long, Double> entry) {
		StringBuilder rickshawFormat = new StringBuilder();
		rickshawFormat.append("[");
		rickshawFormat.append(entry.getValue());
		rickshawFormat.append(",");
		rickshawFormat.append(entry.getKey() / 1000);
		rickshawFormat.append("]");
		rickshawFormat.append(",");
		return rickshawFormat.toString();
	}

	public static String build(String[] messages, String[] metrices) {
		
		StringBuilder metrics = new StringBuilder();
		int count = 0;
		for(String message : messages) {
			metrics.append("\"" + message + "\": \"" + metrices[count++] + "\"");
			metrics.append(",");
		}
		metrics.deleteCharAt(metrics.length() - 1);
		
		String response = "{"+
				"    \"href\": \"http://ambari/clusters/BhupatiAmbari/services/HDFS/components/DATANODE?fields=metrics/dfs/datanode/bytes_written[1351117114,1351120714,60],metrics/dfs/datanode/bytes_read[1351117114,1351120714,60]\","+
				"    \"metrics\": {"+
				"        \"dfs\": {"+
				"            \"datanode\": {"+
								metrics.toString() +
				
				"            }"+
				"        }"+
				"    },"+
				"    \"ServiceComponentInfo\": {"+
				"        \"cluster_name\": \"BhupatiAmbari\","+
				"        \"component_name\": \"DATANODE\","+
				"        \"service_name\": \"HAPROXY\""+
				"    }"+
				"}";
		
		return response;
		
	}
	
}
