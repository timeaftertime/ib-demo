package cn.milai.ibdemo.container.plugin.control;

import cn.milai.ib.container.plugin.control.cmd.Cmd;
import cn.milai.ib.container.plugin.control.cmd.PointCmd;

/**
 * (鼠标)移动 {@link Cmd}
 * @author milai
 * @date 2021.03.20
 */
public class OverCmd extends PointCmd {

	public OverCmd(double x, double y) {
		super(CmdType.OVER.getValue(), x, y);
	}

}
