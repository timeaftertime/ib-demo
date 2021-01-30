package cn.milai.ibdemo.character.plane;

import cn.milai.common.util.Randoms;
import cn.milai.ib.character.weapon.bullet.shooter.BulletShooter;
import cn.milai.ib.container.ui.UIContainer;
import cn.milai.ibdemo.character.bullet.shooter.RedShooter;

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

	public FollowPlane(double x, double y, UIContainer container) {
		super(x, y, container);
		setSpeedX(getRatedSpeedX());
		setSpeedY(getRatedSpeedY());
		followChance = doubleProp(FOLLOW_CHANCE);
		shootChance = doubleProp(SHOOT_CHANCE);
	}

	@Override
	protected String getStatus() { return STATUS[Randoms.nextInt(STATUS.length)]; }

	@Override
	protected void beforeMove() {}

	@Override
	protected void afterMove() {
		redirectIfNeed();
		removeIfOutOfOwner();
		if (getAttackTarget() == null || !getAttackTarget().isAlive()) {
			return;
		}
		randomRedirect();
		if (nearTarget()) {
			randomShoot();
		}
	}

	private void randomRedirect() {
		if (getX() < getAttackTarget().getX() && getSpeedX() < 0) {
			if (Randoms.nextLess(followChance)) {
				setSpeedX(-getSpeedX());
			}
		} else if (getX() > getAttackTarget().getX() && getSpeedX() > 0) {
			if (Randoms.nextLess(followChance)) {
				setSpeedX(-getSpeedX());
			}
		}
	}

	private void redirectIfNeed() {
		if (getIntX() <= 0) {
			setSpeedX(Math.abs(getSpeedX()));
		} else if (getIntX() + getIntW() > getContainer().getWidth()) {
			setSpeedX(-Math.abs(getSpeedX()));
		}
	}

	private void removeIfOutOfOwner() {
		if (getIntY() > getContainer().getHeight()) {
			getContainer().removeObject(this);
		}
	}

	private boolean nearTarget() {
		double targetX = getAttackTarget().getCenterX();
		double targetWidth = getAttackTarget().getW();
		return getCenterX() > targetX - targetWidth && getCenterX() < targetX + targetWidth;
	}

	private void randomShoot() {
		if (Randoms.nextLess(shootChance)) {
			shooter.attack();
		}
	}

}
