package cn.milai.ibdemo.role.fish;

import java.util.List;

import cn.milai.common.base.Randoms;
import cn.milai.ib.role.BaseBot;
import cn.milai.ib.role.Bot;
import cn.milai.ib.role.BotRole;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ib.role.property.Movable;
import cn.milai.ib.role.property.Rigidbody;

/**
 * 敌方鱼类
 * @author milai
 * @date 2020.04.04
 */
public abstract class EnemyFish extends AbstractFish implements BotRole {

	private Bot enemy = new BaseBot();

	public EnemyFish() {
		setDirection(-Math.PI / 4);
	}

	@Override
	protected final void initItem() {
		selectAttackTarget();
		initEnemyFish();
	}

	protected void initEnemyFish() {

	}

	protected void afterRefreshSpeeds(Movable m) {
		Rigidbody r = getRigidbody();
		if (r.accX() < 0) {
			setDirection(-Math.PI / 4);
		} else if (r.accX() > 0) {
			setDirection(Math.PI / 4);
		}
	}

	@Override
	public void selectAttackTarget() {
		List<PlayerRole> targets = container().getAll(PlayerRole.class);
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
