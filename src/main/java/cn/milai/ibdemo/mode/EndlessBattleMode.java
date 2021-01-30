package cn.milai.ibdemo.mode;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.milai.common.util.Randoms;
import cn.milai.ib.IBObject;
import cn.milai.ib.character.IBCharacter;
import cn.milai.ib.character.explosion.Explosion;
import cn.milai.ib.character.property.HasScore;
import cn.milai.ib.character.weapon.bullet.Bullet;
import cn.milai.ib.component.GameOverLabel;
import cn.milai.ib.component.RestartButton;
import cn.milai.ib.conf.SystemConf;
import cn.milai.ib.container.ContainerEventListener;
import cn.milai.ib.container.lifecycle.ContainerClosedException;
import cn.milai.ib.container.ui.Audio;
import cn.milai.ib.container.ui.Image;
import cn.milai.ib.container.ui.form.BattleFormContainer;
import cn.milai.ib.container.ui.form.FormContainer;
import cn.milai.ib.ex.IBException;
import cn.milai.ib.loader.AudioLoader;
import cn.milai.ib.loader.ImageLoader;
import cn.milai.ib.mode.AbstractGameMode;
import cn.milai.ib.util.WaitUtil;
import cn.milai.ibdemo.character.bullet.shooter.BlueShooter;
import cn.milai.ibdemo.character.plane.FollowPlane;
import cn.milai.ibdemo.character.plane.PlayerPlane;
import cn.milai.ibdemo.character.plane.WelcomePlane;

/**
 * 无尽模式
 * @author milai
 */
@Order(100)
@Component
public class EndlessBattleMode extends AbstractGameMode implements ContainerEventListener {

	private static final String AUDIO_BG_FILE = "/audio/bg.mp3";

	private static final String IMG_BACKGROUD_FILE = "/img/backgroud.gif";

	private static final String AUDIO_BOMB_FILE = "/audio/bomb.mp3";

	private static final String DRAMA_CODE = "cn.milai.ibdemo.mode.EndlessBattleMode";

	private static final String BOMB_CODE = "BOMB";

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
	protected FormContainer form;

	@Override
	public String name() {
		return "无尽模式";
	}

	@Override
	public void init() {
		form = new BattleFormContainer();
		form.addEventListener(this);
		player = new PlayerPlane(form.getWidth() / 2, (int) (form.getHeight() * 0.93), form);
		playerScore = 0;
		formTitle = form.getTitle();
		BGI = ImageLoader.load(DRAMA_CODE, IMG_BACKGROUD_FILE);
		BGM = AudioLoader.load(Audio.BGM_CODE, DRAMA_CODE, AUDIO_BG_FILE);
		refreshFormTitle();
	}

	@Override
	public void run() {
		form.start();
		form.addObject(player);
		Thread gameController = new Thread(new GameControl(), THREAD_NAME);
		gameController.setDaemon(true);
		gameController.start();
	}

	private class GameControl implements Runnable {

		@Override
		public void run() {
			try {
				form.playAudio(BGM);
				form.setBackgroud(BGI);
				addWelComePlayer();
				// 消灭所有欢迎机则奖励分数
				if (playerScore >= 29) {
					addPlayerScore(30);
				}
				while (true) {
					if (form.getAll(FollowPlane.class).size() < maxEnemyNum) {
						randomAddEnemy();
						checkLevelUp();
					}
				}
			} catch (ContainerClosedException e) {
				// 容器关闭，退出游戏
			}
		}

		private void addWelComePlayer() {
			addVerticalWelcomePlayer(5, 70);
			addVerticalWelcomePlayer(5, 140);
			addLadderWelcomePlayer(5, 25);
			WaitUtil.wait(form, 42);
		}

		private void addVerticalWelcomePlayer(int row, int disFromCenter) {
			if (row < 1)
				throw new IllegalArgumentException("行数必须大于等于 1 ：" + row);
			for (int i = 0; i < row; i++) {
				form.addObject(new WelcomePlane(form.getWidth() / 2 - disFromCenter, 0, form));
				form.addObject(new WelcomePlane(form.getWidth() / 2 + disFromCenter, 0, form));
				WaitUtil.wait(form, ADD_VERTICAL_WELCOME_PLANE_FRAMES);
			}
		}

