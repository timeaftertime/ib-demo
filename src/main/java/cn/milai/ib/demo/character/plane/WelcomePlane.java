package cn.milai.ib.demo.character.plane;

import cn.milai.ib.container.UIContainer;

public class WelcomePlane extends EnemyPlane {

	public WelcomePlane(int x, int y, UIContainer container) {
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
		if (getY() > getContainer().getHeight())
			getContainer().removeObject(this);
	}

}
