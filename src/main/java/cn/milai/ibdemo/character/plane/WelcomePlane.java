package cn.milai.ibdemo.character.plane;

import cn.milai.ib.container.ui.UIContainer;

public class WelcomePlane extends EnemyPlane {

	public WelcomePlane(double x, double y, UIContainer container) {
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
		if (getIntY() > getContainer().getHeight())
			getContainer().removeObject(this);
	}

}
