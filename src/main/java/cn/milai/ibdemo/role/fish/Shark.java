package cn.milai.ibdemo.role.fish;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.BotRole;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Collider;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.Rigidbody;
import cn.milai.ib.role.property.base.BaseCollider;

/**
 * 鲨鱼
 * @author milai
 * @date 2020.04.03
 */
public class Shark extends EnemyFish implements BotRole {

	/**
	 * 属性 [Wait 状态持续帧数] 的 key
	 */
	public static final String P_WAIT_FRAME = "waitFrame";

	/**
	 * 属性 [Wait 状态试吃帧数最小值] 的 key
	 */
	public static final String P_MIN_WAIT_FRAME = "minWaitFrame";

	/**
	 * 属性 [修改 ACCY 的最小间隔帧数] 的 key
	 */
	public static final String P_CHANGE_ACC_INTERVAL = "changeACCInterval";

	private Status status;
	private int changeForceInterval;
	private int waitFrame;
	private int minWaitFrame;
	private long lastSetForceFrame;

	public Shark(LifecycleContainer container) {
		super(0, 0, container);
		setX(getContainer().getW());
		setY(getContainer().getH());
		changeForceInterval = intConf(P_CHANGE_ACC_INTERVAL);
		lastSetForceFrame = -changeForceInterval;
		waitFrame = intConf(P_WAIT_FRAME);
		minWaitFrame = intConf(P_MIN_WAIT_FRAME);
		setCollider(new BaseCollider(this) {
			@Override
			public void onCrash(Collider crashed) {
				// TODO 鲨鱼上面部分空白不进入判定，临时方案
				Role role = crashed.getRole();
				if (role.getY() + role.getH() < top()) {
					return;
				}
				role.loseLife(Shark.this, 1);
			}
		});
		status = new Wait(movable());
	}

	@Override
	public synchronized void loseLife(Role attacker, int life) throws IllegalArgumentException {
		// TODO 鲨鱼上面部分空白不进入判定，临时方案
		if (attacker.centerY() < top()) {
			return;
		}
		waitFrame = Integer.max(minWaitFrame, (int) (1.0 * getLife() / getInitLife() * intConf(P_WAIT_FRAME)));
		status.loseLife(attacker, life);
	}

	// TODO 鲨鱼上面部分空白不进入判定，临时方案
	private double top() {
		return getY() + getH() * 0.4;
	}

	@Override
	protected void beforeRefreshSpeeds(Movable m) {
		status.beforeRefreshSpeeds(rigidbody());
	}

	@Override
	protected void afterRefreshSpeeds(Movable m) {
		long nowFrame = getContainer().getFrame();
		if (lastSetForceFrame + changeForceInterval > nowFrame) {
			return;
		}
		lastSetForceFrame = nowFrame;
	}

	@Override
	protected void afterMove(Movable m) {
		status.afterMove(m);
	}

	private interface Status {
		default void beforeRefreshSpeeds(Rigidbody r) {};

		default void afterMove(Movable m) {}

		default void loseLife(Role attacker, int life) {}
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
		public void loseLife(Role character, int life) {
			// Wait 状态不受伤害
		}

	}

	private class Attack implements Status {

		private Attack(Movable m) {
			m.setSpeedX(0);
			m.setSpeedY(0);
			lastSetForceFrame = getContainer().getFrame() - changeForceInterval;
			setY(getAttackTarget().centerY() - getH() * 0.7);

		}

		@Override
		public void beforeRefreshSpeeds(Rigidbody r) {
			Movable m = movable();
			r.addForceX((getDirection() > 0 ? 1 : -1) * r.confForceX());
			double targetY = getAttackTarget().centerY();
			if (targetY < top()) {
				r.addForceY(-r.confForceY());
			} else if (targetY > getY() + getH()) {
				r.addForceY(r.confForceY());
			} else {
				r.addForceY((m.getSpeedY() > 0 ? -1 : 1) * Math.min(r.confForceY(), m.getSpeedY() * r.mass()));
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
			return getX() >= getContainer().getW();
		}

		@Override
		public void loseLife(Role character, int life) {
			Shark.super.loseLife(character, life);
		}
	}

}
