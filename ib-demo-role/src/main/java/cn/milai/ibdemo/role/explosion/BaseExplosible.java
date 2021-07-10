package cn.milai.ibdemo.role.explosion;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.property.Explosible;
import cn.milai.ib.role.property.base.BaseRoleProperty;

/**
 * 产生单个{@link BaseExplosion} 的爆炸产生器
 * 2019.11.29
 * @author milai
 */
public class BaseExplosible extends BaseRoleProperty implements Explosible {

	@Override
	public Explosion[] createExplosions() {
		Role role = owner();
		return new Explosion[] { applyXY(new BaseExplosion(), role.getX(), role.getY()) };
	}

}
