package cn.milai.ibdemo.role.helper;

import cn.milai.ib.config.Configurable;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.helper.AbstractHelper;
import cn.milai.ib.role.property.Movable;

/**
 * 加速道具
 * @author milai
 */
public class AccelerateHelper extends AbstractHelper {

	private static final String ACC_STATUS = "accelerate";

	public static final String P_MAX_RATED_SPEED_X = "maxRatedSpeedX";
	public static final String P_MAX_RATED_SPEED_Y = "maxRatedSpeedY";

	private double maxRatedSpeedX;
	private double maxRatedSpeedY;

	public double getMaxRatedSpeedX() { return maxRatedSpeedX; }

	@Configurable
	public void setMaxRatedSpeedX(double maxRatedSpeedX) { this.maxRatedSpeedX = maxRatedSpeedX; }

	public double getMaxRatedSpeedY() { return maxRatedSpeedY; }

	@Configurable
	public void setMaxRatedSpeedY(double maxRatedSpeedY) { this.maxRatedSpeedY = maxRatedSpeedY; }

	@Override
	public void makeFunction(PlayerRole player) {
		if (!(player.hasProperty(Movable.class))) {
			return;
		}
		player.pushStatus(false);
		Movable movable = player.getProperty(Movable.class);
		movable.setRatedSpeedX(Math.min(maxRatedSpeedX, movable.getRatedSpeedX() + 2));
		movable.setRatedSpeedY(Math.min(maxRatedSpeedY, movable.getRatedSpeedY() + 2));
		player.setStatus(ACC_STATUS);
	}

}
