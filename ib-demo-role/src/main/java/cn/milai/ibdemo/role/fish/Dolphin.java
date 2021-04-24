package cn.milai.ibdemo.role.fish;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.container.plugin.control.cmd.Cmd;
import cn.milai.ib.container.plugin.control.cmd.CmdType;
import cn.milai.ib.role.BasePlayer;
import cn.milai.ib.role.Player;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.Rigidbody;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ibdemo.role.bullet.shooter.BlueShooter;

/**
 * 海豚
 * @author milai
 * @date 2020.03.28
 */
public class Dolphin extends AbstractFish implements PlayerRole {

	private static final String STATUS_MOVE = "move";
	private static final String STATUS_DAMAGED = "damaged";
	public static final String P_SHOOT_INTERVAL = "shootInterval";
	public static final String P_MAX_BULLET_NUM = "maxBulletNum";

	private Player player;
	private BulletShooter shooter;
	private int damagedCnt;

	public Dolphin(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		setDirection(Math.PI / 2);
		player = new BasePlayer();
		shooter = new BlueShooter(intConf(P_SHOOT_INTERVAL), intConf(P_MAX_BULLET_NUM), this);
		damagedCnt = 0;
	}

	@Override
	protected void beforeRefreshSpeeds(Movable m) {
		Rigidbody r = rigidbody();
		if (r == null) {
			return;
		}
		if (damagedCnt > 0) {
			damagedCnt--;
			if (damagedCnt <= 0) {
				setStatus(null);
			}
		}
		if (isUp()) {
			setStatus(STATUS_MOVE);
			r.addForceY(-r.confForceY());
		}
		if (isDown()) {
			setStatus(STATUS_MOVE);
			r.addForceY(r.confForceY());
		}
		if (isLeft()) {
			setStatus(STATUS_MOVE);
			r.addForceX(-r.confForceX());
		}
		if (isRight()) {
			setStatus(STATUS_MOVE);
			r.addForceX(r.confForceX());
		}
		if (damagedCnt <= 0 && r.getForceX() == 0 && r.getForceY() == 0) {
			setStatus(null);
		}
		if (player.isA()) {
			shooter.attack();
		}
	}

	@Override
	protected void setStatus(String status) {
		if (STATUS_MOVE.equals(status) && STATUS_DAMAGED.equals(getStatus())) {
			return;
		}
		super.setStatus(status);
	}

	@Override
	protected void afterMove(Movable m) {
		ensureInContainer();
	}

	@Override
	public synchronized void loseLife(Role character, int life) throws IllegalArgumentException {
		damagedCnt = 8;
		setStatus(STATUS_DAMAGED);
		super.loseLife(character, life);
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
	public void setDown() {
		player.setDown();
	}

	@Override
	public void clearDown() {
		player.clearDown();
	}

	@Override
	public void setLeft() {
		setDirection(-Math.PI / 2);
		player.setLeft();
	}

	@Override
	public void clearLeft() {
		player.clearLeft();
	}

	@Override
	public void setRight() {
		setDirection(Math.PI / 2);
		player.setRight();
	}

	@Override
	public void clearRight() {
		player.clearRight();
	}

	@Override
	public void setA() {
		player.setA();
	}

	@Override
	public void clearA() {
		player.clearA();
	}

	@Override
	public boolean isUp() { return player.isUp(); }

	@Override
	public boolean isDown() { return player.isDown(); }

	@Override
	public boolean isLeft() { return player.isLeft(); }

	@Override
	public boolean isRight() { return player.isRight(); }

	@Override
	public boolean isA() { return player.isA(); }

	@Override
	public void pushStatus(boolean createNew) {
		throw new UnsupportedOperationException("暂不支持保存状态");
	}

	@Override
	public boolean accept(Cmd c) {
		return c.getType() != CmdType.CLICKED;
	}

}
