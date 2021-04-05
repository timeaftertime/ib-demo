package cn.milai.ibdemo.drama;

import java.util.Arrays;

import cn.milai.common.thread.counter.BlockDownCounter;
import cn.milai.common.thread.counter.Counter;
import cn.milai.ib.ViewObject;
import cn.milai.ib.container.DramaContainer;
import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.container.listener.LifecycleListener;
import cn.milai.ib.container.plugin.media.Audio;
import cn.milai.ib.container.plugin.ui.Image;
import cn.milai.ib.control.Control;
import cn.milai.ib.control.GameOverLabel;
import cn.milai.ib.control.button.RestartButton;
import cn.milai.ib.control.text.DramaDialog;
import cn.milai.ib.control.text.LinesFullScreenPass;
import cn.milai.ib.control.text.Selections;
import cn.milai.ib.mode.drama.AbstractDrama;
import cn.milai.ib.role.explosion.Explosion;
import cn.milai.ib.util.Waits;
import cn.milai.ibdemo.role.EscapeCraft;
import cn.milai.ibdemo.role.ShiningStar;
import cn.milai.ibdemo.role.UltraSide;
import cn.milai.ibdemo.role.bullet.Missile;
import cn.milai.ibdemo.role.explosion.BaseExplosion;
import cn.milai.ibdemo.role.fish.Dolphin;
import cn.milai.ibdemo.role.fish.Shark;
import cn.milai.ibdemo.role.plane.PlayerPlane;

/**
 * 深海城市剧本
 * @author milai
 * @date 2020.03.27
 */
public class DeepSeaCity extends AbstractDrama {

	private static final int GAME_OVER_LABEL_Y = 266;
	private static final int RESTART_BUTTON_Y = 403;

	private final int SEA_SCENE_WIDTH = 1190;
	private final int SEA_SCENE_HEIGHT = 603;

	private DramaContainer container;

	private Image baseBGI = image("/img/tpc.png");
	private Image deepSeaBGI = image("/img/deepSea.jpg");

	private static final String BOMB_CODE = "BOMB";

	private static final String AUDIO_BOMB_FILE = "/audio/bomb.mp3";

	@Override
	public String getName() { return "深海城市"; }

	@Override
	public void doRun(DramaContainer container) {
		this.container = container;
		inBase();
		container.newSize(SEA_SCENE_WIDTH, SEA_SCENE_HEIGHT);
		container.newUISize(SEA_SCENE_WIDTH, SEA_SCENE_HEIGHT);
		inSea();
		while (!battle()) {
		}
		victory();
		container.restoreSize();
		container.restoreUISize();
		toBeContinued(container);
	}

	private void victory() {
		container.reset();
		container.resizeWithUI(SEA_SCENE_WIDTH, SEA_SCENE_HEIGHT);
		container.setBackgroud(deepSeaBGI);
		ViewObject dolphin = new ViewObject(
			container.getW() / 8, container.getH() / 3, container,
			Dolphin.class
		);
		ViewObject craft = new ViewObject(centerX(), container.getH(), container, EscapeCraft.class);
		ViewObject ultra = new ViewObject(centerX(), centerY(), container, UltraSide.class);
		ViewObject star = new ViewObject(
			(int) ultra.centerX(), (int) ultra.centerY(), container,
			ShiningStar.class
		);
		container.addObject(dolphin);
		container.addObject(craft);
		int craftSpeed = 7;
		int dolphinSpeed = 11;
		while (craft.centerY() > centerY()) {
			craft.moveY(-craftSpeed);
			Waits.wait(container, 1L);
		}
		double radian = 0;
		for (int i = 0; i < 15; i++) {
			radian += Math.PI / 2 / 15;
			if (radian > Math.PI / 2) {
				radian = Math.PI / 2;
			}
			craft.setRotateRadian(radian);
			Waits.wait(container, 1L);
		}
		Selections selections = new Selections(
			centerX(), centerY(), container,
			str("do_you_want_to_attack"),
			new String[] {
				str("yes_attack"),
				str("no_do_not_attack")
			}
		);
		container.addObject(selections);
		Waits.waitRemove(selections, 10L);
		boolean attack = selections.getValue() == 0;
		if (!attack) {
			selections = new Selections(
				centerX(), centerY(), container,
				str("do_you_forget_the_order"),
				new String[] {
					str("ok_let_us_attack"),
					str("no_let_them_go")
				}
			);
			container.addObject(selections);
			Waits.waitRemove(selections, 10L);
			attack = selections.getValue() == 0;
		}
		if (attack) {
			while (dolphin.centerY() < craft.centerY()) {
				dolphin.moveY(dolphinSpeed);
				craft.moveX(craftSpeed);
				Waits.wait(container, 1L);
			}
			ViewObject missile = new ViewObject(
				(int) dolphin.centerX(), (int) dolphin.centerY(), container,
				Missile.class
			);
			missile.rotate(Math.PI / 2);
			container.addObject(missile);
			Audio bomb = audio(BOMB_CODE, AUDIO_BOMB_FILE);
			container.addObject(star);
			while (missile.getIntX() + missile.getIntW() < centerX()) {
				craft.moveX(craftSpeed);
				missile.moveX(14);
				Waits.wait(container, 1L);
			}
			container.removeObject(missile);
			container.removeObject(star);
			container.addObject(ultra);
			BaseExplosion explosion = new BaseExplosion(centerX(), centerY(), container);
			int size = 140;
			explosion.setX(centerX() - size / 2);
			explosion.setY(centerY() - size / 2);
			explosion.setW(size);
			explosion.setH(size);
			container.playAudio(bomb);
			container.addObject(explosion);
		}
		while (container.getAll(Explosion.class).size() > 0) {
			craft.moveX(craftSpeed);
			Waits.wait(container, 1L);
		}
		while (craft.getIntX() <= container.getW()) {
			craft.moveX(craftSpeed);
			Waits.wait(container, 1L);
		}
		container.removeObject(craft);
		if (!attack) {
			container.addObject(star);
			Waits.wait(container, 15L);
			container.removeObject(star);
			container.addObject(ultra);
		}
		if (attack) {
			memberSay("why_you_prevent_me");
			ultraSay("are_you_sure_that_they_are_aggressor");
			memberSay("slient");
		}
		ultraSay("i_need_your_help");

		container.removeObject(ultra);
		container.addObject(star);
		while (star.centerX() > dolphin.centerX()) {
			if (star.centerY() > dolphin.centerY()) {
				star.moveY(-6);
			}
			star.moveX(-6);
			Waits.wait(container, 1L);
		}
		container.removeObject(star);
		memberSay("what_should_we_do");
		ultraSay("let_us_find_the_leader_first");
	}

