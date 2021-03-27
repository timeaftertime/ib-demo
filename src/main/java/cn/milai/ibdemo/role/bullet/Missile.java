package cn.milai.ibdemo.role.bullet;

import cn.milai.ib.role.Role;
import cn.milai.ib.role.weapon.bullet.AbstractBullet;

/**
 * 导弹
 * @author milai
 */
public class Missile extends AbstractBullet {

	public Missile(double x, double y, Role owner) {
		super(x, y, owner);
	}

}
