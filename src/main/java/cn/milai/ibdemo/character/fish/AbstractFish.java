package cn.milai.ibdemo.character.fish;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.common.collect.Maps;

import cn.milai.ib.character.MovableIBCharacter;
import cn.milai.ib.character.explosion.creator.ExplosionCreator;
import cn.milai.ib.character.property.CanCrash;
import cn.milai.ib.character.property.Explosible;
import cn.milai.ib.container.ui.UIContainer;
import cn.milai.ib.util.ImageUtil;
import cn.milai.ibdemo.character.explosion.creator.FishFallCreator;

/**
 * Fish 的抽象基类
 * @author milai
 * @date 2020.04.03
 */
public abstract class AbstractFish extends MovableIBCharacter implements Fish, CanCrash, Explosible {

	private double ratedACCX;
	private double ratedACCY;
	private double accX;
	private double accY;
	private double stopAcc;

	private Map<BufferedImage, BufferedImage> flipped = Maps.newConcurrentMap();

	public AbstractFish(double x, double y, UIContainer container) {
		super(x, y, container);
		ratedACCX = doubleProp(P_RATED_ACC_X);
		ratedACCY = doubleProp(P_RATED_ACC_Y);
		accX = 0;
		accY = 0;
		stopAcc = doubleProp(P_STOP_ACC);
	}

	@Override
	protected void beforeMove() {
		// 计算加速度
		setSpeedX(getSpeedX() + getAccX());
		setSpeedY(getSpeedY() + getAccY());
		// 计算阻力加速度
		double speedX = getSpeedX();
		double speedY = getSpeedY();
		double speed = Math.sqrt(speedX * speedX + speedY * speedY);
		if (speed != 0) {
			double deltaSpeedX = -stopAcc * speedX / speed;
			double deltaSpeedY = -stopAcc * speedY / speed;
			if (Math.abs(deltaSpeedX) > Math.abs(getSpeedX())) {
				deltaSpeedX = -getSpeedX();
			}
			if (Math.abs(deltaSpeedY) > Math.abs(getSpeedY())) {
				deltaSpeedY = -getSpeedY();
			}
			setSpeedX(getSpeedX() + deltaSpeedX);
			setSpeedY(getSpeedY() + deltaSpeedY);
		}
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
	public ExplosionCreator getExplosionCreator() { return new FishFallCreator(); }

	@Override
	public double getRatedAccX() { return ratedACCX; }

	@Override
	public double getRatedAccY() { return ratedACCY; }

	/**
	 * 获取 X 方向加速度
	 * @return
	 */
	protected double getAccX() { return accX; }

	/**
	 * 获取 Y 方向加速度
	 * @return
	 */
	protected double getAccY() { return accY; }

	/**
	 * 设置 X 方向加速度
	 * @param accX
	 */
	public void setACCX(double accX) { this.accX = accX; }

	/**
	 * 设置 Y 方向加速度
	 * @param accY
	 */
	public void setACCY(double accY) { this.accY = accY; }

}
