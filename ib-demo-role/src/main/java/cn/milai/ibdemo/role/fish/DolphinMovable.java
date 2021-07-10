package cn.milai.ibdemo.role.fish;

import cn.milai.ib.role.property.Movable;
import cn.milai.ibdemo.role.property.SpecificMovable;

/**
 * {@link Dolphin} 的 {@link Movable}
 * @author milai
 * @date 2021.06.26
 */
public class DolphinMovable extends SpecificMovable<Dolphin> {

	@Override
	protected void beforeRefreshSpeeds() {
		owner().beforeRefreshSpeeds(this);
	}

	@Override
	public void afterMove() {
		owner().afterMove(this);
	}

}
