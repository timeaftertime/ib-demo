package cn.milai.ibdemo.character.fish;

import cn.milai.ib.character.BotCharacter;
import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.property.CanCrash;
import cn.milai.ib.container.lifecycle.LifecycleContainer;

/**
 * 鲨鱼
 * @author milai
 * @date 2020.04.03
 */
public class Shark extends EnemyFish implements BotCharacter {

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
	private int changeACCInterval;
	private int waitFrame;
	private int minWaitFrame;
	private long lastSetACCFrame;

	public Shark(LifecycleContainer container) {
		super(0, 0, container);
		setX(getContainer().getW());
		setY(getContainer().getH());
		changeACCInterval = intProp(P_CHANGE_ACC_INTERVAL);
		lastSetACCFrame = -changeACCInterval;
		waitFrame = intProp(P_WAIT_FRAME);
		minWaitFrame = intProp(P_MIN_WAIT_FRAME);
		status = new Wait();
	}

	@Override
	public void setACCY(double accY) {
		long nowFrame = getContainer().getFrame();
		if (lastSetACCFrame + changeACCInterval > nowFrame) {
			return;
		}
		lastSetACCFrame = nowFrame;
		super.setACCY(accY);
	}

	// TODO 鲨鱼上面部分空白不进入判定，临时方案
	private double top() {
		return getY() + getH() * 0.4;
	}

	@Override
	public void onCrash(CanCrash crashed) {
		// TODO 鲨鱼上面部分空白不进入判定，临时方案
		if (crashed.getY() + crashed.getH() < top()) {
			return;
		}
		crashed.loseLife(this, 1);
	}

	@Override
	protected void afterMove() {
		status.afterMove();
	}

	@Override
	public synchronized void loseLife(IBCharacter character, int life) throws IllegalArgumentException {
		// TODO 鲨鱼上面部分空白不进入判定，临时方案
		if (character.centerY() < top()) {
			return;
		}
		waitFrame = Integer.max(minWaitFrame, (int) (1.0 * getLife() / getInitLife() * intProp(P_WAIT_FRAME)));
		status.loseLife(character, life);
	}

	private interface Status {
		void afterMove();

		void loseLife(IBCharacter character, int life);
	}

	private class Wait implements Status {

		private int waitCnt = 0;

		Wait() {
			setSpeedX(0);
			setSpeedY(0);
			setACCX(0);
			setACCY(0);
		}

		@Override
		public void afterMove() {
			waitCnt++;
			if (waitCnt >= waitFrame) {
				status = new Attack();
			}
		}

		@Override
		public void loseLife(IBCharacter character, int life) {
			// Wait 状态不受伤害
		}

	}

	private class Attack implements Status {

		Attack() {
			setSpeedX(0);
			setSpeedY(0);
			setACCX(0);
			setACCY(0);
			lastSetACCFrame = getContainer().getFrame() - changeACCInterval;
			setY(getAttackTarget().centerY() - getH() * 0.7);
			if (getDirection() > 0) {
				setACCX(getRatedAccX());
			} else {
				setACCX(-getRatedAccX());
			}
		}

		@Override
		public void afterMove() {
			double targetY = getAttackTarget().centerY();
			if (targetY < top()) {
				setACCY(-getRatedAccY());
			} else if (targetY > getY() + getH()) {
				setACCY(getRatedAccY());
			} else {
				if (getSpeedY() != 0) {
					double accY = Math.min(Math.abs(getSpeedY()), getRatedAccY());
					accY *= getSpeedY() > 0 ? -1 : 1;
					setACCY(accY);
				}
			}
			if (outOfContainer()) {
				setDirection(-getDirection());
				status = new Wait();
			}
		}

		private boolean outOfContainer() {
			if (getDirection() < 0) {
				return getX() + getW() < 0;
			}
			return getX() >= getContainer().getW();
		}

		@Override
		public void loseLife(IBCharacter character, int life) {
			Shark.super.loseLife(character, life);
		}
	}

}
