package physics_game;

import java.awt.image.BufferedImage;
import java.util.List;

public class Star extends CenterOriginedProp implements Expirable {
	
	private boolean positive;
	private boolean expired;
	private int entityId;
	
	
	public Star (boolean positive){
		super(250);
		this.positive=positive;
	}
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		super.recalculate(others, xMin, 0, 0, tDelta);
		if (pos.getY() >= Game1.HEIGHT || pos.getY() < 0 || pos.getX() < 0 || pos.getX() >= Game1.WIDTH)
			expired = true;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("star_" + ((positive) ? "pos" : "neg"));
	}

	@Override
	public int getEntityId() {
		return entityId;
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(){
		expired =true;
	}
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof CannonBall /*charge gun*/) {
			collisionInfo.setCollidedWith(this);
			collisionInfo.negateMinimumTranslationVector();
			other.collision(collisionInfo, others);
			return;
		}
	}
}
