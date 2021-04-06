package cn.milai.ibdemo.role.explosion;

import java.awt.image.BufferedImage;

import cn.milai.ib.IBObject;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.AbstractExplosion;
import cn.milai.ib.util.ImageUtil;

/**
 * 上下翻转角色图片并慢慢下降的爆炸类型
 * @author milai
 * @date 2020.04.05
 */
public class FishFall extends AbstractExplosion {

	/**
	 * 属性 [下降速度] 的 key
	 */
	public static final String P_SPEED = "speed";

	private double speed;
	private BufferedImage img;

	public FishFall(Role character) {
		super(0, 0, character.getContainer());
		speed = doubleConf(P_SPEED);
		img = ImageUtil.verticalFlip(character.getNowImage());
		setX(character.getIntX());
		setY(character.getIntY() + character.getIntH());
		setW(character.getIntW());
		setH(character.getIntH());
	}

	@Override
	public double doubleConf(String key) {
		if (key.equals(IBObject.P_WIDTH) || key.equals(IBObject.P_HEIGHT)) {
			return 0;
		}
		return super.doubleConf(key);
	}

	@Override
	public BufferedImage getNowImage() {
		setY(getIntY() + speed);
		return img;
	}
}
