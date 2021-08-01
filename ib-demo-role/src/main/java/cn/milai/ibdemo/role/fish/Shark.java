package cn.milai.ibdemo.role.fish;

import cn.milai.ib.config.Configurable;
import cn.milai.ib.role.BotRole;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Collider;
import cn.milai.ib.role.property.Health;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.Rigidbody;
import cn.milai.ib.role.property.base.BaseCollider;
import cn.milai.ib.role.property.base.BaseHealth;

/**
 * 鲨鱼
 * @author milai
 * @date 2020.04.03
 */
public class Shark extends EnemyFish implements BotRole {

	private Status status;
	private int changeForceInterval;
	private int waitFrame;
	private int minWaitFrame;
	private long lastSetForceFrame;
	private long attackInterval;
	private long lastAttackFrame;
	private int initWaitFrame;

	public Shark() {
		setCollider(new BaseCollider() {
			@Override
			public void onCollided(Collider crashed) {
				Role r = crashed.owner();
				if (notCollied(r)) {
					return;
				}
				r.getHealth().changeHP(Shark.this, -1);
				lastAttackFrame = container().getFrame();
				Rigidbody b2 = r.getProperty(Rigidbody.class);
				if (b2 != null) {
					b2.addExtraForceX(getMovable().getSpeedX() / b2.getMass());
				}
			}

			@Override
			public void onTouching(Collider c) {
				if (notCollied(c.owner()) || lastAttackFrame + attackInterval > container().getFrame()) {
					return;
				}
				c.owner().getHealth().changeHP(Shark.this, -1);
				lastAttackFrame = container().getFrame();
			}
		});
	}

	@Override
	protected void initEnemyFish() {
		setX(container().getW());
		setY(container().getH());
		initWaitFrame = waitFrame;
		lastSetForceFrame = -changeForceInterval;
		lastAttackFrame = -attackInterval;
		status = new Wait(getMovable());
	}

	@Override
	protected Health createHealth() {
		return new BaseHealth() {
			@Override
			public synchronized void changeHP(Role attacker, int life) throws IllegalArgumentException {
				if (notCollied(attacker)) {
					return;
				}
				waitFrame = Integer.max(minWaitFrame, (int) (1.0 * getHP() / initHP() * initWaitFrame));
				if (status.loseLife(attacker, life)) {
					super.changeHP(attacker, life);
				}
			}
		};
	}

	// TODO 鲨鱼上面部分空白不进入判定，临时方案
	private double top() { return getY() + getH() * 0.4; }

	private boolean notCollied(Role r) { return r.getY() + r.getH() < top(); }

	@Override
	public void beforeRefreshSpeeds(Movable m) { status.beforeRefreshSpeeds(getRigidbody()); }

	@Override
	public void afterRefreshSpeeds(Movable m) {
		long nowFrame = container().getFrame();
		if (lastSetForceFrame + changeForceInterval > nowFrame) {
			return;
		}
		lastSetForceFrame = nowFrame;
	}

	@Override
	public void afterMove(Movable m) { status.afterMove(m); }

	private interface Status {
		default void beforeRefreshSpeeds(Rigidbody r) {};

		default void afterMove(Movable m) {}

		default boolean loseLife(Role attacker, int life) { return true; }
	}

	private class Wait implements Status {

		private int waitCnt = 0;

		private Wait(Movable m) {
			m.setSpeedX(0);
			m.setSpeedY(0);
		}

		@Override
		public void afterMove(Movable m) {
			waitCnt++;
			if (waitCnt >= waitFrame) {
				status = new Attack(m);
			}
		}

		@Override
		public boolean loseLife(Role character, int life) { return false; }

	}

	private class Attack implements Status {

		private Attack(Movable m) {
			m.setSpeedX(0);
			m.setSpeedY(0);
			lastSetForceFrame = container().getFrame() - changeForceInterval;
			setY(getAttackTarget().centerY() - getH() * 0.7);

		}

		@Override
		public void beforeRefreshSpeeds(Rigidbody r) {
			Movable m = getMovable();
			r.addForceX((getDirection() > 0 ? 1 : -1) * getForceX());
			double targetY = getAttackTarget().centerY();
			if (targetY < top()) {
				r.addForceY(-getForceY());
			} else if (targetY > getY() + getH()) {
				r.addForceY(getForceY());
			} else {
				r.addForceY((m.getSpeedY() > 0 ? -1 : 1) * Math.min(getForceY(), m.getSpeedY() * r.getMass()));
			}
		}

		@Override
		public void afterMove(Movable m) {
			if (outOfContainer()) {
				setDirection(-getDirection());
				status = new Wait(m);
			}
		}

		private boolean outOfContainer() {
			if (getDirection() < 0) {
				return getX() + getW() < 0;
			}
			return getX() >= container().getW();
		}

	}

	public int getChangeForceInterval() { return changeForceInterval; }

	@Configurable
	public void setChangeForceInterval(int changeForceInterval) { this.changeForceInterval = changeForceInterval; }

	public int getWaitFrame() { return waitFrame; }

	@Configurable
	public void setWaitFrame(int waitFrame) { this.waitFrame = waitFrame; }

	public int getMinWaitFrame() { return minWaitFrame; }

	@Configurable
	public void setMinWaitFrame(int minWaitFrame) { this.minWaitFrame = minWaitFrame; }

	public long getAttackInterval() { return attackInterval; }

	@Configurable
	public void setAttackInterval(long attackInterval) { this.attackInterval = attackInterval; }

}
