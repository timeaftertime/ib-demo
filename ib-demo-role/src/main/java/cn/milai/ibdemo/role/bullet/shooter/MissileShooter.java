package cn.milai.ibdemo.role.bullet.shooter;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.weapon.bullet.Bullet;
import cn.milai.ib.role.weapon.bullet.shooter.AbstractBulletShooter;
import cn.milai.ibdemo.role.bullet.Missile;

/**
 * 导弹发射器
 * @author milai
 * @date 2020.04.02
 */
public class MissileShooter extends AbstractBulletShooter {

	public MissileShooter(Role owner) {
		super(owner);
	}

	@Override
	public Bullet[] createBullets0() {
		return new Bullet[] { applyCenter(new Missile(owner), owner.centerX(), owner.centerY()) };
	}

}
