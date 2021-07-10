package cn.milai.ibdemo.role.plane;

import cn.milai.ib.role.property.Movable;

/**
 * 简单直行的 {@link EnemyPlane}
 * @author milai
 * @date 2021.06.28
 */
public class WelcomePlane extends EnemyPlane {

	public WelcomePlane() {
		setMovable(new WelcomePlaneMovable());
	}

	@Override
	protected void initEnemyPlane() {
		Movable m = getMovable();
		m.setSpeedX(m.getRatedSpeedX());
		m.setSpeedY(m.getRatedSpeedY());
	}

	protected void afterMove(Movable m) {
		removeIfOutOfOwner();
	}

	private void removeIfOutOfOwner() {
		if (getIntY() > container().getH())
			container().removeObject(this);
	}

}
