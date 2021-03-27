package cn.milai.ibdemo.role.bullet.shooter;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.weapon.bullet.Bullet;
import cn.milai.ib.role.weapon.bullet.shooter.AbstractBulletShooter;
import cn.milai.ibdemo.role.bullet.BlueBullet;

/**
 * 发射蓝色通常子弹的发射器
 * 2019.11.21
 * @author milai
 */
public class BlueShooter extends AbstractBulletShooter {

	public BlueShooter(int shootInterval, int maxBulletNum, Role owner) {
		super(owner, shootInterval, maxBulletNum);
	}

	@Override
	public Bullet[] createBullets0() {
		return new Bullet[] { new BlueBullet(owner.centerX(), owner.centerY(), owner) };
	}

}