	private void toBeContinued(DramaContainer container) {
		container.reset();
		container.resizeWithUI(initW(), initH());
		Control c = new LinesFullScreenPass(
			10L, Integer.MAX_VALUE, 1L, Arrays.asList("未完待续…", "To Be Continued..."), 10,
			container
		);
		container.addObject(c);
		Waits.waitRemove(c, 100L);
	}

	private boolean battle() {
		container.reset();
		container.resizeWithUI(SEA_SCENE_WIDTH, SEA_SCENE_HEIGHT);
		container.setBackgroud(deepSeaBGI);
		DeepSeaBattle deepSeaBattle = new DeepSeaBattle(this, container);
		if (!deepSeaBattle.run()) {
			Counter counter = new BlockDownCounter(1);
			container.addLifecycleListener(new LifecycleListener() {
				@Override
				public void onContainerClosed(LifecycleContainer container) {
					counter.count();
				}
			});
			container.addObject(new GameOverLabel(centerX(), GAME_OVER_LABEL_Y, container));
			container.addObject(new RestartButton(centerX(), RESTART_BUTTON_Y, container, counter::count));
			counter.await();
			return false;
		}
		return true;
	}

	private void inSea() {
		container.setBackgroud(deepSeaBGI);
		memberSay("think_around");
		ViewObject dolphin = new ViewObject(0, centerY(), container, Dolphin.class);
		dolphin.setStatus("move");
		container.addObject(dolphin);
		for (int i = 0; i < 10; i++) {
			dolphin.moveX(11);
			Waits.wait(container, 1L);
		}
		dolphin.setStatus(null);
		memberSay("is_this_the_submarine");
		info("we_have_no_more_picture");
		memberSay("ok_fine");
		ViewObject shark = new ViewObject(container.getW(), dolphin.getIntY(), container, Shark.class);
		shark.horzontalFlip();
		container.addObject(shark);
		for (int i = 0; i < 15; i++) {
			shark.setX(shark.getIntX() - 14);
			Waits.wait(container, 1L);
		}
		memberSay("i_am_not_dolphin");
		info("you_are_dolphin");
	}

	private void inBase() {
		container.setBackgroud(baseBGI);
		int baseX = centerX();
		int baseY = centerY();
		ViewObject plane = new ViewObject(baseX, 0, container, PlayerPlane.class);
		container.addObject(plane);
		int speed = 14;
		while (plane.centerY() < baseY) {
			plane.moveY(speed);
			speed = Integer.max(5, speed - 1);
			Waits.wait(container, 1L);
		}
		container.removeObject(plane);
		officerSay("how_did_you_driver");
		memberSay("do_not_you_know_reverse_parking");
		officerSay("are_you_not_skilled");
		memberSay("you_can_say_it_agin");
		officerSay("are_you_not_skilled2");
		memberSay("wait_and_see");
		plane.setX(baseX);
		plane.setY(baseY);
		container.addObject(plane);
		for (int i = 5; i >= 1; i -= 2) {
			moveCircle(plane, container.getW() / 6 * 4, Math.PI / 6 / i);
			moveCircle(plane, container.getW() / 6 * 2, -Math.PI / 6 / i);
		}
		for (int i = 0; i < 20; i++) {
			plane.moveY(-11);
			Waits.wait(container, 1L);
		}
		plane.rotate(Math.PI);
		for (int i = 0; i < 20; i++) {
			plane.moveY(11);
			Waits.wait(container, 1L);
		}
		container.removeObject(plane);
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
	public void moveCircle(ViewObject obj, int circleX, double deltaRadian) {
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
			obj.setX((int) (x + r * (1 - Math.cos(radian))));
			obj.setY((int) (y - r * Math.sin(radian)));
			obj.rotate(deltaRadian);
			Waits.wait(container, 1L);
		}
		obj.setX((int) (x + r * (1 - Math.cos(2 * Math.PI))));
		obj.setY((int) (y - r * Math.sin(2 * Math.PI)));
		obj.rotate(2 * Math.PI);
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

	private int centerX() {
		return container.getW() / 2;
	}

	private int centerY() {
		return container.getH() / 2;
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
