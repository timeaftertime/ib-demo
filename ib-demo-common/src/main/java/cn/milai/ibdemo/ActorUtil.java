package cn.milai.ibdemo;

import cn.milai.ib.actor.Actor;

/**
 * {@link Actor} 工具类
 * @author milai
 * @date 2022.05.21
 */
public class ActorUtil {

	/**
	 * 确保指定 {@link Actor} 在指定范围，否则移动到最近边缘
	 * @param actor
	 * @param minX
	 * @param maxX
	 * @param minY
	 * @param maxY
	 */
	public static void ensureIn(Actor actor, double minX, double maxX, double minY, double maxY) {
		if (actor.getX() < minX) {
			actor.setX(minX);
		}
		if (actor.getY() < minY) {
			actor.setY(minY);
		}
		if (actor.getX() + actor.getW() > maxX) {
			actor.setX(maxX - actor.getW());
		}
		if (actor.getY() + actor.getH() > maxY) {
			actor.setY(maxY - actor.getH());
		}
	}
}
