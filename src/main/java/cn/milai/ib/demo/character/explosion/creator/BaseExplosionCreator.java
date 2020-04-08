package cn.milai.ib.demo.character.explosion.creator;

import cn.milai.ib.character.explosion.Explosion;
import cn.milai.ib.character.explosion.creator.ExplosionCreator;
import cn.milai.ib.character.property.Explosible;
import cn.milai.ib.demo.character.explosion.BaseExplosion;

/**
 * 产生单个{@link BaseExplosion} 的爆炸产生器
 * 2019.11.29
 * @author milai
 */
public class BaseExplosionCreator implements ExplosionCreator {

	@Override
	public Explosion[] createExplosions(Explosible owner) {
		return new Explosion[] {
			new BaseExplosion((int) owner.getCenterX(), (int) owner.getCenterY(), owner.getContainer()),
		};
	}

}
