package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class ExitDoor extends BottomLeftOriginedProp {
	private static final BoundingPolygon BASE_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(0, 0), new Point2D.Double(201, 0), new Point2D.Double(201, 258), new Point2D.Double(0, 258) }) });

	private boolean hit;
	private String destLevel;

	public ExitDoor(Position v, String destination) {
		super(0);
		Point2D posVector = v.asPoint();
		baseBoundPoly = BASE_POLYGON;

		pos.setX(v.getX());
		pos.setY(v.getY());
		Polygon poly = BASE_POLYGON.getPolygons()[0];
		Point2D[] vertices = new Point2D[poly.getVertexCount()];
		for (int i = 0; i < vertices.length; ++i)
			vertices[i] = new Point2D.Double(poly.getVertices()[i].getX() + posVector.getX(), poly.getVertices()[i].getY() + posVector.getY());
		Polygon newPoly = new Polygon(vertices);
		boundPoly = new BoundingPolygon(new Polygon[] { newPoly });
		destLevel = destination;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof AvatarBody)
			hit = true;
	}

	public boolean changeMap() {
		return hit;
	}

	public String getDestination() {
		return destLevel;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture(hit ? "openDoor" : "door");
	}
}
