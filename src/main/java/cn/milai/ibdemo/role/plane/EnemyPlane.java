package cn.milai.ibdemo.role.plane;

import java.util.List;

import cn.milai.common.base.Randoms;
import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.BaseBot;
import cn.milai.ib.role.Bot;
import cn.milai.ib.role.BotRole;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.property.HasScore;

/**
 * 敌机
 * @author milai
 * @date 2020.04.02
 */
public abstract class EnemyPlane extends AbstractPlane implements BotRole, HasScore {

	private Bot enemy;
	private int score;

	public EnemyPlane(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		// 没有实现 Rotatable 接口，所以图片不会旋转，这里是为使得子弹方向都朝下
		setDirection(Math.PI);
		score = intProp(P_SCORE);
		enemy = new BaseBot();
		selectAttackTarget();
	}

	@Override
	public void selectAttackTarget() {
		List<PlayerRole> targets = getContainer().getAll(PlayerRole.class);
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

	@Override
	public int getDamage() { return 1; }

	@Override
	public final int getScore() { return score; }

}
