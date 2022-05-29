package cn.milai.ibdemo.role.plane;

import cn.milai.ib.role.BaseRole;
import cn.milai.ib.role.nature.Collider;
import cn.milai.ib.role.nature.base.BaseCollider;
import cn.milai.ib.role.nature.base.BaseDamage;
import cn.milai.ib.role.nature.base.BaseMovable;
import cn.milai.ibdemo.role.explosion.BaseExplosible;

/**
 * 战机抽象基类
 * @author milai
 */
public abstract class AbstractPlane extends BaseRole implements Plane {

	public AbstractPlane() {
		setMovable(new BaseMovable(this));
		setExplosible(new BaseExplosible(this));
		setDamage(new BaseDamage(this));
		setCollider(new BaseCollider(this) {
			@Override
			public void onCollided(Collider crashed) {
				crashed.owner().getHealth().changeHP(AbstractPlane.this, -getDamage().getValue());
			}
		});
	}

}
