package cn.milai.ibdemo.story;

import java.util.List;

import cn.milai.common.base.Randoms;
import cn.milai.ib.container.Container;
import cn.milai.ib.container.Stage;
import cn.milai.ib.container.Waits;
import cn.milai.ib.container.listener.ItemListener;
import cn.milai.ib.container.plugin.media.Audio;
import cn.milai.ib.control.BloodStrip;
import cn.milai.ib.control.LifeCounter;
import cn.milai.ib.control.text.TextLines;
import cn.milai.ib.item.Item;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.explosion.Explosion;
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
		container().addItemListener(new ItemListener() {
			@Override
			public void onRemoved(Container container, List<Item> objs) {
				for (Item obj : objs) {
					if (player == obj) {
						container().stopAudio(Audio.BGM_CODE);
						stop();
					}
				}
			}

			@Override
			public void onAdded(Container container, Item obj) {
				if (obj instanceof Explosion) {
					try {
						container().playAudio(drama.audio(BOMB_CODE, AUDIO_BOMB_FILE));
					} catch (BattleStoppedException e) {
					}
				}
			}
		});
		container().playAudio(drama.audio(Audio.BGM_CODE, BATTLE_BGM));
		showBGMInfo();
		beforeBoss();
		MissileBoss boss = drama.newMissileBoss(container().getW() / 2, 0);
		BloodStrip bossBloodStrip = drama.newBloodStrip(container().getW() / 2, BLOOD_STRP_Y, boss);
		Waits.wait(container(), 10L);
		container().addObject(boss);
		container().addObject(bossBloodStrip);
		while (boss.getHealth().isAlive()) {
			randomAddFollowPlane();
			container().addObject(
				drama.newOneLifeHelper(Randoms.nextInt(container().getIntW()), 0, container().getH())
			);
			Waits.wait(container(), 100L);
			randomAddFollowPlane();
			Waits.wait(container(), 100L);
		}
		container().removeObject(bossBloodStrip);
		afterBoss();
		return player.getHealth().isAlive();
	}

	private void showBGMInfo() {
		TextLines bgmInfo = drama.newTextLines(10L, 40L, 10L, drama.str("bgm_info").split("\n"));
		bgmInfo.setX(container().getW() - 1 - bgmInfo.getIntW());
		bgmInfo.setY(container().getH() - 1 - bgmInfo.getIntH());
		container().addObject(bgmInfo);
	}

	private void afterBoss() {
		container().addObject(drama.newAccelerateHelper(container().getW() / 2, 0, container().getH()));
		largeEnemyApear();
		UltraLight light = drama.newUltraLight(player, 20L);
		container().playAudio(drama.audio("ULTRAMAN", "/audio/ultraman.mp3"));
		container().addObject(light);
		Waits.waitRemove(light, 5L);
	}

	private void beforeBoss() {
		// 阶梯 Welcome
		double midX = container().getW() / 2;
		int interval = 20;
		addLadderWelcome(6, midX, interval, 20L);
		// Welcome Follow 混合
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j <= 3; j++) {
				randomAddFollowPlane();
				Waits.wait(container(), 10L);
			}
			Waits.wait(container(), 23L);
			addWelcomes(
				container().getW() * 0.1,
				container().getW() * 0.3,
				container().getW() * 0.5,
				container().getW() * 0.7,
				container().getW() * 0.9
			);
		}
		Waits.wait(container(), 70L);
		hideBoss();
		// 双重阶梯 Welcome
		for (int i = 0; i < 8; i++) {
			container().addObject(drama.newWelcomePlane(midX - interval * i, 0));
			if (i > 0) {
				container().addObject(drama.newWelcomePlane(midX + interval * i, 0));
			}
			Waits.wait(container(), 10L);
			container().addObject(drama.newWelcomePlane(midX - interval * i, 0));
			if (i > 0) {
				container().addObject(drama.newWelcomePlane(midX + interval * i, 0));
			}
			Waits.wait(container(), 20L);
		}
		container().addObject(drama.newAccelerateHelper(midX, 0, container().getH()));
		// 间距逐渐变小的 Welcome 列队
		for (int i = 0; i < 6; i++) {
			addWelcomes(container().getW() * 0.4, container().getW() * 0.6);
			Waits.wait(container(), 5 + 45L / (i + 1));
		}
		Waits.wait(container(), 20L);
		hideBoss();
	}

	private void largeEnemyApear() {
		for (int i = 0; i < 18; i++) {
			for (int j = 0; j < 3; j++) {
				addWelcomes(Randoms.nextInt(container().getIntW()));
				Waits.wait(container(), 1L);
			}
			for (int j = 0; j < 3; j++) {
				randomAddFollowPlane();
				Waits.wait(container(), 3L);
			}
		}
	}

	// Game Over 则不显示 Boss
	private void hideBoss() {
		while (!player.getHealth().isAlive()) {
			randomAddFollowPlane();
			Waits.wait(container(), 30L);
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
			container().addObject(drama.newWelcomePlane(midX - horInterval * i, 0));
			if (i > 0) {
				container().addObject(drama.newWelcomePlane(midX + horInterval * i, 0));
			}
			Waits.wait(container(), waitFrame);
		}
	}

	/**
	 * 添加若干个 x 坐标为指定值，y 坐标为 0 的 WelcomePlane
	 * @param xs
	 */
	private void addWelcomes(double... xs) {
		for (double x : xs) {
			container().addObject(drama.newWelcomePlane(x, 0));
		}
	}

	private void addPlayer() {
		player = drama.newPlayerPlane(container().getW() / 2, container().getH() * 5 / 6);
		LifeCounter lifeCounter = drama.newLifeCounter(container().getW() / 9, container().getH() / 10 * 9, player);
		container().addObject(player);
		container().addObject(lifeCounter);
	}

	private void randomAddFollowPlane() {
		container().addObject(drama.newFollowPlane(Randoms.nextInt(container().getIntW()), 0));
	}

}