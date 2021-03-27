package cn.milai.ibdemo.role.plane;

import java.util.Stack;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.container.plugin.control.cmd.Cmd;
import cn.milai.ib.container.plugin.control.cmd.CmdType;
import cn.milai.ib.container.plugin.ui.Image;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ibdemo.role.bullet.shooter.BlueShooter;
import cn.milai.ib.role.BasePlayer;
import cn.milai.ib.role.Player;
import cn.milai.ib.role.PlayerRole;

/**
 * 玩家飞机
 * @author milai
 */
public class PlayerPlane extends AbstractPlane implements PlayerRole {

	public static final String P_SHOOT_INTERVAL = "shootInterval";
	public static final String P_MAX_BULLET_NUM = "maxBulletNum";

	private Player player;
	private BulletShooter shooter;

	private final Status INIT_STATUS;
	private Stack<Status> statusStack = new Stack<>();

	public PlayerPlane(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		player = new BasePlayer();
		shooter = new BlueShooter(intProp(P_SHOOT_INTERVAL), intProp(P_MAX_BULLET_NUM), this);
		// 在构造方法最后以确保所有变量已经初始化
		INIT_STATUS = new Status();
	}

	/**
	 * 设置使用的子弹发射器
	 * @param shooter
	 */
	public void setShooter(BulletShooter shooter) { this.shooter = shooter; }

	@Override
	public void beforeMove() {
		setSpeedX(0);
		setSpeedY(0);
		if (isUp()) {
			setSpeedY(-getRatedSpeedY());
		}
		if (isDown()) {
			setSpeedY(getRatedSpeedY());
		}
		if (isLeft()) {
			setSpeedX(-getRatedSpeedX());
		}
		if (isRight()) {
			setSpeedX(getRatedSpeedX());
		}
		if (isA()) {
			shooter.attack();
		}
	}

	@Override
	protected void afterMove() {
		ensureInContainer();
	}

	@Override
	public int getDamage() { return 1; }

	@Override
	public synchronized void loseLife(Role character, int life) throws IllegalArgumentException {
		super.loseLife(character, life);
		rollBackStatus();
		// 如果没有死亡，显示受伤效果
		if (isAlive()) {
			for (Explosion explosion : getExplosionCreator().createExplosions(this)) {
				getContainer().addObject(explosion);
			}
		}
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
		return statusStack.isEmpty() ? INIT_STATUS : statusStack.peek();
	}

	public synchronized void rollBackStatus() {
		if (statusStack.isEmpty()) {
			resetStatus(INIT_STATUS);
			return;
		}
		resetStatus(statusStack.pop());
	}

	private void resetStatus(Status status) {
		setW(status.width);
		setH(status.height);
		setRatedSpeedX(status.ratedSpeedX);
		setRatedSpeedY(status.ratedSpeedY);
		setShooter(status.shooter);
		setImage(status.img);
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
		private Image img;

		Status() {
			PlayerPlane plane = PlayerPlane.this;
			this.width = plane.getIntW();
			this.height = plane.getIntH();
			this.ratedSpeedX = plane.getRatedSpeedX();
			this.ratedSpeedY = plane.getRatedSpeedY();
			this.shooter = plane.shooter;
			this.img = plane.getImage();
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

	@Override
	public boolean accept(Cmd c) {
		return c.getType() != CmdType.CLICKED;
	}

}
