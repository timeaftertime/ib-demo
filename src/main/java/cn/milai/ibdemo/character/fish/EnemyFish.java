package cn.milai.ibdemo.character.fish;

import java.util.List;

import cn.milai.common.base.Randoms;
import cn.milai.ib.character.BaseBot;
import cn.milai.ib.character.Bot;
import cn.milai.ib.character.BotCharacter;
import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.container.lifecycle.LifecycleContainer;

/**
 * 敌方鱼类
 * @author milai
 * @date 2020.04.04
 */
public abstract class EnemyFish extends AbstractFish implements BotCharacter {

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
		List<PlayerCharacter> targets = getContainer().getAll(PlayerCharacter.class);
		if (targets.size() <= 0) {
			return;
		}
		setAttackTarget(targets.get(Randoms.nextInt(targets.size())));
	}

	@Override
	public void setAttackTarget(PlayerCharacter target) {
		enemy.setAttackTarget(target);
	}

	@Override
	public PlayerCharacter getAttackTarget() { return enemy.getAttackTarget(); }

}
