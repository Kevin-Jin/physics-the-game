package physics_game;

import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class CollidableDrawable extends AbstractDrawable {
	private Point2D beamHit;

	public abstract BoundingPolygon getBoundingPolygon();
	public abstract void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others);

	public Position getBeamHit() {
		return new Position(getTransformationMatrix().transform(beamHit, null));
	}

	public void setBeamHit(Position value) {
		try {
			beamHit = getTransformationMatrix().inverseTransform(value.asPoint(), null);
		} catch (NoninvertibleTransformException e) {
			beamHit = new Point2D.Double(Double.NaN, Double.NaN);
		}
	}

	public boolean collidesWith(List<CollidableDrawable> others) {
		for (CollidableDrawable curDrawable : others) {
			if (!(curDrawable instanceof Beam) && curDrawable != this) {
				// ignore beam and self
				CollisionResult cr = PolygonCollision.boundingPolygonCollision(curDrawable.getBoundingPolygon(), getBoundingPolygon());
				if (cr.collision() && (cr.getCollisionInformation().getMinimumTranslationVector().getX() != 0 || cr.getCollisionInformation().getMinimumTranslationVector().getY() != 0))
					return true;
			}
		}
		return false;
	}

	public List<CollisionResult> getCollisions(List<CollidableDrawable> others) {
		List<CollisionResult> cols = new ArrayList<CollisionResult>(others.size());
		for (CollidableDrawable curDrawable : others) {
			if (curDrawable instanceof Beam || curDrawable == this)
				continue;
			CollisionResult cr = PolygonCollision.boundingPolygonCollision(curDrawable.getBoundingPolygon(), getBoundingPolygon());
			if (cr.collision()) {
				cr.getCollisionInformation().setCollidedWith(curDrawable);
				cols.add(cr);
			}
		}

		return cols;
	}
}
