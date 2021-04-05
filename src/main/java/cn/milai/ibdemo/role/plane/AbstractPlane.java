package cn.milai.ibdemo.role.plane;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.MovableRole;
import cn.milai.ib.role.property.Collider;
import cn.milai.ib.role.property.base.BaseCollider;
import cn.milai.ib.role.property.base.BaseDamage;
import cn.milai.ibdemo.role.explosion.BaseExplosible;

/**
 * 战机抽象基类
 * @author milai
 */
public abstract class AbstractPlane extends MovableRole implements Plane {

	public AbstractPlane(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		setExplosible(new BaseExplosible(this));
		setCollider(new BaseCollider(this) {
			@Override
			public void onCrash(Collider crashed) {
				crashed.getRole().loseLife(AbstractPlane.this, damage().getValue());
			}
		});
		setDamage(new BaseDamage(this, getDamage()));
	}

	/**
	 * 获取伤害值
	 * @return
	 */
	protected int getDamage() { return 0; }

}
