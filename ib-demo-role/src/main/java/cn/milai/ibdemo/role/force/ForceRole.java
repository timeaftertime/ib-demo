package cn.milai.ibdemo.role.force;

import cn.milai.ib.actor.Actor;
import cn.milai.ib.actor.config.Configurable;
import cn.milai.ib.actor.nature.Painter;
import cn.milai.ib.role.BasePlayer;
import cn.milai.ib.role.BaseRole;
import cn.milai.ib.role.Player;
import cn.milai.ib.role.nature.Movable;
import cn.milai.ib.role.nature.ReversiblePainter;
import cn.milai.ib.role.nature.Rigidbody;
import cn.milai.ib.role.nature.base.BaseMovable;
import cn.milai.ib.role.nature.base.BaseRigidbody;
import cn.milai.ib.role.nature.holder.AwareMovableHolder;
import cn.milai.ib.role.nature.holder.RigidbodyHolder;
import cn.milai.ib.role.weapon.bullet.shooter.BulletShooter;
import cn.milai.ibdemo.role.DemoPlayerRole;

/**
 * 士兵角色
 * @author milai
 * @date 2021.05.01
 */
public class ForceRole extends BaseRole implements DemoPlayerRole, AwareMovableHolder, RigidbodyHolder {

	private int forceX;
	private int forceY;

	private Player player = new BasePlayer();
	private BulletShooter shooter;

	private int damagedCnt;

	public ForceRole() {
		setMovable(new BaseMovable(this));
		setRigidbody(new BaseRigidbody(this));
	}

	@Override
	public final void beforeRefreshSpeeds(Movable m) {
		Rigidbody r = getRigidbody();
		if (r == null) {
			return;
		}
		if (damagedCnt > 0) {
			damagedCnt--;
			if (damagedCnt <= 0) {
				setStatus(Actor.STATUS_DEFAULT);
			}
		}
		applyForce(m, r);
	}

	protected void applyForce(Movable m, Rigidbody r) {
	}

	protected void setShooter(BulletShooter shooter) { this.shooter = shooter; }

	protected BulletShooter getShooter() { return shooter; }

	@Override
	public Player player() {
		return player;
	}

	@Override
	protected Painter createPainter() {
		return new ReversiblePainter(this, true, false);
	}

	@Override
	public void onSetLeft() {
		clearRight();
		setDirection(-Math.PI / 2);
	}

	@Override
	public void onSetRight() {
		clearLeft();
		setDirection(Math.PI / 2);
	}

	public int getForceX() { return forceX; }

	@Configurable
	public void setForceX(int forceX) {
		if (forceX < 0) {
			throw new IllegalArgumentException("力度必须大于等于 0");
		}
		this.forceX = forceX;
	}

	public int getForceY() { return forceY; }

	@Configurable
	public void setForceY(int forceY) {
		if (forceY < 0) {
			throw new IllegalArgumentException("力度必须大于等于 0");
		}
		this.forceY = forceY;
	}
}
