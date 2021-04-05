package cn.milai.ibdemo.role.explosion;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.property.Explosible;
import cn.milai.ib.role.property.base.BaseProperty;

/**
 * 产生单个{@link BaseExplosion} 的爆炸产生器
 * 2019.11.29
 * @author milai
 */
public class BaseExplosible extends BaseProperty implements Explosible {

	public BaseExplosible(Role role) {
		super(role);
	}

	@Override
	public Explosion[] createExplosions() {
		Role role = getRole();
		return new Explosion[] {
			new BaseExplosion(role.centerX(), role.centerY(), role.getContainer()),
		};
	}

}
