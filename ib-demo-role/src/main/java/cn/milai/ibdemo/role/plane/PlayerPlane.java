package cn.milai.ibdemo.role.plane;

import java.util.Stack;

import cn.milai.ib.role.BasePlayer;
import cn.milai.ib.role.Player;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.property.Health;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.base.BaseHealth;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
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
		setMovable(new PlayerPlaneMovable());
	}

	@Override
	protected void initItem() {
		initStatus = new Status();
	}

	/**
	 * 设置使用的子弹发射器
	 * @param shooter
	 */
	public void setShooter(BulletShooter shooter) { this.shooter = shooter; }

	protected void beforeRefreshSpeeds(Movable m) {
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

	protected void afterMove(Movable m) {
		ensureIn(0, container().getW(), 0, container().getH());
	}

	@Override
	protected Health createHealth() {
		return new BaseHealth() {
			@Override
			public synchronized void changeHP(Role character, int life) {
				super.changeHP(character, life);
				rollBackStatus();
				// 如果没有死亡，显示受伤效果
				if (life < 0 && isAlive()) {
					for (Explosion explosion : getExplosible().createExplosions()) {
						container().addObject(explosion);
					}
				}
			}
		};
	}

	/**
	 * 如果状态栈位空，保存当前状态到状态栈中，否则根据 mustCreateNew 决定是否复制当前状态并压入栈
	 * @param mustCreateNew
	 */
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

	@Override
	public void setUp() {
		player.setUp();
	}

	@Override
	public void clearUp() {
		player.clearUp();
	}

	@Override
	public boolean isUp() { return player.isUp(); }

	@Override
	public void setDown() {
		player.setDown();
	}

	@Override
	public void clearDown() {
		player.clearDown();
	}

	@Override
	public boolean isDown() { return player.isDown(); }

	@Override
	public void setLeft() {
		player.setLeft();
	}

	@Override
	public void clearLeft() {
		player.clearLeft();
	}

	@Override
	public boolean isLeft() { return player.isLeft(); }

	@Override
	public void setRight() {
		player.setRight();
	}

	@Override
	public void clearRight() {
		player.clearRight();
	}

	@Override
	public boolean isRight() { return player.isRight(); }

	@Override
	public void setA() {
		player.setA();
	}

	@Override
	public void clearA() {
		player.clearA();
	}

	@Override
	public boolean isA() { return player.isA(); }

}
