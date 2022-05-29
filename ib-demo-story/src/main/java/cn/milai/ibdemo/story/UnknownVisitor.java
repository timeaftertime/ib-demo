package cn.milai.ibdemo.story;

import java.awt.Color;

import cn.milai.common.thread.counter.BlockDownCounter;
import cn.milai.common.thread.counter.Counter;
import cn.milai.ib.actor.prop.text.DramaDialog;
import cn.milai.ib.actor.prop.text.TextLines;
import cn.milai.ib.graphics.Images;
import cn.milai.ib.plugin.audio.Audio;
import cn.milai.ib.plugin.control.CommandShield;
import cn.milai.ib.plugin.ui.BaseImage;
import cn.milai.ib.plugin.ui.Image;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.ViewRole;
import cn.milai.ib.stage.Stage;
import cn.milai.ib.stage.Waits;
import cn.milai.ibdemo.role.UltraFly;
import cn.milai.ibdemo.role.bullet.Missile;
import cn.milai.ibdemo.role.plane.PlayerPlane;

/**
 * 未知来访者 剧本
 * @author milai
 * @date 2020.03.23
 */
public class UnknownVisitor extends DemoDrama {

	private static final String HEIR_OF_LIGHT = "/audio/heirOfLight.mp3";
	private static final int GAME_OVER_LABEL_Y = 266;
	private static final int RESTART_BUTTON_Y = 403;
	private static final String WARNING_AUDIO = "WARNING";

	private Stage stage;

	private Image baseBGI = image("/img/tpc.png");
	private Image universeBGI = image("/img/backgroud.jpg");
	private Image starsBGI = image("/img/stars.gif");

	@Override
	public String getName() { return "未知来访者"; }

	@Override
	public void doRun(Stage container) {
		this.stage = container;
		tip(container);
		inBase();
		inUniverse();
		container.resize(initW(), initH());
		while (!battle()) {
		}
		victory();
	}

	private void victory() {
		stage.setPined(true);
		memberSay("what_happened");
		PlayerRole player = (PlayerRole) stage.getAll(PlayerRole.class).toArray()[0];
		double x = player.getX() > stage.getW() / 2 ? stage.getW() / 4 : stage.getW() / 4 * 3;
		ViewRole ultraFly = newViewRole(UltraFly.class, x, stage.getH());
		stage.addActor(ultraFly);
		while (ultraFly.getIntY() > player.getIntY()) {
			ultraFly.setY(ultraFly.getIntY() - 14);
			Waits.wait(stage, 1L);
		}
		memberSay("it_is_ultra");
		leaderSay("he_is_always_appear_at_critical_time");
		ultraSay("nod");
		CommandShield shield = new CommandShield();
		stage.addActor(shield);
		while (ultraFly.getIntY() + ultraFly.getIntH() > 0) {
			ultraFly.setY(ultraFly.getIntY() - 21);
			Waits.wait(stage, 1L);
		}
		stage.removeActor(shield);
		leaderSay("please_come_back");
		memberSay("gig");
		stage.setPined(true);
		stage.lifecycle().reset();
		stage.clearActor();
		stage.resize(initW(), initH());
		stage.setBackgroud(starsBGI);
		stage.playAudio(audio(Audio.BGM_CODE, HEIR_OF_LIGHT));
		showBGMInfo();
		Waits.wait(stage, 30L);
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
		TextLines bgmInfo = newTextLines(7L, 28L, 7L, str("bgm_info2").split("\n"));
		bgmInfo.setX(stage.getW() - 1 - bgmInfo.getIntW());
		bgmInfo.setY(stage.getH() - stage.getH());
		stage.addActorSync(bgmInfo);
	}

	private void tip(Stage container) {
		container.setBackgroud(new BaseImage(Images.newImage(Color.BLACK, 1, 1)));
		info("controlTip");
	}

	private boolean battle() {
		stage.lifecycle().reset();
		stage.clearActor();
		stage.setBackgroud(universeBGI);
		UniverseBattle universeBattle = new UniverseBattle(this, stage);
		if (!stage.lifecycle().isClosed() && !universeBattle.run()) {
			Counter counter = new BlockDownCounter(1);
			stage.onClosed().subscribeOne(e -> counter.count());
			stage
				.addActor(newGameOverLabel(stage.getW() / 2, GAME_OVER_LABEL_Y))
				.addActor(newRestartButton(stage.getW() / 2, RESTART_BUTTON_Y, () -> counter.count()));
			counter.await();
			return false;
		}
		return true;
	}

	private void inUniverse() {
		stage.setBackgroud(universeBGI);
		ViewRole dodgePlane = newViewRole(PlayerPlane.class, stage.getW() / 2, stage.getH() / 6 * 5);
		stage.addActor(dodgePlane);
		memberSay("what_do_you_want_to_do");
		visitorSay("surrender_as_soon_as_possible");
		leaderSay("why_do_you_want_to_aggress_the_earth");
		visitorSay("the_earth_belongs_to_us");
		ViewRole missile = newViewRole(Missile.class, stage.getW() / 2, 0);
		missile.rotate(Math.PI);
		stage.addActor(missile);
		int missileSpeedY = 35;
		for (int i = 0; i < 5; i++) {
			missile.moveY(missileSpeedY);
			Waits.wait(stage, 1L);
		}
		memberSay("it_is_dangerous");
		for (int i = 0; i < 5; i++) {
			dodgePlane.moveX(11);
			missile.moveY(missileSpeedY);
			Waits.wait(stage, 1L);
		}
		while (missile.getIntY() + missile.getIntH() < stage.getH()) {
			missile.moveY(missileSpeedY);
			Waits.wait(stage, 1L);
		}
		stage.removeActor(missile);
		memberSay("apply_for_counterattack");
		leaderSay("permit_counterattack");
		reporterSay("there_will_be_helper");
		memberSay("gig");
		stage.removeActor(dodgePlane);
	}

	private void inBase() {
		stage.setBackgroud(baseBGI);

		reporterSay("he_is_back");
		leaderSay("you_have_been_working_very_hard");
		memberSay("why_I_have_no_portrait");
		leaderSay("because_the_author_is_lazy");
		memberSay("what_is_the_box");
		leaderSay("heard_that_is_top_secret");

		stage.playAudio(audio(WARNING_AUDIO, "/audio/warning.mp3"));
		info("alarm_sounded");
		stage.stopAudio(WARNING_AUDIO);
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
		ViewRole plane = newViewRole(PlayerPlane.class, stage.getW() / 2, 350);
		stage.addActorSync(plane);
		for (int speed = 0; plane.getIntY() + plane.getIntH() > 0; speed -= 1) {
			plane.moveY(speed);
			Waits.wait(stage, 1L);
		}
		stage.removeActor(plane);
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
		DramaDialog dialog = newDramaDialog(
			0.5 * stage.getW(), 0.75 * stage.getH(), speakerImg, speakerName, stringCode
		);
		stage.addActorSync(dialog);
		Waits.waitRemove(dialog);
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
