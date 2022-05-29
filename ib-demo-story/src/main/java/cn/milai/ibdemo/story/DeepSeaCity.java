package cn.milai.ibdemo.story;

import java.util.Arrays;

import cn.milai.common.thread.counter.BlockDownCounter;
import cn.milai.common.thread.counter.Counter;
import cn.milai.ib.actor.Actor;
import cn.milai.ib.actor.prop.Prop;
import cn.milai.ib.actor.prop.text.DramaDialog;
import cn.milai.ib.actor.prop.text.Selections;
import cn.milai.ib.plugin.audio.Audio;
import cn.milai.ib.plugin.ui.Image;
import cn.milai.ib.role.ViewRole;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.stage.Stage;
import cn.milai.ib.stage.Waits;
import cn.milai.ibdemo.role.EscapeCraft;
import cn.milai.ibdemo.role.ShiningStar;
import cn.milai.ibdemo.role.UltraSide;
import cn.milai.ibdemo.role.bullet.Missile;
import cn.milai.ibdemo.role.fish.Dolphin;
import cn.milai.ibdemo.role.fish.Shark;
import cn.milai.ibdemo.role.plane.PlayerPlane;

/**
 * 深海城市剧本
 * @author milai
 * @date 2020.03.27
 */
public class DeepSeaCity extends DemoDrama {

	private static final int GAME_OVER_LABEL_Y = 266;
	private static final int RESTART_BUTTON_Y = 403;

	private final int SEA_SCENE_WIDTH = 1190;
	private final int SEA_SCENE_HEIGHT = 603;

	private Stage stage;

	private Image baseBGI = image("/img/tpc.png");
	private Image deepSeaBGI = image("/img/deepSea.jpg");

	private static final String BOMB_CODE = "BOMB";

	private static final String AUDIO_BOMB_FILE = "/audio/bomb.mp3";

	@Override
	public String getName() { return "深海城市"; }

	@Override
	public void doRun(Stage stage) {
		this.stage = stage;
		inBase();
		stage.resize(SEA_SCENE_WIDTH, SEA_SCENE_HEIGHT);
		inSea();
		while (!stage.lifecycle().isClosed() && !battle()) {
		}
		victory();
		toBeContinued(stage);
	}

	private void victory() {
		stage.lifecycle().reset();
		stage.clearActor();
		stage.resize(SEA_SCENE_WIDTH, SEA_SCENE_HEIGHT);
		stage.setBackgroud(deepSeaBGI);
		ViewRole dolphin = newViewRole(Dolphin.class, stage.getW() / 8, stage.getH() / 3);
		ViewRole craft = newViewRole(EscapeCraft.class, stage.centerX(), stage.getH());
		ViewRole ultra = newViewRole(UltraSide.class, stage.centerX(), stage.centerY());
		ViewRole star = newViewRole(ShiningStar.class, stage.centerX(), stage.centerY());
		stage.addActor(dolphin).addActorSync(craft);
		int craftSpeed = 7;
		int dolphinSpeed = 11;
		while (craft.centerY() > stage.centerY()) {
			craft.moveY(-craftSpeed);
			Waits.wait(stage, 1L);
		}
		double radian = 0;
		for (int i = 0; i < 15; i++) {
			radian += Math.PI / 2 / 15;
			if (radian > Math.PI / 2) {
				radian = Math.PI / 2;
			}
			craft.setDirection(radian);
			Waits.wait(stage, 1L);
		}
		Selections selections = newSelections(
			stage.centerX(), stage.centerY(),
			str("do_you_want_to_attack"),
			str("yes_attack"),
			str("no_do_not_attack")
		);
		stage.addActorSync(selections);
		Waits.waitRemove(selections);
		boolean attack = selections.getValue() == 0;
		if (!attack) {
			selections = newSelections(
				stage.centerX(), stage.centerY(),
				str("do_you_forget_the_order"),
				str("ok_let_us_attack"),
				str("no_let_them_go")
			);
			stage.addActorSync(selections);
			Waits.waitRemove(selections);
			attack = selections.getValue() == 0;
		}
		Audio bomb = audio(BOMB_CODE, AUDIO_BOMB_FILE);
		if (attack) {
			while (dolphin.centerY() < craft.centerY()) {
				dolphin.moveY(dolphinSpeed);
				craft.moveX(craftSpeed);
				Waits.wait(stage, 1L);
			}
			ViewRole missile = newViewRole(Missile.class, dolphin.centerX(), dolphin.centerY());
			missile.rotate(Math.PI / 2);
			stage.addActor(missile);
			stage.addActor(star);
			while (missile.getIntX() + missile.getIntW() < stage.centerX()) {
				craft.moveX(craftSpeed);
				missile.moveX(14);
				Waits.wait(stage, 1L);
			}
			stage.removeActor(missile).removeActor(star).addActor(ultra);
			stage.playAudio(bomb);
			int size = 140;
			stage.addActor(newBaseExplosion(10).resize(size, size).centerXY(stage.centerX(), stage.centerY()));
		}
		while (stage.getAll(Explosion.class).size() > 0) {
			craft.moveX(craftSpeed);
			Waits.wait(stage, 1L);
		}
		while (craft.getIntX() <= stage.getW()) {
			craft.moveX(craftSpeed);
			Waits.wait(stage, 1L);
		}
		stage.removeActorSync(craft);
		if (!attack) {
			stage.addActor(star);
			Waits.wait(stage, 15L);
			stage.removeActor(star);
			stage.addActor(ultra);
		}
		if (attack) {
			memberSay("why_you_prevent_me");
			ultraSay("are_you_sure_that_they_are_aggressor");
			memberSay("slient");
		}
		ultraSay("i_need_your_help");

		stage.removeActor(ultra).addActor(star);
		while (star.centerX() > dolphin.centerX()) {
			if (star.centerY() > dolphin.centerY()) {
				star.moveY(-6);
			}
			star.moveX(-6);
			Waits.wait(stage, 1L);
		}
		stage.removeActorSync(star);
		memberSay("what_should_we_do");
		ultraSay("let_us_find_the_leader_first");
	}

