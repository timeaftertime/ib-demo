package cn.milai.ibdemo.role.fish;

import cn.milai.ib.role.nature.holder.AwareMovableHolder;
import cn.milai.ib.role.nature.holder.ColliderHolder;
import cn.milai.ib.role.nature.holder.ExplosibleHolder;
import cn.milai.ib.role.nature.holder.RigidbodyHolder;

/**
 * 水中游的游戏角色
 * @author milai
 * @date 2020.04.03
 */
public interface Fish extends AwareMovableHolder, ExplosibleHolder, ColliderHolder, RigidbodyHolder {
}
