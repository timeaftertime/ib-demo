package cn.milai.ibdemo.role.plane;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.property.Movable;

public class WelcomePlane extends EnemyPlane {

	public WelcomePlane(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		Movable m = movable();
		m.setSpeedX(m.getRatedSpeedX());
		m.setSpeedY(m.getRatedSpeedY());
	}

	@Override
	protected void afterMove(Movable m) {
		removeIfOutOfOwner();
	}

	private void removeIfOutOfOwner() {
		if (getIntY() > getContainer().getH())
			getContainer().removeObject(this);
	}

}
