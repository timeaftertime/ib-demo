package cn.milai.ibdemo.role;

import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.base.BaseMovable;

/**
 * {@link UltraLight} 的 {@link Movable}
 * @author milai
 * @date 2021.06.26
 */
public class UltraLightMovable extends BaseMovable {

	@Override
	public UltraLight owner() {
		return (UltraLight) super.owner();
	}

	@Override
	public void afterMove() {
		owner().afterMove(this);
	}
}
