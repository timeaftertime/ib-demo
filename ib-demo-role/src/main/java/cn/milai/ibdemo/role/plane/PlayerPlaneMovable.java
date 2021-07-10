package cn.milai.ibdemo.role.plane;

import cn.milai.ib.role.property.Movable;
import cn.milai.ibdemo.role.property.SpecificMovable;

/**
 * {@link PlayerPlane} 的 {@link Movable}
 * @author milai
 * @date 2021.06.28
 */
public class PlayerPlaneMovable extends SpecificMovable<PlayerPlane> {

	@Override
	protected void beforeRefreshSpeeds() {
		owner().beforeRefreshSpeeds(this);
	}

	@Override
	public void afterMove() {
		owner().afterMove(this);
	}
}
