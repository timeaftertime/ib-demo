package cn.milai.ibdemo.role.explosion;

import cn.milai.ib.actor.config.ItemConfigApplier;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.nature.Explosible;
import cn.milai.ib.role.nature.base.AbstractRoleNature;

/**
 * {@link FishFall} 类型爆炸的构造器
 * @author milai
 * @date 2020.04.05
 */
public class FishFallExplosible extends AbstractRoleNature implements Explosible, ItemConfigApplier {

	public FishFallExplosible(Role owner) {
		super(owner);
	}

	@Override
	public Explosion[] createExplosions() {
		return new Explosion[] { apply(new FishFall(owner())) };
	}

}
