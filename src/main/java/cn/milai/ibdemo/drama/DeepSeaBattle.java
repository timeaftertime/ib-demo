package cn.milai.ibdemo.drama;

import java.awt.Color;

import cn.milai.ib.IBObject;
import cn.milai.ib.component.BloodStrip;
import cn.milai.ib.component.text.TextLines;
import cn.milai.ib.container.ContainerEventListener;
import cn.milai.ib.container.DramaContainer;
import cn.milai.ib.container.ui.Audio;
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
		Dolphin dolphin = new Dolphin(container().getWidth() / 5, container().getHeight() / 2, container());
		BloodStrip dolphinBlood = new BloodStrip(container().getWidth() / 4, container().getHeight() * 9 / 10,
			container(), dolphin);
		container().addObject(dolphin);
		container().addObject(dolphinBlood);
		container().addEventListener(new ContainerEventListener() {
			@Override
			public void onObjectRemoved(IBObject obj) {
				if (dolphin == obj) {
					container().stopAudio(Audio.BGM_CODE);
					stop();
				}
			}
		});
		Shark shark = new Shark(container());
		BloodStrip sharkBlood = new BloodStrip(container().getWidth() * 3 / 4, container().getHeight() / 10,
			container(), shark);
		container().addObject(shark);
		container().addObject(sharkBlood);
		while (shark.isAlive()) {
			WaitUtil.wait(container(), 10L);
		}
		container().removeObject(sharkBlood);
		return dolphin.isAlive();
	}

	private void showBGMInfo() {
		TextLines bgmInfo = new TextLines(0, 0, container(),
			StringUtil.lines(drama.str("bgm_info")), Color.BLACK, 7L, 28L, 7L);
		bgmInfo.setX(container().getWidth() - 1 - bgmInfo.getWidth());
		bgmInfo.setY(container().getHeight() - 1 - bgmInfo.getHeight());
		container().addObject(bgmInfo);
	}

}
