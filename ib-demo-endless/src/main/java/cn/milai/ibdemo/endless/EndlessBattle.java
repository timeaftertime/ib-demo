package cn.milai.ibdemo.endless;

import java.util.List;

import cn.milai.common.base.Randoms;
import cn.milai.ib.container.Container;
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
 * 无尽模式游戏流程
 * @author milai
 * @date 2022.05.03
 */
public class EndlessBattle implements Runnable {

	private static final String DRAMA_CODE = "cn.milai.ibdemo.endless.EndlessBattleMode";

	private static final String BOMB_CODE = "BOMB";

	private Image BGI;

	private int maxEnemyNum = GameConf.INIT_MAX_ENEMY_NUM;
	private int maxBulletNum = 5;
	private int preGameScore = GameConf.INIT_LEVEL_UP_GAME_SCORE;
	private long lastLevelUpTime;
	private long addNormalEnemyInterval = GameConf.INIT_ADD_NORMAL_ENEMEY_FRAMES;
	private PlayerPlane player;
	private String formTitle;
	private int playerScore;

	protected Stage stage;
	private Stage stageCursor;

	public EndlessBattle(Stage stage) {
		this.stage = stageCursor = stage;
		BGI = ImageLoader.load(DRAMA_CODE, GameConf.IMG_BACKGROUD_FILE);
		AudioLoader.load(Audio.BGM_CODE, DRAMA_CODE, GameConf.AUDIO_BG_FILE);
	}

	private Stage nowStage() {
		if (stageCursor == null) {
			throw new RestartInterrupt();
		}
		return stageCursor;
	}

	private void onRoleAdded(Container c, Role r) {
		if (r instanceof Explosion) {
			nowStage().playAudio(AudioLoader.load(BOMB_CODE, DRAMA_CODE, GameConf.AUDIO_BOMB_FILE));
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
	public void run() {
		stage.resize(GameConf.WIDTH, GameConf.HEIGHT);
		stage.addItemListener(
			ContainerListeners.roleListener(
				EndlessBattle.this::onRoleAdded,
				EndlessBattle.this::onRolesRemoved
			)
		);
		while (true) {
			try {
				initGame();
				addWelComePlayer();
				if (playerScore >= 29) {
					addPlayerScore(30);
				}
				while (true) {
					if (nowStage().getAll(FollowPlane.class).size() < maxEnemyNum) {
						randomAddEnemy();
						checkLevelUp();
					}
				}
			} catch (RestartInterrupt e) {

			}
		}
	}

	private void initGame() {
		// 重新开始时
		if (stageCursor == null) {
			stageCursor = stage;
		} else {
			nowStage().setBackgroud(BGI);
		}
		player = DemoFactory.newPlayerPlane(nowStage().getW() / 2, nowStage().getH() * 0.93);
		formTitle = GameConf.TITLE_PREFIX;
		playerScore = 0;
		refreshFormTitle();
		nowStage().addObject(player);
		nowStage().addObject(new PauseSwitcher());

		nowStage().playAudio(AudioLoader.load(Audio.BGM_CODE, DRAMA_CODE, GameConf.AUDIO_BG_FILE));
	}

	private void addWelComePlayer() {
		addVerticalWelcomePlayer(5, 70);
		addVerticalWelcomePlayer(5, 140);
		addLadderWelcomePlayer(5, 25);
		Waits.wait(nowStage(), 42);
	}

	private void addVerticalWelcomePlayer(int row, int disFromCenter) {
		if (row < 1)
			throw new IllegalArgumentException("行数必须大于等于 1 ：" + row);
		for (int i = 0; i < row; i++) {
			nowStage().addObject(newWelcomePlane(nowStage().getW() / 2 - disFromCenter, 0));
			nowStage().addObject(newWelcomePlane(nowStage().getW() / 2 + disFromCenter, 0));
			Waits.wait(nowStage(), GameConf.ADD_VERTICAL_WELCOME_PLANE_FRAMES);
		}
	}

	private void addLadderWelcomePlayer(int row, int disOfX) {
		if (row < 1)
			throw new IllegalArgumentException("行数必须大于等于 1 ：" + row);
		nowStage().addObject(newWelcomePlane(nowStage().getW() / 2, 0));
		Waits.wait(nowStage(), GameConf.ADD_LADDER_WELCOME_PLANE_FRAMES);
		for (int i = 2; i <= row; i++) {
			nowStage().addObject(newWelcomePlane(nowStage().getW() / 2 - i * disOfX, 0));
			nowStage().addObject(newWelcomePlane(nowStage().getW() / 2 + i * disOfX, 0));
			Waits.wait(nowStage(), GameConf.ADD_LADDER_WELCOME_PLANE_FRAMES);
		}
	}

	private void checkLevelUp() {
		if (playerScore >= preGameScore + GameConf.LEVEL_UP_SCORE_INTERVAL)
			levelUp();
		if (nowStage().getFrame() - lastLevelUpTime > GameConf.LEVEL_UP_FRAMES)
			levelUp();
	}

	private void levelUp() {
		maxEnemyNum++;
		preGameScore += GameConf.LEVEL_UP_SCORE_INTERVAL;

		int playerBulletNum = maxBulletNum + 1;
		if (playerBulletNum > GameConf.MAX_PLAYER_BULLET_NUM)
			playerBulletNum = GameConf.MAX_PLAYER_BULLET_NUM;
		player.setShooter(new BlueShooter(3, playerBulletNum, player));

		addNormalEnemyInterval = addNormalEnemyInterval * 2 / 3;
		if (addNormalEnemyInterval < GameConf.MIN_ADD_ENEMEY_FRAMES)
			addNormalEnemyInterval = GameConf.MIN_ADD_ENEMEY_FRAMES;

		lastLevelUpTime = nowStage().getFrame();
	}

	private void randomAddEnemy() {
		if (Randoms.nextLess(GameConf.ADD_ENEMY_CHANCE)) {
			nowStage().addObject(DemoFactory.newFollowPlane(Randoms.nextInt(nowStage().getIntW()), 0));
			Waits.wait(nowStage(), addNormalEnemyInterval);
		}
	}

	private void refreshFormTitle() {
		nowStage().fire(
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
		nowStage().fire(MediaPlugin.class, m -> m.stopAudio(Audio.BGM_CODE));
		showGameOverLabel();
		showRestartButton();
	}

	private void showGameOverLabel() {
		GameOverLabel gameOverLabel = DemoFactory.newGameOverLabel(
			GameConf.GAME_OVER_LABEL_POS_X, GameConf.GAME_OVER_LABEL_POS_Y
		);
		nowStage().addObject(gameOverLabel);
	}

	private void showRestartButton() {
		nowStage().addObject(
			DemoFactory.newRestartButton(GameConf.RESTART_BUTTON_POS_X, GameConf.RESTART_BUTTON_POS_Y, () -> {
				try {
					Stage nowStage = nowStage();
					EndlessBattle.this.stageCursor = null;
					nowStage.reset();
				} catch (RestartInterrupt e) {
				}
			})
		);
	}

	private WelcomePlane newWelcomePlane(double x, double y) {
		return DemoFactory.newWelcomePlane(x, y);
	}

	@SuppressWarnings("serial")
	private static class RestartInterrupt extends RuntimeException {
	}

}
