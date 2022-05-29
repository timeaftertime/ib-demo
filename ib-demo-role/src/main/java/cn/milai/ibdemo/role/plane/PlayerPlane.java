package cn.milai.ibdemo.role.plane;

import java.util.Stack;

import cn.milai.ib.role.BasePlayer;
import cn.milai.ib.role.Player;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.nature.Health;
import cn.milai.ib.role.nature.Movable;
import cn.milai.ib.role.nature.base.BaseHealth;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ib.stage.Stage;
import cn.milai.ibdemo.ActorUtil;
import cn.milai.ibdemo.role.DemoPlayerRole;
import cn.milai.ibdemo.role.bullet.shooter.BlueShooter;

/**
 * 玩家飞机
 * @author milai
 */
public class PlayerPlane extends AbstractPlane implements DemoPlayerRole {

	private Player player;
	private BulletShooter shooter;

	private Status initStatus;
	private Stack<Status> statusStack = new Stack<>();

	public PlayerPlane() {
		player = new BasePlayer();
		shooter = new BlueShooter(3, 3, this);
		onMakeUp(e -> initStatus = new Status());
	}

	@Override
	public Player player() {
		return player;
	}

	/**
	 * 设置使用的子弹发射器
	 * @param shooter
	 */
	public void setShooter(BulletShooter shooter) { this.shooter = shooter; }

	@Override
	public void beforeRefreshSpeeds(Movable m) {
		m.setSpeedX(0);
		m.setSpeedY(0);
		if (isUp()) {
			m.setSpeedY(-m.getRatedSpeedY());
		}
		if (isDown()) {
			m.setSpeedY(m.getRatedSpeedY());
		}
		if (isLeft()) {
			m.setSpeedX(-m.getRatedSpeedX());
		}
		if (isRight()) {
			m.setSpeedX(m.getRatedSpeedX());
		}
		if (isA()) {
			shooter.attack();
		}
	}

	@Override
	public void afterMove(Movable m) {
		Stage stage = stage();
		if (stage == null) {
			return;
		}
		ActorUtil.ensureIn(this, 0, stage.getW(), 0, stage.getH());
	}

	@Override
	protected Health createHealth() {
		return new BaseHealth(this) {
			@Override
			public synchronized void changeHP(Role character, int life) {
				super.changeHP(character, life);
				if (life < 0) {
					rollBackStatus();
					// 如果没有死亡，显示受伤效果
					if (isAlive()) {
						for (Explosion explosion : getExplosible().createExplosions()) {
							stage().addActor(explosion);
						}
					}
				}
			}
		};
	}

	/**
	 * 如果状态栈位空，保存当前状态到状态栈中，否则根据 mustCreateNew 决定是否复制当前状态并压入栈
	 * @param mustCreateNew
	 */
	@Override
	public synchronized void pushStatus(boolean mustCreateNew) {
		if (mustCreateNew || statusStack.isEmpty()) {
			try {
				statusStack.push((Status) currentStatus().clone());
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException("复制状态帧失败", e);
			}
		}
	}

	private Status currentStatus() {
		return statusStack.isEmpty() ? initStatus : statusStack.peek();
	}

	public synchronized void rollBackStatus() {
		if (statusStack.isEmpty()) {
			resetStatus(initStatus);
			return;
		}
		resetStatus(statusStack.pop());
	}

	private void resetStatus(Status status) {
		setW(status.width);
		setH(status.height);
		getMovable().setRatedSpeedX(status.ratedSpeedX);
		getMovable().setRatedSpeedY(status.ratedSpeedY);
		setShooter(status.shooter);
		setStatus(status.status);
	}

	/**
	 * 玩家状态，用于协助道具效果与复原
	 * @author milai
	 */
	private class Status implements Cloneable {
		private double width;
		private double height;
		private double ratedSpeedX;
		private double ratedSpeedY;
		private BulletShooter shooter;
		private String status;

		Status() {
			this.width = getIntW();
			this.height = getIntH();
			this.ratedSpeedX = getMovable().getRatedSpeedX();
			this.ratedSpeedY = getMovable().getRatedSpeedY();
			this.shooter = PlayerPlane.this.shooter;
			this.status = getStatus();
		}

		@Override
		public Object clone() throws CloneNotSupportedException {
			// 必须重载该方法使得访问权限变为 public
			return super.clone();
		}
	}

}
