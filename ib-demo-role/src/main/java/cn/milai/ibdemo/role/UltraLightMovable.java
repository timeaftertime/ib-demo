package cn.milai.ibdemo.role;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.nature.Movable;
import cn.milai.ib.role.nature.base.BaseMovable;

/**
 * {@link UltraLight} 的 {@link Movable}
 * @author milai
 * @date 2021.06.26
 */
public class UltraLightMovable extends BaseMovable {

	public UltraLightMovable(Role owner) {
		super(owner);
	}

	@Override
	public UltraLight owner() {
		return (UltraLight) super.owner();
	}

	@Override
	public void afterMove() {
		owner().afterMove(this);
	}
}
