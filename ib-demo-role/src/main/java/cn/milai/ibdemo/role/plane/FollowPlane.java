package cn.milai.ibdemo.role.plane;

import cn.milai.common.base.Randoms;
import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ibdemo.role.bullet.shooter.RedShooter;

/**
 * 随机跟随攻击目标的敌机
 * @author milai
 */
public class FollowPlane extends EnemyPlane {

	private static final String[] STATUS = { "red", "blue" };

	private static final String FOLLOW_CHANCE = "followChance";
	private static final String SHOOT_CHANCE = "shootChance";

	private double followChance;
	private double shootChance;

	private BulletShooter shooter = new RedShooter(this);

	public FollowPlane(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		Movable m = movable();
		m.setSpeedX(m.getRatedSpeedX());
		m.setSpeedY(m.getRatedSpeedY());
		followChance = doubleConf(FOLLOW_CHANCE);
		shootChance = doubleConf(SHOOT_CHANCE);
	}

	@Override
	protected void afterMove(Movable m) {
		redirectIfNeed(m);
		removeIfOutOfOwner();
		if (getAttackTarget() == null || !getAttackTarget().isAlive()) {
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
		} else if (getIntX() + getIntW() > getContainer().getW()) {
			m.setSpeedX(-Math.abs(m.getSpeedX()));
		}
	}

	private void removeIfOutOfOwner() {
		if (getIntY() > getContainer().getH()) {
			getContainer().removeObject(FollowPlane.this);
		}
	}

	@Override
	protected String getStatus() { return STATUS[Randoms.nextInt(STATUS.length)]; }

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

}
