package cn.milai.ibdemo.role.plane;

import cn.milai.ib.role.BaseRole;
import cn.milai.ib.role.property.Collider;
import cn.milai.ib.role.property.base.BaseCollider;
import cn.milai.ib.role.property.base.BaseDamage;
import cn.milai.ibdemo.role.explosion.BaseExplosible;

/**
 * 战机抽象基类
 * @author milai
 */
public abstract class AbstractPlane extends BaseRole implements Plane {

	public AbstractPlane() {
		setExplosible(new BaseExplosible());
		setDamage(new BaseDamage());
		setCollider(new BaseCollider() {
			@Override
			public void onCollided(Collider crashed) {
				crashed.owner().getHealth().changeHP(AbstractPlane.this, -getDamage().getValue());
			}
		});
	}

}
