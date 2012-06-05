package physics_game;

import java.util.List;

public abstract class CenterOriginedProp extends StationaryEntity {
	public CenterOriginedProp(double decelX) {
		xDeceleration = decelX;
	}

	@Override
	public boolean transformAboutCenter() {
		return true;
	}

	@Override
	public void stretch(List<CollidableDrawable> others, double tDelta) {
        double oldWidth = getWidth();
		double oldScale = scale;
		double oldPosX = pos.getX();
		double oldPosY = pos.getY();
		BoundingPolygon oldPoly = boundPoly;

		scale = Math.min(Math.max(scale + getStretchRate() * tDelta, getMinimumScale()), getMaximumScale());

		if (scale != oldScale) {
			pos.setX(pos.getX() - (getWidth() - oldWidth) / 2);
			pos.setY(Math.max(pos.getY() - (getWidth() - oldWidth) / 2, 0));
			boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
			if (collidesWith(others)) {
				scale = oldScale;
				pos.setX(oldPosX);
				pos.setY(oldPosY);
				boundPoly = oldPoly;
			}
		}
	}

	@Override
	public void shrink(double tDelta) {
		double oldWidth = getWidth();
		super.shrink(tDelta);
		// even when TransformAboutCenter is true, position still refers to
		// bottom left corner
		// and not the center in order to simplify other processing code. we
		// have to adjust
		// x and y to compensate.
		pos.setX(pos.getX() - (getWidth() - oldWidth) / 2);
		// we don't want the y to go below the ground.
		// this essentially just translates the object up a bit after scaling if
		// it does hit the ground
		pos.setY(Math.max(pos.getY() - (getWidth() - oldWidth) / 2, 0));
	}
}
