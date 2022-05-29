package cn.milai.ibdemo.util;

import java.util.List;

import cn.milai.ib.actor.Actor;
import cn.milai.ib.actor.prop.BloodStrip;
import cn.milai.ib.actor.prop.Curtain;
import cn.milai.ib.actor.prop.GameOverLabel;
import cn.milai.ib.actor.prop.LifeCounter;
import cn.milai.ib.actor.prop.button.RestartButton;
import cn.milai.ib.actor.prop.text.DramaDialog;
import cn.milai.ib.actor.prop.text.Selections;
import cn.milai.ib.actor.prop.text.TextLines;
import cn.milai.ib.plugin.ui.Image;
import cn.milai.ib.publisher.Subscribers;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.ViewRole;
import cn.milai.ibdemo.control.KeyDramaDialog;
import cn.milai.ibdemo.role.UltraFly;
import cn.milai.ibdemo.role.UltraLight;
import cn.milai.ibdemo.role.explosion.BaseExplosion;
import cn.milai.ibdemo.role.fish.Dolphin;
import cn.milai.ibdemo.role.fish.Shark;
import cn.milai.ibdemo.role.helper.AccelerateHelper;
import cn.milai.ibdemo.role.helper.OneLifeHelper;
import cn.milai.ibdemo.role.plane.FollowPlane;
import cn.milai.ibdemo.role.plane.MissileBoss;
import cn.milai.ibdemo.role.plane.PlayerPlane;
import cn.milai.ibdemo.role.plane.WelcomePlane;

/**
 * {@link Actor} 工厂类
 * @author milai
 * @date 2021.07.08
 */
public class DemoFactory {

	private static final int DIALOG_W = 525;
	private static final int DIALOG_H = 280;

	private static final int SELECTIONS_W = 595;
	private static final int SELECTIONS_H = 56;

	private static final int GAME_OVER_LABEL_W = 470;
	private static final int GAME__OVER_LABEL_H = 84;

	private static final int RESTART_BUTTON_W = 100;
	private static final int RESTART_BUTTON_H = 25;

	private static final int LIFE_COUNTER_W = 80;
	private static final int LIFE_COUNTER_H = 28;

	private static final int BLOOD_STRIP_W = 472;
	private static final int BLOOD_STRIP_H = 42;

	public static ViewRole newViewRole(Class<? extends Role> clazz, double x, double y) {
		return new ViewRole(clazz).onMakeUp(Subscribers.centerXY(x, y));
	}

	public static Selections newSelections(double x, double y, String quetion, String... selections) {
		return new Selections(quetion, selections)
			.onMakeUp(Subscribers.centerXY(x, y))
			.resize(SELECTIONS_W, SELECTIONS_H);
	}

	public static DramaDialog newDramaDialog(double x, double y, Image speaker, String speakerName, String text) {
		return new KeyDramaDialog(speaker, speakerName, text)
			.onMakeUp(Subscribers.centerXY(x, y))
			.resize(DIALOG_W, DIALOG_H);
	}

	public static Curtain newLinesFullScreenPass(long in, long keep, long out, List<String> lines, int interval) {
		return new Curtain(in, keep, out, lines, interval);
	}

	public static GameOverLabel newGameOverLabel(double x, double y) {
		return new GameOverLabel()
			.onMakeUp(Subscribers.centerXY(x, y))
			.resize(GAME_OVER_LABEL_W, GAME__OVER_LABEL_H);
	}

	public static RestartButton newRestartButton(double x, double y, Runnable r) {
		RestartButton button = new RestartButton();
		button.onPressUp().subscribe(e -> r.run());
		return button.onMakeUp(Subscribers.centerXY(x, y)).resize(RESTART_BUTTON_W, RESTART_BUTTON_H);
	}

	public static TextLines newTextLines(long in, long keep, long out, String... lines) {
		return new TextLines(in, keep, out, lines);
	}

	public static LifeCounter newLifeCounter(double x, double y, Role owner) {
		return new LifeCounter(owner)
			.onMakeUp(Subscribers.centerXY(x, y))
			.resize(LIFE_COUNTER_W, LIFE_COUNTER_H);
	}

	public static BloodStrip newBloodStrip(double x, double y, Role owner) {
		return new BloodStrip(owner)
			.onMakeUp(Subscribers.centerXY(x, y))
			.resize(BLOOD_STRIP_W, BLOOD_STRIP_H);
	}

	public static WelcomePlane newWelcomePlane(double x, double y) {
		return new WelcomePlane().onMakeUp(Subscribers.centerXY(x, y));
	}

	public static AccelerateHelper newAccelerateHelper(double x, double y, double maxY) {
		return new AccelerateHelper(maxY).onMakeUp(Subscribers.centerXY(x, y));
	}

	public static OneLifeHelper newOneLifeHelper(double x, double y, double maxY) {
		return new OneLifeHelper(maxY).onMakeUp(Subscribers.centerXY(x, y));
	}

	public static PlayerPlane newPlayerPlane(double x, double y) {
		return new PlayerPlane().onMakeUp(Subscribers.centerXY(x, y));
	}

	public static FollowPlane newFollowPlane(double x, double y) {
		return new FollowPlane().onMakeUp(Subscribers.centerXY(x, y));
	}

	public static UltraLight newUltraLight(Role owner, long lastFrame) {
		return new UltraLight(owner, lastFrame);
	}

	public static UltraFly newUltraFly(double x, double y) {
		return new UltraFly().onMakeUp(Subscribers.centerXY(x, y));
	}

	public static MissileBoss newMissileBoss(double x, double y) {
		return new MissileBoss().onMakeUp(Subscribers.centerXY(x, y));
	}

	public static BaseExplosion makeUpBaseExplosion(long lastFrame) {
		BaseExplosion explosion = new BaseExplosion();
		explosion.setLastFrame(lastFrame);
		return (BaseExplosion) explosion.makeup();
	}

	public static Dolphin newDolphin(double x, double y) {
		return new Dolphin().onMakeUp(Subscribers.centerXY(x, y));
	}

	public static Shark newShark() {
		return new Shark();
	}
}
