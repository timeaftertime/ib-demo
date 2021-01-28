package cn.milai.ibdemo.character.explosion.creator;

import cn.milai.ib.character.explosion.Explosion;
import cn.milai.ib.character.explosion.creator.ExplosionCreator;
import cn.milai.ib.character.property.Explosible;
import cn.milai.ibdemo.character.explosion.FishFall;

/**
 * {@link FishFall} 类型爆炸的构造器
 * @author milai
 * @date 2020.04.05
 */
public class FishFallCreator implements ExplosionCreator {

	@Override
	public Explosion[] createExplosions(Explosible owner) {
		return new Explosion[] { new FishFall(owner) };
	}

}
