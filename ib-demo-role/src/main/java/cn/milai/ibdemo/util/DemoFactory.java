package cn.milai.ibdemo.util;

import java.util.List;
import java.util.Map;

import cn.milai.ib.config.IBConfig;
import cn.milai.ib.control.BloodStrip;
import cn.milai.ib.control.GameOverLabel;
import cn.milai.ib.control.LifeCounter;
import cn.milai.ib.control.button.RestartButton;
import cn.milai.ib.control.text.DramaDialog;
import cn.milai.ib.control.text.LinesFullScreenPass;
import cn.milai.ib.control.text.Selections;
import cn.milai.ib.control.text.TextLines;
import cn.milai.ib.geometry.Bounds;
import cn.milai.ib.item.Item;
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
 * {@link Item} 工厂类
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

	private static <T extends Item> T apply(T o) {
		return IBConfig.apply(o);
	}

	private static <T extends Item> T applyCenter(T o, double x, double y) {
		return Bounds.centerXY(apply(o), x, y);
	}

	public static ViewRole newViewRole(Class<? extends Role> clazz, double x, double y) {
		return applyCenter(new ViewRole(clazz), x, y);
	}

	public static Selections newSelections(double x, double y, String quetion, String... selections) {
		Selections s = new Selections(quetion, selections);
		s.setW(SELECTIONS_W);
		s.setH(SELECTIONS_H);
		return Bounds.centerXY(s, x, y);
	}

	public static DramaDialog newDramaDialog(Map<String, Object> params, double x, double y) {
		KeyDramaDialog dialog = new KeyDramaDialog(params);
		dialog.setW(DIALOG_W);
		dialog.setH(DIALOG_H);
		return Bounds.centerXY(dialog, x, y);
	}

	public static LinesFullScreenPass newLinesFullScreenPass(long in, long keep, long out, List<String> lines,
		int interval) {
		return new LinesFullScreenPass(in, keep, out, lines, interval);
	}

	public static GameOverLabel newGameOverLabel(double x, double y) {
		GameOverLabel label = new GameOverLabel();
		label.setW(GAME_OVER_LABEL_W);
		label.setH(GAME__OVER_LABEL_H);
		return Bounds.centerXY(label, x, y);
	}

	public static RestartButton newRestartButton(double x, double y, Runnable r) {
		RestartButton button = new RestartButton(r);
		button.setW(RESTART_BUTTON_W);
		button.setH(RESTART_BUTTON_H);
		return Bounds.centerXY(button, x, y);
	}

	public static TextLines newTextLines(long in, long keep, long out, String... lines) {
		return new TextLines(in, keep, out, lines);
	}

	public static LifeCounter newLifeCounter(double x, double y, Role owner) {
		LifeCounter lifeCounter = new LifeCounter(owner);
		lifeCounter.setW(LIFE_COUNTER_W);
		lifeCounter.setH(LIFE_COUNTER_H);
		return Bounds.centerXY(lifeCounter, x, y);
	}

	public static BloodStrip newBloodStrip(double x, double y, Role owner) {
		BloodStrip strip = new BloodStrip(owner);
		strip.setW(BLOOD_STRIP_W);
		strip.setH(BLOOD_STRIP_H);
		return Bounds.centerXY(strip, x, y);
	}

	public static WelcomePlane newWelcomePlane(double x, double y) {
		return applyCenter(new WelcomePlane(), x, y);
	}

	public static AccelerateHelper newAccelerateHelper(double x, double y) {
		return applyCenter(new AccelerateHelper(), x, y);
	}

	public static OneLifeHelper newOneLifeHelper(double x, double y) {
		return applyCenter(new OneLifeHelper(), x, y);
	}

	public static PlayerPlane newPlayerPlane(double x, double y) {
		return applyCenter(new PlayerPlane(), x, y);
	}

	public static FollowPlane newFollowPlane(double x, double y) {
		return applyCenter(new FollowPlane(), x, y);
	}

	public static UltraLight newUltraLight(Role owner, long lastFrame) {
		return apply(new UltraLight(owner, lastFrame));
	}

	public static UltraFly newUltraFly(double x, double y) {
		return applyCenter(new UltraFly(), x, y);
	}

	public static MissileBoss newMissileBoss(double x, double y) {
		return applyCenter(new MissileBoss(), x, y);
	}

	public static BaseExplosion newBaseExplosion() {
		return apply(new BaseExplosion());
	}

	public static Dolphin newDolphin(double x, double y) {
		return applyCenter(new Dolphin(), x, y);
	}

	public static Shark newShark() {
		return apply(new Shark());
	}
}