	private void toBeContinued(Stage s) {
		s.lifecycle().reset();
		s.clearActor();
		s.resize(initW(), initH());
		Prop c = newLinesFullScreenPass(
			10L, Integer.MAX_VALUE, 1L, Arrays.asList("未完待续…", "To Be Continued..."), 10
		);
		s.addActorSync(c);
		Waits.waitRemove(c);
	}

	private boolean battle() {
		stage.lifecycle().reset();
		stage.clearActor();
		stage.resize(SEA_SCENE_WIDTH, SEA_SCENE_HEIGHT);
		stage.setBackgroud(deepSeaBGI);
		DeepSeaBattle deepSeaBattle = new DeepSeaBattle(this, stage);
		if (!deepSeaBattle.run()) {
			Counter counter = new BlockDownCounter(1);
			stage.onClosed().subscribeOne(e -> counter.count());
			stage
				.addActor(newGameOverLabel(stage.centerX(), GAME_OVER_LABEL_Y))
				.addActor(newRestartButton(stage.centerX(), RESTART_BUTTON_Y, counter::count));
			counter.await();
			return false;
		}
		return true;
	}

	private void inSea() {
		stage.setBackgroud(deepSeaBGI);
		memberSay("think_around");
		ViewRole dolphin = newViewRole(Dolphin.class, 0, stage.centerY());
		dolphin.setStatus(Dolphin.STATUS_MOVE);
		stage.addActor(dolphin);
		for (int i = 0; i < 10; i++) {
			dolphin.moveX(11);
			Waits.wait(stage, 1L);
		}
		dolphin.setStatus(Actor.STATUS_DEFAULT);
		memberSay("is_this_the_submarine");
		info("we_have_no_more_picture");
		memberSay("ok_fine");
		ViewRole shark = newViewRole(Shark.class, stage.getW(), dolphin.getIntY());
		shark.horzontalFlip();
		stage.addActor(shark);
		for (int i = 0; i < 15; i++) {
			shark.setX(shark.getIntX() - 14);
			Waits.wait(stage, 1L);
		}
		memberSay("i_am_not_dolphin");
		info("you_are_dolphin");
	}

	private void inBase() {
		stage.setBackgroud(baseBGI);
		double baseX = stage.centerX();
		double baseY = stage.centerY();
		ViewRole plane = newViewRole(PlayerPlane.class, baseX, 0);
		stage.addActorSync(plane);
		int speed = 14;
		while (plane.centerY() < baseY) {
			plane.moveY(speed);
			speed = Integer.max(5, speed - 1);
			Waits.wait(stage, 1L);
		}
		stage.removeActorSync(plane);
		officerSay("how_did_you_driver");
		memberSay("do_not_you_know_reverse_parking");
		officerSay("are_you_not_skilled");
		memberSay("you_can_say_it_agin");
		officerSay("are_you_not_skilled2");
		memberSay("wait_and_see");
		plane.setX(baseX);
		plane.setY(baseY);
		stage.addActorSync(plane);
		for (int i = 5; i >= 1; i -= 2) {
			moveCircle(plane, (int) stage.getW() / 6 * 4, Math.PI / 6 / i);
			moveCircle(plane, (int) stage.getW() / 6 * 2, -Math.PI / 6 / i);
		}
		for (int i = 0; i < 20; i++) {
			plane.moveY(-11);
			Waits.wait(stage, 1L);
		}
		plane.rotate(Math.PI);
		for (int i = 0; i < 20; i++) {
			plane.moveY(11);
			Waits.wait(stage, 1L);
		}
		stage.removeActorSync(plane);
		officerSay("it_is_excellent");
		memberSay("no_no_no");
		officerSay("please_go_to_attack_deep_sea_city");
		memberSay("i_can_not_accept_that");
		officerSay("i_am_the_new_leader");
		memberSay("where_is_leader");
		officerSay("you_do_not_need_to_know");
		memberSay("slient");
	}

	private void officerSay(String stringCode) {
		showDialog("/img/officer.png", "officerName", stringCode);
	}

	private void memberSay(String stringCode) {
		showDialog("/img/member.png", "memberName", stringCode);
	}

	private void ultraSay(String stringCode) {
		showDialog("/img/ultra.png", "ultraName", stringCode);
	}

	private void info(String stringCode) {
		showDialog(null, null, stringCode);
	}

	/**
	 * 使指定视图游戏对象从当前位置开始移动一周
	 * @param obj
	 * @param circleX 中心点的 x 坐标
	 * @param deltaRadian 每一帧移动的弧度
	 */
	public void moveCircle(ViewRole obj, int circleX, double deltaRadian) {
		double x = obj.getIntX();
		double y = obj.getIntY();
		int r = circleX - obj.getIntX();
		double radian = 0;
		while (Math.abs(radian) < 2 * Math.PI) {
			radian += deltaRadian;
			if (Math.abs(radian + deltaRadian) > 2 * Math.PI) {
				obj.rotate(2 * Math.PI - radian);
				radian = 2 * Math.PI;
			}
			obj.setX(x + r * (1 - Math.cos(radian)));
			obj.setY(y - r * Math.sin(radian));
			obj.rotate(deltaRadian);
			Waits.wait(stage, 1L);
		}
		obj.setX(x + r * (1 - Math.cos(2 * Math.PI)));
		obj.setY(y - r * Math.sin(2 * Math.PI));
		obj.rotate(2 * Math.PI);
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
