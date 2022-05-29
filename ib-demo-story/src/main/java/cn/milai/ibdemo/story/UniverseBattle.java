package cn.milai.ibdemo.story;

import cn.milai.common.base.Randoms;
import cn.milai.ib.actor.Actor;
import cn.milai.ib.actor.prop.BloodStrip;
import cn.milai.ib.actor.prop.LifeCounter;
import cn.milai.ib.actor.prop.text.TextLines;
import cn.milai.ib.plugin.audio.Audio;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.stage.Stage;
import cn.milai.ib.stage.Waits;
import cn.milai.ibdemo.role.UltraLight;
import cn.milai.ibdemo.role.plane.MissileBoss;

/**
* 控制战斗进程的类
* @author milai
* @date 2020.03.24
*/
public class UniverseBattle extends Battle {

	private static final String BOMB_CODE = "BOMB";

	private static final String AUDIO_BOMB_FILE = "/audio/bomb.mp3";

	private static final String BATTLE_BGM = "/audio/themeOfUltraman.mp3";

	private static final int BLOOD_STRP_Y = 70;

	private PlayerRole player;

	public UniverseBattle(DemoDrama drama, Stage container) {
		super(drama, container);
	}

	@Override
	public boolean doRun() {
		addPlayer();
		stage().onRemoveActor().subscribe(e -> {
			for (Actor actor : e.actors()) {
				if (player == actor) {
					stage().stopAudio(Audio.BGM_CODE);
					stop();
				}
			}
		});
		stage().onAddActor().subscribe(e -> {
			if (e.actor() instanceof Explosion) {
				try {
					stage().playAudio(drama.audio(BOMB_CODE, AUDIO_BOMB_FILE));
				} catch (BattleStoppedException ex) {
				}
			}
		});
		stage().playAudio(drama.audio(Audio.BGM_CODE, BATTLE_BGM));
		showBGMInfo();
		beforeBoss();
		MissileBoss boss = drama.newMissileBoss(stage().getW() / 2, 0);
		BloodStrip bossBloodStrip = drama.newBloodStrip(stage().getW() / 2, BLOOD_STRP_Y, boss);
		Waits.wait(stage(), 10L);
		stage().addActor(boss).addActorSync(bossBloodStrip);
		while (boss.getHealth().isAlive()) {
			randomAddFollowPlane();
			stage().addActor(
				drama.newOneLifeHelper(Randoms.nextInt(stage().getIntW()), 0, stage().getH())
			);
			Waits.wait(stage(), 100L);
			randomAddFollowPlane();
			Waits.wait(stage(), 100L);
		}
		stage().removeActor(bossBloodStrip);
		afterBoss();
		return player.getHealth().isAlive();
	}

	private void showBGMInfo() {
		TextLines bgmInfo = drama.newTextLines(10L, 40L, 10L, drama.str("bgm_info").split("\n"));
		bgmInfo.setX(stage().getW() - 1 - bgmInfo.getIntW());
		bgmInfo.setY(stage().getH() - 1 - bgmInfo.getIntH());
		stage().addActor(bgmInfo);
	}

	private void afterBoss() {
		stage().addActor(drama.newAccelerateHelper(stage().getW() / 2, 0, stage().getH()));
		largeEnemyApear();
		UltraLight light = drama.newUltraLight(player, 20L);
		stage().playAudio(drama.audio("ULTRAMAN", "/audio/ultraman.mp3"));
		stage().addActorSync(light);
		Waits.waitRemove(light);
	}

	private void beforeBoss() {
		// 阶梯 Welcome
		double midX = stage().getW() / 2;
		int interval = 20;
		addLadderWelcome(6, midX, interval, 20L);
		// Welcome Follow 混合
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j <= 3; j++) {
				randomAddFollowPlane();
				Waits.wait(stage(), 10L);
			}
			Waits.wait(stage(), 23L);
			addWelcomes(
				stage().getW() * 0.1,
				stage().getW() * 0.3,
				stage().getW() * 0.5,
				stage().getW() * 0.7,
				stage().getW() * 0.9
			);
		}
		Waits.wait(stage(), 70L);
		hideBoss();
		// 双重阶梯 Welcome
		for (int i = 0; i < 8; i++) {
			stage().addActor(drama.newWelcomePlane(midX - interval * i, 0));
			if (i > 0) {
				stage().addActor(drama.newWelcomePlane(midX + interval * i, 0));
			}
			Waits.wait(stage(), 10L);
			stage().addActor(drama.newWelcomePlane(midX - interval * i, 0));
			if (i > 0) {
				stage().addActor(drama.newWelcomePlane(midX + interval * i, 0));
			}
			Waits.wait(stage(), 20L);
		}
		stage().addActor(drama.newAccelerateHelper(midX, 0, stage().getH()));
		// 间距逐渐变小的 Welcome 列队
		for (int i = 0; i < 6; i++) {
			addWelcomes(stage().getW() * 0.4, stage().getW() * 0.6);
			Waits.wait(stage(), 5 + 45L / (i + 1));
		}
		Waits.wait(stage(), 20L);
		hideBoss();
	}

	private void largeEnemyApear() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 3; j++) {
				addWelcomes(Randoms.nextInt(stage().getIntW()));
				Waits.wait(stage(), 1L);
			}
			for (int j = 0; j < 3; j++) {
				randomAddFollowPlane();
				Waits.wait(stage(), 3L);
			}
		}
	}

	// Game Over 则不显示 Boss
	private void hideBoss() {
		while (!player.getHealth().isAlive()) {
			randomAddFollowPlane();
			Waits.wait(stage(), 30L);
		}
	}

	/**
	 * 添加一系列阶梯式 WelcomePlane
	 * @param num 梯数
	 * @param midX 中间 X 坐标
	 * @param horInterval 水平间距
	 * @param waitFrame 垂直等待帧数
	 */
	private void addLadderWelcome(int num, double midX, int horInterval, long waitFrame) {
		for (int i = 0; i < num; i++) {
			stage().addActor(drama.newWelcomePlane(midX - horInterval * i, 0));
			if (i > 0) {
				stage().addActor(drama.newWelcomePlane(midX + horInterval * i, 0));
			}
			Waits.wait(stage(), waitFrame);
		}
	}

	/**
	 * 添加若干个 x 坐标为指定值，y 坐标为 0 的 WelcomePlane
	 * @param xs
	 */
	private void addWelcomes(double... xs) {
		for (double x : xs) {
			stage().addActor(drama.newWelcomePlane(x, 0));
		}
	}

	private void addPlayer() {
		player = drama.newPlayerPlane(stage().getW() / 2, stage().getH() * 5 / 6);
		LifeCounter lifeCounter = drama.newLifeCounter(stage().getW() / 9, stage().getH() / 10 * 9, player);
		stage().addActor(player);
		stage().addActor(lifeCounter);
	}

	private void randomAddFollowPlane() {
		stage().addActor(drama.newFollowPlane(Randoms.nextInt(stage().getIntW()), 0));
	}

}