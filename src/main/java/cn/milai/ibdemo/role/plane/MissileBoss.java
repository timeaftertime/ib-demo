package cn.milai.ibdemo.role.plane;

import cn.milai.common.base.Randoms;
import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.container.plugin.ui.Image;
import cn.milai.ib.loader.ImageLoader;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ibdemo.role.bullet.shooter.DoubleRedShooter;
import cn.milai.ibdemo.role.bullet.shooter.MissileShooter;
import cn.milai.ibdemo.role.explosion.BaseExplosion;

/**
 * 导弹 BOSS
 * @author milai
 */
public class MissileBoss extends EnemyPlane {

	private static final String P_COMMING_MAX_Y = "commingMaxY";
	private static final String P_PURSUING_SPEED_X = "pursuingSpeedX";

	private BulletShooter mainShooter;
	private BulletShooter sideShooter;

	/**
	 * 进入“危险”状态的最大生命值
	 */
	private final int DANGER_LIFE = getLife() / 4;

	private Status status;

	private final Image DANGER_IMG = ImageLoader.load(MissileBoss.class, "danger");

	public MissileBoss(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		mainShooter = new DoubleRedShooter(this);
		sideShooter = new MissileShooter(this);
		status = new Comming();
	}

	@Override
	protected void beforeRefreshSpeeds(Movable m) {
		status.beforeRefreshSpeeds(m);
		mainShooter.attack();
	}

	@Override
	protected void afterMove(Movable m) {
		status.afterMove(m);
	}

	@Override
	public synchronized void loseLife(Role character, int life) throws IllegalArgumentException {
		super.loseLife(character, life);
		if (isAlive()) {
			getContainer().addObject(
				new BaseExplosion(character.centerX(), character.centerY(), getContainer())
			);
		}
		if (getImage() != DANGER_IMG && getLife() <= DANGER_LIFE) {
			setImage(DANGER_IMG);
		}
	}

	private interface Status {
		default void beforeRefreshSpeeds(Movable m) {}

		default void afterMove(Movable m) {}
	}

	private class Comming implements Status {

		private final double COMMING_MAX_Y = doubleConf(P_COMMING_MAX_Y);

		public Comming() {
			movable().setSpeedX(0);
			movable().setSpeedY(movable().getRatedSpeedY());
		}

		@Override
		public void beforeRefreshSpeeds(Movable m) {
			if (getY() + getH() >= COMMING_MAX_Y) {
				status = new Pareparing();
				return;
			}
		}

	}

	private class Pareparing implements Status {

		private final double PREPARE_MIN_Y = doubleConf("prepareMinY");
		private final double PREPARE_MAX_Y = doubleConf("prepareMaxY");

		private final double TURN_Y_CHANCE = doubleConf("turnYChance");

		/**
		 * 从 Prepareing 转换为 Pursuing 状态的间隔帧数
		 */
		private final long PREPARE_INTERVAL = longConf("prepareInterval");

		private final long CREATE_FRAME = getContainer().getFrame();

		public Pareparing() {
			Movable m = movable();
			m.setSpeedX((Randoms.nextLess(0.5) ? 1 : (-1)) * m.getRatedSpeedX());
			m.setSpeedY(-m.getRatedSpeedY());
		}

		@Override
		public void beforeRefreshSpeeds(Movable m) {
			if (getX() + getW() >= getContainer().getW()) {
				m.setSpeedX(-Math.abs(m.getSpeedX()));
			} else if (getIntX() <= 0) {
				m.setSpeedX(Math.abs(m.getSpeedX()));
			}
			if (Randoms.nextLess(TURN_Y_CHANCE)) {
				m.setSpeedY(m.getSpeedY() * -1);
			}
			if (getAttackTarget().centerY() < centerY() && m.getSpeedY() > 0 && Randoms.nextLess(TURN_Y_CHANCE)) {
				m.setSpeedY(m.getSpeedY() * -1);
			}
		}

		@Override
		public void afterMove(Movable m) {
			ensureIn(0, getContainer().getW(), PREPARE_MIN_Y, PREPARE_MAX_Y);
			if (getContainer().getFrame() >= CREATE_FRAME + PREPARE_INTERVAL) {
				status = new Pursuing();
			}
		}

	}

	private class Pursuing implements Status {

		private final double PURSUING_SPEED_X = doubleConf(P_PURSUING_SPEED_X);

		public Pursuing() {
			movable().setSpeedX(PURSUING_SPEED_X);
			movable().setSpeedY(0);
		}

		@Override
		public void beforeRefreshSpeeds(Movable m) {
			if (getAttackTarget() == null) {
				return;
			}
			if (centerX() > getAttackTarget().centerX()) {
				m.setSpeedX(-Math.abs(m.getSpeedX()));
			}
			if (centerX() < getAttackTarget().centerX()) {
				m.setSpeedX(Math.abs(m.getSpeedX()));
			}
		}

		@Override
		public void afterMove(Movable m) {
			PlayerRole target = getAttackTarget();
			if (target == null || (centerX() > target.getX() && centerX() < target.getX() + target.getW())) {
				sideShooter.attack();
				status = new Pareparing();
			}
		}

	}
}
