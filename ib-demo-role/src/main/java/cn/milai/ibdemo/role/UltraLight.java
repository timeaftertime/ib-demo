package cn.milai.ibdemo.role;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.nature.AlwaysAliveHealth;
import cn.milai.ib.role.nature.Collider;
import cn.milai.ib.role.nature.Health;
import cn.milai.ib.role.nature.Movable;
import cn.milai.ib.role.weapon.bullet.AbstractBullet;
import cn.milai.ib.stage.Stage;

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
		setMovable(new UltraLightMovable(this));
		getDamage().setValue(100);
	}

	@Override
	protected void onEnterStage(Stage stage) {
		double w = stage().getW();
		double h = stage().getH();
		width = w / 6 + 1;
		height = Math.sqrt(h * h + w * w);
		setW(width);
		setH(height * 2);
		setY(h - height);
	}

	@Override
	protected Health createHealth() {
		return new AlwaysAliveHealth(this);
	}

	@Override
	public void afterMove(Movable m) {
		durationFrame--;
		if (durationFrame < 0) {
			exit();
		}
		setDirection(getDirection() - deltaRadian);
	}

	@Override
	protected boolean canCrashWith(Collider crashed) {
		return true;
	}

}
