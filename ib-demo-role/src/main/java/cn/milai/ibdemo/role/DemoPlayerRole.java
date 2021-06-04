package cn.milai.ibdemo.role;

import cn.milai.ib.container.plugin.control.cmd.Cmd;
import cn.milai.ib.role.PlayerRole;
import cn.milai.ibdemo.container.plugin.control.CmdCode;

/**
 * {@link PlayerRole} 示例实现
 * @author milai
 * @date 2021.05.13
 */
public interface DemoPlayerRole extends PlayerRole {

	@Override
	default boolean exec(Cmd cmd) {
		switch (CmdCode.of(cmd.getType())) {
			case UP : {
				setUp();
				return false;
			}
			case DOWN : {
				setDown();
				return false;
			}
			case LEFT : {
				setLeft();
				return false;
			}
			case RIGHT : {
				setRight();
				return false;
			}
			case A : {
				setA();
				return false;
			}
			case U_UP : {
				clearUp();
				return false;
			}
			case U_DOWN : {
				clearDown();
				return false;
			}
			case U_LEFT : {
				clearLeft();
				return false;
			}
			case U_RIGHT : {
				clearRight();
				return false;
			}
			case U_A : {
				clearA();
				return false;
			}
			default: {
				return true;
			}
		}
	}
}
