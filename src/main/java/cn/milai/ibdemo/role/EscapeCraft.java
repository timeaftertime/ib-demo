package cn.milai.ibdemo.role;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.AbstractRole;

/**
 * 逃生飞船
 * @author milai
 * @date 2020.04.08
 */
public class EscapeCraft extends AbstractRole {

	public EscapeCraft(double x, double y, LifecycleContainer container) {
		super(x, y, container);
	}

}
