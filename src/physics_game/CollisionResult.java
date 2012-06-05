package physics_game;

import java.awt.geom.Point2D;

public class CollisionResult {
	private boolean collided;
	private CollisionInformation collisionInfo;

	public CollisionResult() {
		collided = false;
	}

	public CollisionResult(Point2D toNegate, Point2D surface, CollidableDrawable other) {
		collisionInfo = new CollisionInformation(toNegate, surface, other);
		collided = true;
	}

	public boolean collision() {
		return collided;
	}

	public CollisionInformation getCollisionInformation() {
		return collisionInfo;
	}

	@Override
	public String toString() {
		if (collided)
			return "Collided : " + collisionInfo;
		return "Did not collide.";
	}
}
