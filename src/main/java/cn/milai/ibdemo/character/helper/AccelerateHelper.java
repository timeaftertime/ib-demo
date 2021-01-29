package cn.milai.ibdemo.character.helper;

import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.character.helper.AbstractHelper;
import cn.milai.ib.character.property.Movable;
import cn.milai.ib.conf.SystemConf;
import cn.milai.ib.container.ui.UIContainer;
import cn.milai.ib.loader.ImageLoader;

public class AccelerateHelper extends AbstractHelper {

	private static final String ACC_STATUS = "accelerate";

	public static final String P_MAX_RATED_SPEED_X = "maxRatedSpeedX";
	public static final String P_MAX_RATED_SPEED_Y = "maxRatedSpeedY";

	private int maxRatedSpeedX = 21;
	private int maxRatedSpeedY = 21;

	public AccelerateHelper(int x, int y, UIContainer container) {
		super(x, y, container);
		maxRatedSpeedX = intProp(P_MAX_RATED_SPEED_X);
		maxRatedSpeedY = intProp(P_MAX_RATED_SPEED_Y);
	}

	@Override
	public void makeFunction(PlayerCharacter player) {
		if (!(player instanceof Movable)) {
			return;
		}
		player.pushStatus(false);
		Movable movable = (Movable) player;
		movable.setRatedSpeedX(Integer.min(maxRatedSpeedX,
			movable.getRatedSpeedX() + SystemConf.frameProrate(2)));
		movable.setRatedSpeedY(Integer.min(maxRatedSpeedY,
			movable.getRatedSpeedY() + SystemConf.frameProrate(2)));
		player.setImage(ImageLoader.load(player.getClass(), ACC_STATUS));
	}

}
