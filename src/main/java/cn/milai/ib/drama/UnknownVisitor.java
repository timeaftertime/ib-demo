package cn.milai.ib.drama;

import java.awt.Color;
import java.awt.Image;
import java.util.concurrent.CountDownLatch;

import cn.milai.ib.character.bullet.Missile;
import cn.milai.ib.character.explosion.Explosion;
import cn.milai.ib.character.plane.PlayerPlane;
import cn.milai.ib.component.form.CommandShield;
import cn.milai.ib.component.form.GameOverLabel;
import cn.milai.ib.component.form.RestartButton;
import cn.milai.ib.component.form.text.DramaDialog;
import cn.milai.ib.conf.SystemConf;
import cn.milai.ib.container.Audio;
import cn.milai.ib.container.Container;
import cn.milai.ib.container.LifecycleContainer;
import cn.milai.ib.container.form.FormContainer;
import cn.milai.ib.container.listener.Command;
import cn.milai.ib.container.listener.ContainerEventListener;
import cn.milai.ib.obj.IBObject;
import cn.milai.ib.obj.Player;
import cn.milai.ib.util.ImageUtil;
import cn.milai.ib.util.WaitUtil;

/**
 * 未知来访者 剧本
 * @author milai
 * @date 2020.03.23
 */
public class UnknownVisitor extends AbstractDrama {

	private static final String HEIR_OF_LIGHT = "/audio/heirOfLight.mp3";
	private static final int GAME_OVER_LABEL_POS_X = SystemConf.prorate(400);
	private static final int GAME_OVER_LABEL_POS_Y = SystemConf.prorate(380);
	private static final int RESTART_BUTTON_POS_X = SystemConf.prorate(390);
	private static final int RESTART_BUTTON_POS_Y = SystemConf.prorate(576);
	private static final String WARNING_AUDIO = "WARNING";

	private Container container;

	private Image baseBGI = image("/img/tpc.png");
	private Image universeBGI = image("/img/backgroud.jpg");
	private Image starsBGI = image("/img/stars.gif");

	@Override
	public String getName() {
		return "未知来访者";
	}

	@Override
	public void run(Container container) {
		init(container);
		tip(container);
		inBase();
		inUniverse();
		while (!battle()) {
		}
		victory();
	}

	private void victory() {
		memberSay("what_happened");
		Player player = container.getAll(Player.class).get(0);
		int x = player.getX() > container.getWidth() / 2 ? container.getWidth() / 4 : container.getWidth() / 4 * 3;
		UltraFly ultraFly = new UltraFly(x, container.getHeight(), container);
		container.addObject(ultraFly);
		while (ultraFly.getY() > player.getY()) {
			ultraFly.setY(ultraFly.getY() - SystemConf.prorate(20));
			WaitUtil.wait(container, 1L);
		}
		memberSay("it_is_ultra");
		leaderSay("he_is_always_appear_at_critical_time");
		ultraSay("nod");
		CommandShield shield = new CommandShield(container);
		container.addObject(shield);
		while (ultraFly.getY() + ultraFly.getHeight() > 0) {
			ultraFly.setY(ultraFly.getY() - SystemConf.prorate(30));
			WaitUtil.wait(container, 1L);
		}
		container.removeObject(shield);
		leaderSay("please_come_back");
		memberSay("gig");
		container.reset();
		container.setBackgroud(starsBGI);
		container.playAudio(audio(Audio.BGM_CODE, HEIR_OF_LIGHT));
		WaitUtil.wait(container, 30L);
		visitorSay("why_you_protect_human");
		ultraSay("aggression_is_not_permit");
		visitorSay("human_is_the_real_aggressor");
		ultraSay("do_you_have_evidence");
		visitorSay("box_is_the_evidence");
		ultraSay("silent");

	}

	private void tip(Container container) {
		container.setBackgroud(ImageUtil.newImage(Color.BLACK, 1, 1));
		info("controlTip");
	}

	private boolean battle() {
		((LifecycleContainer) container).reset();
		UniverseBattle universeBattle = new UniverseBattle(this, container);
		container.addEventListener(new ContainerEventListener() {
			@Override
			public void onObjectAdded(IBObject obj) {
				if (obj instanceof Explosion) {
					container.playAudio(audio("BOMB", "/audio/bomb.mp3"));
				}
			}
		});
		if (!universeBattle.run()) {
			CountDownLatch latch = new CountDownLatch(1);
			((LifecycleContainer) container).addLifeCycleListener(() -> {
				latch.countDown();
			});
			container.addObject(new GameOverLabel(GAME_OVER_LABEL_POS_X, GAME_OVER_LABEL_POS_Y, container));
			container.addObject(new RestartButton(RESTART_BUTTON_POS_X, RESTART_BUTTON_POS_Y, container,
				() -> {
					latch.countDown();
				}));
			try {
				latch.await();
			} catch (InterruptedException e) {
			}
			return false;
		}
		return true;
	}

