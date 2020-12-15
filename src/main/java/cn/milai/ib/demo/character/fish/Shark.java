package cn.milai.ib.demo.character.fish;

import cn.milai.ib.character.BotCharacter;
import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.property.CanCrash;
import cn.milai.ib.container.ui.UIContainer;

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

	public Shark(UIContainer container) {
		super(0, 0, container);
		setX(getContainer().getWidth());
		setY(getContainer().getHeight());
		changeACCInterval = intProp(P_CHANGE_ACC_INTERVAL);
		lastSetACCFrame = -changeACCInterval;
		waitFrame = intProp(P_WAIT_FRAME);
		minWaitFrame = intProp(P_MIN_WAIT_FRAME);
		status = new Wait();
	}

	@Override
	public void setACCY(int accY) {
		long nowFrame = getContainer().getFrame();
		if (lastSetACCFrame + changeACCInterval > nowFrame) {
			return;
		}
		lastSetACCFrame = nowFrame;
		super.setACCY(accY);
	}

	// TODO 鲨鱼上面部分空白不进入判定，临时方案
	private int top() {
		return (int) (getY() + getHeight() * 0.4);
	}

	@Override
	public void onCrash(CanCrash crashed) {
		// TODO 鲨鱼上面部分空白不进入判定，临时方案
		if (crashed.getY() + crashed.getHeight() < top()) {
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
		if (character.getCenterY() < top()) {
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
			setY((int) (getAttackTarget().getCenterY() - getHeight() * 0.7));
			if (getDirection() > 0) {
				setACCX(getRatedACCX());
			} else {
				setACCX(-getRatedACCX());
			}
		}

		@Override
		public void afterMove() {
			int targetY = (int) getAttackTarget().getCenterY();
			if (targetY < top()) {
				setACCY(-getRatedACCY());
			} else if (targetY > getY() + getHeight()) {
				setACCY(getRatedACCY());
			} else {
				if (getSpeedY() != 0) {
					int accY = Integer.min(Math.abs(getSpeedY()), getRatedACCY());
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
			return (getDirection() < 0 && getX() + getWidth() < 0)
				|| (getDirection() > 0 && getX() >= getContainer().getWidth());
		}

		@Override
		public void loseLife(IBCharacter character, int life) {
			Shark.super.loseLife(character, life);
		}
	}

}
