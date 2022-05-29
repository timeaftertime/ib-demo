package cn.milai.ibdemo.role.force;

import cn.milai.ib.role.nature.Movable;
import cn.milai.ib.role.nature.Rigidbody;
import cn.milai.ibdemo.role.bullet.shooter.BlueShooter;

/**
 * 空军
 * @author milai
 * @date 2021.05.01
 */
public class AirForce extends ForceRole {

	public static final String STATUS_MOVE = "move";
	public static final String STATUS_DAMAGED = "damaged";

	public static final double TILT_RADIAN = Math.PI / 6;

	public AirForce() {
		setShooter(new BlueShooter(5, 5, this));
	}

	@Override
	public void applyForce(Movable m, Rigidbody r) {
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
		if (player().isA()) {
			getShooter().attack();
		}
	}

}
