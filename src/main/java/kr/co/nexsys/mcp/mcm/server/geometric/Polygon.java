package kr.co.nexsys.mcp.mcm.server.geometric;

/**
 * @author kimyh(kimyhjava@pukyong.ac.kr)
 * Creation: Sep 27, 2018
 * File name: Polygon.java
 */
public class Polygon {
	private Point[] points;

	public Polygon(Point[] points) {
		this.points=points;
	}
	public Point[] getPoints() {
		return points;
	}
}