	private void inUniverse() {
		container.setBackgroud(universeBGI);
		DodgePlane dodgePlane = new DodgePlane(
			container.getWidth() / 2,
			container.getContentHeight() / 6 * 5,
			container);
		container.addObject(dodgePlane);
		memberSay("what_do_you_want_to_do");
		visitorSay("surrender_as_soon_as_possible");
		leaderSay("why_do_you_want_to_aggress_the_earth");
		visitorSay("the_earth_belongs_to_us");
		// owner 不能为 null，所以直接用前面创建的 DodgePlane
		Missile missile = new Missile(container.getWidth() / 2, 0, dodgePlane);
		container.addObject(missile);
		WaitUtil.wait(container, 6);
		memberSay("it_is_dangerous");
		dodgePlane.dodge();
		WaitUtil.waitRemove(missile, 10);
		memberSay("apply_for_counterattack");
		leaderSay("permit_counterattack");
		reporterSay("there_will_be_helper");
		memberSay("gig");
	}

	private void inBase() {
		container.setBackgroud(baseBGI);

		reporterSay("he_is_back");
		leaderSay("you_have_been_working_very_hard");
		memberSay("why_I_have_no_portrait");
		leaderSay("because_the_author_is_lazy");
		memberSay("what_is_the_box");
		leaderSay("heard_that_is_top_secret");

		container.playAudio(audio(WARNING_AUDIO, "/audio/warning.mp3"));
		info("alarm_sounded");
		container.stopAudio(WARNING_AUDIO);
		leaderSay("reporter_what_happened");
		reporterSay("the_unkown_visitor_appear");
		leaderSay("please_go_and_investigate");
		memberSay("why_me_again");
		leaderSay("because_there_are_only_you");
		memberSay("why_need_talk");
		leaderSay("we_have_arms_too");
		memberSay("gig");

		showPlaneTakeOff();
	}

	private void showPlaneTakeOff() {
		info("take_off");
		PlayerPlane takeOffPlane = new TaskOffPlane(container.getWidth() / 2, SystemConf.prorate(500), container);
		container.addObject(takeOffPlane);
		WaitUtil.waitRemove(takeOffPlane, 20);
	}

	private void init(Container container) {
		this.container = container;
	}

	private void leaderSay(String stringCode) {
		showDialog("/img/leader.png", "leaderName", stringCode);
	}

	private void reporterSay(String stringCode) {
		showDialog("/img/reporter.png", "reporterName", stringCode);
	}

	private void memberSay(String stringCode) {
		showDialog("/img/member.png", "memberName", stringCode);
	}

	private void info(String stringCode) {
		showDialog(null, null, stringCode);
	}

	private void visitorSay(String stringCode) {
		showDialog("/img/visitor.png", "visitorName", stringCode);
	}

	private void ultraSay(String stringCode) {
		showDialog("/img/ultra.png", "ultraName", stringCode);
	}

	private void showDialog(String speakerImg, String speakerName, String stringCode) {
		DramaDialog dialog = new DramaDialog(
			(int) (0.5 * container.getWidth()),
			(int) (0.75 * container.getContentHeight()),
			(FormContainer) container,
			DramaDialog.asParams(
				str(stringCode),
				speakerImg == null ? null : image(speakerImg),
				speakerName == null ? "" : str(speakerName)));
		container.addObject(dialog);
		WaitUtil.waitRemove(dialog, 5);
	}

	/**
	 * 显示起飞动画的飞机
	 * @author milai
	 * @date 2020.03.24
	 */
	private static class TaskOffPlane extends PlayerPlane {

		public TaskOffPlane(int x, int y, Container container) {
			super(x, y, container);
		}

		private int speed = 1;

		@Override
		protected Class<?> getConfigClass() {
			return PlayerPlane.class;
		}

		@Override
		public void beforeMove() {
			setSpeedY(SystemConf.prorate(speed -= 2));
		}

		@Override
		protected void afterMove() {
			if (getY() + getHeight() <= 0) {
				getContainer().removeObject(this);
			}
		}

		@Override
		public boolean onCancel(Command command) {
			return true;
		}

		@Override
		public boolean onReceive(Command command) {
			return true;
		}
	}

	/**
	 * 显示闪避动画的飞机
	 * @author milai
	 * @date 2020.03.24
	 */
	private static class DodgePlane extends PlayerPlane {
		public DodgePlane(int x, int y, Container container) {
			super(x, y, container);
		}

		final int dodgeDistance = getWidth() * 5;
		private boolean startDodge = false;
		private int distance = 0;

		@Override
		protected Class<?> getConfigClass() {
			return PlayerPlane.class;
		}

		@Override
		public void beforeMove() {
			if (!startDodge || distance >= dodgeDistance) {
				setSpeedX(0);
				return;
			}
			setSpeedX(getRatedSpeedX());
		}

		@Override
		protected void afterMove() {
			distance += getRatedSpeedX();
		}

		public void dodge() {
			startDodge = true;
		}

		@Override
		public boolean onCancel(Command command) {
			return true;
		}

		@Override
		public boolean onReceive(Command command) {
			return true;
		}

	};

}
