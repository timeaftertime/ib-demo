package cn.milai.ibdemo.story;

import java.util.List;

import cn.milai.ib.container.Container;
import cn.milai.ib.container.Stage;
import cn.milai.ib.container.Waits;
import cn.milai.ib.container.listener.ItemListener;
import cn.milai.ib.container.plugin.media.Audio;
import cn.milai.ib.control.BloodStrip;
import cn.milai.ib.control.text.TextLines;
import cn.milai.ib.item.Item;
import cn.milai.ibdemo.role.fish.Dolphin;
import cn.milai.ibdemo.role.fish.Shark;

/**
 * 深海战斗
 * @author milai
 * @date 2020.03.28
 */
public class DeepSeaBattle extends Battle {

	private static final String BATTLE_BGM = "/audio/newAwakening.mp3";

	public DeepSeaBattle(DemoDrama drama, Stage container) {
		super(drama, container);
	}

	@Override
	protected boolean doRun() {
		container().playAudio(drama.audio(Audio.BGM_CODE, BATTLE_BGM));
		showBGMInfo();
		Dolphin dolphin = drama.newDolphin(container().getW() / 5, container().getH() / 2);
		BloodStrip dolphinBlood = drama.newBloodStrip(container().getW() / 4, container().getH() * 9 / 10, dolphin);
		container().addObject(dolphin);
		container().addObject(dolphinBlood);
		container().addItemListener(new ItemListener() {
			@Override
			public void onRemoved(Container container, List<Item> objs) {
				for (Item obj : objs) {
					if (dolphin == obj) {
						container().stopAudio(Audio.BGM_CODE);
						stop();
					}
				}
			}
		});
		Shark shark = drama.newShark();
		BloodStrip sharkBlood = drama.newBloodStrip(container().getW() * 3 / 4, container().getH() / 10, shark);
		container().addObject(shark);
		container().addObject(sharkBlood);
		while (shark.getHealth().isAlive()) {
			Waits.wait(container(), 10L);
		}
		Waits.wait(container(), 30L);
		container().removeObject(sharkBlood);
		return dolphin.getHealth().isAlive();
	}

	private void showBGMInfo() {
		TextLines bgmInfo = drama.newTextLines(7L, 28L, 7L, drama.str("bgm_info").split("\n"));
		bgmInfo.setX(container().getW() - 1 - bgmInfo.getIntW());
		bgmInfo.setY(container().getH() - 1 - bgmInfo.getIntH());
		container().addObject(bgmInfo);
	}

}
