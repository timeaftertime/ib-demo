package cn.milai.ibdemo.drama;

import java.awt.Color;

import cn.milai.common.util.Randoms;
import cn.milai.ib.IBObject;
import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.character.explosion.Explosion;
import cn.milai.ib.component.BloodStrip;
import cn.milai.ib.component.LifeCounter;
import cn.milai.ib.component.text.TextLines;
import cn.milai.ib.container.ContainerEventListener;
import cn.milai.ib.container.DramaContainer;
import cn.milai.ib.container.ui.Audio;
import cn.milai.ib.drama.AbstractDrama;
import cn.milai.ib.util.StringUtil;
import cn.milai.ib.util.WaitUtil;
import cn.milai.ibdemo.character.UltraLight;
import cn.milai.ibdemo.character.helper.AccelerateHelper;
import cn.milai.ibdemo.character.helper.OneLifeHelper;
import cn.milai.ibdemo.character.plane.FollowPlane;
import cn.milai.ibdemo.character.plane.MissileBoss;
import cn.milai.ibdemo.character.plane.PlayerPlane;
import cn.milai.ibdemo.character.plane.WelcomePlane;

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

	private PlayerCharacter player;

	public UniverseBattle(AbstractDrama drama, DramaContainer container) {
		super(drama, container);
	}

	@Override
	public boolean doRun() {
		addPlayer();
		container().addEventListener(new ContainerEventListener() {
			@Override
			public void onObjectRemoved(IBObject obj) {
				if (player == obj) {
					container().stopAudio(Audio.BGM_CODE);
					stop();
				}
			}

			@Override
			public void onObjectAdded(IBObject obj) {
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
		MissileBoss boss = new MissileBoss(container().getWidth() / 2, 0, container());
		BloodStrip bossBloodStrip = new BloodStrip(
			container().getWidth() / 2, BLOOD_STRP_Y, container(), boss
		);
		WaitUtil.wait(container(), 10L);
		container().addObject(boss);
		container().addObject(bossBloodStrip);
		while (boss.isAlive()) {
			randomAddFollowPlane();
			container().addObject(
				new OneLifeHelper(
					Randoms.nextInt(container().getWidth()), 0,
					container()
				)
			);
			WaitUtil.wait(container(), 100L);
			randomAddFollowPlane();
			WaitUtil.wait(container(), 100L);
		}
		container().removeObject(bossBloodStrip);
		afterBoss();
		return player.isAlive();
	}

	private void showBGMInfo() {
		TextLines bgmInfo = new TextLines(
			0, 0, container(),
			StringUtil.lines(drama.str("bgm_info")), Color.BLACK, 10L, 40L, 10L
		);
		bgmInfo.setX(container().getWidth() - 1 - bgmInfo.getWidth());
		bgmInfo.setY(container().getHeight() - 1 - bgmInfo.getHeight());
		container().addObject(bgmInfo);
	}

	private void afterBoss() {
		container().addObject(new AccelerateHelper(container().getWidth() / 2, 0, container()));
		largeEnemyApear();
		UltraLight light = new UltraLight(player, 25L);
		container().playAudio(drama.audio("ULTRAMAN", "/audio/ultraman.mp3"));
		container().addObject(light);
		WaitUtil.waitRemove(light, 5L);
	}

	private void beforeBoss() {
		// 阶梯 Welcome
		int midX = container().getWidth() / 2;
		int interval = 25;
		addLadderWelcome(8, midX, interval, 20L);
		// Welcome Follow 混合
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j <= i; j++) {
				randomAddFollowPlane();
				WaitUtil.wait(container(), 10L);
			}
			WaitUtil.wait(container(), 25L);
			addWelcomes(
				(int) (container().getWidth() * 0.1),
				(int) (container().getWidth() * 0.3),
				(int) (container().getWidth() * 0.5),
				(int) (container().getWidth() * 0.7),
				(int) (container().getWidth() * 0.9)
			);
		}
		WaitUtil.wait(container(), 30L);
		hideBoss();
		// 双重阶梯 Welcome
		for (int i = 0; i < 8; i++) {
			container().addObject(new WelcomePlane(midX - interval * i, 0, container()));
			if (i > 0) {
				container().addObject(new WelcomePlane(midX + interval * i, 0, container()));
			}
			WaitUtil.wait(container(), 10L);
			container().addObject(new WelcomePlane(midX - interval * i, 0, container()));
			if (i > 0) {
				container().addObject(new WelcomePlane(midX + interval * i, 0, container()));
			}
			WaitUtil.wait(container(), 20L);
		}
		container().addObject(new AccelerateHelper(midX, 0, container()));
		// 间距逐渐变小的 Welcome 列队
		for (int i = 0; i < 6; i++) {
			addWelcomes((int) (container().getWidth() * 0.4), (int) (container().getWidth() * 0.6));
			WaitUtil.wait(container(), 5 + 60L / (i + 1));
		}
		WaitUtil.wait(container(), 10L);
		hideBoss();
	}

	private void largeEnemyApear() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 3; j++) {
				addWelcomes(Randoms.nextInt(container().getWidth()));
				WaitUtil.wait(container(), 1L);
			}
			for (int j = 0; j < 3; j++) {
				randomAddFollowPlane();
				WaitUtil.wait(container(), 3L);
			}
		}
	}

	// Game Over 则不显示 Boss
	private void hideBoss() {
		while (!player.isAlive()) {
			randomAddFollowPlane();
			WaitUtil.wait(container(), 30L);
		}
	}

	/**
	 * 添加一系列阶梯式 WelcomePlane
	 * @param num 梯数
	 * @param midX 中间 X 坐标
	 * @param horInterval 水平间距
	 * @param waitFrame 垂直等待帧数
	 */
	private void addLadderWelcome(int num, int midX, int horInterval, long waitFrame) {
		for (int i = 0; i < num; i++) {
			container().addObject(new WelcomePlane(midX - horInterval * i, 0, container()));
			if (i > 0) {
				container().addObject(new WelcomePlane(midX + horInterval * i, 0, container()));
			}
			WaitUtil.wait(container(), waitFrame);
		}
	}

	/**
	 * 添加若干个 x 坐标为指定值，y 坐标为 0 的 WelcomePlane
	 * @param xs
	 */
	private void addWelcomes(int... xs) {
		for (int x : xs) {
			container().addObject(new WelcomePlane(x, 0, container()));
		}
	}

	private void addPlayer() {
		player = new PlayerPlane(
			container().getWidth() / 2,
			container().getHeight() * 5 / 6,
			container()
		);
		LifeCounter lifeCounter = new LifeCounter(
			container().getWidth() / 10,
			container().getHeight() / 10 * 9,
			container(), player
		);
		container().addObject(player);
		container().addObject(lifeCounter);
	}

	private void randomAddFollowPlane() {
		container().addObject(
			new FollowPlane(
				Randoms.nextInt(container().getWidth()), 0, container()
			)
		);
	}

}