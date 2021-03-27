package cn.milai.ibdemo.role.fish;

import java.util.List;

import cn.milai.common.base.Randoms;
import cn.milai.ib.container.lifecycle.LifecycleContainer;
import cn.milai.ib.role.BaseBot;
import cn.milai.ib.role.Bot;
import cn.milai.ib.role.BotRole;
import cn.milai.ib.role.PlayerRole;

/**
 * 敌方鱼类
 * @author milai
 * @date 2020.04.04
 */
public abstract class EnemyFish extends AbstractFish implements BotRole {

	private Bot enemy;

	public EnemyFish(double x, double y, LifecycleContainer container) {
		super(x, y, container);
		enemy = new BaseBot();
		selectAttackTarget();
		setDirection(-Math.PI / 4);
	}

	@Override
	public void setACCX(double accX) {
		if (accX < 0) {
			setDirection(-Math.PI / 4);
		} else if (accX > 0) {
			setDirection(Math.PI / 4);
		}
		super.setACCX(accX);
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

	@Override
	public PlayerRole getAttackTarget() { return enemy.getAttackTarget(); }

}
