package cn.milai.ibdemo.role.explosion;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.explosion.AbstractExplosion;

/**
 * 默认的爆炸实现
 * 2019.11.29
 * @author milai
 */
public class BaseExplosion extends AbstractExplosion {

	public BaseExplosion(double x, double y, LifecycleContainer container) {
		super(x, y, container);
	}

}
