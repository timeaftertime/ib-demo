package cn.milai.ibdemo.role.fish;

import cn.milai.ib.config.Configurable;
import cn.milai.ib.item.property.Painter;
import cn.milai.ib.role.BaseRole;
import cn.milai.ib.role.property.ReversiblePainter;
import cn.milai.ib.role.property.base.BaseCollider;
import cn.milai.ib.role.property.base.BaseMovable;
import cn.milai.ib.role.property.base.BaseRigidbody;
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
		setRigidbody(new BaseRigidbody());
		setExplosible(new FishFallExplosible());
		setCollider(new BaseCollider());
		setMovable(new BaseMovable());
	}

	@Override
	protected Painter createPainter() { return new ReversiblePainter(true, false); }

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
