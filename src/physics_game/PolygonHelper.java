package physics_game;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PolygonHelper {
	public static Polygon createPolygon(Polygon p, Point2D translation) {
		Point2D[] vertices = new Point2D[p.getVertexCount()];
		for (int i = 0; i < p.getVertexCount(); ++i)
			vertices[i] = new Point2D.Double(p.getVertices()[i].getX() + translation.getX(), p.getVertices()[i].getY() + translation.getY());

		return new Polygon(vertices);
	}

	public static Polygon polygonRepresentingTranslation(Polygon startPoly, Point2D translation) {
		final boolean sameSign = Math.signum(translation.getX()) == Math.signum(translation.getY());
		int vertexcount = startPoly.getVertexCount();
		Polygon endPoly = createPolygon(startPoly, translation);

		Polygon polyForTop = (translation.getY() > 0) ? endPoly : startPoly;
		Polygon polyForBot = (polyForTop == startPoly) ? endPoly : startPoly;
		List<Point2D> topVertices = new ArrayList<Point2D>(vertexcount * 2);
		List<Point2D> bottomVertices = new ArrayList<Point2D>(vertexcount * 2);
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < vertexcount; ++i) {
			//top left clockwise
			Point2D vertex = polyForTop.getVertices()[i];
			if (vertex.getX() < minX) {
				minX = vertex.getX();
				if (vertex.getX() > maxX)
					maxX = vertex.getX();
				topVertices.add(vertex);
			} else if (!sameSign && vertex.getX() == minX) {
				topVertices.add(vertex);
			} else if (vertex.getX() > maxX) {
				maxX = vertex.getX();
				topVertices.add(vertex);
			} else if (sameSign && vertex.getX() == maxX) {
				topVertices.add(vertex);
			}
		}

		Collections.sort(topVertices, new Comparator<Point2D>() {
			@Override
			public int compare(Point2D a, Point2D b) {
				int x = Double.compare(a.getX(), b.getX());
				if (x != 0)
					return x;
				return sameSign ? Double.compare(b.getY(), a.getY()) : Double.compare(a.getY(), b.getY());
			}
		});

		minX = Double.POSITIVE_INFINITY;
		maxX = Double.NEGATIVE_INFINITY;
		for (int i = vertexcount - 1; i >= 0; --i) {
			//bottom left counterclockwise
			Point2D vertex = polyForBot.getVertices()[i];
			if (vertex.getX() < minX) {
				minX = vertex.getX();
				if (vertex.getX() > maxX)
					maxX = vertex.getX();
				bottomVertices.add(vertex);
			} else if (sameSign && vertex.getX() == minX) {
				bottomVertices.add(vertex);
			} else if (vertex.getX() > maxX) {
				maxX = vertex.getX();
				bottomVertices.add(vertex);
			} else if (!sameSign && vertex.getX() == maxX) {
				bottomVertices.add(vertex);
			}
		}

		Collections.sort(bottomVertices, new Comparator<Point2D>() {
			@Override
			public int compare(Point2D a, Point2D b) {
				int x = Double.compare(b.getX(), a.getX());
				if (x != 0)
					return x;
				return sameSign ? Double.compare(a.getY(), b.getY()) : Double.compare(b.getY(), a.getY());
			}
		});

		topVertices.addAll(bottomVertices);

		return new Polygon(topVertices.toArray(new Point2D[0]));
	}

	public static BoundingPolygon boundingPolygonRepresentingTranslation(BoundingPolygon b, Point2D translation) {
		Polygon[] polygons = new Polygon[b.getPolygons().length];
		for (int i = 0; i < b.getPolygons().length; i++)
			polygons[i] = polygonRepresentingTranslation(b.getPolygons()[i], translation);
		return new BoundingPolygon(polygons);
	}
}
