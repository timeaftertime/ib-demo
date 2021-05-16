package cn.milai.ibdemo.container.plugin.control;

import cn.milai.ib.container.plugin.control.cmd.PointCmd;

/**
 * {@link CmdType#CLICK} 类型指令
 * @author milai
 * @date 2021.02.10
 */
public class ClickCmd extends PointCmd {

	/**
	 * 构造一个点击指令
	 * @param fromId
	 * @param x 点击的 x 坐标
	 * @param y 点击的 y 坐标
	 */
	public ClickCmd(int fromId, double x, double y) {
		super(CmdType.CLICK.getValue(), x, y);
	}

}
