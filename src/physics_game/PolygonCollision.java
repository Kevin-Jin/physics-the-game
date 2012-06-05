package physics_game;

import java.awt.geom.Point2D;

public class PolygonCollision {
	private static double dotProduct(Point2D a, Point2D b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}

	private static void normalize(Point2D a) {
		double length = Math.sqrt(a.getX() * a.getX() + a.getY() * a.getY());
		if (length == 0)
			length = 1;
		a.setLocation(a.getX() / length, a.getY() / length);
	}

	private static double lengthSquared(Point2D a) {
		return a.getX() * a.getX() + a.getY() * a.getY();
	}

	public static CollisionResult collision(Polygon a, Polygon b) {
		Point2D axis = new Point2D.Double(1, 1);
		Point2D translationAxis = new Point2D.Double(0, 0);
		Point2D collisionEdge = new Point2D.Double(0, 0);

		final int NOTFOUND = -1;
		double minIntervalDistance = NOTFOUND;

		double intervalDist;
		double tmp, maxA, minA, minB, maxB;
		for (int i = 0; i < a.getVertexCount(); ++i) {
			Point2D edge = a.getEdges()[i];
			axis = new Point2D.Double(-edge.getY(), edge.getX());
			normalize(axis);
			minA = maxA = dotProduct(a.getVertices()[0], axis);
			for (int j = 1; j < a.getVertexCount(); ++j) {
				tmp = dotProduct(a.getVertices()[j], axis);
				if (tmp > maxA)
					maxA = tmp;
				else if (tmp < minA)
					minA = tmp;
			}
			minB = maxB = dotProduct(b.getVertices()[0], axis);
			for (int j = 1; j < b.getVertexCount(); ++j) {
				tmp = dotProduct(b.getVertices()[j], axis);
				if (tmp > maxB)
					maxB = tmp;
				else if (tmp < minB)
					minB = tmp;
			}
			intervalDist = intervalDistance(minA, maxA, minB, maxB);
			if (intervalDist > 0)
				return new CollisionResult();
			else {
				intervalDist = Math.abs(intervalDist);
				if (intervalDist < minIntervalDistance || minIntervalDistance == NOTFOUND) {
					minIntervalDistance = intervalDist;
					collisionEdge = edge;
					translationAxis = axis;
				}
			}
		}

		for (int i = 0; i < b.getVertexCount(); ++i) {
			Point2D edge = b.getEdges()[i];
			axis = new Point2D.Double(-edge.getY(), edge.getX());
			normalize(axis);
			minB = maxB = dotProduct(axis, b.getVertices()[0]);
			for (int j = 1; j < b.getVertexCount(); ++j) {
				tmp = dotProduct(axis, b.getVertices()[j]);
				if (tmp > maxB)
					maxB = tmp;
				else if (tmp < minB)
					minB = tmp;
			}
			minA = maxA = dotProduct(a.getVertices()[0], axis);
			for (int j = 1; j < a.getVertexCount(); ++j) {
				tmp = dotProduct(a.getVertices()[j], axis);
				if (tmp > maxA)
					maxA = tmp;
				else if (tmp < minA)
					minA = tmp;
			}
			intervalDist = intervalDistance(minA, maxA, minB, maxB);
			if (intervalDist > 0)
				return new CollisionResult();
			else {
				intervalDist = Math.abs(intervalDist);
				if (intervalDist < minIntervalDistance || minIntervalDistance == NOTFOUND) {
					minIntervalDistance = intervalDist;
					collisionEdge = edge;
					translationAxis = axis;
				}
			}
		}

		Point2D d = new Point2D.Double(a.getCenter().getX() - b.getCenter().getX(), a.getCenter().getY() - b.getCenter().getY());
		if (dotProduct(d, translationAxis) > 0.0f)
			translationAxis.setLocation(translationAxis.getX() * -1, translationAxis.getY() * -1);
		translationAxis.setLocation(translationAxis.getX() * minIntervalDistance, translationAxis.getY() * minIntervalDistance);
		return new CollisionResult(translationAxis, collisionEdge, null);
	}

	private static double intervalDistance(double minA, double maxA, double minB, double maxB) {
		if (minA < minB)
			return minB - maxA;
		else
			return minA - maxB;
	}

	public static CollisionResult boundingPolygonCollision(BoundingPolygon a, BoundingPolygon b) {
		CollisionResult largestTranslation = new CollisionResult();
		for (Polygon polygonA : a.getPolygons()) {
			for (Polygon polygonB : b.getPolygons()) {
				CollisionResult result = collision(polygonA, polygonB);
				if (result.collision() && (!largestTranslation.collision() || lengthSquared(largestTranslation.getCollisionInformation().getMinimumTranslationVector()) < lengthSquared(result.getCollisionInformation().getMinimumTranslationVector())))
					largestTranslation = result;
			}
		}
		return largestTranslation;
	}
}
