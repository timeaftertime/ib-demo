package cn.milai.ibdemo.role.explosion;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.property.Explosible;
import cn.milai.ib.role.property.base.BaseProperty;

/**
 * {@link FishFall} 类型爆炸的构造器
 * @author milai
 * @date 2020.04.05
 */
public class FishFallExplosible extends BaseProperty implements Explosible {

	public FishFallExplosible(Role role) {
		super(role);
	}

	@Override
	public Explosion[] createExplosions() {
		return new Explosion[] { new FishFall(getRole()) };
	}

}
