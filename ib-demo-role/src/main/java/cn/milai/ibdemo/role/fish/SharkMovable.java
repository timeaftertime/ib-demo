package cn.milai.ibdemo.role.fish;

import cn.milai.ib.role.property.Movable;
import cn.milai.ibdemo.role.property.SpecificMovable;

/**
 * {@link Shark} 的 {@link Movable}
 * @author milai
 * @date 2021.06.26
 */
public class SharkMovable extends SpecificMovable<Shark> {

	@Override
	protected void beforeRefreshSpeeds() {
		owner().beforeRefreshSpeeds(this);
	}

	@Override
	public void afterMove() {
		owner().afterMove(this);
	}

	@Override
	protected void afterRefreshSpeeds() {
		owner().afterRefreshSpeeds(this);
	}

}
