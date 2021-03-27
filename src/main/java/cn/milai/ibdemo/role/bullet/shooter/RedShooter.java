package cn.milai.ibdemo.role.bullet.shooter;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.weapon.bullet.Bullet;
import cn.milai.ib.role.weapon.bullet.shooter.AbstractBulletShooter;
import cn.milai.ibdemo.role.bullet.RedBullet;

/**
 * 发射红色通常子弹的发射器
 * 2019.11.21
 * @author milai
 */
public class RedShooter extends AbstractBulletShooter {

	private static final long INIT_SHOOT_INTERVAL = 10L;

	private static final int INIT_MAX_BULLET_NUM = 3;

	public RedShooter(Role owner) {
		super(owner, INIT_SHOOT_INTERVAL, INIT_MAX_BULLET_NUM);
	}

	@Override
	protected Bullet[] createBullets0() {
		return new Bullet[] { new RedBullet((int) owner.centerX(), owner.getIntY() + owner.getIntH(), owner) };
	}

}
