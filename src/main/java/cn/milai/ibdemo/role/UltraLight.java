package cn.milai.ibdemo.role;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.container.listener.LifecycleListener;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.property.Collider;
import cn.milai.ib.role.weapon.bullet.AbstractBullet;
import cn.milai.ib.role.weapon.bullet.Bullet;

/**
 * 光线特效窗口组件
 * @author milai
 * @date 2020.03.25
 */
public class UltraLight extends AbstractBullet implements LifecycleListener {

	private long durationFrame;
	private double deltaRadian;
	private double width = 0;
	private double height = 0;

	public UltraLight(Role owner, long durationFrame) {
		super(0, 0, owner);
		setDirection(Math.PI / 2);
		this.durationFrame = durationFrame;
		deltaRadian = getDirection() / durationFrame;
		// 计算实际坐标、宽度和高度
		int w = getContainer().getW();
		int h = getContainer().getH();
		width = w / 8 + 1;
		height = Math.sqrt(h * h + w * w);
		setW(width);
		setH(height * 2);
		setY(h - height);
		getContainer().addLifecycleListener(this);
	}

	@Override
	public int intProp(String key) {
		if (key.equals(Bullet.P_POWER)) {
			return 100;
		}
		return 0;
	}

	@Override
	public double doubleProp(String key) {
		return 0;
	}

	@Override
	public boolean isAlive() { return true; }

	@Override
	public void afterRefresh(LifecycleContainer container) {
		durationFrame--;
		if (durationFrame < 0) {
			getContainer().removeObject(this);
			getContainer().removeLifecycleListener(this);
		}
		setDirection(getDirection() - deltaRadian);
	}

	@Override
	protected boolean canCrashWith(Collider crashed) {
		return true;
	}

}
