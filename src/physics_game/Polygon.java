package physics_game;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Polygon {
	protected final Point2D[] vertices;
	private final int numOfVertices;
	protected final Point2D[] edges;
	protected Point2D center;

	public Polygon(Point2D center, Point2D[] vertices) {
		this.center = center;
		this.vertices = vertices;
		numOfVertices = vertices.length;
		edges = new Point2D[numOfVertices];
		calculateEdges();
	}

	public Polygon(Point2D[] vertices) {
		this.vertices = vertices;
		numOfVertices = vertices.length;
		edges = new Point2D[numOfVertices];
		center = new Point2D.Double();
		calculateCenter();
		calculateEdges();
	}

	public int getVertexCount() {
		return numOfVertices;
	}

	public Point2D[] getVertices() {
		return vertices;
	}

	public Point2D[] getEdges() {
		return edges;
	}

	public Point2D getCenter() {
		return center;
	}

	private void calculateCenter() {
		double cx = 0, cy = 0;
		for (Point2D v : vertices) {
			cx += v.getX();
			cy += v.getY();
		}
		center.setLocation(cx / numOfVertices, cy / numOfVertices);
	}

	private void calculateEdges() {
		for (int i = 0; i < numOfVertices - 1; ++i)
			edges[i] = new Point2D.Double(vertices[i + 1].getX() - vertices[i].getX(), vertices[i + 1].getY() - vertices[i].getY());
		edges[numOfVertices - 1] = new Point2D.Double(vertices[0].getX() - vertices[numOfVertices - 1].getX(), vertices[0].getY() - vertices[numOfVertices - 1].getY());
	}

	public void transform(AffineTransform m) {
		for (int i = 0; i < numOfVertices; ++i)
			vertices[i] = m.transform(vertices[i], null);
		center = m.transform(center, null);
		calculateEdges();
	}

	public boolean isPointInsidePolygon(Point2D point) {
		boolean inside = false;
		for (int i = 0; i < numOfVertices; ++i) {
			Point2D cur = vertices[i];
			Point2D next;
			if (i == numOfVertices - 1)
				next = vertices[0];
			else
				next = vertices[i + 1];
			double x = point.getX();
			double y = point.getY();
			if ((cur.getY() < y && next.getY() >= y) || (next.getY() < y && cur.getY() >= y))
				if (cur.getX() + (y - cur.getY()) / (next.getY() - cur.getY()) * (next.getX() - cur.getX()) < x)
					inside = !inside;
		}
		return inside;
	}

	public static Polygon transformPolygon(Polygon p, AffineTransform m) {
		Point2D[] polygonVertices = new Point2D[p.vertices.length];
		System.arraycopy(p.vertices, 0, polygonVertices, 0, polygonVertices.length);
		Polygon polygon = new Polygon(p.center, polygonVertices);
		polygon.transform(m);
		return polygon;
	}

	public static Polygon transformPolygon(Polygon p, AbstractDrawable ent) {
		return transformPolygon(p, ent.getTransformationMatrix());
	}
}
