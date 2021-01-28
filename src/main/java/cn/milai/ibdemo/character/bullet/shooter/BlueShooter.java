package cn.milai.ibdemo.character.bullet.shooter;

import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.weapon.bullet.Bullet;
import cn.milai.ib.character.weapon.bullet.shooter.AbstractBulletShooter;
import cn.milai.ibdemo.character.bullet.BlueBullet;

/**
 * 发射蓝色通常子弹的发射器
 * 2019.11.21
 * @author milai
 */
public class BlueShooter extends AbstractBulletShooter {

	public BlueShooter(int shootInterval, int maxBulletNum, IBCharacter owner) {
		super(owner, shootInterval, maxBulletNum);
	}

	@Override
	public Bullet[] createBullets0() {
		return new Bullet[] { new BlueBullet((int) owner.getCenterX(), (int) owner.getCenterY(), owner) };
	}

}
