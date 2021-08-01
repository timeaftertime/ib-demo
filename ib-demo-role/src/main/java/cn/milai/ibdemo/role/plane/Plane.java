package cn.milai.ibdemo.role.plane;

import cn.milai.ib.role.property.holder.AwareMovableHolder;
import cn.milai.ib.role.property.holder.ColliderHolder;
import cn.milai.ib.role.property.holder.DamageHolder;
import cn.milai.ib.role.property.holder.ExplosibleHolder;

/**
 * 飞机类型角色
 * @author milai
 * @date 2020.02.20
 */
public interface Plane extends ColliderHolder, ExplosibleHolder, DamageHolder, AwareMovableHolder {

}
