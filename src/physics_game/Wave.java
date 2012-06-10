package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Wave extends CenterOriginedProp {
	private static final double AIR_REFRACTION_INDEX = 1;
	private static final double HOLD_TIME = 1;

	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
			new Point2D.Double(0, 0),
			new Point2D.Double(5, 0),
			new Point2D.Double(5,19),
			new Point2D.Double(0, 19)
	}) });

	private double incidence;
	private double remainingHoldTime;
	protected double rot;
	private boolean ignoreRefraction;

	public Wave() {
		super(0);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		rot = 0;
	}
	public boolean shouldCreateFadingWave(){
		return remainingHoldTime <= 0;
	}
	public FadingWave getFadingWave(){
		return  new FadingWave(rot, new Position(pos.getX(),pos.getY()));
	}

	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		if (remainingHoldTime > 0 && (remainingHoldTime -= tDelta) > 0) {
			Velocity saved = vel;
			vel = new Velocity(0, 0);
			super.recalculate(others, xMin, 0, -1000, tDelta);
			vel = saved;
		} else {
			super.recalculate(others, xMin, 0, -1000, tDelta);
		}
	}

	public void reset(boolean p1Won) {
		this.pos = new Position(637, 320);
		// [0, pi / 2) limits angle distribution to a range of 90 degrees.
		// Subtract pi / 4 to get -45 to 45 degrees.
		double angle = Math.random() * Math.PI / 2 - Math.PI / 4;
		incidence = Math.abs(angle);
		if (p1Won) //give wave to winner
			angle += Math.PI; //rotate 180 degrees
		vel = new Velocity(angle, 500);
		rot = angle;
		remainingHoldTime = HOLD_TIME;
		ignoreRefraction = true;
	}


	public boolean isBetween(double xmin, double xmax) {
		double minX = 2000, maxX = -1;
		for (Point2D d : boundPoly.getPolygons()[0].vertices) {
			double x = d.getX();
			if (x < minX)
				minX = x;
			if (x > maxX)
				maxX = x;
		}
		return minX >= xmin && maxX <= xmax;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("wave");
	}

	public double getAngleOfIncidence() {
		return incidence;
	}

	@Override
	public double getWidth() {
		return super.getWidth() * 2;
	}

	@Override
	public double getHeight() {
		return super.getHeight() * 2;
	}

	@Override
	public double getRotation() {
		return rot;
	}

	public boolean flipHorizontally() {
		return true;
	}

	private double angleMod(double x, double y) {
		if (y == 0)
			return x;
		return x - y * Math.floor(x / y);
	}

	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable collidedWith = collisionInfo.getCollidedWith();
		if (collidedWith instanceof RefractionRectangle){
			if (!ignoreRefraction) {
				double theta = angleMod(rot, 2 * Math.PI);
				boolean reflectOverXEqualsY = false;
				if (theta > Math.PI) {
					theta -= Math.PI;
					reflectOverXEqualsY = true;
				}
				boolean reflectOverYAxis = false;
				if (theta > Math.PI / 2) {
					theta = Math.PI - theta;
					reflectOverYAxis = true;
				}
				rot = Math.asin(AIR_REFRACTION_INDEX * Math.sin(theta) / RefractionRectangle.REFRACTION_INDEX);
				if (reflectOverYAxis)
					rot = Math.PI - rot;
				if (reflectOverXEqualsY)
					rot += Math.PI;
				vel = new Velocity(rot, 500);
			}
			ignoreRefraction =true;
			return;
		}
		Point2D d = collisionInfo.getMinimumTranslationVector();
		pos.setX(pos.getX() + d.getX());
		pos.setY(pos.getY() + d.getY());
		Point2D surface = collisionInfo.getCollidingSurface();
		boolean found = false;
		if (surface.getX() == 0) {
			vel.setX(vel.getX() * -1);
			found = true;
		}
		if (surface.getY() == 0) {
			vel.setY(vel.getY() * -1);
			found = true;
		}
		if (!found) {
			vel.setX(vel.getX() * -1);
			vel.setY(vel.getY() * -1);
		}
		rot = Math.atan2(vel.getY(), vel.getX());
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		if (collidedWith instanceof Paddle)
			ignoreRefraction = false;
	}
}
