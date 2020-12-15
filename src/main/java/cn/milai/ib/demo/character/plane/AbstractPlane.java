package cn.milai.ib.demo.character.plane;

import cn.milai.ib.character.MovableIBCharacter;
import cn.milai.ib.character.explosion.creator.ExplosionCreator;
import cn.milai.ib.character.property.CanCrash;
import cn.milai.ib.container.ui.UIContainer;
import cn.milai.ib.demo.character.explosion.creator.BaseExplosionCreator;

/**
 * 战机抽象基类
 * @author milai
 */
public abstract class AbstractPlane extends MovableIBCharacter implements Plane {

	private ExplosionCreator explosionCreator;

	public AbstractPlane(int x, int y, UIContainer container) {
		super(x, y, container);
		explosionCreator = new BaseExplosionCreator();
	}

	@Override
	public ExplosionCreator getExplosionCreator() {
		return explosionCreator;
	}

	@Override
	public void onCrash(CanCrash crashed) {
		crashed.loseLife(this, getDamage());
	}

}
