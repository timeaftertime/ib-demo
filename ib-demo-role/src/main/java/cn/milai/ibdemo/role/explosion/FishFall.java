package cn.milai.ibdemo.role.explosion;

import java.awt.image.BufferedImage;

import cn.milai.ib.actor.config.Configurable;
import cn.milai.ib.actor.nature.BasePainter;
import cn.milai.ib.actor.nature.Painter;
import cn.milai.ib.graphics.Images;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.AbstractExplosion;

/**
 * 上下翻转角色图片并慢慢下降的爆炸类型
 * @author milai
 * @date 2020.04.05
 */
public class FishFall extends AbstractExplosion {

	private double speed;
	private BufferedImage img;

	public FishFall(Role character) {
		if (character.getPainter() != null) {
			img = Images.verticalFlip(character.getPainter().getNowImage());
		}
		setX(character.getIntX());
		setY(character.getIntY() + character.getIntH());
		setW(character.getIntW());
		setH(character.getIntH());
	}

	@Override
	protected Painter createPainter() {
		return new BasePainter(this) {
			@Override
			public BufferedImage getNowImage() {
				setY(getIntY() + speed);
				return img;
			}
		};
	}

	public double getSpeed() { return speed; }

	@Configurable
	public void setSpeed(double speed) { this.speed = speed; }

}
