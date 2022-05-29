package cn.milai.ibdemo.endless;

import cn.milai.common.base.Randoms;
import cn.milai.ib.actor.Actor;
import cn.milai.ib.loader.AudioLoader;
import cn.milai.ib.loader.ImageLoader;
import cn.milai.ib.plugin.audio.Audio;
import cn.milai.ib.plugin.audio.AudioCrew;
import cn.milai.ib.plugin.control.PauseSwitcher;
import cn.milai.ib.plugin.ui.Image;
import cn.milai.ib.plugin.ui.UICrew;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.role.nature.Score;
import cn.milai.ib.role.weapon.bullet.Bullet;
import cn.milai.ib.stage.Stage;
import cn.milai.ib.stage.Waits;
import cn.milai.ib.stage.event.AddActorEvent;
import cn.milai.ib.stage.event.RemoveActorEvent;
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

	private void onRoleAdded(AddActorEvent e) {
		if (e.actor() instanceof Explosion) {
			nowStage().playAudio(AudioLoader.load(BOMB_CODE, DRAMA_CODE, GameConf.AUDIO_BOMB_FILE));
		}
	}

	private void onRolesRemoved(RemoveActorEvent e) {
		for (Actor actor : e.actors()) {
			if (!(actor instanceof Role)) {
				continue;
			}
			Role r = (Role) actor;
			if (r.getHealth().isAlive()) {
				continue;
			}
			if (r == player) {
				gameOver();
				return;
			}
			if (r.hasNature(Score.NAME)) {
				Score s = r.getNature(Score.NAME);
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
		stage.onAddActor().subscribe(this::onRoleAdded);
		stage.onRemoveActor().subscribe(this::onRolesRemoved);
		stage.onClosed().subscribe(e -> stageCursor = null);
		while (!stage.lifecycle().isClosed()) {
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
		nowStage()
			.addActor(player)
			.addActor(new PauseSwitcher())
			.playAudio(AudioLoader.load(Audio.BGM_CODE, DRAMA_CODE, GameConf.AUDIO_BG_FILE));
		Waits.wait(nowStage(), 1);
		refreshFormTitle();
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
			nowStage()
				.addActor(newWelcomePlane(nowStage().getW() / 2 - disFromCenter, 0))
				.addActor(newWelcomePlane(nowStage().getW() / 2 + disFromCenter, 0));
			Waits.wait(nowStage(), GameConf.ADD_VERTICAL_WELCOME_PLANE_FRAMES);
		}
	}

	private void addLadderWelcomePlayer(int row, int disOfX) {
		if (row < 1) {
			throw new IllegalArgumentException("行数必须大于等于 1 ：" + row);
		}
		nowStage().addActor(newWelcomePlane(nowStage().getW() / 2, 0));
		Waits.wait(nowStage(), GameConf.ADD_LADDER_WELCOME_PLANE_FRAMES);
		for (int i = 2; i <= row; i++) {
			nowStage()
				.addActor(newWelcomePlane(nowStage().getW() / 2 - i * disOfX, 0))
				.addActor(newWelcomePlane(nowStage().getW() / 2 + i * disOfX, 0));
			Waits.wait(nowStage(), GameConf.ADD_LADDER_WELCOME_PLANE_FRAMES);
		}
	}

	private void checkLevelUp() {
		if (playerScore >= preGameScore + GameConf.LEVEL_UP_SCORE_INTERVAL) {
			levelUp();
		}
		if (nowStage().lifecycle().getFrame() - lastLevelUpTime > GameConf.LEVEL_UP_FRAMES) {
			levelUp();
		}
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

		lastLevelUpTime = nowStage().lifecycle().getFrame();
	}

	private void randomAddEnemy() {
		if (Randoms.nextLess(GameConf.ADD_ENEMY_CHANCE)) {
			nowStage().addActor(DemoFactory.newFollowPlane(Randoms.nextInt(nowStage().getIntW()), 0));
			Waits.wait(nowStage(), addNormalEnemyInterval);
		}
	}

	private void refreshFormTitle() {
		nowStage().fire(
			UICrew.class, f -> f.setTitle(
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
		nowStage().fire(AudioCrew.class, m -> m.stopAudio(Audio.BGM_CODE));
		showGameOverLabel();
		showRestartButton();
	}

	private void showGameOverLabel() {
		nowStage().addActor(
			DemoFactory.newGameOverLabel(
				GameConf.GAME_OVER_LABEL_POS_X, GameConf.GAME_OVER_LABEL_POS_Y
			)
		);
	}

	private void showRestartButton() {
		nowStage().addActor(
			DemoFactory.newRestartButton(GameConf.RESTART_BUTTON_POS_X, GameConf.RESTART_BUTTON_POS_Y, () -> {
				try {
					Stage nowStage = nowStage();
					EndlessBattle.this.stageCursor = null;
					nowStage.lifecycle().reset();
					nowStage.clearActor();
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
