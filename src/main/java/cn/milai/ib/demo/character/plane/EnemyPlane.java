package cn.milai.ib.demo.character.plane;

import java.util.List;

import cn.milai.ib.BaseEnemy;
import cn.milai.ib.Enemy;
import cn.milai.ib.character.EnemyCharacter;
import cn.milai.ib.character.PlayerCharacter;
import cn.milai.ib.character.property.HasScore;
import cn.milai.ib.container.Container;
import cn.milai.ib.util.RandomUtil;

/**
 * 
 * @author milai
 * @date 2020.04.02
 */
public abstract class EnemyPlane extends AbstractPlane implements EnemyCharacter, HasScore {

	private Enemy enemy;
	private int score;

	public EnemyPlane(int x, int y, Container container) {
		super(x, y, container);
		// 没有实现 Rotatable 接口，所以图片不会旋转
		// 这里是为使得子弹方向都朝下
		setDirection(Math.PI);
		score = intProp(P_SCORE);
		enemy = new BaseEnemy();
		selectAttackTarget();
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

	public PlayerCharacter getAttackTarget() {
		return enemy.getAttackTarget();
	}

	@Override
	public int getDamage() {
		return 1;
	}

	@Override
	public final int getScore() {
		return score;
	}

}
