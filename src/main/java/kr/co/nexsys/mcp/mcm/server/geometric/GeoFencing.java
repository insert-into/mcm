package kr.co.nexsys.mcp.mcm.server.geometric;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;
import kr.co.nexsys.mcp.mcm.geocasting.service.GeocastCircleService;
import kr.co.nexsys.mcp.mcm.server.geometric.Calculate;
import kr.co.nexsys.mcp.mcm.server.geometric.ConvexHull;
import kr.co.nexsys.mcp.mcm.server.geometric.Point;
import kr.co.nexsys.mcp.mcm.server.geometric.Polygon;
import kr.co.nexsys.mcp.mcm.server.grahamscan.convexhull.HullFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kimyh(kimyhjava@pukyong.ac.kr)
 * Creation: Sep 27, 2018
 * File name: Geo_Fencing_.java
 */
@Slf4j
public class GeoFencing implements Calculate {
	
	private GeocastCircleService cs;

	boolean graphics = true;
	int numPoints = 5000;
	HullFrame frame;
	Random rand;

	public GeoFencing(GeocastCircleService cs) {
		rand = new Random();
		frame = new HullFrame();
		this.cs = cs;
	}

	public List<CoreTRDvo> GeoFencingCalculation(ArrayList<Double> latiArray, ArrayList<Double> longiArray) {
		// Begin computations
		long startTime = new Date().getTime();
		long totalHullPoints = 0;

		Point2D[] pointsRaw = new Point2D[latiArray.size()];
		for (int h = 0; h < latiArray.size(); h++) {
			double x = longiArray.get(h);
			double y = latiArray.get(h);
			pointsRaw[h] = new Point2D.Double(x, y);
		}

		List<Point> pointList = new ArrayList<Point>();
		List<Point> hullPoint = new ArrayList<Point>();
		ConvexHull cvxHull = new ConvexHull();

		for (int ii_ = 0; ii_ < pointsRaw.length; ii_++) {
			pointList.add(new Point(pointsRaw[ii_].getX(), pointsRaw[ii_].getY()));
		}
		hullPoint = cvxHull.getConvexHull(pointList);

		// set polygon
		Point2D[] hullPoints = new Point2D[hullPoint.size()];
		for (int ii = 0; ii < hullPoints.length; ii++) {
			double x = hullPoint.get(ii).getX();
			double y = hullPoint.get(ii).getY();
			hullPoints[ii] = new Point2D.Double(x, y);
		}

		// set minimum rectangle
		Point hullMin = new Point(cvxHull.getxxPoint().getX(), cvxHull.getyyPoint().getX());
		Point hullMax = new Point(cvxHull.getxxPoint().getY(), cvxHull.getyyPoint().getY());

		Point2D[] minmaxPoints = new Point2D[4];
		double x0 = hullMax.getX();
		double y0 = hullMax.getY();
		double x1 = hullMin.getX();
		double y1 = hullMax.getY();
		double x2 = hullMin.getX();
		double y2 = hullMin.getY();
		double x3 = hullMax.getX();
		double y3 = hullMin.getY();

		minmaxPoints[0] = new Point2D.Double(x0, y0);
		minmaxPoints[1] = new Point2D.Double(x1, y1);
		minmaxPoints[2] = new Point2D.Double(x2, y2);
		minmaxPoints[3] = new Point2D.Double(x3, y3);

		totalHullPoints += pointsRaw.length;

		List<CoreTRDvo> trList = this.cs.findByItemNameOrIdContainingCoreTRs(
				minmaxPoints[2].getY(),
				minmaxPoints[0].getY(),
				minmaxPoints[1].getX(),
				minmaxPoints[0].getX()
				);
		
		int trSize = trList.size(); 

		Point2D[] pointsRange = new Point2D[trSize];
		for (int j = 0; j < trSize; ++j) {
			double x = Double.parseDouble(trList.get(j).getLongitude().toString());
			double y = Double.parseDouble(trList.get(j).getLatitude().toString());
			pointsRange[j] = new Point2D.Double(x, y);
		}
		List<Point> pointsRangeList = new ArrayList<Point>();
		for (int ii = 0; ii < pointsRange.length; ii++) {
			pointsRangeList.add(new Point(pointsRange[ii].getX(), pointsRange[ii].getY()));
		}

		Point[] sARange = pointsRangeList.toArray(new Point[pointsRangeList.size()]);
		pointsRangeList.clear();
		Point[] sA = hullPoint.toArray(new Point[hullPoint.size()]);
		Polygon h_p_11 = new Polygon(sA);
		log.debug("\r\rPolygon====== {}",(sA.length - 1));
		int countTrue = 0;
		
		List<CoreTRDvo> vData = new ArrayList<CoreTRDvo>();
		vData.clear();
		for (int ki = 0; ki < sARange.length; ki++) {
			boolean check = Calculate.checkInside(h_p_11, sARange[ki].getX(), sARange[ki].getY());
			if (check == true) {
				log.debug("\rtr~~~~~~~~~~~~~~~~~~~{}\r",trList.get(ki));
				vData.add(trList.get(ki));
				countTrue += 1;
			}
		}
		log.debug("v_data == {}\r",vData);
		log.debug("v_data.size == {}",vData.size());
		log.debug("check_true = {}      check_false = {}   total_check = {}",
				countTrue,
				(sARange.length - countTrue),
				(countTrue + sARange.length - countTrue)
		);

		hullPoint.clear();
		pointList.clear();
		long endTime = new Date().getTime();

		// Calculate statistics
		long duration = endTime - startTime;
		double averageTime = ((double) duration);
		double averageHullPoints = ((double) totalHullPoints);

		// Print statistics to screen
		log.debug("Total time elapsed: {} ms", duration);
		log.debug("Average time per iteration: {} ms", averageTime);
		log.debug("Average number of hull points: {}", averageHullPoints);
		return vData;
	}
}
