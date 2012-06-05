package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Beam extends BottomLeftOriginedProp {
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(0, 0), new Point2D.Double(0, 18), new Point2D.Double(210, 18), new Point2D.Double(210, 0) }) });
	private final int[] TOP_VERTEX_INDEX = { 0, 0 };
	private final int[] BOTTOM_VERTEX_INDEX = { 0, 1 };

	private static final double SHOOT_VELOCITY = 1000;
	private static final double RETRACT_VELOCITY = 5000;

	private GameMap m;
	private double length;
	private double remainingSound;

	public Beam(GameMap m) {
		super(0);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;

		this.m = m;
	}

	public void start(Position initial, double angle, boolean left) {
		pos = initial;
		if (left)
			angle += Math.PI;
		rot = angle;
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}

	public void continueBeam(Position now, double angle, boolean left, double tDelta) {
		pos = now;
		if (m.getSelectedEntity() == null)
			length += SHOOT_VELOCITY * tDelta;
		else
			length = Math.sqrt(Math.pow(now.getX() - m.getSelectedEntity().getBeamHit().getX(), 2) + Math.pow(now.getY() - m.getSelectedEntity().getBeamHit().getY(), 2));
		if (left)
			angle += Math.PI;
		rot = angle;
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		remainingSound -= tDelta;
		if (remainingSound < 0) {
			SoundCache.getSound("beam").play();
			remainingSound = SoundCache.getSound("beam").getDuration();
		}
	}

	public void finish(Position now, double angle, boolean left, double tDelta) {
		pos = now;
		length -= RETRACT_VELOCITY * tDelta;
		m.setSelectedEntity(null);
		if (left)
			angle += Math.PI;
		rot = angle;
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		remainingSound -= tDelta;
		if (remainingSound < 0) {
			SoundCache.getSound("beam").play();
			remainingSound = SoundCache.getSound("beam").getDuration();
		}
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		//we already updated boundPoly in start, continueBeam, or finish
        //and we have no velocity (and hence no change in position)
        //so performing the inherited Recalculate is a waste of time
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (m.getSelectedEntity() != other && !(other instanceof AvatarBody)) {
			Position hitPos = new Position(pos.getX() + length * Math.cos(rot), pos.getY() + length * Math.sin(rot));
			m.setSelectedEntity(other);
			Point2D hitPosPoint = hitPos.asPoint();
			Point2D lengthPoint = new Point2D.Double(hitPosPoint.getX() - getPosition().asPoint().getX(), hitPosPoint.getY() - getPosition().asPoint().getY());
			if (!other.getBoundingPolygon().isPointInsideBoundingPolygon(hitPosPoint)
					&& !other.getBoundingPolygon().isPointInsideBoundingPolygon(new Point2D.Double(getBottomCornerPosition().getX() + lengthPoint.getX(), getBottomCornerPosition().getY() + lengthPoint.getY()))
					&& !other.getBoundingPolygon().isPointInsideBoundingPolygon(new Point2D.Double(getTopCornerPosition().getX() + lengthPoint.getX(), getTopCornerPosition().getY() + lengthPoint.getY()))) {
				// our beam overextends to the collided entity (the front of the beam does not actually hit the collided entity), so trim it
				// assert that either the bottom or the top edge of the beam had to have intersected one of the edges of the collided entity

				// find the intersection of the line that travels in between the two edges of the beam, and the closest edge of the collided entity
				// though center of beam's front does not necessarily have to intersect with the collided entity, if it does, we can make the grabbing
				// process smoother and less "jumpy" because we change the angle of the player less
				Point2D polygonEdgeIntersect = other.getBoundingPolygon().closestPoint(pos, hitPos);
				if (!Double.isNaN(polygonEdgeIntersect.getX()) && !Double.isNaN(polygonEdgeIntersect.getY())) {
					// an intersection was made
					hitPos = new Position(polygonEdgeIntersect);
					length = Math.sqrt(Math.pow(pos.getX() - hitPos.getX(), 2) + Math.pow(pos.getY() - hitPos.getY(), 2));
				} else {
					// if our center line did not intersect, find the intersection of the bottom edge of the beam and the closest edge of the collided entity
					polygonEdgeIntersect = other.getBoundingPolygon().closestPoint(getBottomCornerPosition(), new Position(getBottomCornerPosition().getX() + lengthPoint.getX(), getBottomCornerPosition().getY() + lengthPoint.getY()));
					if (!Double.isNaN(polygonEdgeIntersect.getX()) && !Double.isNaN(polygonEdgeIntersect.getY())) {
						// an intersection was made
						hitPos = new Position(polygonEdgeIntersect);
						length = Math.sqrt(Math.pow(pos.getX() - hitPos.getX(), 2) + Math.pow(pos.getY() - hitPos.getY(), 2));
					} else {
						// if neither our bottom edge nor center line intersected, find the intersection of the top edge of the beam and the closest edge of the collided entity
						polygonEdgeIntersect = other.getBoundingPolygon().closestPoint(getTopCornerPosition(), new Position(getTopCornerPosition().getX() + lengthPoint.getX(), getTopCornerPosition().getY() + lengthPoint.getY()));
						if (!Double.isNaN(polygonEdgeIntersect.getX()) && !Double.isNaN(polygonEdgeIntersect.getY())) {
							// an intersection was made
							hitPos = new Position(polygonEdgeIntersect);
							length = Math.sqrt(Math.pow(pos.getX() - hitPos.getX(), 2) + Math.pow(pos.getY() - hitPos.getY(), 2));
						}
					}
				}
			}
			other.setBeamHit(hitPos);
		}
	}

	public boolean outOfView() {
		return length <= 0;
	}

	public Position getTopCornerPosition() {
		return new Position(boundPoly.getPolygons()[TOP_VERTEX_INDEX[0]].getVertices()[TOP_VERTEX_INDEX[1]]);
	}

	public Position getBottomCornerPosition() {
		return new Position(boundPoly.getPolygons()[BOTTOM_VERTEX_INDEX[0]].getVertices()[BOTTOM_VERTEX_INDEX[1]]);
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("beam");
	}

	@Override
	public double getWidth() {
		return length;
	}

	@Override
	public double getHeight() {
		return getTexture().getHeight();
	}

	@Override
	public Point2D getOrigin() {
		return new Point2D.Double(0, getTexture().getHeight() / 2d);
	}

	@Override
	public Point2D getDrawPosition() {
		return getPosition().asPoint();
	}
}
