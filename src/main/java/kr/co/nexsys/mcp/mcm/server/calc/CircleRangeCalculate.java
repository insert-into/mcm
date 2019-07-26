package kr.co.nexsys.mcp.mcm.server.calc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CircleRangeCalculate {

	/**
	 * 
	 */
	/*
	 * public CircleRangeCalculate() {
	 * 
	 * }
	 */
	
	public Map<String, Double> rangeCalculate(
			 final double lati
			,final double longi
			,final double dDis) {
		final Map<String, Double> concMap = new ConcurrentHashMap<>();
		
		final double dis = dDis * 1000;
		final double diffLatitude = getLatitudeInDifference(dis);
		final double diffLongitude = getLongitudeInDifference(lati, dis);

		concMap.put("minLati" , lati  - diffLatitude);
		concMap.put("maxLati" , lati  + diffLatitude);
		concMap.put("minLongi", longi - diffLongitude);
		concMap.put("maxLongi", longi + diffLongitude);
		log.warn("range_cal_ result ....{}\n", concMap);
		return concMap;
	}

	private double getLongitudeInDifference(double lati, double dis) {
		int earth = 6371000;

		return (dis * 360.0) / (2 * Math.PI * earth * Math.cos(Math.toRadians(lati)));
	}

	private double getLatitudeInDifference(double dis) {
		int earth = 6371000;

		return (dis * 360.0) / (2 * Math.PI * earth);
	}
}
