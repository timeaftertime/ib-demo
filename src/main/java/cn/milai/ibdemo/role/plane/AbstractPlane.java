package cn.milai.ibdemo.role.plane;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.MovableRole;
import cn.milai.ib.role.explosion.creator.ExplosionCreator;
import cn.milai.ib.role.property.CanCrash;
import cn.milai.ibdemo.role.explosion.creator.BaseExplosionCreator;

/**
 * 战机抽象基类
 * @author milai
 */
public abstract class AbstractPlane extends MovableRole implements Plane {

	private ExplosionCreator explosionCreator;

	public AbstractPlane(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		explosionCreator = new BaseExplosionCreator();
	}

	@Override
	public ExplosionCreator getExplosionCreator() { return explosionCreator; }

	@Override
	public void onCrash(CanCrash crashed) {
		crashed.loseLife(this, getDamage());
	}

}
