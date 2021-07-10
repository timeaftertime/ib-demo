package cn.milai.ibdemo.role.bullet.shooter;

import cn.milai.ib.geometry.Point;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.weapon.bullet.Bullet;
import cn.milai.ib.role.weapon.bullet.shooter.AbstractBulletShooter;
import cn.milai.ibdemo.role.bullet.RedBullet;

/**
 * 一次放射两颗平行红色普通子弹、发射间隔随着使用者生命值减小而变短的发射器
 * @author milai
 * @date 2020.04.02
 */
public class DoubleRedShooter extends AbstractBulletShooter {

	private static final long INIT_SHOOT_INTERVAL = 20L;
	private static final long MIN_SHOOT_INTERVAL = 6;

	public DoubleRedShooter(Role owner) {
		super(owner);
	}

	@Override
	public boolean canShoot() {
		setShootInterval(
			Long.max(
				MIN_SHOOT_INTERVAL,
				(long) (1.0 * owner.getHealth().getHP() / owner.getHealth().initHP() * INIT_SHOOT_INTERVAL)
			)
		);
		return super.canShoot();
	}

	@Override
	public Bullet[] createBullets0() {
		Point p1 = new Point(
			(long) (owner.centerX() - owner.getW() / 4), (long) owner.centerY()
		).rotate(owner.centerX(), owner.centerY(), owner.getDirection());
		Point p2 = new Point(
			(long) (owner.centerX() + owner.getW() / 4), (long) owner.centerY()
		).rotate(owner.centerX(), owner.centerY(), owner.getDirection());
		return new Bullet[] {
			applyCenter(new RedBullet(owner), p1.getX(), p1.getY()),
			applyCenter(new RedBullet(owner), p2.getX(), p2.getY()),
		};
	}

}
