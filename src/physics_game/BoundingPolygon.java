package physics_game;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class BoundingPolygon {
	private Polygon[] polygons;

	public BoundingPolygon(Polygon[] polygons) {
		this.polygons = polygons;
	}

	public BoundingPolygon(BoundingPolygon[] boundPolygons) {
		polygons = makeBoundingPolygon(boundPolygons);
	}

	public static Polygon[] makeBoundingPolygon(BoundingPolygon[] boundPolygons) {
		int size = 0;
		for (BoundingPolygon boundPolygon : boundPolygons)
			size += boundPolygon.getPolygons().length;
		Polygon[] polygons = new Polygon[size];
		int lastIndex = 0;
		for (BoundingPolygon boundPolygon : boundPolygons) {
			int polygonCount = boundPolygon.getPolygons().length;
			System.arraycopy(boundPolygon.getPolygons(), 0, polygons, lastIndex, polygonCount);
			lastIndex += polygonCount;
		}
		return polygons;
	}

	public Polygon[] getPolygons() {
		return polygons;
	}

	public void setPolygons(Polygon[] value) {
		polygons = value;
	}

	public static BoundingPolygon transformBoundingPolygon(BoundingPolygon boundingPoly, AbstractDrawable ent) {
		Polygon[] oldPolygons = boundingPoly.polygons;
		Polygon[] polygons = new Polygon[oldPolygons.length];
		AffineTransform m = ent.getTransformationMatrix();
		for (int i = 0; i < polygons.length; ++i)
			polygons[i] = Polygon.transformPolygon(oldPolygons[i], m);
		return new BoundingPolygon(polygons);
	}

	public boolean isPointInsideBoundingPolygon(Point2D point) {
		for (Polygon p : polygons)
			if (p.isPointInsidePolygon(point))
				return true;
		return false;
	}

	/**
	 * pointA1 and pointA2 are on the same line, while pointB1 and pointB2 are on a different line
	 * http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
	 * @param pointA1
	 * @param pointA2
	 * @param pointB1
	 * @param pointB2
	 * @return Point2D(NaN, NaN) if no intersection found
	 */
	private static Point2D findIntersection(Point2D pointA1, Point2D pointA2, Point2D pointB1, Point2D pointB2) {
		double ua = (pointB2.getX() - pointB1.getX()) * (pointA1.getY() - pointB1.getY()) - (pointB2.getY() - pointB1.getY()) * (pointA1.getX() - pointB1.getX());
		double ub = (pointA2.getX() - pointA1.getX()) * (pointA1.getY() - pointB1.getY()) - (pointA2.getY() - pointA1.getY()) * (pointA1.getX() - pointB1.getX());
		double denominator = (pointB2.getY() - pointB1.getY()) * (pointA2.getX() - pointA1.getX()) - (pointB2.getX() - pointB1.getX()) * (pointA2.getY() - pointA1.getY());

		if (denominator != 0) {
			ua /= denominator;
			ub /= denominator;

			// check if the intersection occurs on the segments, and not some
			// far distance away
			if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1)
				return new Point2D.Double(pointA1.getX() + ua * (pointA2.getX() - pointA1.getX()), pointA1.getY() + ua * (pointA2.getY() - pointA1.getY()));
		} else {
			// parallel
			if (ua == 0 && ub == 0)
				// coincident - infinity many intersection points
				return new Point2D.Double((pointA1.getX() + pointA2.getX()) / 2, (pointA1.getY() + pointA2.getY()) / 2);
		}

		return new Point2D.Double(Double.NaN, Double.NaN);
	}

	public Point2D closestPoint(Position source, Position end) {
		Point2D pt = new Point2D.Double(Double.NaN, Double.NaN);
		double smallestDistanceSquared = Double.POSITIVE_INFINITY;
		for (Polygon p : polygons) {
			Point2D intersectPoint;
			for (int i = 0; i < p.getVertexCount(); i++) {
				intersectPoint = findIntersection(p.getVertices()[i], new Point2D.Double(p.getVertices()[i].getX() + p.getEdges()[i].getX(), p.getVertices()[i].getY() + p.getEdges()[i].getY()), source.asPoint(), end.asPoint());
				if (!Double.isNaN(intersectPoint.getX()) && !Double.isNaN(intersectPoint.getY())) {
					double distanceSquaredFromThisEdge = Math.pow(intersectPoint.getX() + source.getX(), 2) + Math.pow(intersectPoint.getY() + source.getY(), 2);
					if (distanceSquaredFromThisEdge < smallestDistanceSquared) {
						pt = intersectPoint;
						smallestDistanceSquared = distanceSquaredFromThisEdge;
					}
				}
			}
		}
		return pt;
	}
}
