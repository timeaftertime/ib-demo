package cn.milai.ib.drama;

import cn.milai.ib.character.helper.AccelerateHelper;
import cn.milai.ib.character.helper.OneLifeHelper;
import cn.milai.ib.character.plane.FollowPlane;
import cn.milai.ib.character.plane.MissileBoss;
import cn.milai.ib.character.plane.PlayerPlane;
import cn.milai.ib.character.plane.WelcomePlane;
import cn.milai.ib.character.property.Shootable;
import cn.milai.ib.component.form.BloodStrip;
import cn.milai.ib.component.form.LifeCounter;
import cn.milai.ib.conf.SystemConf;
import cn.milai.ib.container.Audio;
import cn.milai.ib.container.Container;
import cn.milai.ib.container.listener.ContainerEventListener;
import cn.milai.ib.ex.IBException;
import cn.milai.ib.obj.IBObject;
import cn.milai.ib.obj.Player;
import cn.milai.ib.util.RandomUtil;
import cn.milai.ib.util.WaitUtil;

/**
	 * 控制战斗进程的类
	 * @author milai
	 * @date 2020.03.24
	 */
public class UniverseBattle {

	private static final String BATTLE_BGM = "/audio/themeOfUltraman.mp3";

	private static final int BLOOD_STRP_Y = SystemConf.prorate(100);

	private Thread battleThread;
	private Container container;
	private Player player;
	private AbstractDrama drama;

	public UniverseBattle(AbstractDrama drama, Container container) {
		this.container = container;
		this.drama = drama;
	}

	public Container getContainer() {
		Container c = this.container;
		if (c == null) {
			throw new BattleStoppedException();
		}
		return c;
	}

	public void stop() {
		container = null;
		battleThread.interrupt();
	}

	/**
	 * 开始战斗，返回是否成功完成战斗
	 * @return
	 */
	public boolean run() {
		try {
			init();
			getContainer().playAudio(drama.audio(Audio.BGM_CODE, BATTLE_BGM));
			WaitUtil.wait(getContainer(), 5L);
			// 阶梯 Welcome
			int midX = getContainer().getWidth() / 2;
			int interval = SystemConf.prorate(35);
			addLadderWelcome(8, midX, interval, 20L);
			// Welcome Follow 混合
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j <= i; j++) {
					randomAddFollowPlane();
					WaitUtil.wait(getContainer(), 10L);
				}
				WaitUtil.wait(getContainer(), 25L);
				addWelcomes((int) (getContainer().getWidth() * 0.1),
					(int) (getContainer().getWidth() * 0.3),
					(int) (getContainer().getWidth() * 0.5),
					(int) (getContainer().getWidth() * 0.7),
					(int) (getContainer().getWidth() * 0.9));
			}
			WaitUtil.wait(getContainer(), 30L);
			hideBoss();
			// 双重阶梯 Welcome
			for (int i = 0; i < 8; i++) {
				getContainer().addObject(new WelcomePlane(midX - interval * i, 0, getContainer()));
				if (i > 0) {
					getContainer().addObject(new WelcomePlane(midX + interval * i, 0, getContainer()));
				}
				WaitUtil.wait(getContainer(), 10L);
				getContainer().addObject(new WelcomePlane(midX - interval * i, 0, getContainer()));
				if (i > 0) {
					getContainer().addObject(new WelcomePlane(midX + interval * i, 0, getContainer()));
				}
				WaitUtil.wait(getContainer(), 20L);
			}
			// 间距逐渐变小的 Welcome 列队
			getContainer().addObject(new AccelerateHelper(midX, 0, getContainer()));
			for (int i = 0; i < 6; i++) {
				addWelcomes((int) (getContainer().getWidth() * 0.4), (int) (getContainer().getWidth() * 0.6));
				WaitUtil.wait(getContainer(), 5 + 60L / (i + 1));
			}
			WaitUtil.wait(getContainer(), 10L);
			hideBoss();
			MissileBoss boss = new MissileBoss(midX, 0, getContainer());
			BloodStrip bossBloodStrip = new BloodStrip(
				getContainer().getWidth() / 2, BLOOD_STRP_Y, getContainer(), boss);
			getContainer().addObject(boss);
			getContainer().addObject(bossBloodStrip);
			while (boss.isAlive()) {
				randomAddFollowPlane();
				getContainer().addObject(new OneLifeHelper(RandomUtil.nextInt(getContainer().getWidth()), 0,
					container));
				WaitUtil.wait(getContainer(), 100L);
				randomAddFollowPlane();
				WaitUtil.wait(getContainer(), 100L);
			}
			getContainer().removeObject(bossBloodStrip);
			largeEnemyApear();
			UltraLight light = new UltraLight((Shootable) player, 25L);
			getContainer().playAudio(drama.audio("ULTRAMAN", "/audio/ultraman.mp3"));
			getContainer().addObject(light);
			WaitUtil.waitRemove(light, 5L);

			return player.isAlive();
		} catch (BattleStoppedException e) {
			while (!Thread.interrupted()) {
				// 等待 RestartButton 回调函数的 interrupt 以确保退出时清除中断状态
			}
			return false;
		}
	}

	private void largeEnemyApear() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 3; j++) {
				addWelcomes(RandomUtil.nextInt(getContainer().getWidth()));
				WaitUtil.wait(getContainer(), 1L);
			}
			for (int j = 0; j < 3; j++) {
				randomAddFollowPlane();
				WaitUtil.wait(getContainer(), 3L);
			}
		}
	}

	// Game Over 则不显示 Boss
	private void hideBoss() {
		while (!player.isAlive()) {
			randomAddFollowPlane();
			WaitUtil.wait(getContainer(), 30L);
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
			getContainer().addObject(new WelcomePlane(midX - horInterval * i, 0, getContainer()));
			if (i > 0) {
				getContainer().addObject(new WelcomePlane(midX + horInterval * i, 0, getContainer()));
			}
			WaitUtil.wait(getContainer(), waitFrame);
		}
	}

	/**
	 * 添加若干个 x 坐标为指定值，y 坐标为 0 的 WelcomePlane
	 * @param xs
	 */
	private void addWelcomes(int... xs) {
		for (int x : xs) {
			getContainer().addObject(new WelcomePlane(x, 0, getContainer()));
		}
	}

	private void init() {
		battleThread = Thread.currentThread();
		addPlayer();
		getContainer().addEventListener(new ContainerEventListener() {
			@Override
			public void onObjectRemoved(IBObject obj) {
				if (player == obj) {
					container.stopAudio(Audio.BGM_CODE);
					stop();
				}
			}
		});
	}

	private void addPlayer() {
		player = new PlayerPlane(getContainer().getWidth() / 2,
			getContainer().getContentHeight() * 5 / 6,
			getContainer());
		LifeCounter lifeCounter = new LifeCounter(getContainer().getWidth() / 10,
			getContainer().getContentHeight() / 10 * 9,
			getContainer(), player);
		getContainer().addObject(player);
		getContainer().addObject(lifeCounter);
	}

	private void randomAddFollowPlane() {
		getContainer().addObject(new FollowPlane(RandomUtil.nextInt(getContainer().getWidth()), 0,
			getContainer()));
	}

	@SuppressWarnings("serial")
	private static class BattleStoppedException extends IBException {
	}
}