package cn.milai.ibdemo.story;

import cn.milai.ib.actor.Actor;
import cn.milai.ib.actor.prop.BloodStrip;
import cn.milai.ib.actor.prop.text.TextLines;
import cn.milai.ib.plugin.audio.Audio;
import cn.milai.ib.stage.Stage;
import cn.milai.ib.stage.Waits;
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
		stage().playAudio(drama.audio(Audio.BGM_CODE, BATTLE_BGM));
		showBGMInfo();

		Dolphin dolphin = drama.newDolphin(stage().getW() / 5, stage().getH() / 2);
		BloodStrip dolphinBlood = drama.newBloodStrip(stage().getW() / 4, stage().getH() * 9 / 10, dolphin);
		stage().onRemoveActor().subscribe(e -> {
			for (Actor actor : e.actors()) {
				if (dolphin == actor) {
					stage().stopAudio(Audio.BGM_CODE);
					stop();
				}
			}
		});
		stage().addActor(dolphin).addActor(dolphinBlood);

		Shark shark = drama.newShark();
		BloodStrip sharkBlood = drama.newBloodStrip(stage().getW() * 3 / 4, stage().getH() / 10, shark);
		stage().addActor(shark).addActorSync(sharkBlood);
		while (shark.getHealth().isAlive()) {
			Waits.wait(stage(), 10L);
		}
		Waits.wait(stage(), 30L);
		stage().removeActorSync(sharkBlood);
		return dolphin.getHealth().isAlive();
	}

	private void showBGMInfo() {
		TextLines bgmInfo = drama.newTextLines(7L, 28L, 7L, drama.str("bgm_info").split("\n"));
		bgmInfo.setX(stage().getW() - 1 - bgmInfo.getIntW());
		bgmInfo.setY(stage().getH() - 1 - bgmInfo.getIntH());
		stage().addActor(bgmInfo);
	}

}
