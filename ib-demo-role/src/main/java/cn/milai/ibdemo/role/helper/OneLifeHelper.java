package cn.milai.ibdemo.role.helper;

import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.helper.AbstractHelper;

/**
 * 生命道具
 * @author milai
 */
public class OneLifeHelper extends AbstractHelper {

	private static final int MAX_LIFE = 7;

	private static final int GAIN_LIFE = 1;

	public OneLifeHelper(double maxY) {
		super(maxY);
	}

	@Override
	public void makeFunction(PlayerRole player) {
		if (player.getHealth().getHP() >= MAX_LIFE) {
			return;
		}
		player.getHealth().changeHP(this, GAIN_LIFE);
	}

}
