package cn.milai.ibdemo.drama;

import java.awt.Color;

import cn.milai.common.base.Strings;
import cn.milai.common.thread.counter.BlockDownCounter;
import cn.milai.common.thread.counter.Counter;
import cn.milai.ib.ViewObject;
import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.component.CommandShield;
import cn.milai.ib.component.GameOverLabel;
import cn.milai.ib.component.RestartButton;
import cn.milai.ib.component.text.DramaDialog;
import cn.milai.ib.component.text.TextLines;
import cn.milai.ib.container.DramaContainer;
import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.container.listener.LifecycleListener;
import cn.milai.ib.container.plugin.media.Audio;
import cn.milai.ib.container.plugin.ui.BaseImage;
import cn.milai.ib.container.plugin.ui.Image;
import cn.milai.ib.drama.AbstractDrama;
import cn.milai.ib.util.ImageUtil;
import cn.milai.ib.util.Waits;
import cn.milai.ibdemo.character.UltraFly;
import cn.milai.ibdemo.character.bullet.Missile;
import cn.milai.ibdemo.character.plane.PlayerPlane;

/**
 * 未知来访者 剧本
 * @author milai
 * @date 2020.03.23
 */
public class UnknownVisitor extends AbstractDrama {

	private static final String HEIR_OF_LIGHT = "/audio/heirOfLight.mp3";
	private static final int GAME_OVER_LABEL_Y = 266;
	private static final int RESTART_BUTTON_Y = 403;
	private static final String WARNING_AUDIO = "WARNING";

	private DramaContainer container;

	private Image baseBGI = image("/img/tpc.png");
	private Image universeBGI = image("/img/backgroud.jpg");
	private Image starsBGI = image("/img/stars.gif");

	@Override
	public String getName() { return "未知来访者"; }

	@Override
	public void doRun(DramaContainer container) {
		this.container = container;
		tip(container);
		inBase();
		inUniverse();
		while (!battle()) {
		}
		victory();
	}

	private void victory() {
		container.setPined(true);
		memberSay("what_happened");
		PlayerCharacter player = container.getAll(PlayerCharacter.class).get(0);
		int x = player.getIntX() > container.getW() / 2 ? container.getW() / 4 : container.getW() / 4 * 3;
		UltraFly ultraFly = new UltraFly(x, container.getH(), container);
		container.addObject(ultraFly);
		while (ultraFly.getIntY() > player.getIntY()) {
			ultraFly.setY(ultraFly.getIntY() - 14);
			Waits.wait(container, 1L);
		}
		memberSay("it_is_ultra");
		leaderSay("he_is_always_appear_at_critical_time");
		ultraSay("nod");
		CommandShield shield = new CommandShield(container);
		container.addObject(shield);
		while (ultraFly.getIntY() + ultraFly.getIntH() > 0) {
			ultraFly.setY(ultraFly.getIntY() - 21);
			Waits.wait(container, 1L);
		}
		container.removeObject(shield);
		leaderSay("please_come_back");
		memberSay("gig");
		container.setPined(true);
		container.reset();
		container.resizeWithUI(initW(), initH());
		container.setBackgroud(starsBGI);
		container.playAudio(audio(Audio.BGM_CODE, HEIR_OF_LIGHT));
		showBGMInfo();
		Waits.wait(container, 30L);
		visitorSay("why_you_protect_human");
		ultraSay("aggression_is_not_permit");
		visitorSay("human_is_the_real_aggressor");
		ultraSay("do_you_have_evidence");
		visitorSay("box_is_the_evidence");
		ultraSay("can_not_hava_a_quiet_talk");
		visitorSay("no_words_to_say");
		ultraSay("if_that_is_true");
		// 确保清除线程中断状态
		Thread.interrupted();
	}

	private void showBGMInfo() {
		TextLines bgmInfo = new TextLines(
			0, 0, container,
			Strings.toLines(str("bgm_info2")), Color.BLACK, 7L, 28L, 7L
		);
		bgmInfo.setX(container.getW() - 1 - bgmInfo.getIntW());
		bgmInfo.setY(container.getH() - container.getH());
		container.addObject(bgmInfo);
	}

	private void tip(DramaContainer container) {
		container.setBackgroud(new BaseImage(ImageUtil.newImage(Color.BLACK, 1, 1)));
		info("controlTip");
	}

	private boolean battle() {
		container.reset();
		container.resizeWithUI(initW(), initH());
		container.setBackgroud(universeBGI);
		UniverseBattle universeBattle = new UniverseBattle(this, container);
		if (!universeBattle.run()) {
			Counter counter = new BlockDownCounter(1);
			container.addLifecycleListener(new LifecycleListener() {
				@Override
				public void onContainerClosed(LifecycleContainer container) {
					counter.count();
				}
			});
			container.addObject(new GameOverLabel(container.getW() / 2, GAME_OVER_LABEL_Y, container));
			container.addObject(new RestartButton(container.getW() / 2, RESTART_BUTTON_Y, container, counter::count));
			counter.await();
			return false;
		}
		return true;
	}

	private void inUniverse() {
		container.setBackgroud(universeBGI);
		ViewObject dodgePlane = new ViewObject(
			container.getW() / 2,
			container.getH() / 6 * 5,
			container, PlayerPlane.class
		);
		container.addObject(dodgePlane);
		memberSay("what_do_you_want_to_do");
		visitorSay("surrender_as_soon_as_possible");
		leaderSay("why_do_you_want_to_aggress_the_earth");
		visitorSay("the_earth_belongs_to_us");
		ViewObject missile = new ViewObject(container.getW() / 2, 0, container, Missile.class);
		missile.rotate(Math.PI);
		container.addObject(missile);
		int missileSpeedY = 35;
		for (int i = 0; i < 5; i++) {
			missile.moveY(missileSpeedY);
			Waits.wait(container, 1L);
		}
		memberSay("it_is_dangerous");
		for (int i = 0; i < 5; i++) {
			dodgePlane.moveX(11);
			missile.moveY(missileSpeedY);
			Waits.wait(container, 1L);
		}
		while (missile.getIntY() + missile.getIntH() < container.getH()) {
			missile.moveY(missileSpeedY);
			Waits.wait(container, 1L);
		}
		container.removeObject(missile);
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
		int x = container.getW() / 2;
		int y = 350;
		ViewObject plane = new ViewObject(x, y, container, PlayerPlane.class);
		container.addObject(plane);
		for (int speed = 0; plane.getIntY() + plane.getIntH() > 0; speed -= 1) {
			plane.moveY(speed);
			Waits.wait(container, 1L);
		}
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
			(int) (0.5 * container.getW()),
			(int) (0.75 * container.getH()),
			container,
			DramaDialog.asParams(
				str(stringCode),
				speakerImg == null ? null : image(speakerImg),
				speakerName == null ? "" : str(speakerName)
			)
		);
		container.addObject(dialog);
		Waits.waitRemove(dialog, 5);
	}

	@Override
	protected int initW() {
		return 554;
	}

	@Override
	protected int initH() {
		return 689;
	}

}
