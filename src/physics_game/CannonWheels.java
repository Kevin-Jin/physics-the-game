package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CannonWheels extends BottomLeftOriginedProp {
	public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(0, 0), new Point2D.Double(42, 0), new Point2D.Double(42, 34), new Point2D.Double(0, 34) }) });

	private Cannon parent;
	private Animation animation;
	private boolean animate, flip;

	public CannonWheels(Cannon parent) {
		super(0);
		animate = true;
		this.parent = parent;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = baseBoundPoly;
		animation = new Animation(new String[] { "legsStand", "legs1", "legs2", "legs3", "legs4", "legs5" }, .06);
	}

	@Override
	public BufferedImage getTexture() {
		return animation.getTexture();
	}

	@Override
	public BoundingPolygon getBoundingPolygon() {
		return new BoundingPolygon(new Polygon[0]);
	}

	public void setAnimate(boolean value) {
		animate = value;
	}

	public BoundingPolygon getRealBoundingPolygon() {
		return boundPoly;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		boolean hitGround = false;
		for (CollisionResult res : parent.getBody().getCollisions(others)) {
			if (res.getCollisionInformation().getCollidingSurface().getY() == 0) {
				hitGround = true;
				break;
			}
		}
		if (animate && hitGround)
			animation.update(tDelta);
		else
			animation.reset();
	}

	public void sync(Position pos, boolean flip) {
		this.pos = pos;
		this.flip = flip;
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		
	}

	@Override
	public boolean flipHorizontally() {
		return flip;
	}
}
