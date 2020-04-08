package cn.milai.ib.demo.character.fish;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.common.collect.Maps;

import cn.milai.ib.character.MovableIBCharacter;
import cn.milai.ib.character.explosion.creator.ExplosionCreator;
import cn.milai.ib.character.property.CanCrash;
import cn.milai.ib.character.property.Explosible;
import cn.milai.ib.container.Container;
import cn.milai.ib.demo.character.explosion.creator.FishFallCreator;
import cn.milai.ib.util.ImageUtil;

/**
 * Fish 的抽象基类
 * @author milai
 * @date 2020.04.03
 */
public abstract class AbstractFish extends MovableIBCharacter implements Fish, CanCrash, Explosible {

	private int ratedACCX;
	private int ratedACCY;
	private int accX;
	private int accY;
	private int stopACC;

	private Map<BufferedImage, BufferedImage> flipped = Maps.newConcurrentMap();

	public AbstractFish(int x, int y, Container container) {
		super(x, y, container);
		ratedACCX = proratedIntProp(P_RATED_ACC_X);
		ratedACCY = proratedIntProp(P_RATED_ACC_Y);
		accX = 0;
		accY = 0;
		stopACC = proratedIntProp(P_STOP_ACC);
	}

	@Override
	protected void beforeMove() {
		// 计算加速度
		setSpeedX(getSpeedX() + getACCX());
		setSpeedY(getSpeedY() + getACCY());
		// 计算阻力加速度
		int speedX = getSpeedX();
		int speedY = getSpeedY();
		double speed = Math.sqrt(speedX * speedX + speedY * speedY);
		int deltaSpeedX = (int) (stopACC * -speedX / speed);
		int deltaSpeedY = (int) (stopACC * -speedY / speed);
		if (deltaSpeedX == 0) {
			deltaSpeedX = speedX > 0 ? -1 : 1;
		}
		if (deltaSpeedY == 0) {
			deltaSpeedY = speedY > 0 ? -1 : 1;
		}
		if (Math.abs(deltaSpeedX) > Math.abs(getSpeedX())) {
			deltaSpeedX = -getSpeedX();
		}
		if (Math.abs(deltaSpeedY) > Math.abs(getSpeedY())) {
			deltaSpeedY = -getSpeedY();
		}
		setSpeedX(getSpeedX() + deltaSpeedX);
		setSpeedY(getSpeedY() + deltaSpeedY);
		// 不超过额定速度
		if (getSpeedX() > getRatedSpeedX()) {
			setSpeedX(getRatedSpeedX());
		} else if (getSpeedX() < -getRatedSpeedX()) {
			setSpeedX(-getRatedSpeedX());
		}
		if (getSpeedY() > getRatedSpeedY()) {
			setSpeedY(getRatedSpeedY());
		} else if (getSpeedY() < -getRatedSpeedY()) {
			setSpeedY(-getRatedSpeedY());
		}
	}

	@Override
	public BufferedImage getNowImage() {
		BufferedImage nowImage = super.getNowImage();
		if (getDirection() < 0) {
			return flipped.computeIfAbsent(nowImage, img -> ImageUtil.horizontalFlip(img));
		}
		return nowImage;
	}

	@Override
	public ExplosionCreator getExplosionCreator() {
		return new FishFallCreator();
	}

	@Override
	public int getRatedACCX() {
		return ratedACCX;
	}

	@Override
	public int getRatedACCY() {
		return ratedACCY;
	}

	/**
	 * 获取 X 方向加速度
	 * @return
	 */
	protected int getACCX() {
		return accX;
	}

	/**
	 * 获取 Y 方向加速度
	 * @return
	 */
	protected int getACCY() {
		return accY;
	}

	/**
	 * 设置 X 方向加速度
	 * @param accX
	 */
	public void setACCX(int accX) {
		this.accX = accX;
	}

	/**
	 * 设置 Y 方向加速度
	 * @param accY
	 */
	public void setACCY(int accY) {
		this.accY = accY;
	}

}
