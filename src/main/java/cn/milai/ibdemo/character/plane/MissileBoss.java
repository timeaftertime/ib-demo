package cn.milai.ibdemo.character.plane;

import cn.milai.common.util.Randoms;
import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.character.weapon.bullet.shooter.BulletShooter;
import cn.milai.ib.container.ui.Image;
import cn.milai.ib.container.ui.UIContainer;
import cn.milai.ib.loader.ImageLoader;
import cn.milai.ibdemo.character.bullet.shooter.DoubleRedShooter;
import cn.milai.ibdemo.character.bullet.shooter.MissileShooter;
import cn.milai.ibdemo.character.explosion.BaseExplosion;

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

	public MissileBoss(double x, double y, UIContainer container) {
		super(x, y, container);
		mainShooter = new DoubleRedShooter(this);
		sideShooter = new MissileShooter(this);
		status = new Comming();
	}

	@Override
	protected void beforeMove() {
		status.beforeMove();
		mainShooter.attack();
	}

	@Override
	protected void afterMove() { status.afterMove(); }

	@Override
	public synchronized void loseLife(IBCharacter character, int life) throws IllegalArgumentException {
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
		void beforeMove();

		void afterMove();
	}

	private class Comming implements Status {

		private final double COMMING_MAX_Y = doubleProp(P_COMMING_MAX_Y);

		public Comming() {
			setSpeedX(0);
			setSpeedY(getRatedSpeedY());
		}

		@Override
		public void beforeMove() {
			if (getY() + getH() >= COMMING_MAX_Y) {
				status = new Pareparing();
				return;
			}
		}

		@Override
		public void afterMove() {}

	}

	private class Pareparing implements Status {

		private final double PREPARE_MIN_Y = doubleProp("prepareMinY");
		private final double PREPARE_MAX_Y = doubleProp("prepareMaxY");

		private final double TURN_Y_CHANCE = doubleProp("turnYChance");

		/**
		 * 从 Prepareing 转换为 Pursuing 状态的间隔帧数
		 */
		private final long PREPARE_INTERVAL = longProp("prepareInterval");

		private final long CREATE_FRAME = getContainer().getFrame();

		public Pareparing() {
			setSpeedX((Randoms.nextLess(0.5) ? 1 : (-1)) * getRatedSpeedX());
			setSpeedY(-getRatedSpeedY());
		}

		@Override
		public void beforeMove() {
			if (getX() + getW() >= getContainer().getW()) {
				setSpeedX(-Math.abs(getSpeedX()));
			} else if (getIntX() <= 0) {
				setSpeedX(Math.abs(getSpeedX()));
			}
			if (Randoms.nextLess(TURN_Y_CHANCE)) {
				setSpeedY(getSpeedY() * -1);
			}
			if (getAttackTarget().centerY() < centerY() && getSpeedY() > 0 && Randoms.nextLess(TURN_Y_CHANCE)) {
				setSpeedY(getSpeedY() * -1);
			}
		}

		@Override
		public void afterMove() {
			ensureIn(0, getContainer().getW(), PREPARE_MIN_Y, PREPARE_MAX_Y);
			if (getContainer().getFrame() >= CREATE_FRAME + PREPARE_INTERVAL) {
				status = new Pursuing();
			}
		}

	}

	private class Pursuing implements Status {

		private final double PURSUING_SPEED_X = doubleProp(P_PURSUING_SPEED_X);

		public Pursuing() {
			setSpeedX(PURSUING_SPEED_X);
			setSpeedY(0);
		}

		@Override
		public void beforeMove() {
			if (getAttackTarget() == null) {
				return;
			}
			if (centerX() > getAttackTarget().centerX()) {
				setSpeedX(-Math.abs(getSpeedX()));
			}
			if (centerX() < getAttackTarget().centerX()) {
				setSpeedX(Math.abs(getSpeedX()));
			}
		}

		@Override
		public void afterMove() {
			PlayerCharacter target = getAttackTarget();
			if (target == null || (centerX() > target.getX() && centerX() < target.getX() + target.getW())) {
				sideShooter.attack();
				status = new Pareparing();
			}
		}

	}
}
