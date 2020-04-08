package cn.milai.ib.demo.character.bullet.shooter;

import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.weapn.bullet.Bullet;
import cn.milai.ib.character.weapn.bullet.shooter.AbstractBulletShooter;
import cn.milai.ib.demo.character.bullet.Missile;

/**
 * 导弹发射器
 * @author milai
 * @date 2020.04.02
 */
public class MissileShooter extends AbstractBulletShooter {

	public MissileShooter(IBCharacter owner) {
		super(owner);
	}

	@Override
	public Bullet[] createBullets0() {
		return new Bullet[] { new Missile((int) owner.getCenterX(), (int) owner.getCenterY(), owner) };
	}

}
