package cn.milai.ibdemo.role.fish;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.graphics.Images;
import cn.milai.ib.role.MovableRole;
import cn.milai.ib.role.property.base.BaseCollider;
import cn.milai.ib.role.property.base.BaseRigidbody;
import cn.milai.ibdemo.role.explosion.FishFallExplosible;

/**
 * Fish 的抽象基类
 * @author milai
 * @date 2020.04.03
 */
public abstract class AbstractFish extends MovableRole implements Fish {

	private Map<BufferedImage, BufferedImage> flipped = new ConcurrentHashMap<>();

	public AbstractFish(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		setRigidbody(new BaseRigidbody(this));
		setExplosible(new FishFallExplosible(this));
		setCollider(new BaseCollider(this));
	}

	@Override
	public BufferedImage getNowImage() {
		BufferedImage nowImage = super.getNowImage();
		if (getDirection() < 0) {
			return flipped.computeIfAbsent(nowImage, Images::horizontalFlip);
		}
		return nowImage;
	}

}
