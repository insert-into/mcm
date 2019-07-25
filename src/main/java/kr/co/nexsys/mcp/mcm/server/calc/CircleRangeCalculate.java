package kr.co.nexsys.mcp.mcm.server.calc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CircleRangeCalculate {

	public CircleRangeCalculate() {
		
	}
	
	public Map<String, Double> rangeCalculate(double lati, double longi, double dDis) {
		double dis = dDis * 1000;
		Map<String, Double> concMap = new ConcurrentHashMap<String, Double>();
		double diffLatitude = getLatitudeInDifference(dis);
		double diffLongitude = getLongitudeInDifference(lati, dis);

		concMap.put("minLati" , (lati  - diffLatitude));
		concMap.put("maxLati" , (lati  + diffLatitude));
		concMap.put("minLongi", (longi - diffLongitude));
		concMap.put("maxLongi", (longi + diffLongitude));
		log.warn("range_cal_ result ....\n" + concMap);
		return concMap;
	}

	private double getLongitudeInDifference(double lati, double dis) {
		final int earth = 6371000;

		return (dis * 360.0) / (2 * Math.PI * earth * Math.cos(Math.toRadians(lati)));
	}

	private double getLatitudeInDifference(double dis) {
		final int earth = 6371000;

		return (dis * 360.0) / (2 * Math.PI * earth);
	}
}
