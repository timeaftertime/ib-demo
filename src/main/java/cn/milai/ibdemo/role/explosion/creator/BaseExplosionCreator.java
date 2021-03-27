package cn.milai.ibdemo.role.explosion.creator;

import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.explosion.creator.ExplosionCreator;
import cn.milai.ib.role.property.Explosible;
import cn.milai.ibdemo.role.explosion.BaseExplosion;

/**
 * 产生单个{@link BaseExplosion} 的爆炸产生器
 * 2019.11.29
 * @author milai
 */
public class BaseExplosionCreator implements ExplosionCreator {

	@Override
	public Explosion[] createExplosions(Explosible owner) {
		return new Explosion[] {
			new BaseExplosion(owner.centerX(), owner.centerY(), owner.getContainer()),
		};
	}

}
