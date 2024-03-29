package cn.milai.ibdemo.role.plane;

import java.util.ArrayList;
import java.util.List;

import cn.milai.common.base.Randoms;
import cn.milai.ib.role.BaseBot;
import cn.milai.ib.role.Bot;
import cn.milai.ib.role.BotRole;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.nature.base.BaseScore;
import cn.milai.ib.role.nature.holder.ScoreHolder;
import cn.milai.ib.stage.Stage;

/**
 * 敌机
 * @author milai
 * @date 2020.04.02
 */
public abstract class EnemyPlane extends AbstractPlane implements BotRole, ScoreHolder {

	private Bot enemy = new BaseBot();

	public EnemyPlane() {
		setDirection(Math.PI);
		setScore(new BaseScore(this));
	}

	@Override
	protected void onEnterStage(Stage stage) {
		selectAttackTarget();
		initEnemyPlane();
	}

	protected void initEnemyPlane() {
	}

	@Override
	public void selectAttackTarget() {
		List<PlayerRole> targets = new ArrayList<>(stage().getAll(PlayerRole.class));
		if (targets.size() <= 0) {
			return;
		}
		setAttackTarget(targets.get(Randoms.nextInt(targets.size())));
	}

	@Override
	public void setAttackTarget(PlayerRole target) {
		enemy.setAttackTarget(target);
	}

	public PlayerRole getAttackTarget() { return enemy.getAttackTarget(); }

}
