package cn.milai.ibdemo.character.fish;

import cn.milai.ib.character.BasePlayer;
import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.Player;
import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.character.property.CanCrash;
import cn.milai.ib.character.weapon.bullet.shooter.BulletShooter;
import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.container.plugin.control.cmd.Cmd;
import cn.milai.ib.container.plugin.control.cmd.CmdType;
import cn.milai.ibdemo.character.bullet.shooter.BlueShooter;

/**
 * 海豚
 * @author milai
 * @date 2020.03.28
 */
public class Dolphin extends AbstractFish implements PlayerCharacter {

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
		shooter = new BlueShooter(intProp(P_SHOOT_INTERVAL), intProp(P_MAX_BULLET_NUM), this);
		damagedCnt = 0;
	}

	@Override
	protected void beforeMove() {
		setACCX(0);
		setACCY(0);
		if (isUp()) {
			setStatus(STATUS_MOVE);
			setACCY(-getRatedAccY());
		}
		if (isDown()) {
			setStatus(STATUS_MOVE);
			setACCY(getRatedAccY());
		}
		if (isLeft()) {
			setStatus(STATUS_MOVE);
			setACCX(-getRatedAccX());
		}
		if (isRight()) {
			setStatus(STATUS_MOVE);
			setACCX(getRatedAccX());
		}
		if (getAccX() == 0 && getAccY() == 0) {
			setStatus(null);
		}
		if (player.isA()) {
			shooter.attack();
		}
		super.beforeMove();
	}

	@Override
	protected void setStatus(String status) {
		if (STATUS_MOVE.equals(status) && STATUS_DAMAGED.equals(getStatus())) {
			return;
		}
		super.setStatus(status);
	}

	@Override
	protected void afterMove() {
		ensureInContainer();
		if (damagedCnt > 0) {
			damagedCnt--;
			if (damagedCnt <= 0) {
				setStatus(null);
			}
		}
	}

	@Override
	public synchronized void loseLife(IBCharacter character, int life) throws IllegalArgumentException {
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
	public void onCrash(CanCrash crashed) {}

	@Override
	public boolean accept(Cmd c) {
		return c.getType() != CmdType.CLICKED;
	}

}
