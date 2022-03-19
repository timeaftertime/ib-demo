package cn.milai.ibdemo.endless;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.milai.common.base.Randoms;
import cn.milai.ib.IBBeans;
import cn.milai.ib.container.CloseableContainer;
import cn.milai.ib.container.Container;
import cn.milai.ib.container.ContainerClosedException;
import cn.milai.ib.container.Stage;
import cn.milai.ib.container.Waits;
import cn.milai.ib.container.listener.ContainerListeners;
import cn.milai.ib.container.plugin.control.PauseSwitcher;
import cn.milai.ib.container.plugin.media.Audio;
import cn.milai.ib.container.plugin.media.MediaPlugin;
import cn.milai.ib.container.plugin.ui.Image;
import cn.milai.ib.container.plugin.ui.UIPlugin;
import cn.milai.ib.control.GameOverLabel;
import cn.milai.ib.loader.AudioLoader;
import cn.milai.ib.loader.ImageLoader;
import cn.milai.ib.mode.AbstractGameMode;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.property.Score;
import cn.milai.ib.role.weapon.bullet.Bullet;
import cn.milai.ibdemo.role.bullet.shooter.BlueShooter;
import cn.milai.ibdemo.role.plane.FollowPlane;
import cn.milai.ibdemo.role.plane.PlayerPlane;
import cn.milai.ibdemo.role.plane.WelcomePlane;
import cn.milai.ibdemo.util.DemoFactory;

/**
 * 无尽模式
 * @author milai
 */
@Order(100)
@Component
public class EndlessBattleMode extends AbstractGameMode {

	private static final Logger LOG = LoggerFactory.getLogger(EndlessBattleMode.class);

	private static final String TITLE_PREFIX = "敌星弹雨 - 无尽模式";

	private static final String AUDIO_BG_FILE = "/audio/bg.mp3";

	private static final String IMG_BACKGROUD_FILE = "/img/backgroud.gif";

	private static final String AUDIO_BOMB_FILE = "/audio/bomb.mp3";

	private static final String DRAMA_CODE = EndlessBattleMode.class.getName();

	private static final String BOMB_CODE = "BOMB";

	/**
	 * 默认宽度
	 */
	private static final int WIDTH = 554;

	/**
	 * 默认高度
	 */
	private static final int HEIGHT = 689;

	private static final int INIT_LEVEL_UP_GAME_SCORE = 30;
	private static final int MAX_PLAYER_BULLET_NUM = 10;
	private static final int INIT_MAX_ENEMY_NUM = 5;
	private static final int LEVEL_UP_SCORE_INTERVAL = 10;
	public static final int GAME_OVER_LABEL_POS_X = 280;
	public static final int GAME_OVER_LABEL_POS_Y = 216;
	public static final int RESTART_BUTTON_POS_X = 273;
	public static final int RESTART_BUTTON_POS_Y = 403;

	/**
	 * 最短添加新敌机的时间间隔
	 */
	private static final long MIN_ADD_ENEMEY_FRAMES = 1;

	/**
	 * 初始添加新敌机的时间间隔
	 */
	private static final int INIT_ADD_NORMAL_ENEMEY_FRAMES = 20;

	private static final long LEVEL_UP_FRAMES = 200;

	private static final long ADD_LADDER_WELCOME_PLANE_FRAMES = 24;
	private static final long ADD_VERTICAL_WELCOME_PLANE_FRAMES = 16;
	private static final double ADD_ENEMY_CHANCE = 0.2;

	private static final String THREAD_NAME = "EndlessBattle";

	private Image BGI;
	private Audio BGM;

	private int maxEnemyNum = INIT_MAX_ENEMY_NUM;
	private int maxBulletNum = 5;
	private int preGameScore = INIT_LEVEL_UP_GAME_SCORE;
	private long lastLevelUpTime;
	private long addNormalEnemyInterval = INIT_ADD_NORMAL_ENEMEY_FRAMES;
	private PlayerPlane player;
	private String formTitle;
	private int playerScore;

	protected Stage stage;

	@Override
	public String name() {
		return "无尽模式";
	}

	private WelcomePlane newWelcomePlane(double x, double y) {
		return DemoFactory.newWelcomePlane(x, y);
	}

	private Stage container() {
		if (stage == null) {
			throw new RestartInterrupt();
		}
		return stage;
	}

	private void onRoleAdded(Container c, Role r) {
		if (r instanceof Explosion) {
			container().fire(
				MediaPlugin.class, m -> m.playAudio(AudioLoader.load(BOMB_CODE, DRAMA_CODE, AUDIO_BOMB_FILE))
			);
		}
	}

	private void onRolesRemoved(Container c, List<Role> rs) {
		for (Role r : rs) {
			if (r.getHealth().isAlive()) {
				continue;
			}
			if (r == player) {
				gameOver();
				return;
			}
			if (r.hasProperty(Score.class)) {
				Score s = r.getProperty(Score.class);
				Role lastAttacker = r.getHealth().lastAttacker();
				if (lastAttacker instanceof Bullet) {
					if (((Bullet) lastAttacker).getOwner() == player) {
						addPlayerScore(s.getValue());
					}
				}
			}
			refreshFormTitle();
		}
	}

	@Override
	public void init() {
		BGI = ImageLoader.load(DRAMA_CODE, IMG_BACKGROUD_FILE);
	}

	private void initContainer() {
		stage = IBBeans.getBean(Stage.class);
		if (!container().isRunning()) {
			container().start();
			container().addItemListener(
				ContainerListeners.roleListener(
					EndlessBattleMode.this::onRoleAdded,
					EndlessBattleMode.this::onRolesRemoved
				)
			);
		} else {
			container().reset();
		}

		container().resize(WIDTH, HEIGHT);
	}

