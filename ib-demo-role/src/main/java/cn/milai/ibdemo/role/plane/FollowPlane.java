package cn.milai.ibdemo.role.plane;

import cn.milai.common.base.Randoms;
import cn.milai.ib.config.Configurable;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ibdemo.role.bullet.shooter.RedShooter;

/**
 * 随机跟随攻击目标的敌机
 * @author milai
 */
public class FollowPlane extends EnemyPlane {

	private static final String[] STATUS = { "red", "blue" };

	private double followChance;
	private double shootChance;

	private BulletShooter shooter = new RedShooter(this);

	public FollowPlane() {
		setStatus(STATUS[Randoms.nextInt(STATUS.length)]);
		setMovable(new FollowPlaneMovable());
	}

	@Override
	protected void initEnemyPlane() {
		Movable m = getMovable();
		m.setSpeedX(m.getRatedSpeedX());
		m.setSpeedY(m.getRatedSpeedY());
	}

	protected void afterMove(Movable m) {
		redirectIfNeed(m);
		removeIfOutOfOwner();
		if (getAttackTarget() == null || !getAttackTarget().getHealth().isAlive()) {
			return;
		}
		randomRedirect(m);
		if (nearTarget()) {
			randomShoot();
		}
	}

	private void randomRedirect(Movable m) {
		if (getX() < getAttackTarget().getX() && m.getSpeedX() < 0) {
			if (Randoms.nextLess(followChance)) {
				m.setSpeedX(-m.getSpeedX());
			}
		} else if (getX() > getAttackTarget().getX() && m.getSpeedX() > 0) {
			if (Randoms.nextLess(followChance)) {
				m.setSpeedX(-m.getSpeedX());
			}
		}
	}

	private void redirectIfNeed(Movable m) {
		if (getIntX() <= 0) {
			m.setSpeedX(Math.abs(m.getSpeedX()));
		} else if (getIntX() + getIntW() > container().getW()) {
			m.setSpeedX(-Math.abs(m.getSpeedX()));
		}
	}

	private void removeIfOutOfOwner() {
		if (getIntY() > container().getH()) {
			container().removeObject(FollowPlane.this);
		}
	}

	private boolean nearTarget() {
		double targetX = getAttackTarget().centerX();
		double targetWidth = getAttackTarget().getW();
		return centerX() > targetX - targetWidth && centerX() < targetX + targetWidth;
	}

	private void randomShoot() {
		if (Randoms.nextLess(shootChance)) {
			shooter.attack();
		}
	}

	@Configurable
	public void setFollowChance(double followChance) { this.followChance = followChance; }

	@Configurable
	public void setShootChance(double shootChance) { this.shootChance = shootChance; }

}
