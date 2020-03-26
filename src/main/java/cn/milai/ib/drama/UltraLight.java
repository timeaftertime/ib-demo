package cn.milai.ib.drama;

import cn.milai.ib.character.bullet.AbstractBullet;
import cn.milai.ib.character.bullet.Bullet;
import cn.milai.ib.character.property.Shootable;
import cn.milai.ib.container.Container;
import cn.milai.ib.container.listener.ContainerEventListener;

/**
 * 光线特效窗口组件
 * @author milai
 * @date 2020.03.25
 */
public class UltraLight extends AbstractBullet implements ContainerEventListener {

	private long durationFrame;
	private double deltaRadian;
	private double radian = Math.PI / 2;
	private int width = 0;
	private int height = 0;

	public UltraLight(Shootable owner, long durationFrame) {
		super(0, 0, owner);
		this.durationFrame = durationFrame;
		deltaRadian = radian / durationFrame;
		// 计算实际坐标、宽度和高度
		int w = getContainer().getWidth();
		int h = getContainer().getHeight();
		width = w / 8 + 1;
		height = (int) Math.sqrt(h * h + w * w);
		setWidth(width);
		setHeight(height * 2);
		setY(h - height);
		getContainer().addEventListener(this);
	}

	@Override
	public double speedRadian() {
		return -radian;
	}

	@Override
	protected int proratedIntProp(String key) {
		return 0;
	}

	@Override
	protected int intProp(String key) {
		if (key.equals(Bullet.P_POWER)) {
			return 100;
		}
		return 0;
	}

	@Override
	public boolean isAlive() {
		return true;
	}

	@Override
	public void afterRefresh(Container container) {
		durationFrame--;
		if (durationFrame < 0) {
			getContainer().removeObject(this);
			getContainer().removeEventListener(this);
		}
		radian -= deltaRadian;
	}

}
