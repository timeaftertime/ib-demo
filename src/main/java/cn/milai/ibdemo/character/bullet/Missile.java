package cn.milai.ibdemo.character.bullet;

import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.weapon.bullet.AbstractBullet;

/**
 * 导弹
 * @author milai
 */
public class Missile extends AbstractBullet {

	public Missile(int x, int y, IBCharacter owner) {
		super(x, y, owner);
	}

}
