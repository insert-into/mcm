package kr.co.nexsys.mcp.mcm.server.geometric;

/**
 * @author kimyh(kimyhjava@pukyong.ac.kr)
 * Creation: Sep 27, 2018
 * File name: Line.java
 */
public class Line {
	private Point from;
	private Point to;
	
	public Line(Point from, Point to) {
		this.from=from;
		this.to=to;
	}
	public Point getFrom() {
		return from;
	}
	public Point getTo() {
		return to;
	}
	public void lineView() {
		System.out.print("[");
		from.PointView();
		System.out.print("--> ");
		to.PointView();
		System.out.println("]");
	}
}
