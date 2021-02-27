package cn.milai.ibdemo.drama;

import java.awt.Color;
import java.util.List;

import cn.milai.ib.IBObject;
import cn.milai.ib.component.BloodStrip;
import cn.milai.ib.component.text.TextLines;
import cn.milai.ib.container.Container;
import cn.milai.ib.container.DramaContainer;
import cn.milai.ib.container.listener.ObjectListener;
import cn.milai.ib.container.plugin.media.Audio;
import cn.milai.ib.drama.Drama;
import cn.milai.ib.util.StringUtil;
import cn.milai.ib.util.WaitUtil;
import cn.milai.ibdemo.character.fish.Dolphin;
import cn.milai.ibdemo.character.fish.Shark;

/**
 * 深海战斗
 * @author milai
 * @date 2020.03.28
 */
public class DeepSeaBattle extends Battle {

	private static final String BATTLE_BGM = "/audio/newAwakening.mp3";

	public DeepSeaBattle(Drama drama, DramaContainer container) {
		super(drama, container);
	}

	@Override
	protected boolean doRun() {
		container().playAudio(drama.audio(Audio.BGM_CODE, BATTLE_BGM));
		showBGMInfo();
		Dolphin dolphin = new Dolphin(container().getW() / 5, container().getH() / 2, container());
		BloodStrip dolphinBlood = new BloodStrip(
			container().getW() / 4, container().getH() * 9 / 10, container(), dolphin
		);
		container().addObject(dolphin);
		container().addObject(dolphinBlood);
		container().addObjectListener(new ObjectListener() {
			@Override
			public void onObjectRemoved(Container container, List<IBObject> objs) {
				for (IBObject obj : objs) {
					if (dolphin == obj) {
						container().stopAudio(Audio.BGM_CODE);
						stop();
					}
				}
			}
		});
		Shark shark = new Shark(container());
		BloodStrip sharkBlood = new BloodStrip(
			container().getW() * 3 / 4, container().getH() / 10, container(), shark
		);
		container().addObject(shark);
		container().addObject(sharkBlood);
		while (shark.isAlive()) {
			WaitUtil.wait(container(), 10L);
		}
		container().removeObject(sharkBlood);
		return dolphin.isAlive();
	}

	private void showBGMInfo() {
		TextLines bgmInfo = new TextLines(
			0, 0, container(), StringUtil.lines(drama.str("bgm_info")), Color.BLACK, 7L, 28L, 7L
		);
		bgmInfo.setX(container().getW() - 1 - bgmInfo.getIntW());
		bgmInfo.setY(container().getH() - 1 - bgmInfo.getIntH());
		container().addObject(bgmInfo);
	}

}