		private void addLadderWelcomePlayer(int row, int disOfX) {
			if (row < 1)
				throw new IllegalArgumentException("行数必须大于等于 1 ：" + row);
			form.addObject(new WelcomePlane(form.getWidth() / 2, 0, form));
			WaitUtil.wait(form, ADD_LADDER_WELCOME_PLANE_FRAMES);
			for (int i = 2; i <= row; i++) {
				form.addObject(new WelcomePlane(form.getWidth() / 2 - i * disOfX, 0, form));
				form.addObject(new WelcomePlane(form.getWidth() / 2 + i * disOfX, 0, form));
				WaitUtil.wait(form, ADD_LADDER_WELCOME_PLANE_FRAMES);
			}
		}

		private void checkLevelUp() {
			if (playerScore >= preGameScore + LEVEL_UP_SCORE_INTERVAL)
				levelUp();
			if (form.getFrame() - lastLevelUpTime > LEVEL_UP_FRAMES)
				levelUp();
		}

		private void levelUp() {
			maxEnemyNum++;
			preGameScore += LEVEL_UP_SCORE_INTERVAL;

			int playerBulletNum = maxBulletNum + 1;
			if (playerBulletNum > MAX_PLAYER_BULLET_NUM)
				playerBulletNum = MAX_PLAYER_BULLET_NUM;
			player.setShooter(
				new BlueShooter(
					SystemConf.intProp(player.getClass(), PlayerPlane.P_SHOOT_INTERVAL), maxBulletNum, player
				)
			);

			addNormalEnemyInterval = addNormalEnemyInterval * 2 / 3;
			if (addNormalEnemyInterval < MIN_ADD_ENEMEY_FRAMES)
				addNormalEnemyInterval = MIN_ADD_ENEMEY_FRAMES;

			lastLevelUpTime = form.getFrame();
		}

		private void randomAddEnemy() {
			if (Randoms.nextLess(ADD_ENEMY_CHANCE)) {
				form.addObject(new FollowPlane(Randoms.nextInt(form.getWidth()), 0, form));
				WaitUtil.wait(form, addNormalEnemyInterval);
			}
		}
	}

	@Override
	public void onObjectAdded(IBObject obj) {
		if (obj instanceof Explosion) {
			form.playAudio(AudioLoader.load(BOMB_CODE, DRAMA_CODE, AUDIO_BOMB_FILE));
		}
	}

	@Override
	public void onObjectRemoved(IBObject obj) {
		if (obj == player) {
			gameOver();
		} else {
			if (obj instanceof HasScore) {
				HasScore hasScore = ((HasScore) obj);
				IBCharacter lastAttacker = hasScore.getLastAttacker();
				if (lastAttacker instanceof Bullet) {
					if (((Bullet) lastAttacker).getOwner() == player) {
						addPlayerScore(hasScore.getScore());
					}
				}
			}
			refreshFormTitle();
		}
	}

	private void refreshFormTitle() {
		form.setTitle(formTitle + "         得分：" + playerScore + "      生命：" + player.getLife());
	}

	/**
	 * 线程安全地添加玩家分数
	 * @param score
	 */
	protected synchronized void addPlayerScore(int score) {
		playerScore += score;
	}

	private void gameOver() {
		form.stopAudio(Audio.BGM_CODE);
		showGameOverLabel();
		showRestartButton();
	}

	private void showGameOverLabel() {
		GameOverLabel gameOverLabel = new GameOverLabel(GAME_OVER_LABEL_POS_X, GAME_OVER_LABEL_POS_Y, form);
		form.addObject(gameOverLabel);
	}

	private void showRestartButton() {
		form.addObject(new RestartButton(RESTART_BUTTON_POS_X, RESTART_BUTTON_POS_Y, form, () -> {
			form.close();
			try {
				EndlessBattleMode.class.newInstance().start();
			} catch (InstantiationException | IllegalAccessException e1) {
				throw new IBException("创建 EndlessBattleMode 实例失败");
			}
		}));
	}

}
