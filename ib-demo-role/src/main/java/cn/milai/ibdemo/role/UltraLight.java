package cn.milai.ibdemo.role;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Collider;
import cn.milai.ib.role.property.Health;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.base.BaseHealth;
import cn.milai.ib.role.weapon.bullet.AbstractBullet;

/**
 * 光线
 * @author milai
 * @date 2020.03.25
 */
public class UltraLight extends AbstractBullet {

	private long durationFrame;
	private double deltaRadian;
	private double width = 0;
	private double height = 0;

	public UltraLight(Role owner, long durationFrame) {
		super(owner);
		setDirection(Math.PI / 2);
		this.durationFrame = durationFrame;
		deltaRadian = getDirection() / durationFrame;
		setMovable(new UltraLightMovable());
		getDamage().setValue(100);
	}

	@Override
	protected void initItem() {
		double w = container().getW();
		double h = container().getH();
		width = w / 6 + 1;
		height = Math.sqrt(h * h + w * w);
		setW(width);
		setH(height * 2);
		setY(h - height);
	}

	@Override
	protected Health createHealth() {
		return new BaseHealth() {
			@Override
			public boolean isAlive() { return true; }
		};
	}

	@Override
	public void afterMove(Movable m) {
		durationFrame--;
		if (durationFrame < 0) {
			container().removeObject(this);
		}
		setDirection(getDirection() - deltaRadian);
	}

	@Override
	protected boolean canCrashWith(Collider crashed) {
		return true;
	}

}
