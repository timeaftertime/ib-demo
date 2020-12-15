package cn.milai.ib.demo.character;

import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.property.CanCrash;
import cn.milai.ib.character.weapon.bullet.AbstractBullet;
import cn.milai.ib.character.weapon.bullet.Bullet;
import cn.milai.ib.container.Container;
import cn.milai.ib.container.ContainerEventListener;

/**
 * 光线特效窗口组件
 * @author milai
 * @date 2020.03.25
 */
public class UltraLight extends AbstractBullet implements ContainerEventListener {

	private long durationFrame;
	private double deltaRadian;
	private int width = 0;
	private int height = 0;

	public UltraLight(IBCharacter owner, long durationFrame) {
		super(0, 0, owner);
		setDirection(Math.PI / 2);
		this.durationFrame = durationFrame;
		deltaRadian = getDirection() / durationFrame;
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
		setDirection(getDirection() - deltaRadian);
	}

	@Override
	public void onCrash(CanCrash crashed) {
		crashed.loseLife(this, getDamage());
	}

}
