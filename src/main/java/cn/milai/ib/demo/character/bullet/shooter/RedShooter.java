package cn.milai.ib.demo.character.bullet.shooter;

import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.weapn.bullet.Bullet;
import cn.milai.ib.character.weapn.bullet.shooter.AbstractBulletShooter;
import cn.milai.ib.demo.character.bullet.RedBullet;

/**
 * 发射红色通常子弹的发射器
 * 2019.11.21
 * @author milai
 */
public class RedShooter extends AbstractBulletShooter {

	private static final long INIT_SHOOT_INTERVAL = 10L;

	private static final int INIT_MAX_BULLET_NUM = 3;

	public RedShooter(IBCharacter owner) {
		super(owner, INIT_SHOOT_INTERVAL, INIT_MAX_BULLET_NUM);
	}

	@Override
	protected Bullet[] createBullets0() {
		return new Bullet[] { new RedBullet((int) owner.getCenterX(), owner.getY() + owner.getHeight(), owner) };
	}

}
