package cn.milai.ib.demo.character.helper;

import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.character.helper.AbstractHelper;
import cn.milai.ib.container.Container;

public class OneLifeHelper extends AbstractHelper {

	private static final int MAX_LIFE = 7;

	private static final int GAIN_LIFE = 1;

	public OneLifeHelper(int x, int y, Container container) {
		super(x, y, container);
	}

	@Override
	public void makeFunction(PlayerCharacter player) {
		if (player.getLife() >= MAX_LIFE) {
			return;
		}
		player.gainLife(this, GAIN_LIFE);
	}

}
