package cn.milai.ibdemo.role.fish;

import cn.milai.ib.config.Configurable;
import cn.milai.ib.item.Item;
import cn.milai.ib.role.BasePlayer;
import cn.milai.ib.role.Player;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Health;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.Rigidbody;
import cn.milai.ib.role.property.base.BaseHealth;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ibdemo.role.DemoPlayerRole;
import cn.milai.ibdemo.role.bullet.shooter.BlueShooter;

/**
 * 海豚
 * @author milai
 * @date 2020.03.28
 */
public class Dolphin extends AbstractFish implements DemoPlayerRole {

	public static final String STATUS_MOVE = "move";
	public static final String STATUS_DAMAGED = "damaged";

	private int shootInterval = 5;
	private int maxBulletNum = 5;

	private Player player = new BasePlayer();
	private BulletShooter shooter;
	private int damagedCnt;

	public Dolphin() {
		setMovable(new DolphinMovable());
		setDirection(Math.PI / 2);
	}

	@Override
	protected void initItem() {
		shooter = new BlueShooter(shootInterval, maxBulletNum, this);
	}

	protected void beforeRefreshSpeeds(Movable m) {
		Rigidbody r = getRigidbody();
		if (r == null) {
			return;
		}
		if (damagedCnt > 0) {
			damagedCnt--;
			if (damagedCnt <= 0) {
				setStatus(Item.STATUS_DEFAULT);
			}
		}
		if (isUp()) {
			setStatus(STATUS_MOVE);
			r.addForceY(-getForceY());
		}
		if (isDown()) {
			setStatus(STATUS_MOVE);
			r.addForceY(getForceY());
		}
		if (isLeft()) {
			setStatus(STATUS_MOVE);
			r.addForceX(-getForceX());
		}
		if (isRight()) {
			setStatus(STATUS_MOVE);
			r.addForceX(getForceX());
		}
		if (damagedCnt <= 0 && r.getForceX() == 0 && r.getForceY() == 0) {
			setStatus(Item.STATUS_DEFAULT);
		}
		if (player.isA()) {
			shooter.attack();
		}
	}

	@Override
	public void setStatus(String status) {
		if (STATUS_MOVE.equals(status) && STATUS_DAMAGED.equals(getStatus())) {
			return;
		}
		super.setStatus(status);
	}

	protected void afterMove(Movable m) {
		ensureIn(0, container().getW(), 0, container().getH());
	}

	@Override
	protected Health createHealth() {
		return new BaseHealth() {
			@Override
			public synchronized void changeHP(Role character, int life) throws IllegalArgumentException {
				damagedCnt = 8;
				setStatus(STATUS_DAMAGED);
				super.changeHP(character, life);
			}
		};
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

	public int getShootInterval() { return shootInterval; }

	@Configurable
	public void setShootInterval(int shootInterval) {
		if (shootInterval < 0) {
			throw new IllegalArgumentException("发射间隔必须大于等于 0");
		}
		this.shootInterval = shootInterval;
	}

	public int getMaxBulletNum() { return maxBulletNum; }

	@Configurable
	public void setMaxBulletNum(int maxBulletNum) {
		if (maxBulletNum < 0) {
			throw new IllegalArgumentException("最大 bullet 数量必须大于等于 0");
		}
		this.maxBulletNum = maxBulletNum;
	}

}
