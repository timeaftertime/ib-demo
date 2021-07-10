package cn.milai.ibdemo.role.plane;

import cn.milai.common.base.Randoms;
import cn.milai.ib.config.Configurable;
import cn.milai.ib.config.ItemConfigApplier;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Health;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.base.BaseHealth;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ibdemo.role.bullet.shooter.DoubleRedShooter;
import cn.milai.ibdemo.role.bullet.shooter.MissileShooter;
import cn.milai.ibdemo.role.explosion.BaseExplosion;

/**
 * 导弹 BOSS
 * @author milai
 */
public class MissileBoss extends EnemyPlane implements ItemConfigApplier {

	private double commingMaxY;
	private double prepareMinY;
	private double prepareMaxY;
	private double turnYChance;
	private long prepareInterval;
	private double pursuingSpeedX;

	private BulletShooter mainShooter;
	private BulletShooter sideShooter;

	private Status status;

	String STATUS_DANGER = "danger";

	public MissileBoss() {
		setMovable(new MissileBossMovable());
		mainShooter = new DoubleRedShooter(this);
		sideShooter = new MissileShooter(this);
	}

	protected void initEnemyPlane() {
		status = new Comming();
	};

	protected Health createHealth() {
		return new BaseHealth() {
			@Override
			public synchronized void changeHP(Role character, int life) throws IllegalArgumentException {
				super.changeHP(character, life);
				if (isAlive()) {
					container().addObject(applyCenter(new BaseExplosion(), character.centerX(), character.centerY()));
				}
				if (!getStatus().equals(STATUS_DANGER) && getHP() <= initHP() / 4) {
					setStatus(STATUS_DANGER);
				}
			}
		};
	};

	protected void beforeRefreshSpeeds(Movable m) {
		status.beforeRefreshSpeeds(m);
		mainShooter.attack();
	}

	protected void afterMove(Movable m) {
		status.afterMove(m);
	}

	private interface Status {
		default void beforeRefreshSpeeds(Movable m) {}

		default void afterMove(Movable m) {}
	}

	private class Comming implements Status {

		public Comming() {
			getMovable().setSpeedX(0);
			getMovable().setSpeedY(getMovable().getRatedSpeedY());
		}

		@Override
		public void beforeRefreshSpeeds(Movable m) {
			if (getY() + getH() >= commingMaxY) {
				status = new Pareparing();
				return;
			}
		}

	}

	private class Pareparing implements Status {

		private final long CREATE_FRAME = container().getFrame();

		public Pareparing() {
			Movable m = getMovable();
			m.setSpeedX((Randoms.nextLess(0.5) ? 1 : (-1)) * m.getRatedSpeedX());
			m.setSpeedY(-m.getRatedSpeedY());
		}

		@Override
		public void beforeRefreshSpeeds(Movable m) {
			if (getX() + getW() >= container().getW()) {
				m.setSpeedX(-Math.abs(m.getSpeedX()));
			} else if (getIntX() <= 0) {
				m.setSpeedX(Math.abs(m.getSpeedX()));
			}
			if (Randoms.nextLess(turnYChance)) {
				m.setSpeedY(m.getSpeedY() * -1);
			}
			if (getAttackTarget().centerY() < centerY() && m.getSpeedY() > 0 && Randoms.nextLess(turnYChance)) {
				m.setSpeedY(m.getSpeedY() * -1);
			}
		}

		@Override
		public void afterMove(Movable m) {
			ensureIn(0, container().getW(), prepareMinY, prepareMaxY);
			if (container().getFrame() >= CREATE_FRAME + prepareInterval) {
				status = new Pursuing();
			}
		}

	}

	private class Pursuing implements Status {

		public Pursuing() {
			getMovable().setSpeedX(pursuingSpeedX);
			getMovable().setSpeedY(0);
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

	public double getCommingMaxY() { return commingMaxY; }

	@Configurable
	public void setCommingMaxY(double commingMaxY) { this.commingMaxY = commingMaxY; }

	public double getPrepareMinY() { return prepareMinY; }

	@Configurable
	public void setPrepareMinY(double prepareMinY) { this.prepareMinY = prepareMinY; }

	public double getPrepareMaxY() { return prepareMaxY; }

	@Configurable
	public void setPrepareMaxY(double prepareMaxY) { this.prepareMaxY = prepareMaxY; }

	public double getTurnYChance() { return turnYChance; }

	@Configurable
	public void setTurnYChance(double turnYChance) { this.turnYChance = turnYChance; }

	public long getPrepareInterval() { return prepareInterval; }

	@Configurable
	public void setPrepareInterval(long prepareInterval) { this.prepareInterval = prepareInterval; }

	public double getPursuingSpeedX() { return pursuingSpeedX; }

	@Configurable
	public void setPursuingSpeedX(double pursuingSpeedX) { this.pursuingSpeedX = pursuingSpeedX; }

}
