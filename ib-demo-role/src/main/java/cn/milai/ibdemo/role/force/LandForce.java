package cn.milai.ibdemo.role.force;

import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.Rigidbody;

/**
 * 陆军
 * @author milai
 * @date 2021.04.30
 */
public class LandForce extends ForceRole {

	public static final String STATUS_MOVE = "move";

	public static final String STATUS_JUMP = "jump";

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
