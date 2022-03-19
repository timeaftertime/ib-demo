package cn.milai.ibdemo.story;

import java.util.List;

import cn.milai.ib.control.BloodStrip;
import cn.milai.ib.control.GameOverLabel;
import cn.milai.ib.control.LifeCounter;
import cn.milai.ib.control.button.RestartButton;
import cn.milai.ib.control.stage.Curtain;
import cn.milai.ib.control.text.DramaDialog;
import cn.milai.ib.control.text.Selections;
import cn.milai.ib.control.text.TextLines;
import cn.milai.ib.mode.drama.AbstractDrama;
import cn.milai.ib.mode.drama.Drama;
import cn.milai.ib.role.Role;
import cn.milai.ib.role.ViewRole;
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
import cn.milai.ibdemo.util.DemoFactory;

/**
 * 实例抽象 {@link Drama}
 * @author milai
 * @date 2021.07.07
 */
public abstract class DemoDrama extends AbstractDrama {

	protected ViewRole newViewRole(Class<? extends Role> clazz, double x, double y) {
		return DemoFactory.newViewRole(clazz, x, y);
	}

	protected Selections newSelections(double x, double y, String quetion, String... selections) {
		return DemoFactory.newSelections(x, y, quetion, selections);
	}

	protected DramaDialog newDramaDialog(double x, double y, String speakerImg, String speakerName, String stringCode) {
		return DemoFactory.newDramaDialog(
			x, y,
			speakerImg == null ? null : image(speakerImg),
			speakerName == null ? "" : str(speakerName),
			str(stringCode)
		);
	}

	protected Curtain newLinesFullScreenPass(long in, long keep, long out, List<String> lines,
		int interval) {
		return DemoFactory.newLinesFullScreenPass(in, keep, out, lines, interval);
	}

	protected GameOverLabel newGameOverLabel(double x, double y) {
		return DemoFactory.newGameOverLabel(x, y);
	}

	protected RestartButton newRestartButton(double x, double y, Runnable r) {
		return DemoFactory.newRestartButton(x, y, r);
	}

	protected TextLines newTextLines(long in, long keep, long out, String... lines) {
		return DemoFactory.newTextLines(in, keep, out, lines);
	}

	protected LifeCounter newLifeCounter(double x, double y, Role owner) {
		return DemoFactory.newLifeCounter(x, y, owner);
	}

	protected BloodStrip newBloodStrip(double x, double y, Role owner) {
		return DemoFactory.newBloodStrip(x, y, owner);
	}

	protected WelcomePlane newWelcomePlane(double x, double y) {
		return DemoFactory.newWelcomePlane(x, y);
	}

	protected AccelerateHelper newAccelerateHelper(double x, double y, double maxY) {
		return DemoFactory.newAccelerateHelper(x, y, maxY);
	}

	protected OneLifeHelper newOneLifeHelper(double x, double y, double maxY) {
		return DemoFactory.newOneLifeHelper(x, y, maxY);
	}

	protected PlayerPlane newPlayerPlane(double x, double y) {
		return DemoFactory.newPlayerPlane(x, y);
	}

	protected FollowPlane newFollowPlane(double x, double y) {
		return DemoFactory.newFollowPlane(x, y);
	}

	protected UltraLight newUltraLight(Role owner, long lastFrame) {
		return DemoFactory.newUltraLight(owner, lastFrame);
	}

	protected UltraFly newUltraFly(double x, double y) {
		return DemoFactory.newUltraFly(x, y);
	}

	protected MissileBoss newMissileBoss(double x, double y) {
		return DemoFactory.newMissileBoss(x, y);
	}

	protected BaseExplosion newBaseExplosion() {
		return DemoFactory.newBaseExplosion();
	}

	protected Dolphin newDolphin(double x, double y) {
		return DemoFactory.newDolphin(x, y);
	}

	protected Shark newShark() {
		return DemoFactory.newShark();
	}

}