	@Override
	public void run() {
		Thread gameController = new Thread(new GameControl(), THREAD_NAME);
		gameController.setDaemon(true);
		gameController.start();
	}

	private class GameControl implements Runnable {

		private void initGame() {
			container().setBackgroud(BGI);
			player = DemoFactory.newPlayerPlane(container().getW() / 2, container().getH() * 0.93);
			formTitle = TITLE_PREFIX;
			playerScore = 0;
			refreshFormTitle();
			container().addObject(player);
			container().addObject(new PauseSwitcher());

			BGM = AudioLoader.load(Audio.BGM_CODE, DRAMA_CODE, AUDIO_BG_FILE);
			container().fire(MediaPlugin.class, m -> m.playAudio(BGM));
		}

		@Override
		public void run() {
			try {
				restartableRun();
			} catch (ContainerClosedException e) {
				LOG.debug("容器已关闭: {}", e);
			}
		}

		void restartableRun() {
			while (true) {
				try {
					initGame();

					addWelComePlayer();
					// 消灭所有欢迎机则奖励分数
					if (playerScore >= 29) {
						addPlayerScore(30);
					}
					while (true) {
						if (container().getAll(FollowPlane.class).size() < maxEnemyNum) {
							randomAddEnemy();
							checkLevelUp();
						}
					}
				} catch (RestartInterrupt e) {
					initContainer();
				}
			}
		}

		private void addWelComePlayer() {
			addVerticalWelcomePlayer(5, 70);
			addVerticalWelcomePlayer(5, 140);
			addLadderWelcomePlayer(5, 25);
			Waits.wait(container(), 42);
		}

		private void addVerticalWelcomePlayer(int row, int disFromCenter) {
			if (row < 1)
				throw new IllegalArgumentException("行数必须大于等于 1 ：" + row);
			for (int i = 0; i < row; i++) {
				container().addObject(newWelcomePlane(container().getW() / 2 - disFromCenter, 0));
				container().addObject(newWelcomePlane(container().getW() / 2 + disFromCenter, 0));
				Waits.wait(container(), ADD_VERTICAL_WELCOME_PLANE_FRAMES);
			}
		}

		private void addLadderWelcomePlayer(int row, int disOfX) {
			if (row < 1)
				throw new IllegalArgumentException("行数必须大于等于 1 ：" + row);
			container().addObject(newWelcomePlane(container().getW() / 2, 0));
			Waits.wait(container(), ADD_LADDER_WELCOME_PLANE_FRAMES);
			for (int i = 2; i <= row; i++) {
				container().addObject(newWelcomePlane(container().getW() / 2 - i * disOfX, 0));
				container().addObject(newWelcomePlane(container().getW() / 2 + i * disOfX, 0));
				Waits.wait(container(), ADD_LADDER_WELCOME_PLANE_FRAMES);
			}
		}

		private void checkLevelUp() {
			if (playerScore >= preGameScore + LEVEL_UP_SCORE_INTERVAL)
				levelUp();
			if (container().getFrame() - lastLevelUpTime > LEVEL_UP_FRAMES)
				levelUp();
		}

		private void levelUp() {
			maxEnemyNum++;
			preGameScore += LEVEL_UP_SCORE_INTERVAL;

			int playerBulletNum = maxBulletNum + 1;
			if (playerBulletNum > MAX_PLAYER_BULLET_NUM)
				playerBulletNum = MAX_PLAYER_BULLET_NUM;
			player.setShooter(new BlueShooter(3, playerBulletNum, player));

			addNormalEnemyInterval = addNormalEnemyInterval * 2 / 3;
			if (addNormalEnemyInterval < MIN_ADD_ENEMEY_FRAMES)
				addNormalEnemyInterval = MIN_ADD_ENEMEY_FRAMES;

			lastLevelUpTime = container().getFrame();
		}

		private void randomAddEnemy() {
			if (Randoms.nextLess(ADD_ENEMY_CHANCE)) {
				container().addObject(DemoFactory.newFollowPlane(Randoms.nextInt(container().getIntW()), 0));
				Waits.wait(container(), addNormalEnemyInterval);
			}
		}
	}

	private void refreshFormTitle() {
		container().fire(
			UIPlugin.class, f -> f.setTitle(
				formTitle + "         得分：" + playerScore + "      生命：" + player.getHealth().getHP()
			)
		);
	}

	/**
	 * 线程安全地添加玩家分数
	 * @param score
	 */
	protected synchronized void addPlayerScore(int score) {
		playerScore += score;
	}

	private void gameOver() {
		container().fire(MediaPlugin.class, m -> m.stopAudio(Audio.BGM_CODE));
		showGameOverLabel();
		showRestartButton();
	}

	private void showGameOverLabel() {
		GameOverLabel gameOverLabel = DemoFactory.newGameOverLabel(GAME_OVER_LABEL_POS_X, GAME_OVER_LABEL_POS_Y);
		container().addObject(gameOverLabel);
	}

	private void showRestartButton() {
		container().addObject(DemoFactory.newRestartButton(RESTART_BUTTON_POS_X, RESTART_BUTTON_POS_Y, new Runnable() {
			@Override
			public void run() {
				try {
					CloseableContainer c = container();
					EndlessBattleMode.this.stage = null;
					c.close();
				} catch (RestartInterrupt e) {
					LOG.debug("已经重启: {}", e);
				}
			}
		}));
	}

	@SuppressWarnings("serial")
	private static class RestartInterrupt extends RuntimeException {
	}
}
