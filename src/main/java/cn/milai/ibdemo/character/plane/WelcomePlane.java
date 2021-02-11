package cn.milai.ibdemo.character.plane;

import cn.milai.ib.container.lifecycle.LifecycleContainer;

public class WelcomePlane extends EnemyPlane {

	public WelcomePlane(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		setSpeedX(getRatedSpeedX());
		setSpeedY(getRatedSpeedY());
	}

	@Override
	protected void beforeMove() {

	}

	@Override
	protected void afterMove() {
		removeIfOutOfOwner();
	}

	private void removeIfOutOfOwner() {
		if (getIntY() > getContainer().getH())
			getContainer().removeObject(this);
	}

}
