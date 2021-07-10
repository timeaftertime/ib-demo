package cn.milai.ibdemo.role.property;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.base.BaseMovable;

/**
 * 指定类型的 {@link Movable}
 * @author milai
 * @date 2021.06.28
 */
public class SpecificMovable<T extends Role> extends BaseMovable {

	@SuppressWarnings("unchecked")
	@Override
	public T owner() {
		return (T) super.owner();
	}
}
