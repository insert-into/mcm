package kr.co.nexsys.mcp.mcm.server.geometric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * @author kimyh(kimyhjava@pukyong.ac.kr) Creation: Sep 27, 2018 File name:
 *         Convex_hull.java
 */
public class ConvexHull {
	public static double minX = Double.MAX_VALUE;
	public static double minY = Double.MAX_VALUE;
	public static int minXi = Integer.MAX_VALUE;
	public  int pointListSize;
	public  List<Point> listPoint;
	public static Stack<Point> pointStack = new Stack<Point>();
	public static Point xx, yy;
	

	public List<Point> getConvexHull(List<Point> pList) {
		pointListSize = pList.size();
		listPoint = new ArrayList<Point>();
		listPoint = pList;

		for (int i = 0; i < pointListSize; i++) {
			double px = ((Point) pList.get(i)).getX();
			if (minX > px) {
				minX = px;
				minXi = i;
			}
		}
		Point minXPoint = pList.get(minXi);
		pList.remove(minXi);
		Collections.sort(pList, new PointComp(minXPoint));
		pointStack.add(pList.get(pList.size() - 1));
		pointStack.add(minXPoint);

		for (int i = 0; i < pointListSize - 1; i++) {
			while (PointComp.calExpr(pointStack.elementAt(pointStack.size() - 2), pointStack.lastElement(), pList.get(i)) <= 0) {
				pointStack.pop();
			}
			pointStack.add(pList.get(i));
		}

		double dblM = Double.MAX_VALUE;
		double dblM1 = 0;// Double.MAX_VALUE;

		for (int i = 0; i < pointStack.size(); i++) {
			double px = ((Point) pointStack.get(i)).getX();
			if (dblM1 < px) {
				dblM1 = px;
			}
		}
		for (int i = 0; i < pointStack.size(); i++) {
			double px = ((Point) pointStack.get(i)).getX();
			if (dblM > px) {
				dblM = px;
			}
		}
		Point minMinPoint = new Point(dblM, dblM1);

		dblM = Double.MAX_VALUE;
		dblM1 = 0;// Double.MAX_VALUE;
		for (int i = 0; i < pointStack.size(); i++) {
			double py = ((Point) pointStack.get(i)).getY();
			if (dblM1 < py) {
				dblM1 = py;
			}
		}
		for (int i = 0; i < pointStack.size(); i++) {
			double py = ((Point) pointStack.get(i)).getY();
			if (dblM > py) {
				dblM = py;
			}
		}
		Point maxMaxPoint = new Point(dblM, dblM1);
		xx = minMinPoint;
		yy = maxMaxPoint;
		minXi = Integer.MAX_VALUE;
		minX = Double.MAX_VALUE;
		return pointStack;
	}

	public Point getxxPoint() {
		return xx;
	}

	public Point getyyPoint() {
		return yy;
	}
}

class PointComp implements Comparator<Point> {

	Point minXi;

	public PointComp(Point p) {
		this.minXi = p;
	}

	public static double calExpr(Point i, Point j, Point k) {
		double area1 = i.getX() * j.getY() + j.getX() * k.getY() + k.getX() * i.getY();
		double area2 = i.getY() * j.getX() + j.getY() * k.getX() + k.getY() * i.getX();
		return area1 - area2;
	}

	@Override
	public int compare(Point p1, Point p2) {
		double d = calExpr(p1, minXi, p2);
		if (d > 0 || (d == 0) && getDistance(minXi, p1) > getDistance(minXi, p2))
			return 1;
		else
			return -1;
	}

	public double getDistance(Point i, Point j) {
		double dx = i.getX() - j.getX();
		double dy = i.getY() - j.getY();
		return dx * dx + dy * dy;
	}
}
