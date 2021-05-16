package cn.milai.ibdemo.container.plugin.form;

import java.awt.event.KeyEvent;

import org.springframework.stereotype.Component;

import cn.milai.ib.container.plugin.control.cmd.BaseCmd;
import cn.milai.ib.container.plugin.control.cmd.Cmd;
import cn.milai.ib.container.plugin.ui.form.KeyCodeMapping;
import cn.milai.ib.container.plugin.ui.form.KeyMapping;
import cn.milai.ibdemo.container.plugin.control.CmdType;

/**
 * {@link KeyMapping} 实现
 * @author milai
 * @date 2021.05.12
 */
@Component
public class DemoKeyMapping extends KeyCodeMapping {

	private static final Cmd UP_CMD = baseCmd(CmdType.UP);
	private static final Cmd U_UP_CMD = baseCmd(CmdType.U_UP);
	private static final Cmd DOWN_CMD = baseCmd(CmdType.DOWN);
	private static final Cmd U_DOWN_CMD = baseCmd(CmdType.U_DOWN);
	private static final Cmd LEFT_CMD = baseCmd(CmdType.LEFT);
	private static final Cmd U_LEFT_CMD = baseCmd(CmdType.U_LEFT);
	private static final Cmd RIGHT_CMD = baseCmd(CmdType.RIGHT);
	private static final Cmd U_RIGHT_CMD = baseCmd(CmdType.U_RIGHT);
	private static final Cmd A_CMD = baseCmd(CmdType.A);
	private static final Cmd U_A_CMD = baseCmd(CmdType.U_A);
	//	private static final Cmd B_CMD = baseCmd(CmdType.B);
	//	private static final Cmd U_B_CMD = baseCmd(CmdType.U_B);
	//	private static final Cmd C_CMD = baseCmd(CmdType.C);
	//	private static final Cmd U_C_CMD = baseCmd(CmdType.U_C);
	//	private static final Cmd D_CMD = baseCmd(CmdType.D);
	//	private static final Cmd U_D_CMD = baseCmd(CmdType.U_D);
	private static final Cmd PAUSE_CMD = baseCmd(CmdType.PAUSE);

	public DemoKeyMapping() {
		onPressed(KeyEvent.VK_W, () -> UP_CMD)
			.onPressed(KeyEvent.VK_S, () -> DOWN_CMD)
			.onPressed(KeyEvent.VK_A, () -> LEFT_CMD)
			.onPressed(KeyEvent.VK_D, () -> RIGHT_CMD)
			.onPressed(KeyEvent.VK_J, () -> A_CMD);

		onReleased(KeyEvent.VK_W, () -> U_UP_CMD)
			.onReleased(KeyEvent.VK_S, () -> U_DOWN_CMD)
			.onReleased(KeyEvent.VK_A, () -> U_LEFT_CMD)
			.onReleased(KeyEvent.VK_D, () -> U_RIGHT_CMD)
			.onReleased(KeyEvent.VK_J, () -> U_A_CMD)
			.onReleased(KeyEvent.VK_P, () -> PAUSE_CMD);
	}

	private static Cmd baseCmd(CmdType type) {
		return new BaseCmd(type.getValue());
	}

}
