package kr.co.nexsys.mcp.mcm.server.geometric;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kimyh(kimyhjava@pukyong.ac.kr)
 * Creation: Sep 27, 2018
 * File name: Calculate_.java
 */
public interface Calculate {
	static boolean checkInside(Polygon polygon, double x, double y) {
		List<Line> lines = calculateLines(polygon);
		for(Line a:lines) {
			a.lineView();
		}
		List<Line> intersectionLines = filterIntersectingLines(lines, y);
		List<Point> intersectionPoints = calculateIntersectionPoints(intersectionLines, y);
		sortPoinsByX(intersectionPoints);
		return calculateInside(intersectionPoints, x);
	}

	static List<Line> calculateLines(Polygon polygon) {
		List<Line> results = new LinkedList<Line>();
		Point[] points = polygon.getPoints();

		Point lastPoint = null;
		for (Point point : points) {
			if (lastPoint != null) {
				results.add(new Line(lastPoint, point));
			}
			lastPoint = point;
		}
		results.add(new Line(lastPoint, points[0]));
		return results;
	}

	static List<Line> filterIntersectingLines(List<Line> lines, double y) {
		List<Line> results = new LinkedList<Line>();
		for (Line line : lines) {
			if (isLineIntersectingAtY(line, y)) {
				results.add(line);
			}
		}
		return results;
	}

	static boolean isLineIntersectingAtY(Line line, double y) {
		double minY = Math.min(line.getFrom().getY(), line.getTo().getY());
		double maxY = Math.max(line.getFrom().getY(), line.getTo().getY());
		return y > minY && y <= maxY;

	}

	static List<Point> calculateIntersectionPoints(List<Line> lines, double y) {
		List<Point> results = new LinkedList<Point>();
		for (Line line : lines) {
			double x = calculateLineXAtY(line, y);
			results.add(new Point(x, y));
		}
		return results;
	}

	static double calculateLineXAtY(Line line, double y) {
		Point from = line.getFrom();
		double slope = calculateSlope(line);
		return from.getX() + (y - from.getY()) / slope;
	}

	static double calculateSlope(Line line) {
		Point from = line.getFrom();
		Point to = line.getTo();
		return (to.getY() - from.getY()) / (to.getX() - from.getX());
	}

	static void sortPoinsByX(List<Point> points) {
		Collections.sort(points, new Comparator<Point>() {
			public int compare(Point p1, Point p2) {
				return Double.compare(p1.getX(), p2.getX());
			}
		});
	}

	static boolean calculateInside(List<Point> sortedPoints, double x) {
		boolean inside = false;
		for (Point point : sortedPoints) {
			if (x < point.getX()) {
				break;
			}
			inside = !inside;
		}
		return inside;
	}
}
