package cn.milai.ibdemo.role.fish;

import cn.milai.ib.actor.config.Configurable;
import cn.milai.ib.actor.nature.Painter;
import cn.milai.ib.role.BaseRole;
import cn.milai.ib.role.nature.ReversiblePainter;
import cn.milai.ib.role.nature.base.BaseCollider;
import cn.milai.ib.role.nature.base.BaseMovable;
import cn.milai.ib.role.nature.base.BaseRigidbody;
import cn.milai.ibdemo.role.explosion.FishFallExplosible;

/**
 * Fish 的抽象基类
 * @author milai
 * @date 2020.04.03
 */
public abstract class AbstractFish extends BaseRole implements Fish {

	private int forceX;
	private int forceY;

	public AbstractFish() {
		setRigidbody(new BaseRigidbody(this));
		setExplosible(new FishFallExplosible(this));
		setCollider(new BaseCollider(this));
		setMovable(new BaseMovable(this));
	}

	@Override
	protected Painter createPainter() {
		return new ReversiblePainter(this, true, false);
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
