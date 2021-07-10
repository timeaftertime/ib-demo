package cn.milai.ibdemo.role.explosion;

import cn.milai.ib.config.ItemConfigApplier;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.property.Explosible;
import cn.milai.ib.role.property.base.BaseRoleProperty;

/**
 * {@link FishFall} 类型爆炸的构造器
 * @author milai
 * @date 2020.04.05
 */
public class FishFallExplosible extends BaseRoleProperty implements Explosible, ItemConfigApplier {

	@Override
	public Explosion[] createExplosions() {
		return new Explosion[] { apply(new FishFall(owner())) };
	}

}
