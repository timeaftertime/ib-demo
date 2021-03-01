package cn.milai.ibdemo.character.helper;

import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.character.helper.AbstractHelper;
import cn.milai.ib.character.property.Movable;
import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.loader.ImageLoader;

/**
 * 加速道具
 * @author milai
 */
public class AccelerateHelper extends AbstractHelper {

	private static final String ACC_STATUS = "accelerate";

	public static final String P_MAX_RATED_SPEED_X = "maxRatedSpeedX";
	public static final String P_MAX_RATED_SPEED_Y = "maxRatedSpeedY";

	private double maxRatedSpeedX = 21;
	private double maxRatedSpeedY = 21;

	public AccelerateHelper(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		maxRatedSpeedX = doubleProp(P_MAX_RATED_SPEED_X);
		maxRatedSpeedY = doubleProp(P_MAX_RATED_SPEED_Y);
	}

	@Override
	public void makeFunction(PlayerCharacter player) {
		if (!(player instanceof Movable)) {
			return;
		}
		player.pushStatus(false);
		Movable movable = (Movable) player;
		movable.setRatedSpeedX(Math.min(maxRatedSpeedX, movable.getRatedSpeedX() + 2));
		movable.setRatedSpeedY(Math.min(maxRatedSpeedY, movable.getRatedSpeedY() + 2));
		player.setImage(ImageLoader.load(player.getClass(), ACC_STATUS));
	}

}
