package cn.milai.ibdemo.role.plane;

import cn.milai.ib.role.nature.holder.AwareMovableHolder;
import cn.milai.ib.role.nature.holder.ColliderHolder;
import cn.milai.ib.role.nature.holder.DamageHolder;
import cn.milai.ib.role.nature.holder.ExplosibleHolder;

/**
 * 飞机类型角色
 * @author milai
 * @date 2020.02.20
 */
public interface Plane extends ColliderHolder, ExplosibleHolder, DamageHolder, AwareMovableHolder {

}
