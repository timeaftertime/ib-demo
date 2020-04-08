package cn.milai.ib.demo.character.fish;

import java.util.List;

import cn.milai.ib.BaseEnemy;
import cn.milai.ib.Enemy;
import cn.milai.ib.character.EnemyCharacter;
import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.container.Container;
import cn.milai.ib.util.RandomUtil;

/**
 * 敌方鱼类
 * @author milai
 * @date 2020.04.04
 */
public abstract class EnemyFish extends AbstractFish implements EnemyCharacter {

	private Enemy enemy;

	public EnemyFish(int x, int y, Container container) {
		super(x, y, container);
		enemy = new BaseEnemy();
		selectAttackTarget();
		setDirection(-Math.PI / 4);
	}

	@Override
	public void setACCX(int accX) {
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
		setAttackTarget(targets.get(RandomUtil.nextInt(targets.size())));
	}

	@Override
	public void setAttackTarget(PlayerCharacter target) {
		enemy.setAttackTarget(target);
	}

	@Override
	public PlayerCharacter getAttackTarget() {
		return enemy.getAttackTarget();
	}

}
