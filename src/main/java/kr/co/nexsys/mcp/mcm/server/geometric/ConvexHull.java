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
	public static int N;
	public static List<Point> ps;
	public static Stack<Point> s = new Stack<Point>();
	public static Point xx, yy;

	public List<Point> getConvexHull(List<Point> h) {
		N = h.size();
		ps = new ArrayList<Point>();
		ps = h;

		for (int i = 0; i < N; i++) {
			double px = ((Point) ps.get(i)).getX();
			if (minX > px) {
				minX = px;
				minXi = i;
			}
		}
		Point minX_p = ps.get(minXi);
		ps.remove(minXi);
		Collections.sort(ps, new PointComp(minX_p));
		s.add(ps.get(ps.size() - 1));
		s.add(minX_p);

		for (int i = 0; i < N - 1; i++) {
			while (PointComp.calExpr(s.elementAt(s.size() - 2), s.lastElement(), ps.get(i)) <= 0) {
				s.pop();
			}
			s.add(ps.get(i));
		}

		double m_a = Double.MAX_VALUE;
		double m_a1 = 0;// Double.MAX_VALUE;

		for (int i = 0; i < s.size(); i++) {
			double px = ((Point) s.get(i)).getX();
			if (m_a1 < px) {
				m_a1 = px;
			}
		}
		for (int i = 0; i < s.size(); i++) {
			double px = ((Point) s.get(i)).getX();
			if (m_a > px) {
				m_a = px;
			}
		}
		Point minmin_p = new Point(m_a, m_a1);

		m_a = Double.MAX_VALUE;
		m_a1 = 0;// Double.MAX_VALUE;
		for (int i = 0; i < s.size(); i++) {
			double py = ((Point) s.get(i)).getY();
			if (m_a1 < py) {
				m_a1 = py;
			}
		}
		for (int i = 0; i < s.size(); i++) {
			double py = ((Point) s.get(i)).getY();
			if (m_a > py) {
				m_a = py;
			}
		}
		Point maxmax_p = new Point(m_a, m_a1);
		xx = minmin_p;
		yy = maxmax_p;
		minXi = Integer.MAX_VALUE;
		minX = Double.MAX_VALUE;
		return s;
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
		if (d > 0 || ((d == 0) && getDistance(minXi, p1) > getDistance(minXi, p2)))
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
