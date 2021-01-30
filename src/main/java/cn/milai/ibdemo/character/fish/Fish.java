package cn.milai.ibdemo.character.fish;

import cn.milai.ib.character.property.Movable;

/**
 * 水中游的（不一定是真正的“鱼”）游戏角色
 * @author milai
 * @date 2020.04.03
 */
public interface Fish extends Movable {

	/**
	 * 属性 [X 方向加速度] 的 key
	 */
	String P_RATED_ACC_X = "ratedACCX";

	/**
	 * 属性 [Y 方向加速度] 的 key
	 */
	String P_RATED_ACC_Y = "ratedACCY";

	/**
	 * 属性 [阻力加速度大小]
	 */
	String P_STOP_ACC = "stopACC";

	/**
	 * 获取 X 方向额定加速度大小
	 * @return
	 */
	double getRatedACCX();

	/**
	 * 获取 Y 方向额定加速度大小
	 * @return
	 */
	double getRatedACCY();
}
